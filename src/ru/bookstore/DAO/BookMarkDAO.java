package ru.bookstore.DAO;

import org.apache.log4j.Logger;
import ru.bookstore.POJO.BookMark;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Johnny D on 15.10.2014.
 */
public class BookMarkDAO extends BookStoreAccess {

    private static final Logger logger = Logger.getLogger("BookMarkDAO");

    private static String REQUEST_BY_ID = "SELECT * from BOOK_MARK where ID = ?";
    private static String REQUEST_BY_CLIENT_ID = "SELECT * FROM BOOK_MARK WHERE CLIENT_ID = ?";
    private static String REQUEST_BY_BOOK_ID = "SELECT * FROM BOOK_MARK WHERE BOOK_ID = ?";
    private static String REQUEST_RATE = "SELECT * FROM BOOK_MARK WHERE MARK=?";
    private static String REQUEST_INSERT = "INSERT INTO BOOK_MARK (ID, CLIENT_ID, BOOK_ID, MARK) VALUES(?,?,?,?)";

    private static PreparedStatement getBookMarkByIdStmt;

    static {
        try {
            getBookMarkByIdStmt = con.prepareStatement(REQUEST_BY_ID);
        } catch (SQLException e) {
            logger.error("SQL Exception in initialising of getting BookMark by ID", e);
        }
    }

    private static PreparedStatement getBookMarkByClientIDStmt;

    static {
        try {
            getBookMarkByClientIDStmt = con.prepareStatement(REQUEST_BY_CLIENT_ID);
        } catch (SQLException e) {
            logger.error("SQL Exception in initialising of getBookMark by Client_id: ", e);
        }
    }

    private static PreparedStatement getBookMarkByBookIDStmt;

    static {
        try {
            getBookMarkByBookIDStmt = con.prepareStatement(REQUEST_BY_BOOK_ID);
        } catch (SQLException e) {
            logger.error("SQL Exception in initialising of getBookMark by Book_id: ", e);
        }
    }

    private static PreparedStatement getBookMarkRate;

    static {
        try {
            getBookMarkRate = con.prepareStatement(REQUEST_RATE);
        } catch (SQLException e) {
            logger.error("SQL Exception in initialising of get BookMark by mark", e);
        }
    }

    private static PreparedStatement insertBookMark;

    static {
        try {
            insertBookMark = con.prepareStatement(REQUEST_INSERT);
        } catch (SQLException e) {
            logger.error("SQL Exception in initialising of get BookMark by mark", e);
        }
    }


    public BookMark getBookMarkById(long id) {
        BookMark newBookMark = null;
        long clientID;
        long bookID;
        int mark;

        ResultSet resultSet;
        try {
            getBookMarkByIdStmt.setLong(1, id);
            resultSet = getBookMarkByIdStmt.executeQuery();
            resultSet.next();
            clientID = resultSet.getLong("CLIENT_ID");
            bookID = resultSet.getLong("BOOK_ID");
            mark = resultSet.getInt("MARK");
            newBookMark = new BookMark(clientID, bookID, mark);
        } catch (SQLException e) {
            logger.error("There is no bookMark with id: " + id);
        }
        return newBookMark;
    }

    public BookMark getBookMarkByClientID(long client_id) {
        BookMark neededBookMark = null;
        try {
            getBookMarkByClientIDStmt.setLong(1, client_id);
            ResultSet results = getBookMarkByClientIDStmt.executeQuery();
            results.next();
            neededBookMark = getBookMarkById(results.getLong("ID"));
        } catch (SQLException e) {
            logger.debug("There is no BookMarks of client with id: " + client_id);
        }
        return neededBookMark;
    }

    public List<BookMark> getBookMarkByBookID(long book_id) {

        ArrayList<BookMark> listBookMark;
        listBookMark = new ArrayList<BookMark>();

        try {
            getBookMarkByBookIDStmt.setLong(1, book_id);
            ResultSet results = getBookMarkByBookIDStmt.executeQuery();
            while (results.next()) {
                BookMark neededBook;
                neededBook = getBookMarkById(results.getLong("ID"));
                listBookMark.add(neededBook);
            }
        } catch (SQLException e) {
            logger.debug("There is no bookMark with book_id: " + book_id);
        }
        return listBookMark;
    }

    public List<BookMark> getBookMarkByMark(int mark) {

        ArrayList<BookMark> listBookMark;
        listBookMark = new ArrayList<BookMark>();

        try {
            getBookMarkRate.setInt(1, mark);
            ResultSet results = getBookMarkRate.executeQuery();
            while (results.next()) {
                BookMark neededBookMark;
                neededBookMark = getBookMarkById(results.getLong("ID"));
                listBookMark.add(neededBookMark);
            }
        } catch (SQLException e) {
            logger.debug("There is no bookMark with mark: " + mark);
        }
        return listBookMark;
    }

    public void addNewMark(BookMark newBookMark) {
        BookMark ifMarkExists = getBookMarkById(newBookMark.getID());
        if (ifMarkExists == null) {
            try {
                insertBookMark.setLong(1, newBookMark.getID());
                insertBookMark.setLong(2, newBookMark.getClient_id());
                insertBookMark.setLong(3, newBookMark.getBook_id());
                insertBookMark.setInt(4, newBookMark.getMark());
                insertBookMark.execute();
            } catch (SQLException e) {
                logger.error("SQL request insert error", e);
            }
        } else {
            logger.debug("Mark already exists with ID: " + newBookMark.getID());
        }
    }
    /* public static void main(String[] args) {
        BookMarkDAO bk = new BookMarkDAO();


        ClientDAO cl = new ClientDAO();
        for (BookMark temp : bk.getBookMarkByBookID(12L)) {
            System.out.println(cl.getClientById(temp.getClient_id()).getName());
        }
    }*/



}
