package ru.bookstore.DAO;

/**
 * Created by Johnny D on 14.10.2014.
 */

import java.sql.*;
import java.util.*;
import org.apache.log4j.Logger;

import ru.bookstore.POJO.Book;

public class BookDAO extends BookStoreAccess {

    public List<Book> wholeBookList = new ArrayList<Book>();

    private static final Logger logger = Logger.getLogger("BookDAO");

    private static String REQUEST_BY_ID = "SELECT * FROM BOOK WHERE ID = ?";
    private static String REQUEST_ALL_BOOKS = "SELECT b.*, AVG(bm.mark) avg\n" +
                                              "FROM BOOK_MARK bm, BOOK b\n" +
                                              "WHERE b.ID = bm.BOOK_ID\n" +
                                              "GROUP BY b.name, b.author,b.GENRE, b.ID";
    private static String REQUEST_BY_NAME = "SELECT * FROM BOOK WHERE NAME = ?";
    private static String REQUEST_BY_AUTHOR = "SELECT * FROM BOOK WHERE AUTHOR = ?";
    private static String REQUEST_INSERT_BOOK = "INSERT INTO BOOK (ID, NAME, AUTHOR, GENRE) VALUES(?,?,?,?)";
    private static String REMOVE_BOOK = "DELETE FROM BOOK WHERE ID = ?";
    private static String RATE_REQUEST = "SELECT b.*, AVG(bm.mark) avg\n" +
                                         "FROM BOOK_MARK bm, BOOK b\n" +
                                         "WHERE b.ID = bm.BOOK_ID\n" +
                                         "GROUP BY b.name, b.author,b.GENRE, b.ID";

    private static String GET_CLIENT_BOOKS = "SELECT b.NAME, b.AUTHOR, b.GENRE\n" +
            "FROM BOOK b, CLIENT cl, HISTORY h\n" +
            "WHERE cl.ID = h.CLIENT_ID \n" +
            "  AND b.ID = h.BOOK_ID\n" +
            "  AND cl.ID=?";

    private static PreparedStatement removeBook;

    static{
        try {
            removeBook = con.prepareStatement(REMOVE_BOOK);
        } catch (SQLException e) {
            logger.error("SQL Exception in initialising of getting Book by ID", e);
        }
    }

    private static PreparedStatement getAllBooks;

    static {
        try {
            getAllBooks = con.prepareStatement(REQUEST_ALL_BOOKS);
        } catch (SQLException e) {
            logger.error("SQL Exception in initialising of getting Book by ID", e);
        }
    }

    private static PreparedStatement getBookByIdStmt;

    static {
        try {
            getBookByIdStmt = con.prepareStatement(REQUEST_BY_ID);
        } catch (SQLException e) {
            logger.error("SQL Exception in initialising of getting Book by ID", e);
        }
    }

    private static PreparedStatement getBookByNameStmt;

    static {
        try {
            getBookByNameStmt = con.prepareStatement(REQUEST_BY_NAME);
        } catch (SQLException e) {
            logger.error("SQL Exception in initialising of getting Book by Name", e);
        }
    }

    private static PreparedStatement getBookByAuthorStmt;

    static {
        try {
            getBookByAuthorStmt = con.prepareStatement(REQUEST_BY_AUTHOR);
        } catch (SQLException e) {
            logger.error("SQL Exception in initialising of getting Book by Author", e);
        }
    }

    private static PreparedStatement insertBookStmt;

    static {
        try {
            insertBookStmt = con.prepareStatement(REQUEST_INSERT_BOOK);
        } catch (SQLException e) {
            logger.error("SQL Exception in initialising of insert Book", e);
        }
    }

    private static PreparedStatement rateRequest;

    static {
        try {
            rateRequest = con.prepareStatement(RATE_REQUEST);
        } catch (SQLException e) {
            logger.error("SQL Exception in initialising of rateRequest");
        }
    }

    private static PreparedStatement clientBooks;

    static {
        try {
            clientBooks = con.prepareStatement(GET_CLIENT_BOOKS);
        } catch (SQLException e) {
            logger.error("SQL exception in initialising of change password request", e);
        }
    }

    public List<Book> getAllBooks() {
        Book newBook = null;
        long id;
        String name;
        String author;
        String genre;
        int avgMark = 0;

        ResultSet resultSet;
        try {
            resultSet = getAllBooks.executeQuery();
            while(resultSet.next()) {
                id = resultSet.getLong("ID");
                name = resultSet.getString("NAME");
                author = resultSet.getString("AUTHOR");
                genre = resultSet.getString("GENRE");
                avgMark = resultSet.getInt("AVG");
                newBook = new Book(id, name, author, genre, avgMark);
                if (!wholeBookList.contains(newBook)) {
                    wholeBookList.add(newBook);
                }
            }
        } catch (SQLException e) {
            logger.error("mistake in getting all books", e);
        }
        return wholeBookList;
    }



    public Book getBookById(long id) {
        Book newBook = null;
        String name;
        String author;
        String genre;

        ResultSet resultSet;
        try {
            getBookByIdStmt.setLong(1, id);
            resultSet = getBookByIdStmt.executeQuery();
            resultSet.next();
            name = resultSet.getString("NAME");
            author = resultSet.getString("AUTHOR");
            genre = resultSet.getString("GENRE");
            newBook = new Book(id, name, author, genre);
        } catch (SQLException e) {
            logger.error("There is no book with id: " + id);
        }
        return newBook;
    }

    public Book getBookByName(String name) {

        Book neededBook = null;

        try {
            getBookByNameStmt.setString(1, name);
            ResultSet results = getBookByNameStmt.executeQuery();
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

        try {
            getBookByAuthorStmt.setString(1, author);
            ResultSet results = getBookByAuthorStmt.executeQuery();
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

    public void addNewBook(Book newBook) {

        Book neededBook = getBookByName(newBook.getName());
        if (neededBook != null) {
            logger.debug("This name of Book already exists " + newBook.getName());
        } else {
            try {
                insertBookStmt.setLong(1, newBook.getID());
                insertBookStmt.setString(2, newBook.getName());
                insertBookStmt.setString(3, newBook.getAuthor());
                insertBookStmt.setString(4, newBook.getGenre());
                insertBookStmt.execute();
            } catch (SQLException e) {
                logger.error("SQL request insert error", e);
            }
        }
    }

    public void removeBook(long bookID) {
        Book neededBook = getBookById(bookID);
        if (neededBook == null) {
            logger.debug("This name of Book does not exists " + bookID);
        } else {
            try {
                removeBook.setLong(1, bookID);
            } catch (SQLException e) {
                logger.error("SQL request insert error", e);
            }
        }
    }

    public List<Book> getClientBooks(long clientID) {
        Book newBook = null;
        long id;
        String name;
        String author;
        String genre;
        List<Book> clientsBooks = new ArrayList<Book>();

        ResultSet resultSet;
        try {
            resultSet = getAllBooks.executeQuery();
            while(resultSet.next()) {
                id = resultSet.getLong("ID");
                name = resultSet.getString("NAME");
                author = resultSet.getString("AUTHOR");
                genre = resultSet.getString("GENRE");
                newBook = new Book(id, name, author, genre);
                clientsBooks.add(newBook);
            }
        } catch (SQLException e) {
            logger.error("mistake in getting all books", e);
        }
        return clientsBooks;
    }
}


