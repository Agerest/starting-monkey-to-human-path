package RPIS61.Kres.wdad.learn.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface XmlDataManager extends Remote {

    public List<Reader> negligentReaders() throws RemoteException;
    public void removeBook (Reader reader, Book book)throws RemoteException;
    public void addBook (Reader reader, Book book)throws RemoteException;
    public HashMap<Book, Date> getDateReturn (Reader reader)throws RemoteException;
    public void addReader(Reader reader) throws RemoteException;
    public List<Reader> getReaders() throws RemoteException;

}
