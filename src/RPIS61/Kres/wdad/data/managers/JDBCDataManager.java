package RPIS61.Kres.wdad.data.managers;

import RPIS61.Kres.wdad.data.model.Author;
import RPIS61.Kres.wdad.data.model.Book;
import RPIS61.Kres.wdad.data.model.Genres;
import RPIS61.Kres.wdad.data.model.Reader;
import RPIS61.Kres.wdad.data.storage.DataSourceFactory;

import javax.sql.DataSource;
import java.rmi.RemoteException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class JDBCDataManager implements DataManager {

    private Statement statement;
    //todo - в топку
    private List<Reader> readers;
    private List<Book> books;
    private List<Author> authors;
    private List<Genres> genres;
    private SimpleDateFormat format;

    public JDBCDataManager() {
        try {
            format = new SimpleDateFormat("yyyy-MM-dd");
            readers = new ArrayList<>();
            books = new ArrayList<>();
            authors = new ArrayList<>();
            genres = new ArrayList<>();
            DataSource dataSource = DataSourceFactory.createDataSource();
            Connection connection = dataSource.getConnection();
            statement = connection.createStatement();
            //readBD();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void readBD() throws SQLException {
        /* todo в топку =))))
        ResultSet resul = statement.executeQuery("select * from readers");
        Reader reader;
        while (resul.next()) {
            reader = new Reader();
            reader.setID(resul.getInt(1));
            reader.setFirstName(resul.getString(2));
            reader.setSecondName(resul.getString(3));
            reader.setBirthDate(resul.getString(4));
            readers.add(reader);
        }
        resul = statement.executeQuery("select * from authors");
        Author author;
        while (resul.next()) {
            author = new Author();
            author.setID(resul.getInt(1));
            author.setFirstName(resul.getString(2));
            author.setSecondName(resul.getString(3));
            author.setBirthDate(resul.getString(4));
            authors.add(author);
        }
        resul = statement.executeQuery("select * from genres");
        Genres genres;
        while (resul.next()) {
            genres = new Genres();
            genres.setID(resul.getInt(1));
            genres.setName(resul.getString(2));
            genres.setDescription(resul.getString(3));
            this.genres.add(genres);
        }
        resul = statement.executeQuery("select * from books");
        Book book;
        while (resul.next()) {
            book = new Book();
            book.setID(resul.getInt(1));
            book.setName(resul.getString(2));
            book.setDescription(resul.getString(3));
            book.setPrintYear(Integer.parseInt(resul.getString(4)));
            books.add(book);
        }
        */
    }

    private Reader currentReader(int ID) {
        for (Reader reader: readers) {
            if (reader.getID()==ID) return reader;
        }
        return null;
    }

    private Book currentBook(int ID) {
        for (Book book: books) {
            if (book.getID()==ID) return book;
        }
        return null;
    }

    @Override
    public List<Reader> negligentReaders() throws RemoteException {
        //todo getCOnnection(), createStatement, process, close Statement, close connection
        List<Reader> negligentReaders = new ArrayList<>();
        //long MILLISECONDS_IN_2_WEEKS = 7 * 24 * 60 * 60 * 1000 * 2;
        try {
            ResultSet books_readers = statement.executeQuery("select * from readers JOIN books_readers ON (readers.ID = book_readers.reader_id) where (CURRENT_DATE - DATE) > 12");
                //todo создаешь reader-ов и набиваешь negligentReaders

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return negligentReaders;

    }

    @Override
    public void removeBook(Reader reader, Book book) throws RemoteException {
        try {
            //todo preparedStatement
            PreparedStatement statement;
            ("delete from books_readers where readers_id=? and books_dictionary_id=?");
            statement.setInt(1, reader.getID());
            statement.setInt(2, book.getID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addBook(Reader reader, Book book) throws RemoteException {
        //todo preparedStatement
        StringBuilder sb = new StringBuilder();
        sb.append("insert into books_readers values (default,").append(book.getID())
                .append(",").append(reader.getID())
                .append(",\"").append(format.format(new Date()))
                .append("\",\"2019-12-12\")");
        try {
            statement.execute( sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public HashMap<Book, Date> getDateReturn(Reader reader) throws RemoteException {
        //todo preparedStatement
        HashMap<Book,Date> result = new HashMap<>();
        try {
            ResultSet resultSet = statement.executeQuery("select * from books_readers where reader_ID = ?");
            while (resultSet.next()) {
                result.put(currentBook(resultSet.getInt(2)),resultSet.getDate(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Reader> getReaders() throws RemoteException {
        //todo
        ResultSet resul = statement.executeQuery("select * from readers");
        Reader reader;
        while (resul.next()) {
            reader = new Reader();
            reader.setID(resul.getInt(1));
            reader.setFirstName(resul.getString(2));
            reader.setSecondName(resul.getString(3));
            reader.setBirthDate(resul.getString(4));
            readers.add(reader);
        }

    }

    public List<Book> getBooks() {
        return books.subList(0,books.size());
    }
}
