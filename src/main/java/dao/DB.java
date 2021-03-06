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
        List<Book> books = new ArrayList<>();
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * From book");
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

    public int addBookToReader(String username, int bookId, int countOfCopies) throws SQLException {
        int added = 0;
        if (!checkBooksAmount(bookId, countOfCopies)) {
            System.out.println("checkBooksAmount failed");
            return 0;
        }
        try {
            System.out.println("try block addbooktoreader");
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from reader where username = ? and borrowed_book_id = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, bookId);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.executeQuery();

            PreparedStatement preparedStatement6 = connection.prepareStatement("select countofcopies from book where book_id = ?");
            preparedStatement6.setInt(1, bookId);
            ResultSet resultSet2 = preparedStatement6.executeQuery();
            int amountOfBooksInDB = 0;
            while (resultSet2.next()) {
                amountOfBooksInDB = resultSet2.getInt("countofcopies");
            }

            PreparedStatement preparedStatement8 = connection.prepareStatement("select borrowed_amount from reader where username = ?");
            preparedStatement8.setString(1, username);
            ResultSet resultSet3 = preparedStatement8.executeQuery();
            int amountOfBooksUserHas = 0;
            while (resultSet3.next()) {
                amountOfBooksUserHas = resultSet3.getInt("borrowed_amount");
            }
            preparedStatement8.close();

            if (resultSet.next()) {
                System.out.println("resultsetnext:");
                PreparedStatement preparedStatement2 = connection.prepareStatement("update reader set borrowed_amount = ? where username = ? and borrowed_book_id = ?");
                preparedStatement2.setInt(1, amountOfBooksUserHas + countOfCopies);
                preparedStatement2.setString(2, username);
                preparedStatement2.setInt(3, bookId);
                added = preparedStatement2.executeUpdate();
                preparedStatement2.close();

                System.out.println("amount: " + amountOfBooksUserHas);
                PreparedStatement preparedStatement5 = connection.prepareStatement("update book set countofcopies = ? where book_id = ?");
                preparedStatement5.setInt(1, amountOfBooksInDB - countOfCopies);
                preparedStatement5.setInt(2, bookId);
                added = preparedStatement5.executeUpdate();

                preparedStatement5.close();
                connection.close();
            } else {
                System.out.println("else clause");
                PreparedStatement preparedStatement4 = connection.prepareStatement("select reader_id from reader where username = ?");
                preparedStatement4.setString(1, username);
                resultSet = preparedStatement4.executeQuery();
                int readerId = 0;
                while (resultSet.next()) {
                    readerId = resultSet.getInt("reader_id");
                }
                PreparedStatement preparedStatement3 = connection.prepareStatement("insert into reader(reader_id, username, borrowed_book_id, borrowed_amount) values (?,?,?,?)");
                preparedStatement3.setInt(1, readerId);
                preparedStatement3.setString(2, username);
                preparedStatement3.setInt(3, bookId);
                preparedStatement3.setInt(4, countOfCopies);

                added = preparedStatement3.executeUpdate();
                preparedStatement3.close();
                preparedStatement4.close();

                PreparedStatement preparedStatement7 = connection.prepareStatement("update book set countofcopies = ? where book_id = ?");
                preparedStatement7.setInt(1, amountOfBooksInDB - countOfCopies);
                preparedStatement7.setInt(2, bookId);
                added = preparedStatement7.executeUpdate();

                preparedStatement7.close();
                connection.close();

                return added;
            }
            preparedStatement6.close();
        } catch (SQLException e) {
            System.out.println("catch clause:");
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

    private boolean checkBooksAmount(int bookId, int countOfCopies) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select countofcopies from book where book_id = ?");
            preparedStatement.setInt(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int amount = resultSet.getInt("countofcopies");
                System.out.println("amount of books: " + amount);
                if (amount - countOfCopies >= 0) {
                    return true;
                }
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
}
