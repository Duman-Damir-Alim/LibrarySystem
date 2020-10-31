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
        Context initialContext = null;
        Connection connection = null;

        try {
            initialContext = new InitialContext();
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

    public static boolean checkLibrarian(String username, String password) {
        boolean st = false;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM librarian WHERE username=? and password=?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            st = resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return st;
    }

    public static boolean checkReader(String username, String password) {
        boolean st2 = false;
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM reader WHERE username=? and password=?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            st2 = resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return st2;
    }

    public List<Book> listAll() {
        List<Book> books = new ArrayList<>();
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * From book");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public int add(int id, String name, String author, int countOfCopies, String imageUrl) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int added = 0;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("Insert into book(book_id, book_name, author, countofcopies, book_url) VALUES (?,?,?,?,?)");
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

    public int update(int id, String name, String author, int countOfCopies, String imageURL) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int updated = 0;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("update book set book_name=?, author=?, countofcopies=?, book_url=? where id=?");
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


    public int delete(String id) {
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

    public ArrayList<Book> search(String name) {
        ArrayList<Book> bookList = new ArrayList();
        try {
            System.out.println("trying to search"); //
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

}
