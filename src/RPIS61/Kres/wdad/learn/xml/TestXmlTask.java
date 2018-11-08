package RPIS61.Kres.wdad.learn.xml;

import RPIS61.Kres.wdad.data.model.*;

import java.rmi.RemoteException;

public class TestXmlTask {
    public static void main(String[] args) throws RemoteException {
        XmlTask task = new XmlTask();
        Book book = new Book();
        book.setAuthor(new Author("wwwe","weee"));
        book.setGenre(Genre.EPOPEE);
        book.setName("ttt");
        book.setPrintYear(1999);
        book.setTakeDate("05.01.2018");
//        task.addBook(XmlTask.getReaders().get(1),book);
//        task.removeBook(XmlTask.getReaders().get(1),book);
        task.negligentReaders().forEach(System.out::println);
    }
}
