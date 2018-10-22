package RPIS61.Kres.wdad.learn.rmi.client;

import RPIS61.Kres.wdad.data.managers.PreferencesManager;
import RPIS61.Kres.wdad.learn.rmi.*;
import RPIS61.Kres.wdad.utils.PreferencesManagerConstants;

import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client {

    private static final String BIND_NAME = "XmlDataManager";
    private static int port;
    private static String address;


    public static void main(String... args) {
        PreferencesManager pm = PreferencesManager.getInstance();
        address = pm.getProperty(PreferencesManagerConstants.registryaddress);
        port = Integer.parseInt(pm.getProperty(PreferencesManagerConstants.registryport));
        System.setProperty("java.security.policy", pm.getProperty(PreferencesManagerConstants.policypath));
        System.setProperty("java.rmi.server.useCodebaseOnly", pm.getProperty(PreferencesManagerConstants.usecodebaseonly));
        System.setSecurityManager(new RMISecurityManager());
        try {
            System.out.print("Authorization...");
            Registry registry = LocateRegistry.getRegistry(address, port);
            XmlDataManager service = (XmlDataManager) registry.lookup(BIND_NAME);
            System.out.println(" OK");
            test(service);
        } catch (Exception e) {
            System.out.println(" fail");
            e.printStackTrace();
        }
    }

    private static void test(XmlDataManager xmlDataManager) throws RemoteException {
        Reader reader1 = new Reader("1","1");
        Book book = new Book(new Author("11","11"),"11",1999, Genre.EPOPEE,"12.12.2017");
        reader1.addBook(book);
        reader1.addBook(new Book(new Author("12","12"),"12",1989,Genre.EPOPEE,"12.12.2016"));
        Reader reader2 = new Reader("2","2");
        reader2.addBook(new Book(new Author("21","21"),"21",1999,Genre.EPOPEE,"19.10.2018"));
        reader2.addBook(new Book(new Author("22","22"),"22",1989,Genre.EPOPEE,"18.10.2018"));
        xmlDataManager.addReader(reader1);
        xmlDataManager.addReader(reader2);
        xmlDataManager.addBook(reader1,new Book(new Author("13","13"),"13",1999,Genre.EPOPEE,"19.10.2018"));
        xmlDataManager.removeBook(reader1,book);
//        reader1.getReturnList().get(book);
//        for(Book b:xmlDataManager.getDateReturn(reader1).keySet()){
//            System.out.println(b+" "+xmlDataManager.getDateReturn(reader1).get(b));
//        }
        xmlDataManager.negligentReaders().forEach(System.out::println);
//        xmlDataManager.getReaders().forEach(System.out::println);
    }

}
