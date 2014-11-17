package ru.bookstore.User;

import ru.bookstore.DAO.*;
import ru.bookstore.DAO.ClientDAO;
import ru.bookstore.DAO.HistoryDAO;
import ru.bookstore.POJO.*;
import ru.bookstore.view.ConsoleView;

import java.util.*;
import java.sql.Date;

/**
 * Created by Johnny D on 28.10.2014.
 */
public class UserHelper {
    private ConsoleView consoleView;
    Map<Book, BookMark> mapBooksAndMarks = new TreeMap<Book, BookMark>(new Comparator<Book>() {
        @Override
        public int compare(Book o1, Book o2) {
            return o1.getName().compareTo(o2.getName());
        }
    });

    private Client currentClient;

    private UserCart usrCart = new UserCart();

    private ClientDAO clientAccessDB = new ClientDAO();
    private HistoryDAO clientHistoryDB = new HistoryDAO();
    private BookDAO clientBookDB = new BookDAO();
    private BookMarkDAO clientBookMarkDAO = new BookMarkDAO();

    public UserHelper(ConsoleView consoleView) {
        this.consoleView = consoleView;
    }

    public boolean checkUserValidity(String login, String password) {
        currentClient = clientAccessDB.getClientByLogin(login);
        if (currentClient != null) {
            if (currentClient.getPassword().equals(password)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void addToCart(String name) {
        Book book = clientBookDB.getBookByName(name);
        usrCart.addToCart(book);
        consoleView.println("added");
    }

    public void removeFromCart(String name) {
        Book book = clientBookDB.getBookByName(name);
        if (book == null) {
            consoleView.println("There is no such book in cart");
        } else {
            usrCart.removeFromCart(book);
            consoleView.println("removed");
        }
    }

    public UserCart getUsrCart() {
        return usrCart;
    }

    public void buyBooks() {
        for(Book temp : usrCart.getNeededBooks()) {
            Calendar calendar = Calendar.getInstance();
            Date date = new Date(calendar.getTimeInMillis());
            History newHistory = new History(currentClient.getID(), temp.getID(), date);
            clientHistoryDB.addNewHistoryNote(newHistory);
        }
    }

    public void changePassword(String oldPassword) {
        consoleView.print("Enter new password: ");
        String newPassword = consoleView.readPassword();
        clientAccessDB.changeClientPassword(currentClient, oldPassword, newPassword);
    }

    public void changeLogin(String oldPassword) {
        consoleView.print("Enter new login: ");
        String newLogin = consoleView.readLogin();
        clientAccessDB.changeLogin(currentClient, newLogin, oldPassword);
    }

    public boolean createUserSession() {
        String login;
        String password;
        consoleView.print("Login: ");
        login = consoleView.readLogin();
        consoleView.print("Password: ");
        password = consoleView.readPassword();
        if (checkUserValidity(login, password)) {
            consoleView.println("Success!");
            return true;
        } else {
            consoleView.println("Invalid username or password");
            return false;
        }
    }

    public void exitUserSession() {
        usrCart.clearCart();
        currentClient = null;
        mapBooksAndMarks.clear();
    }

    public Map<Book, BookMark> getClientBooks() {
        List<History> currentClientHistory = clientHistoryDB.getHistoryByClientID(currentClient.getID());

        for (History temp : currentClientHistory) {
            Book book = clientBookDB.getBookById(temp.getBook_id());
            BookMark bookMark = clientBookMarkDAO.getBookMarkbyClientAndBookID(currentClient.getID(), temp.getBook_id());
            mapBooksAndMarks.put(book, bookMark);
        }
        return mapBooksAndMarks;
    }

    public List<Book> getAllBooks() {
        return clientBookDB.getAllBooks();
    }

    public Map<Book, Integer> getAllBooksWithMarks() {
        List<Book> listBook = clientBookDB.getAllBooks();
        Map<Book, Integer> map = new TreeMap<Book, Integer>(new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        for(Book temp : listBook) {
            int summ = 0;
            int iter = 0;
            for (BookMark tempBookMark : clientBookMarkDAO.getBookMarkByBookID(temp.getID())) {
                summ += tempBookMark.getMark();
                iter++;
            }
            if (iter != 0){
                Integer average = summ / iter;
                map.put(temp, average);
            } else {
                map.put(temp, 0);
            }
        }
        return map;
    }

    public void rateBook(String bookName, int mark) {
        long bookID = clientBookDB.getBookByName(bookName).getID();
        long clientID = currentClient.getID();
        if (clientBookMarkDAO.getBookMarkbyClientAndBookID(clientID, bookID) == null) {
            BookMark bm = new BookMark(clientID, bookID, mark);
            clientBookMarkDAO.addNewMark(bm);
        } else {
            consoleView.println("The mark is already exist!");
        }
    }


    public Client getCurrentClient() {
        return currentClient;
    }

}
