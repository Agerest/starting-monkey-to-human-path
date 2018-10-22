package RPIS61.Kres.wdad.learn.rmi;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class XmlDataManagerImpl implements XmlDataManager, Serializable {

    private ArrayList<Reader> readers;

    XmlDataManagerImpl() {
        readers = new ArrayList<>();
    }

    @Override
    public void addReader(Reader reader) throws RemoteException {
        readers.add(reader);
    }

    public List<Reader> getReaders() throws RemoteException {
        return readers;
    }

    @Override
    public List<Reader> negligentReaders() throws RemoteException {
        List<Reader> result = new ArrayList<>();
        for (Reader reader : readers) {
            if (reader.isNegligent())
                result.add(reader);
        }
        return result;
    }

    @Override
    public void removeBook(Reader reader, Book book)throws RemoteException {
        reader.removeBook(book);
    }

    @Override
    public void addBook(Reader reader, Book book) throws RemoteException {
        reader.addBook(book);
    }

    @Override
    public HashMap<Book, Date> getDateReturn(Reader reader) throws RemoteException {
        System.out.println(reader.getReturnList().get(reader.getBooksList().get(0)));
        return (HashMap<Book, Date>) reader.getReturnList();
    }
}
