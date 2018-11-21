package RPIS61.Kres.wdad.data.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Reader implements Serializable {

    private String firstName;
    private String secondName;
    private Date birthDate;
    private ArrayList<Book> booksList;
    private HashMap<Book,Date> bookReturnList;
    private final long MILLISECONDS_IN_2_WEEKS = 7*24 * 60 * 60 * 1000 *2;
    private SimpleDateFormat format;
    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }



    public Reader() {
        booksList = new ArrayList<>();
        bookReturnList = new HashMap<>();
        format = new SimpleDateFormat("yyyy-MM-dd");
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setBirthDate(String birthDate) {
        try {
            this.birthDate = format.parse(birthDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        bookReturnList.put(book,new Date());
    }

    public Map<Book,Date> getReturnList() {
        return bookReturnList;
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
        return booksList.subList(0,booksList.size());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Читатель: ").append(firstName).append(" ").append(secondName).append("\n").append(getBooks());
        return sb.toString();
    }
}
