package RPIS61.Kres.wdad.learn.xml;

import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class XmlTask {

    private static ArrayList<Reader> readers;
    private static final String XML_PATH = "src\\RPIS61\\Kres\\wdad\\learn\\xml\\library.xml";
    private Document document;

    public XmlTask() {
        readers = new ArrayList<>();
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(XML_PATH);
            readXML();
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    private void readXML() {
        NodeList readers = document.getDocumentElement().getElementsByTagName("reader");
        Book tempBook = new Book();
        Reader tempReader;
        Node reader;
        NamedNodeMap attributesReader;
        NodeList books;
        NodeList book;
        NodeList author;
        for (int i = 0; i < readers.getLength(); i++) {
            tempReader = new Reader();
            reader = readers.item(i);
            attributesReader = reader.getAttributes();
            tempReader.setFirstName(attributesReader.getNamedItem("firstname").getNodeValue());
            tempReader.setSecondName(attributesReader.getNamedItem("secondname").getNodeValue());
            books = reader.getChildNodes();
            for (int j = 0; j < books.getLength(); j++) {
                if (books.item(j).getNodeType() == Node.ELEMENT_NODE) {
                    if (books.item(j).getNodeName().equals("book")) {
                        tempBook = new Book();
                        book = books.item(j).getChildNodes();
                        for (int k = 0; k < book.getLength(); k++) {
                            if (book.item(k).getNodeType() == Node.ELEMENT_NODE) {

                                switch (book.item(k).getNodeName()) {
                                    case "author": {
                                        author = book.item(k).getChildNodes();
                                        for (int l = 0; l < author.getLength(); l++) {
                                            if (book.item(l).getNodeType() == Node.ELEMENT_NODE) {
                                                if (Objects.equals(author.item(l).getNodeName(), "firstname")) {
                                                    tempBook.setAuthorFirstName(author.item(l).getTextContent());
                                                } else tempBook.setAuthorSecondName(author.item(l).getTextContent());
                                            }
                                        }
                                        break;
                                    }
                                    case "name": {
                                        tempBook.setName(book.item(k).getTextContent());
                                        break;
                                    }
                                    case "printyear": {
                                        tempBook.setPrintYear(Integer.parseInt(book.item(k).getTextContent()));
                                        break;
                                    }
                                    case "genre": {
                                        tempBook.setGenre(Genre.valueOf((book.item(k).getAttributes().getNamedItem("value").getNodeValue()).toUpperCase()));
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        tempBook.setTakeDate(books.item(j).getAttributes().getNamedItem("day").getNodeValue() + "."
                                + books.item(j).getAttributes().getNamedItem("month").getNodeValue() + "."
                                + books.item(j).getAttributes().getNamedItem("year").getNodeValue());
                        tempReader.addBook(tempBook);
                    }
                }
            }
            XmlTask.readers.add(tempReader);
        }
    }

    public List<Reader> negligentReaders() {
        List<Reader> result = new ArrayList<>();
        for (Reader reader : readers) {
            if (reader.isNegligent())
                result.add(reader);
        }
        return result;
    }

    public List<Book> debtBooks(Reader reader) {
        return reader.getBooksList();
    }

    public void removeBook(Reader reader, Book book) {
        Node tempReader = searchReader(reader);
        NodeList books = ((Element)tempReader).getElementsByTagName("book");
        NodeList takeDates = ((Element)tempReader).getElementsByTagName("takedate");
        NodeList author;
        for (int i = 0; i < books.getLength(); i++) {
            author = ((Element)books.item(i)).getElementsByTagName("author");
            if (((Element)author.item(0)).getElementsByTagName("firstname").item(0).getTextContent().equals(book.getAuthorFirstName())
                    && ((Element)author.item(0)).getElementsByTagName("secondname").item(0).getTextContent().equals(book.getAuthorSecondName())
                    && ((Element)books.item(0)).getElementsByTagName("name").item(0).getTextContent().equals(book.getName())
                    && (Integer.parseInt(((Element)books).getElementsByTagName("printyear").item(0).getTextContent()) == book.getPrintYear())
                    && ((Element)books).getElementsByTagName("genre").item(0).getAttributes().item(0).getTextContent().equalsIgnoreCase(book.getGenre().toString())) {
                books.item(i).getParentNode().removeChild(books.item(i));
                takeDates.item(i).getParentNode().removeChild(takeDates.item(i));
                break;
            }
        }
        try {
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document),new StreamResult(new File(XML_PATH)));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        reader.removeBook(book);
    }

    public void addBook(Reader reader, Book book) {
        Element tempBook = document.createElement("book");
        Element author = document.createElement("author");
        Element tempElement = document.createElement("firstname");
        tempElement.setTextContent(book.getAuthorFirstName());
        author.appendChild(tempElement);
        tempElement = document.createElement("secondname");
        tempElement.setTextContent(book.getAuthorSecondName());
        author.appendChild(tempElement);
        tempBook.appendChild(author);
        tempElement = document.createElement("name");
        tempElement.setTextContent(book.getName());
        tempBook.appendChild(tempElement);
        tempElement = document.createElement("printyear");
        tempElement.setTextContent(Integer.toString(book.getPrintYear()));
        tempBook.appendChild(tempElement);
        tempElement = document.createElement("genre");
        tempElement.setAttribute("value", book.getGenre().toString().toLowerCase());
        tempBook.appendChild(tempElement);
        Node tempReader = searchReader(reader);
        tempReader.appendChild(tempBook);
        Element takeDate = document.createElement("takedate");
        takeDate.setAttribute("day", Integer.toString(book.getTakeDate().getDay()));
        takeDate.setAttribute("month", Integer.toString(book.getTakeDate().getMonth()));
        takeDate.setAttribute("year", Integer.toString(book.getTakeDate().getYear()));
        tempReader.appendChild(takeDate);
        reader.addBook(book);
        try {
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document),new StreamResult(new File(XML_PATH)));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Reader> getReaders() {
        return readers;
    }

    private Node searchReader(Reader reader) {
        NodeList readers = document.getDocumentElement().getElementsByTagName("reader");
        for (int i = 0; i < readers.getLength(); i++) {
            if (readers.item(i).getAttributes().item(0).getTextContent().equals(reader.getFirstName())
                    && readers.item(i).getAttributes().item(1).getTextContent().equals(reader.getSecondName())) {
                return readers.item(i);
            }
        }
        return null;
    }

}
