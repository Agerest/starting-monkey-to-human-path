package RPIS61.Kres.wdad.learn.xml;

public class TestXmlTask {
    public static void main(String[] args) {
        XmlTask task = new XmlTask();
        Book book = new Book();
        book.setAuthorFirstName("wwwe");
        book.setAuthorSecondName("weee");
        book.setGenre(Genre.EPOPEE);
        book.setName("ttt");
        book.setPrintYear(1999);
        book.setTakeDate("11.12.2013");
//        task.addBook(XmlTask.getReaders().get(1),book);
//        task.removeBook(XmlTask.getReaders().get(1),book);
        XmlTask.getReaders().forEach(System.out::println);
    }
}
