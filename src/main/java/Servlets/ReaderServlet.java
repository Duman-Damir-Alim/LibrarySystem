package Servlets;

import Model.Book;
import dao.DB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "Servlets.ReaderServlet")
public class ReaderServlet extends HttpServlet {
    DB db = new DB();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("username");
        System.out.println("username " + username);
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        System.out.println("Bookid " + bookId);
        int countOfCopies = Integer.parseInt(request.getParameter("countOfCopies"));
        System.out.println("countOfCopies: " + countOfCopies);
        int added = 0;
        try {
            added = db.addBookToReader(username, bookId, countOfCopies);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("added:" + added);
        request.setAttribute("crud", "c" + added);
        request.getRequestDispatcher("/MainServlet").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Connection connection = DB.getConnection();
            ArrayList<Book> bookList = db.read(connection);
            connection.close();
            request.setAttribute("bookList", bookList);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        String recommendation = "Check out Master and Margarita!";
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(recommendation);
        request.getRequestDispatcher("reader.jsp");
    }
}