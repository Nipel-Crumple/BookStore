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

    public Client(String name, String login, String password) {
        setName(name);
        setLogin(login);
        setPassword(password);
        hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (id != client.id) return false;
        if (!login.equals(client.login)) return false;
        if (!name.equals(client.name)) return false;
        if (!password.equals(client.password)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        id = 0;
        id = 31 * id + name.hashCode();
        id = 31 * id + login.hashCode();
        id = 31 * id + password.hashCode();
        return id;
    }

    public int getID() {
        return (id != 0) ? id : hashCode();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if ((password != null) && (this.password == null)) {
            this.password = password;
        } else if (password == null) {
            throw new NullPointerException();
        } else {
            System.out.println("Password was settled early");
        }
    }

    public void changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
        }
        System.out.println("Arguments cannot be null");
    }

    public void changeLogin(String newLogin, String password) {
        if (this.password.equals(password)) {
            this.login = newLogin;
        }
    }
}
