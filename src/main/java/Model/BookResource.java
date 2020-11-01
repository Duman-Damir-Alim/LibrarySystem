package Model;

import Model.Book;
import Model.BookDao;
import dao.DB;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/books")
public class BookResource {
    private DB db = new DB();
    private BookDao dao = BookDao.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> list() {
        return db.listAll();
    }
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {
        Book book = db.getById(id);
        if(book != null) {
            return Response.ok(book, MediaType.APPLICATION_JSON).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void add(Book book)  {
        db.add(book);
    }
    @DELETE
    @Path("{id}")
    public void deleteById(@PathParam("id") int id) {
        db.deleteById(id);
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(Book book) {
        db.update(book);
    }
}
