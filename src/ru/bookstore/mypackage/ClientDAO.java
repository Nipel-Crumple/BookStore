package ru.bookstore.mypackage;

import java.sql.*;


/**
 * Created by Johnny D on 06.10.2014.
 */

public class ClientDAO extends BookStoreDAO {
    private String PASSWORD = null;
    private String LOGIN = null;


    public String getName() throws SQLException, ClassNotFoundException {
        String getNameSQL = "SELECT NAME from CLIENT";
        Statement stmt = con.createStatement();
        ResultSet res = stmt.executeQuery(getNameSQL);
        res.next();
        return res.getString("NAME");
    }

    public String getLogin() throws SQLException, ClassNotFoundException {
        String getLoginSQL = "SELECT LOGIN from CLIENT";
        Statement stmt = con.createStatement();
        ResultSet res = stmt.executeQuery(getLoginSQL);
        res.next();
        return res.getString("LOGIN");
    }

    public String getPassword() throws SQLException, ClassNotFoundException {
        String getPasswordSQL = "SELECT PASSWORD from CLIENT";
        Statement stmt = con.createStatement();
        ResultSet res = stmt.executeQuery(getPasswordSQL);
        res.next();
        return res.getString("PASSWORD");
    }

    public int getID() throws SQLException, ClassNotFoundException {
        String getIDSQL = "SELECT ID from CLIENT";
        Statement stmt = con.createStatement();
        ResultSet res = stmt.executeQuery(getIDSQL);
        res.next();
        return res.getInt("ID");
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ClientDAO cl = new ClientDAO();
        System.out.println(cl.getName());
        System.out.println(cl.getID());
        System.out.println(cl.getLogin());
        System.out.println(cl.getPassword());
    }
}
