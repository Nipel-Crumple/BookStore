package ru.bookstore.DAO;

/**
 * Created by Johnny D on 14.10.2014.
 */

import java.sql.*;
import java.util.*;
import org.apache.log4j.Logger;

import ru.bookstore.POJO.Book;

public class BookDAO extends BookStoreAccess {

    public Set<Book> bookSet = new HashSet<Book>();

    private static final Logger logger = Logger.getLogger("BookDAO");

    private void getAllBooksFromDB() {
        Book newBook;
        long id;
        String name;
        String author;
        String genre;
        String sqlRequest = "SELECT * FROM BOOK";
        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sqlRequest);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getLong("ID");
                name = resultSet.getString("NAME");
                author = resultSet.getString("AUTHOR");
                genre = resultSet.getString("GENRE");
                newBook = new Book(id, name, author, genre);
                bookSet.add(newBook);
            }

        } catch (SQLException e1) {
            logger.error("Smth's going wrong in getting all books", e1.getCause());
        }

    }

    public Set<Book> getAllBooks() {
        getAllBooksFromDB();
        return bookSet;
    }

    public Book getBookById(long id) {
        Book newBook = null;
        String name;
        String author;
        String genre;
        String sqlRequest = "SELECT * from BOOK where ID=" + id;
        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sqlRequest);
            resultSet = preparedStatement.executeQuery(sqlRequest);
            resultSet.next();
            name = resultSet.getString("NAME");
            author = resultSet.getString("AUTHOR");
            genre = resultSet.getString("GENRE");
            newBook = new Book(id, name, author, genre);
            bookSet.add(newBook);
        } catch (SQLException e) {
            logger.error("Something's going bad in getBookById");
        }
        return newBook;
    }

    public Book getBookByName(String name) {

        Book neededBook = null;
        String sql = "SELECT * FROM BOOK WHERE NAME = '" + name + "'";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet results = statement.executeQuery();
            results.next();
            neededBook = getBookById(results.getLong("ID"));
        } catch (SQLException e) {
            logger.debug("There is no book with this name: " + name);
        }
        return neededBook;
    }

    public Set<Book> getBooksByAuthor(String author) {

        Set<Book> listBook;
        listBook = new TreeSet<Book>(new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        String sql = "SELECT * FROM BOOK WHERE AUTHOR = '" + author + "'";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                Book neededBook;
                neededBook = getBookById(results.getLong("ID"));
                listBook.add(neededBook);
            }
        } catch (SQLException e) {
            logger.debug("There is no book with this author" + author);
        }
        return listBook;
    }

    public Book addNewBook(Book newBook) {
        String sql = "INSERT INTO BOOK (ID, NAME, AUTHOR, GENRE) VALUES(?,?,?,?)";

        Book neededBook = getBookByName(newBook.getName());
        if (neededBook != null) {
            logger.debug("This name of Book already exists " + newBook.getName());
            return neededBook;
        } else {
            try {
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setLong(1, newBook.getID());
                statement.setString(2, newBook.getName());
                statement.setString(3, newBook.getAuthor());
                statement.setString(4, newBook.getGenre());
                statement.execute();
                return getBookById(newBook.getID());
            } catch (SQLException e) {
                logger.error("SQL request insert error", e);
            }
        }
        return neededBook;

    }

}
