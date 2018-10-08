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

public class XmlTask {

    private static ArrayList<Reader> readers = new ArrayList<>();
    private static final String XML_PATH = "src/RPIS61/Kres/wdad/learn/xml/library.xml";
    private static Document document;

    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(XML_PATH);
            NodeList readers = document.getDocumentElement().getElementsByTagName("reader");
            Book b = new Book();
            Reader r;
            Node reader;
            NamedNodeMap attributesReader;
            NodeList books;
            NodeList book;
            NodeList author;
            for (int i = 0; i < readers.getLength(); i++) {
                r = new Reader();
                reader = readers.item(i);
                attributesReader = reader.getAttributes();
                r.setFirstName(attributesReader.getNamedItem("firstname").getNodeValue());
                r.setSecondName(attributesReader.getNamedItem("secondname").getNodeValue());
                books = reader.getChildNodes();
                for (int j = 0; j < books.getLength(); j++) {
                    if (books.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        if (books.item(j).getNodeName().equals("book")) {
                            b = new Book();
                            book = books.item(j).getChildNodes();
                            for (int k = 0; k < book.getLength(); k++) {
                                if (book.item(k).getNodeType() == Node.ELEMENT_NODE) {

                                    switch (book.item(k).getNodeName()) {
                                        case "author": {
                                            author = book.item(k).getChildNodes();
                                            for (int l = 0; l < author.getLength(); l++) {
                                                if (book.item(l).getNodeType() == Node.ELEMENT_NODE) {
                                                    if (author.item(l).getNodeName() == "firstname") {
                                                        b.setAuthorFirstName(author.item(l).getTextContent());
                                                    } else b.setAuthorSecondName(author.item(l).getTextContent());
                                                }
                                            }
                                            break;
                                        }
                                        case "name": {
                                            b.setName(book.item(k).getTextContent());
                                            break;
                                        }
                                        case "printyear": {
                                            b.setPrintYear(Integer.parseInt(book.item(k).getTextContent()));
                                            break;
                                        }
                                        case "genre": {
                                            b.setGenre(Genre.valueOf((book.item(k).getAttributes().getNamedItem("value").getNodeValue()).toUpperCase()));
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            r.addBook(b, books.item(j).getAttributes().getNamedItem("day").getNodeValue() + "."
                                    + books.item(j).getAttributes().getNamedItem("month").getNodeValue() + "."
                                    + books.item(j).getAttributes().getNamedItem("year").getNodeValue());
                        }
                    }
                }
                XmlTask.readers.add(r);
            }
            for (Reader read : negligentReaders()) {
                System.out.println(read);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public static List<Reader> negligentReaders() {
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
        NodeList readers = document.getDocumentElement().getElementsByTagName("reader");
        Node r = null;
        for (int i = 0; i < readers.getLength(); i++) {
            if (readers.item(i).getAttributes().item(0).getTextContent().equals(reader.getFirstName())
                            && readers.item(i).getAttributes().item(1).getTextContent().equals(reader.getSecondName()))
                r = readers.item(i);
        }
        NodeList books = ((Element)r).getElementsByTagName("book");
        NodeList takeDates = ((Element)r).getElementsByTagName("takedate");
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
            }
        }
        try {
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document),new StreamResult(new File(XML_PATH)));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        reader.removeBook(book);
    }
}
