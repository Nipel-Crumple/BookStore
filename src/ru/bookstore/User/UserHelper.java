package ru.bookstore.User;

import ru.bookstore.DAO.ClientDAO;

/**
 * Created by Johnny D on 28.10.2014.
 */
public class UserHelper {
    private String login;
    private String password;
    private UserHelper(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static UserHelper getInstance(String login, String password) {
        return new UserHelper(login, password);
    }

}
