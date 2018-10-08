package RPIS61.Kres.wdad.learn.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Reader {

    private String firstName;
    private String secondName;
    private ArrayList<Book> booksList;
    private ArrayList<Date> dateList;
    private SimpleDateFormat format;

    public Reader() {
        booksList = new ArrayList<>();
        dateList = new ArrayList<>();
        format = new SimpleDateFormat("dd.MM.yyyy");
    }

    public boolean isNegligent() {
        Date dateNow = new Date();
        for (Date date:dateList) {
            if ((double)(dateNow.getTime()-date.getTime())/(7*24 * 60 * 60 * 1000)>2)
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
        dateList.remove(booksList.indexOf(book));
        booksList.remove(book);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void addBook(Book book, String date) {
        booksList.add(book);
        try {
            dateList.add(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getBooks() {
        StringBuilder sb = new StringBuilder();
        sb.append("Книги, которые должен вернуть:\n");
        for (int i = 0; i < booksList.size(); i++) {
            sb.append(booksList.get(i).toString()).append("\n");
            sb.append("Дата выдачи: " + format.format(dateList.get(i))).append("\n");
        }
        return sb.toString();
    }

    public ArrayList<Book> getBooksList() {
        return booksList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Читатель: ").append(firstName).append(" ").append(secondName);
        return sb.toString();
    }
}
