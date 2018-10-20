package RPIS61.Kres.wdad.learn.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Test extends UnicastRemoteObject {

    private XmlDataManager xmlDataManager;

    protected Test(XmlDataManager xmlDataManager) throws RemoteException {
        this.xmlDataManager = xmlDataManager;
    }

    public void start() throws RemoteException {
        Reader reader1 = new Reader("1","1");
        reader1.addBook(new Book(new Author("11","11"),"11",1999,Genre.EPOPEE,"12.12.2017"));
        reader1.addBook(new Book(new Author("12","12"),"12",1989,Genre.EPOPEE,"12.12.2016"));
        Reader reader2 = new Reader("2","2");
        reader2.addBook(new Book(new Author("21","21"),"21",1999,Genre.EPOPEE,"19.10.2018"));
        reader2.addBook(new Book(new Author("22","22"),"22",1989,Genre.EPOPEE,"18.10.2018"));
        xmlDataManager.addReader(reader1);
        xmlDataManager.addReader(reader2);
        xmlDataManager.getReaders().forEach(System.out::println);
    }
}
