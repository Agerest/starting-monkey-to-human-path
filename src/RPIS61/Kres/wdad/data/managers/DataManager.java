package RPIS61.Kres.wdad.data.managers;

import RPIS61.Kres.wdad.data.model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface DataManager extends Remote {

    public List<Reader> negligentReaders() throws RemoteException;
    public void removeBook (Reader reader, Book book)throws RemoteException;
    public void addBook (Reader reader, Book book)throws RemoteException;
    public HashMap<Book, Date> getDateReturn (Reader reader)throws RemoteException;
    public List<Reader> getReaders() throws RemoteException;

}
