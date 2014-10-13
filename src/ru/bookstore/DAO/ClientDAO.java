package ru.bookstore.DAO;

/**
 * Created by Johnny D on 07.10.2014.
 */


import org.apache.log4j.Logger;
import ru.bookstore.POJO.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Set;

public class ClientDAO extends BookStoreAccess {

    public Set<Client> clientsTree = new TreeSet<Client>(new Comparator<Client>() {
        @Override
        public int compare(Client o1, Client o2) {
            return -o1.getName().compareTo(o2.getName());
        }
    });
    private static final Logger logger = Logger.getLogger("ClientDAO");

    private void readAllClientsFromDB() {
        Client newClient;
        int id;
        String name;
        String login;
        String password;
        String sqlRequest = "SELECT * FROM CLIENT";
        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sqlRequest);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getInt("ID");
                name = resultSet.getString("NAME");
                login = resultSet.getString("LOGIN");
                password = resultSet.getString("PASSWORD");
                newClient = new Client(name, login, password);
                clientsTree.add(newClient);
            }

        } catch (SQLException e1) {
            logger.error("Smth's going wrong", e1.getCause());
        }

    }

    public Set<Client> getAllClients() {
        readAllClientsFromDB();
        return clientsTree;
    }

    public Client getClientById(int id) {
        Client newClient = null;
        String name;
        String login;
        String password;
        String sqlRequest = "SELECT * from CLIENT where ID=" + id;
        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sqlRequest);
            resultSet = preparedStatement.executeQuery(sqlRequest);
            resultSet.next();
            name = resultSet.getString("NAME");
            login = resultSet.getString("LOGIN");
            password = resultSet.getString("PASSWORD");
            newClient = new Client(name, login, password);
            clientsTree.add(newClient);
        } catch (SQLException e) {
            logger.error("Something's going bad");
        }
        return newClient;
    }

    public Client getClientByLogin(String login) {

        Client neededClient = null;
        String sql = "SELECT * FROM CLIENT WHERE LOGIN = '" + login + "'";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet results = statement.executeQuery();
            results.next();
            neededClient = getClientById(results.getInt("ID"));
        } catch (SQLException e) {
            logger.debug("There is no client with this login", e);
        }
        return neededClient;
    }

    public void addNewClient(Client newClient) {
        String sql = "INSERT INTO CLIENT (ID, NAME, LOGIN, PASSWORD) VALUES(?,?,?,?)";

        if (getClientByLogin(newClient.getLogin()) != null) {
            logger.debug("Cannot add new Client with the same Login");
        } else {
            try {
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setInt(1, newClient.getID());
                statement.setString(2, newClient.getName());
                statement.setString(3, newClient.getLogin());
                statement.setString(4, newClient.getPassword());
                statement.execute();
            } catch (SQLException e) {
                logger.error("SQL request insert error", e);
            }
        }

    }

//    public static void main(String[] args) {
//        ClientDAO cl = new ClientDAO();
//        Client newClient = new Client("Egor", "Kuznec", "1839Wolper");
//        cl.addNewClient(newClient);
//
//        cl.getAllClients();
//        for (Client temp : cl.getAllClients()) {
//            System.out.println(temp.getName());
//        }
//    }

}
