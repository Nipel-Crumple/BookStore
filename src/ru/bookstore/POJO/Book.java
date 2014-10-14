package ru.bookstore.POJO;

import java.util.UUID;

/**
 * Created by Johnny D on 07.10.2014.
 */
public class Book {
    public long getID() {
        if (id == 0) {
            setID();
        }
        return id;
    }
    private long id = 0;
    private String name = null;
    private String author = null;
    private String genre = null;

    private int hash = 0;

    public Book() {

    }

    public Book(String name, String author, String genre) {
        setName(name);
        setAuthor(author);
        setGenre(genre);
        setID();
        hash = hashCode();
    }

    public Book(long id,String name, String author, String genre) {
        setName(name);
        setAuthor(author);
        setGenre(genre);
        hash = hashCode();
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (id != book.id) return false;
        if (!author.equals(book.author)) return false;
        if (!genre.equals(book.genre)) return false;
        if (!name.equals(book.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + genre.hashCode();
        return result;
    }

    public void setID() {
        UUID uuid = UUID.randomUUID();
        id = -uuid.getLeastSignificantBits();
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
