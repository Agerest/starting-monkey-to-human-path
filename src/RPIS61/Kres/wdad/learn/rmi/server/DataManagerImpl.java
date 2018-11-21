package RPIS61.Kres.wdad.learn.rmi.server;

import RPIS61.Kres.wdad.data.model.*;
import RPIS61.Kres.wdad.data.managers.DataManager;
import RPIS61.Kres.wdad.learn.xml.XmlTask;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//todo здесь внутри лежит XMLTask и методы обращаются к нему
public class DataManagerImpl implements DataManager, Serializable {
    private XmlTask xmlTask;

    DataManagerImpl() throws RemoteException {
        xmlTask = new XmlTask();
    }


    public List<Reader> getReaders() throws RemoteException {
        return xmlTask.getReaders();
    }

    @Override
    public List<Reader> negligentReaders() throws RemoteException {
        return xmlTask.negligentReaders();
    }

    @Override
    public void removeBook(Reader reader, Book book)throws RemoteException {
        xmlTask.removeBook(reader,book);
    }

    @Override
    public void addBook(Reader reader, Book book) throws RemoteException {
        xmlTask.addBook(reader,book);
    }

    @Override
    public HashMap<Book, Date> getDateReturn(Reader reader) throws RemoteException {
        return (HashMap<Book, Date>) reader.getReturnList();
    }
}
