package ru.bookstore.controller;

import org.apache.commons.cli.*;
import ru.bookstore.view.ConsoleView;

import org.apache.log4j.Logger;

/**
 * Created by Johnny D on 28.10.2014.
 */
public class Controller {
    private Logger logger = Logger.getLogger("Controller.java");
    private ConsoleView consoleView = null;
    private Options options = new Options();
    private State state = null;
    private String login;
    private String password;
    private WelcomeState welcomeState = new WelcomeState();
    private ErrorState errorState = new ErrorState();
    private RunningState runningState = new RunningState();
    private CommandLineParser parser = new BasicParser();

    public Controller() {
        this.consoleView = new ConsoleView(System.in, System.out);
        options.addOption("exit", false, "Leaving User's session");
        options.addOption("set", true, "set session");
        options.addOption("close", false, "Stop running an application");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("BookStore", options);

    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return this.state;
    }

    private void setLogin() {
        consoleView.print("Login: ");
        login = consoleView.readLogin();
        if (login == null) {
            errorState.runController(this);
        }
    }

    private void setPassword() {
        consoleView.print("Password: ");
        password = consoleView.readPassword();
        if (password == null) {
            errorState.runController(this);
        }
    }

    public void createSession() {
        setLogin();
        setPassword();
        runningState.runController(this);
    }

    public void exitSession() {
        welcomeState.runController(this);
    }

    public void parseCommand() {
        try {
            System.out.print("BookStore: ");
            CommandLine cl = parser.parse(this.options, consoleView.readLine());

            if (!this.state.equals(this.errorState)) {
                if (cl.hasOption("exit")) {
                    exitSession();
                }

                if (cl.hasOption("set")) {
                    if (cl.getOptionValue("set").equalsIgnoreCase("session")) {
                        createSession();
                    }
                }

                if (cl.hasOption("close")) {
                    System.exit(0);
                }

            } else {
                logger.error("Error State in Controller. Try again!");
                this.setState(runningState);
            }

        } catch (ParseException e) {
            logger.error("Bad Command parser");
        }


    }

    public static void main(String[] args) {
        Controller cntr = new Controller();
        cntr.setState(cntr.welcomeState);
        while (true) {
            cntr.parseCommand();
        }
    }
}
