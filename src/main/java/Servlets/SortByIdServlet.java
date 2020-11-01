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

@WebServlet(name = "Servlets.SortByIdServlet")
public class SortByIdServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DB db = new DB();
        System.out.println("im here");
        try {
            System.out.println("im in try block");
            Connection connection = DB.getConnection();
            ArrayList<Book> bookList = db.read(connection);
            Book book = new Book();
            String price = request.getParameter("sort");
            if (price.equals("low-to-high")) {
                bookList = book.lowToHigh(bookList);
            } else {
                bookList = book.highToLow(bookList);
            }
            connection.close();
            request.removeAttribute("bookList");
            request.setAttribute("bookList", bookList);
            HttpSession session = request.getSession(true);
            System.out.println(session.getAttribute("username"));
            if (session.getAttribute("username").equals("admin")) {
                System.out.println("Redirecting to librarian");
                request.getRequestDispatcher("librarian.jsp").forward(request,response);
            } else {
                request.getRequestDispatcher("reader.jsp").forward(request, response);
            }
        } catch (SQLException exception) {
            System.out.println("im in catch block");
            exception.printStackTrace();
        }
    }
}

