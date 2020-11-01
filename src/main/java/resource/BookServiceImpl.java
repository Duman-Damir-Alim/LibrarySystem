package resource;

import Model.Book;
import resource.BookService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BookServiceImpl implements BookService {
    private static Map<Integer, Book> books = new HashMap<Integer, Book>();


    public boolean addBook(Book b) {
        if(books.get(b.getId()) != null) return false;
        books.put(b.getId(),b);
        return true;
    }


    public boolean deleteBook(int id) {
        if(books.get(id)!=null) return false;
        books.remove(id);
        return true;
    }


    public Book getBook(int id) {
        return books.get(id);
    }


    public Book[] getAllBooks() {
        Set<Integer> ids = books.keySet();
        Book[] b = new Book[ids.size()];
        int i = 0;
        for(Integer id: ids) {
            b[i] = books.get(id);
            i++;
        }
        return b;
    }
}
