package RPIS61.Kres.wdad;

import RPIS61.Kres.wdad.data.managers.JDBCDataManager;
import RPIS61.Kres.wdad.data.model.Book;
import RPIS61.Kres.wdad.data.model.Reader;

import java.rmi.RemoteException;

public class Application {
    public static void main(String[] args) throws RemoteException {
        JDBCDataManager jdbcDataManager = new JDBCDataManager();
//        jdbcDataManager.getReaders().forEach(System.out::println);
        jdbcDataManager.negligentReaders().forEach(System.out::println);
    }
}
