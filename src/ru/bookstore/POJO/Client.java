package ru.bookstore.POJO;

/**
 * Created by Johnny D on 07.10.2014.
 */
public class Client {
    private int id = 0;
    private String name = null;
    private String login = null;
    private String password = null;

    public Client() {

    }
    public Client(int id, String name, String login, String password) {
        try {
            setID(id);
            setName(name);
            setLogin(login);
            setPassword(password);
        } catch (NullPointerException e) {
            e.printStackTrace(System.out);
        }
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        if (id != 0) {
            this.id = id;
        } else {
            throw new NullPointerException();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        } else {
            throw new NullPointerException();
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if (login != null) {
            this.login = login;
        } else {
            throw new NullPointerException();
        }
    }

    private String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        if ((password != null) && (this.password == null)) {
            this.password = password;
        } else if (password == null){
            throw new NullPointerException();
        } else {
            System.out.println("Password was settled early");
        }
    }

    private void changePassword(String oldPassword, String newPassword) {
        try {
            if (this.password.equals(oldPassword)) {
                this.password = newPassword;
            }
        } catch(NullPointerException e) {
            System.out.println("Arguments cannot be null");
        }
    }

    private void changeLogin(String newLogin, String password) {
        if ((newLogin != null) && (password != null)) {
            if (this.password.equals(password)) {
                this.login = newLogin;
            }
        } else {
            throw new NullPointerException();
        }
    }
}
