package RPIS61.Kres.wdad.learn.xml;

public class Book {

    private String authorFirstName;
    private String authorSecondName;
    private String name;
    private int printYear;
    private Genre genre;

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public void setAuthorSecondName(String authorSecondName) {
        this.authorSecondName = authorSecondName;
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

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public String getAuthorSecondName() {
        return authorSecondName;
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
        sb.append("Автор: ").append(authorFirstName).append(" ")
                .append(authorSecondName).append(" Название ")
                .append(name).append(" Год издания: ")
                .append(printYear).append(" Жанр: ")
                .append(genre);
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Book)) return false;
        return authorFirstName.equals(((Book) obj).authorFirstName)
                && authorSecondName.equals(((Book) obj).authorSecondName)
                && name.equals(((Book) obj).name)
                && printYear==((Book) obj).printYear
                && genre.equals(((Book) obj).genre);
    }
}
