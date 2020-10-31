package Model;

//Trying to push changes 3

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 668812036984863675L;
    private int id;
    private String name;
    private String author;
    private int countOfCopies;
    private String imageURL;

    public Book(int id, String name, String author, int countOfCopies, String imageURL) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.countOfCopies = countOfCopies;
        this.imageURL = imageURL;
    }

    public Book(String[] bookFields) {
        if (bookFields.length == 5) {
            this.id = Integer.parseInt(bookFields[0]);
            this.name = bookFields[1];
            this.author = bookFields[2];
            this.countOfCopies = Integer.parseInt(bookFields[3]);
            this.imageURL = bookFields[4];
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCountOfCopies() {
        return countOfCopies;
    }

    public void setCountOfCopies(int countOfCopies) {
        this.countOfCopies = countOfCopies;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", countOfCopies=" + countOfCopies +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
