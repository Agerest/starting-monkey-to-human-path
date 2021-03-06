package RPIS61.Kres.wdad.data.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Book implements Serializable {

    private Author author;
    private String name;
    private int printYear;
    private Genre genre;
    private Date takeDate;
    private SimpleDateFormat format;

    public Book() {
        format = new SimpleDateFormat("dd.MM.yyyy");
    }

    public Date getTakeDate() {
        return takeDate;
    }
    public void setTakeDate(Date takeDate) {
       this.takeDate = takeDate;
    }
    public void setTakeDate(String takeDate) {
        try {
            this.takeDate = format.parse(takeDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrintYear(int printYear) {
        this.printYear = printYear;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public int getPrintYear() {
        return printYear;
    }

    public Genre getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Автор: ").append(author.toString()).append(", Название: ")
                .append(name).append(", Год издания: ")
                .append(printYear).append(", Жанр: ")
                .append(genre).append(", Дата выдачи: ")
                .append(format.format(takeDate));
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Book)) return false;
        return author.equals(((Book) obj).getAuthor())
                && name.equals(((Book) obj).name)
                && printYear==((Book) obj).printYear
                && genre.equals(((Book) obj).genre);
    }
}
