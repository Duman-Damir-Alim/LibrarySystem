package resource;

import Model.Book;

public interface BookService {
    //asd
    public boolean addBook(Book b);
    public boolean deleteBook(int id);
    public Book getBook(int id);
    public Book[] getAllBooks();
}
