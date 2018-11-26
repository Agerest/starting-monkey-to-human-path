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

    private DataSource dataSource;

    public JDBCDataManager() {
        try {
            dataSource = DataSourceFactory.createDataSource();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setAuthor(Book book, Connection connection) {
        ResultSet result = null;
        try (PreparedStatement statement = connection.prepareStatement("select authors_id, first_name, second_name, birth_date from books_authors JOIN authors ON (authors.ID = books_authors.authors_id) where books_id = ?")) { //todo try-with-resources (connection & statement)
            statement.setInt(1,book.getID());
            result = statement.executeQuery();
            result.next();
            Author author = new Author();
            author.setID(result.getInt(1));
            author.setFirstName(result.getString(2));
            author.setSecondName(result.getString(3));
            author.setBirthDate(result.getDate(4));
            book.setAuthor(author);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try { result.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    private void setGenre(Book book, Connection connection) {
        ResultSet result = null;
        try(PreparedStatement statement = connection.prepareStatement("select genres_id, name, description from books_genres JOIN genres ON (genres.ID = books_genres.genres_id) where books_id = ?")) {//todo try-with-resources (connection & statement)
            statement.setInt(1, book.getID());
            result = statement.executeQuery();
            result.next();
            Genres genre = new Genres();
            genre.setID(result.getInt(1));
            genre.setName(result.getString(2));
            genre.setDescription(result.getString(3));
            book.setGenres(genre);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try { result.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    private void setBooks(Reader reader, Connection connection) {
        ResultSet result = null;
        try(PreparedStatement statement = connection.prepareStatement("select books_dictionary_id, take_date, name, description, print_year from books_readers JOIN books ON (books.ID = books_readers.books_dictionary_id) where readers_id = ?")) {//todo try-with-resources (connection & statement)
            statement.setInt(1, reader.getID());
            result = statement.executeQuery();
            Book book;
            while (result.next()) {
                book = new Book();
                book.setID(result.getInt(1));
                book.setTakeDate(result.getDate(2));
                book.setName(result.getString(3));
                book.setDescription(result.getString(4));
                book.setPrintYear(result.getInt(5));
                setAuthor(book, connection); //todo
                setGenre(book, connection);  //todo
                reader.addBook(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try { result.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    @Override
    public List<Reader> negligentReaders() throws RemoteException {
        List<Reader> negligentReaders = new ArrayList<>();
        ResultSet result = null;
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("select readers_id, first_name, second_name, birth_date from readers JOIN books_readers ON (readers.ID = books_readers.readers_id) where (CURRENT_DATE - take_date) > ?")
            ) {//todo try-with-resources (connection & statement)
            int countDay = 14;
            statement.setInt(1, countDay);
            result = statement.executeQuery();
            Reader reader;
            while (result.next()) {
                reader = new Reader();
                reader.setID(result.getInt(1));
                reader.setFirstName(result.getString(2));
                reader.setSecondName(result.getString(3));
                reader.setBirthDate(result.getDate(4));
                setBooks(reader, connection);//todo
                negligentReaders.add(reader);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try { result.close(); } catch (Exception e) { /* ignored */ }
        }
        return negligentReaders;
    }

    @Override
    public void removeBook(Reader reader, Book book) throws RemoteException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("delete from books_readers where readers_id=? and books_dictionary_id=?")
            ) {//todo try-with-resources (connection & statement)
            statement.setInt(1, reader.getID());
            statement.setInt(2, book.getID());
            statement.executeUpdate();
            reader.removeBook(book);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addBook(Reader reader, Book book) throws RemoteException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into books_readers values (default, ?, ?, current_date, DATE_ADD(current_date, INTERVAL 14 DAY))")
            ) {//todo try-with-resources (connection & statement)
            statement.setInt(1, book.getID());
            statement.setInt(2, reader.getID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HashMap<Book, Date> getDateReturn(Reader reader) throws RemoteException {
        HashMap<Book,Date> dateReturnList = new HashMap<>();
        ResultSet result = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select take_date, books_dictionary_id, name, description, print_year, return_date from books_readers join books on (books_readers.books_dictionary_id=books.id) where reader_ID = ?")
            ) {//todo try-with-resources (connection & statement)
            statement.setInt(1, reader.getID());
            result = statement.executeQuery();
            Book book;
            while (result.next()) {
                book = new Book();
                book.setID(result.getInt(1));
                book.setTakeDate(result.getDate(2));
                book.setName(result.getString(3));
                book.setDescription(result.getString(4));
                book.setPrintYear(result.getInt(5));
                setAuthor(book, connection);//todo
                setGenre(book, connection);//todo
                dateReturnList.put(book,result.getDate(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try { result.close(); } catch (Exception e) { /* ignored */ }
        }
        return dateReturnList;
    }

    @Override
    public List<Reader> getReaders() throws RemoteException {
        List<Reader> readers = new ArrayList<>();
        ResultSet result = null;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()
        ) {//todo try-with-resources (connection & statement)
            result = statement.executeQuery("select id, first_name, second_name, birth_date from readers");
            Reader reader;
            while (result.next()) {
                reader = new Reader();
                reader.setID(result.getInt(1));
                reader.setFirstName(result.getString(2));
                reader.setSecondName(result.getString(3));
                reader.setBirthDate(result.getDate(4));
                setBooks(reader, connection);//todo
                readers.add(reader);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try { result.close(); } catch (Exception e) { /* ignored */ }
        }
        return readers;
    }

}
