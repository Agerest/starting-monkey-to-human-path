package RPIS61.Kres.wdad.learn.xml;

import java.io.File;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import RPIS61.Kres.wdad.data.model.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class XmlTask implements Serializable {

    private static ArrayList<Reader> readers;
    private static final String XML_PATH = "src\\RPIS61\\Kres\\wdad\\learn\\xml\\library.xml";
    private Document document;

    public XmlTask() throws RemoteException {
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

    private void readXML() throws RemoteException{
        NodeList readers = document.getDocumentElement().getElementsByTagName("reader");
        Book tempBook = new Book();
        Reader tempReader;
        Element reader;
        NodeList books;
        NodeList book;
        Element authorNode;
        Element dateNode;
        for (int i = 0; i < readers.getLength(); i++) {
            tempReader = new Reader();
            reader = (Element) readers.item(i);
            tempReader.setFirstName(reader.getAttribute("firstname"));
            tempReader.setSecondName(reader.getAttribute("secondname"));
            books = reader.getChildNodes();
            for (int j = 0; j < books.getLength(); j++) {
                if (books.item(j).getNodeName().equals("book")) {
                    tempBook = new Book();
                    book = books.item(j).getChildNodes();
                    for (int k = 0; k < book.getLength(); k++) {
                        switch (book.item(k).getNodeName()) {
                            case "author": {
                                authorNode = (Element) book.item(k);
                                tempBook.setAuthor(new Author(authorNode.getElementsByTagName("firstname").item(0).getTextContent(),authorNode.getElementsByTagName("secondname").item(0).getTextContent()));
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
                    tempBook.setTakeDate(new StringBuilder().append(dateNode.getAttribute("day")).append(".")
                            .append(dateNode.getAttribute("month")).append(".")
                            .append(dateNode.getAttribute("year")).toString());
                    tempReader.addBook(tempBook);
                }
            }
            XmlTask.readers.add(tempReader);
        }
    }

    public List<Reader> negligentReaders()throws RemoteException {
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

    public void removeBook(Reader reader, Book book)throws RemoteException {
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
            if ((author.getElementsByTagName("firstname").item(0).getTextContent().equals(book.getAuthor().getFirstName()))
                    && (author.getElementsByTagName("secondname").item(0).getTextContent().equals(book.getAuthor().getSecondName()))
                    && (nameNode.getTextContent().equals(book.getName()))
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

    public void addBook(Reader reader, Book book) throws RemoteException{
        Element tempBook = document.createElement("book");
        Element author = document.createElement("author");
        Element tempElement = document.createElement("firstname");
        tempElement.setTextContent(book.getAuthor().getFirstName());
        author.appendChild(tempElement);
        tempElement = document.createElement("secondname");
        tempElement.setTextContent(book.getAuthor().getSecondName());
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

    public static List<Reader> getReaders() throws RemoteException {
        return readers;//.subList(0,readers.size());
    }

    private Node searchReader(Reader reader) throws RemoteException{
        NodeList readers = document.getDocumentElement().getElementsByTagName("reader");
        Element readerNode;
        for (int i = 0; i < readers.getLength(); i++) {
            readerNode = (Element)readers.item(i);
            if (readerNode.getAttribute("firstname").equals(reader.getFirstName())
                    && readerNode.getAttribute("secondname").equals(reader.getSecondName())) {
                return readers.item(i);
            }
        }
        return null;
    }

    private void saveXML()throws RemoteException {
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
