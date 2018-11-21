package RPIS61.Kres.wdad;

import RPIS61.Kres.wdad.data.managers.JDBCDataManager;
import RPIS61.Kres.wdad.data.model.Book;
import RPIS61.Kres.wdad.data.model.Reader;

import java.rmi.RemoteException;

public class Application {
    public static void main(String[] args) throws RemoteException {
        JDBCDataManager jdbcDataManager = new JDBCDataManager();
//        jdbcDataManager.negligentReaders().forEach(System.out::println);
//        jdbcDataManager.addBook(jdbcDataManager.getReaders().get(1),jdbcDataManager.getBooks().get(1));
        jdbcDataManager.removeBook(jdbcDataManager.getReaders().get(1),jdbcDataManager.getBooks().get(1));
    }
}
