package dao;

import Model.Book;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {
    private static List<Book> list = new ArrayList<>();

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Context initialContext = new InitialContext();
            Context envCtx = (Context) initialContext.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/week");
            connection = ds.getConnection();
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public ArrayList<Book> read(Connection connection) {
        ArrayList<Book> bookList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select*from book");
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numOfColumns = metaData.getColumnCount();
            Book book;
            while (resultSet.next()) {
                String[] bookFields = new String[numOfColumns];
                for (int a = 1; a <= numOfColumns; a++) {
                    bookFields[a - 1] = resultSet.getObject(a).toString();
                }
                book = new Book(bookFields);
                bookList.add(book);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return bookList;
    }

    public static boolean checkReader(String username, String password) {
        boolean st = false;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM reader WHERE username=? and password=?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            st = resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return st;
    }

    public List<Book> listAll() {
        ArrayList<Book> books = new ArrayList<>();
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * From book");
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Book book = new Book(rs.getInt(1),rs.getString(2), rs.getString(3),rs.getInt(4),rs.getString(5));
                books.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public int addBook(int id, String name, String author, int countOfCopies, String imageUrl) {
        int added = 0;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("Insert into book(book_id, book_name, author, countofcopies, book_url) VALUES (?,?,?,?,?)");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, author);
            preparedStatement.setInt(4, countOfCopies);
            preparedStatement.setString(5, imageUrl);
            added = preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return added;
    }

    public String addReader(int id, String username, String password, String address, String phone) {
        String errorMessage = "ok";
        if (!checkReaderId(id)) {
            errorMessage = "reader_id " + id + " already exist!";
        } else {
            try {
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("Insert into reader(reader_id, username, password, address, phone) VALUES (?,?,?,?,?)");
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, password);
                preparedStatement.setString(4, address);
                preparedStatement.setString(5, phone);
                int result = preparedStatement.executeUpdate();
                if (result == 0) {
                    errorMessage = "Couldn't add reader for some reason";
                }
                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                return errorMessage;
            }
        }
        return errorMessage;
    }

    private boolean checkReaderId(int id) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select reader_id from reader where reader_id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkBooksAmount(int id) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select countofcopies from book where book_id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            int amount = resultSet.getInt("countofcopies");
            if (amount != 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int updateBook(int id, String name, String author, int countOfCopies, String imageURL) {
        int updated = 0;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("update book set book_name=?, author=?, countofcopies=?, book_url=? where book_id=?");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, author);
            preparedStatement.setInt(3, countOfCopies);
            preparedStatement.setString(4, imageURL);
            preparedStatement.setInt(5, id);
            updated = preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return updated;
    }


    public int deleteBook(String id) {
        int deleted = 0;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("Delete from book WHERE book_id =?");
            preparedStatement.setInt(1, Integer.parseInt(id));
            deleted = preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return deleted;
    }

    public ArrayList<Book> searchReader(String name) {
        ArrayList<Book> bookList = new ArrayList();
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("Select * from book where book_name=? or author=?");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            Book book;
            while (resultSet.next()) {
                String[] bookFields = new String[numberOfColumns];
                for (int a = 1; a <= numberOfColumns; a++) {
                    bookFields[a - 1] = resultSet.getObject(a).toString();
                }
                book = new Book(bookFields);
                bookList.add(book);
            }
            resultSet.close();
            connection.close();
            preparedStatement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return bookList;
    }

    public Book getById(Integer id) {
        Book book = new Book();

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * From book where book_id = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                book = new Book(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5));
            }
            return book;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    public void add(Book book)  {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("Insert into book(book_id, book_name, author,countofcopies, book_url) VALUES (?,?,?,?,?)?");
            preparedStatement.setInt(1, book.getId());
            preparedStatement.setString(2, book.getName());
            preparedStatement.setString(3,book.getAuthor());
            preparedStatement.setInt(4,book.getCountOfCopies());
            preparedStatement.setString(5, book.getImageURL());
            preparedStatement.executeQuery();
            System.out.println("Book successfully added.");
            connection.close();
            preparedStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
