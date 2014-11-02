package ru.bookstore.controller;

import org.apache.commons.cli.CommandLine;

/**
 * Created by Johnny D on 02.11.2014.
 */
public class StartState implements State {

    private StartState() {
    }

    public static StartState getInstance(){
        return new StartState();
    }

   @Override
   public String toString() {
       return "Starting state";
   }
    @Override
    public void analyseCommands(Controller controller, CommandLine cl) {
        controller.setState(this);
        if (cl.hasOption("help")) {
            controller.formatter.printHelp("BookStore", controller.getOptions());
            controller.setState(this);
            return;
        }

        if (cl.hasOption("state")) {
            controller.getConsoleView().println(toString());
            return;
        }

        if (cl.hasOption("set")) {
            if (cl.getOptionValue("set").equalsIgnoreCase("user") && controller.createUserSession()) {
                controller.setState(RunningState.getInstance());
                return;
            } else {
                controller.setState(this);
                return;
            }
        }

        if (cl.hasOption("close")) {
            System.exit(0);
        }
    }
}
