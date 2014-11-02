package ru.bookstore.controller;

import org.apache.commons.cli.*;
import ru.bookstore.DAO.ClientDAO;
import ru.bookstore.POJO.Client;
import ru.bookstore.view.ConsoleView;

import org.apache.log4j.Logger;

/**
 * Created by Johnny D on 28.10.2014.
 */
public class Controller {
    private Logger logger = Logger.getLogger("Controller.java");


    private ConsoleView consoleView = null;
    private Options options = new Options();
    private CommandLineParser parser = new BasicParser();
    private Client currentClient;
    private CommandLine cl = null;
    private ClientDAO clientAccessDB = new ClientDAO();
    private State state = StartState.getInstance();
    HelpFormatter formatter = new HelpFormatter();


    public Controller() {
        this.consoleView = new ConsoleView(System.in, System.out);
        options.addOption("exit", false, "Leaving User's session");
        options.addOption("set", true, "set session");
        options.addOption("change", true, "Changing <arg = password> or <arg = username>");
        options.addOption("close", false, "Stop running an application");
        options.addOption("help", false, "Possible commands");
        options.addOption("state", false, "Current state");

        formatter.printHelp("BookStore", options);
    }

    public ConsoleView getConsoleView() {
        return consoleView;
    }


    public Options getOptions() {
        return options;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public boolean checkUserValidity(String login, String password) {

        currentClient = clientAccessDB.getClientByLogin(login);
        if (currentClient != null) {
            return true;
        } else {
            return false;
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
        clientAccessDB = null;
        currentClient = null;
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void parseCommand() {
        try {
            System.out.print("BookStore: ");
            cl = parser.parse(this.options, consoleView.readLine());
            state.analyseCommands(this, this.cl);

        } catch (ParseException e) {
            logger.error("Bad Command parser");
        }
    }

    public static void main(String[] args) {
        Controller cntr = new Controller();
        while (true) {
            cntr.parseCommand();
        }
    }
}
