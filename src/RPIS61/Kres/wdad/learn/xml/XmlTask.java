package RPIS61.Kres.wdad.learn.xml;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
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
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(XML_PATH);
            readXML();
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    private void readXML() {
        NodeList readers = document.getDocumentElement().getElementsByTagName("reader");
        Book tempBook = new Book();
        Reader tempReader;
        Element reader; //todo Element
        NodeList books;
        NodeList book;
        Element authorNode;
        Element dateNode;
        for (int i = 0; i < readers.getLength(); i++) {
            tempReader = new Reader();
            reader = (Element) readers.item(i);
            tempReader.setFirstName(reader.getAttribute("firstname")); //todo reader.getAttribute("firstname")
            tempReader.setSecondName(reader.getAttribute("secondname")); //todo reader.getAttribute("secondname")
            books = reader.getChildNodes();
            for (int j = 0; j < books.getLength(); j++) {
                if (books.item(j).getNodeName().equals("book")) {
                    tempBook = new Book();
                    book = books.item(j).getChildNodes();
                    for (int k = 0; k < book.getLength(); k++) {
                        switch (book.item(k).getNodeName()) {
                            case "author": {
                                authorNode = (Element) book.item(k);
                                tempBook.setAuthorFirstName(authorNode.getElementsByTagName("firstname").item(0).getTextContent());
                                tempBook.setAuthorSecondName(authorNode.getElementsByTagName("secondname").item(0).getTextContent());
                                //todo same with secondName
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
                if (books.item(j).getNodeName().equals("takedate")) {
                    dateNode = (Element) books.item(j);
                    tempBook.setTakeDate(dateNode.getAttribute("day") + "."//todo the same
                            + dateNode.getAttribute("month") + "."
                            + dateNode.getAttribute("year"));
                    tempReader.addBook(tempBook);
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
        Element tempReader = (Element)searchReader(reader);
        NodeList books = (tempReader).getElementsByTagName("book");
        NodeList takeDates = (tempReader).getElementsByTagName("takedate");
        Element author;
        Element nameNode;
        Element printYearNode;
        Element genreNode;
        for (int i = 0; i < books.getLength(); i++) {
            author = (Element) ((Element)books.item(i)).getElementsByTagName("author").item(0);
            genreNode = (Element) ((Element) books.item(i)).getElementsByTagName("genre").item(0);
            printYearNode = (Element) ((Element)books.item(i)).getElementsByTagName("printyear").item(0);
            nameNode = (Element)((Element)books.item(i)).getElementsByTagName("name").item(0);
            //todo разбей эти адские цепочки вызовов Заведи переменных типа Element
            if ((author.getElementsByTagName("firstname").item(0).getTextContent().equals(book.getAuthorFirstName()))
                    && (author.getElementsByTagName("secondname").item(0).getTextContent().equals(book.getAuthorSecondName()))
                    && (nameNode.getTextContent().equals(book.getName())) //todo changed from 0 to i - check
                    && (Integer.parseInt((printYearNode.getTextContent())) == book.getPrintYear())
                    && (genreNode.getAttribute("value").equalsIgnoreCase(book.getGenre().toString()))) {
                books.item(i).getParentNode().removeChild(books.item(i));
                takeDates.item(i).getParentNode().removeChild(takeDates.item(i));
                break;
            }
        }
        saveXML();
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
        LocalDate localDate = book.getTakeDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        takeDate.setAttribute("day", Integer.toString(localDate.getDayOfMonth()));
        takeDate.setAttribute("month", Integer.toString(localDate.getMonthValue()));
        takeDate.setAttribute("year", Integer.toString(localDate.getYear()));
        tempReader.appendChild(takeDate);
        reader.addBook(book);
        saveXML();
    }

    public static List<Reader> getReaders() {
        return readers.subList(0,readers.size());
    }

    private Node searchReader(Reader reader) {
        NodeList readers = document.getDocumentElement().getElementsByTagName("reader");
        Element readerNode;
        for (int i = 0; i < readers.getLength(); i++) {
            //todo Element reader
            readerNode = (Element)readers.item(i);
            if (readerNode.getAttribute("firstname").equals(reader.getFirstName())
                    && readerNode.getAttribute("secondname").equals(reader.getSecondName())) {
                return readers.item(i);
            }
        }
        return null;
    }

    private void saveXML() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(new File(XML_PATH));
            Source input = new DOMSource(document);
            transformer.transform(input, output);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

}
