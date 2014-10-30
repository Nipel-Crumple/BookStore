package ru.bookstore.controller;

/**
 * Created by Johnny D on 28.10.2014.
 */
public class ErrorState implements State {

    public void runController(Controller controller) {
        System.out.println("Error State");
        controller.setState(this);
    }
}
