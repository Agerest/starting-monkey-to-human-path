package RPIS61.Kres.wdad.learn.xml;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Reader {

    private String firstName;
    private String secondName;
    private ArrayList<Book> booksList;
    private final long MILLISECONDS_IN_2_WEEKS = 7*24 * 60 * 60 * 1000 *2;

    public Reader() {
        booksList = new ArrayList<>();
    }

    public boolean isNegligent() {
        Date dateNow = new Date();
        for (Book book:booksList) {
            if ((dateNow.getTime()-book.getTakeDate().getTime()) > MILLISECONDS_IN_2_WEEKS)
                return true;
        }
        return false;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void removeBook(Book book) {
        booksList.remove(book);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void addBook(Book book) {
        booksList.add(book);
    }

    private String getBooks() {
        StringBuilder sb = new StringBuilder();
        sb.append("Книги, которые должен вернуть:\n");
        for (Book book:booksList) {
            sb.append(book.toString()).append("\n");
        }
        return sb.toString();
    }

    public List<Book> getBooksList() {
        return booksList.subList(0,booksList.size()-1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Читатель: ").append(firstName).append(" ").append(secondName).append("\n").append(getBooks());
        return sb.toString();
    }
}
