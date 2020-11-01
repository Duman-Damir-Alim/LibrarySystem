package Model;

import dao.DB;

import java.util.ArrayList;
import java.util.List;

public class BookDao {
    public static DB db = new DB();
    public static BookDao instance;
    private static List<Book> books;

    static {
        books = db.listAll();
    }
    public static BookDao getInstance() {
        if(instance == null) {
            instance = new BookDao();
        }
        return instance;
    }
    public Book getBookById(int id) {
        Book bookToFind = new Book(id);
        int index = books.indexOf(bookToFind);
        if(index >= 0 ) {
            return books.get(index);
        }
        return null;
    }
    public int add(Book book) {
        int newId = books.size() + 1;
        book.setId(newId);
        books.add(book);
        return newId;
    }
    public List<Book> listAll() {
        return new ArrayList<Book>(books);
    }
    public boolean delete(int id) {
        Book bookToFind = new Book(id);
        int index = books.indexOf(bookToFind);
        if(index >=0) {
            books.remove(index);
            return true;
        }
        return false;
    }
    public boolean update(Book book) {
        int index = books.indexOf(book);
        if (index >=0) {
            books.set(index, book);
            return true;
        }
        return false;
    }

}
