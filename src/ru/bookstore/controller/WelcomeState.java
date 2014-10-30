package ru.bookstore.controller;

/**
 * Created by Johnny D on 28.10.2014.
 */
public class WelcomeState implements State {

    public void runController(Controller controller) {
        System.out.println("You're Welcome to the Book Store!");
        controller.setState(this);
    }
}
