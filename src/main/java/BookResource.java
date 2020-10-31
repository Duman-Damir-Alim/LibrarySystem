import Model.Book;
import dao.DB;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/books")
public class BookResource {
    private DB db = new DB();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> list() {
        return db.listAll();
    }
//    @GET
//    @Path("{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response get(@PathParam("id") int id) {
//        Book book = db.
//    }
}
