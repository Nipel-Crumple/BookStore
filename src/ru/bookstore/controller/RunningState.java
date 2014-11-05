package ru.bookstore.controller;

import org.apache.commons.cli.CommandLine;
import ru.bookstore.POJO.Book;
import ru.bookstore.POJO.BookMark;
import ru.bookstore.User.*;
import ru.bookstore.admin.Admin;
import ru.bookstore.view.ConsoleView;

import java.util.List;
import java.util.Map;

/**
 * Created by Johnny D on 02.11.2014.
 */
public class RunningState implements State {

    CommandLine cl = null;
    UserHelper usrHelper = null;
    UserCart usrCart = null;
    ConsoleView consoleView = null;
    Admin admin = null;

    private RunningState() {
    }

    public static RunningState getInstance() {
        return new RunningState();
    }

    @Override
    public String toString() {
        return "Running state";
    }

    @Override
    public void analyseCommands(Controller controller) {
        cl = controller.getCommandLine();
        usrHelper = controller.getUsrHelper();
        usrCart = usrHelper.getUsrCart();
        consoleView = controller.getConsoleView();
        if (cl.hasOption("help")) {
            controller.formatter.printHelp("BookStore", controller.getOptions());
            controller.setState(this);
            return;
        }

        if (cl.hasOption("add")) {
            admin = Admin.getInstance(consoleView);
            if (cl.getOptionValue("add").equalsIgnoreCase("client")) {
                consoleView.print("Name: ");
                String name = consoleView.readWholeLine();
                consoleView.print("Login: ");
                String login = consoleView.readLogin();
                consoleView.print("Password: ");
                String password = consoleView.readPassword();
                admin.addNewClient(name, login, password);
                consoleView.println("Client was added");
                return;
            }
            if (cl.getOptionValue("add").equalsIgnoreCase("book")) {
                consoleView.print("Name: ");
                String name = consoleView.readWholeLine();
                consoleView.print("Author: ");
                String author = consoleView.readWholeLine();
                consoleView.print("Genre: ");
                String genre = consoleView.readWholeLine();
                consoleView.print("Publishing: ");
                String publishing = consoleView.readWholeLine();
                admin.addNewBook(name, author, genre, publishing);
                consoleView.println("Book was added!");
                return;
            }
        }

        if (cl.hasOption("remove")) {
            admin = Admin.getInstance(consoleView);
            if (cl.getOptionValue("remove").equalsIgnoreCase("client")) {
                consoleView.print("Login: ");
                String login = consoleView.readLogin();
                admin.removeClient(login);
                consoleView.println("Client was removed");
                return;
            }
            if (cl.getOptionValue("remove").equalsIgnoreCase("book")) {
                consoleView.print("Name: ");
                String name = consoleView.readWholeLine();
                admin.removeBook(name);
                consoleView.println("Book was removed!");
                return;
            }
        }

        if (cl.hasOption("get")) {
            if (cl.getOptionValue("get").equalsIgnoreCase("mybooks")) {
                Map<Book, BookMark> map = usrHelper.getClientBooks();
                consoleView.printMapBooks(map);
                return;
            } else if (cl.getOptionValue("get").equalsIgnoreCase("allbooks")) {
                List<Book> listBook = usrHelper.getAllBooks();
                consoleView.printListBooks(listBook);
                return;
            } else {
                consoleView.print("Something's going wrong in getting books");
                return;
            }
        }

        if (cl.hasOption("put")) {
            consoleView.println("What book to put in your basket?");
            consoleView.printListBooks(usrHelper.getAllBooks());
            consoleView.print("Book's Name: ");
            String name = consoleView.readWholeLine();
            usrHelper.addToCart(name);
            return;
        }

        if (cl.hasOption("remove")) {
            consoleView.print("What book to remove from your basket?");
            usrCart.showCart(consoleView);
            consoleView.println();
            consoleView.print("Book's Name: ");
            String name = consoleView.readWholeLine();
            usrHelper.removeFromCart(name);
            return;
        }

        if (cl.hasOption("buy")) {
            usrHelper.buyBooks();
            consoleView.println("Has bought!");
            return;
        }

        if (cl.hasOption("clear")) {
            usrCart.clearCart();
            return;
        }

        if (cl.hasOption("show")) {
            if (cl.getOptionValue("show").equalsIgnoreCase("cart")) {
                usrCart.showCart(consoleView);
                return;
            } else {
                consoleView.println("Mistake in getting cart");
            }
        }

        if (cl.hasOption("state")) {
            consoleView.println(toString());
            return;
        }

        if (cl.hasOption("exit")) {
            controller.getUsrHelper().exitUserSession();
            admin.exitUserSession();
            controller.setState(StartState.getInstance());
            return;
        }

        if (cl.hasOption("change")) {
            if (cl.getOptionValue("change").equalsIgnoreCase("password")) {
                usrHelper.changePassword(usrHelper.getCurrentClient().getPassword());
                return;
            }
            if (cl.getOptionValue("change").equalsIgnoreCase("username")) {
                usrHelper.changeLogin(usrHelper.getCurrentClient().getPassword());
                return;
            }
        }

        if (cl.hasOption("close")) {
            System.exit(0);
        }

        if (true) {
            consoleView.println("Invalid command, try again");
            return;
        }
    }
}
