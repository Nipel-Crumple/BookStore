package ru.bookstore.controller;

/**
 * Created by Johnny D on 28.10.2014.
 */
public class RunningState implements State{
    public void runController(Controller controller) {
        System.out.println("Let's start!");
        controller.setState(this);
    }
}
