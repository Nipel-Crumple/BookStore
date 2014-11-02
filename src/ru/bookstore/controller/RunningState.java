package ru.bookstore.controller;

import org.apache.commons.cli.CommandLine;

/**
 * Created by Johnny D on 02.11.2014.
 */
public class RunningState implements State {

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
    public void analyseCommands(Controller controller, CommandLine cl) {

        if (cl.hasOption("exit")) {
            controller.setState(StartState.getInstance());
            return;
        }
        if (cl.hasOption("help")) {
            controller.formatter.printHelp("BookStore", controller.getOptions());
            controller.setState(this);
            return;
        }

        if (cl.hasOption("state")) {
            controller.getConsoleView().println(toString());
            return;
        }

        if (cl.hasOption("exit")) {
            controller.exitUserSession();
            controller.setState(this);
            return;
        }

        if (cl.hasOption("change")) {
            if (cl.getOptionValue("change").equalsIgnoreCase("password")) {
                controller.changePassword(controller.getCurrentClient().getPassword());
                return;
            }
            if (cl.getOptionValue("change").equalsIgnoreCase("username")) {
                controller.changeLogin(controller.getCurrentClient().getPassword());
                return;
            }
        }

        if (cl.hasOption("close")) {
            System.exit(0);
        }
    }
}
