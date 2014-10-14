package ru.bookstore.DAO;

/**
 * Created by Johnny D on 07.10.2014.
 */

import java.sql.*;
import java.util.*;
import org.apache.log4j.Logger;

import ru.bookstore.POJO.Client;

public class ClientDAO extends BookStoreAccess {

    public Set<Client> clientsSet = new HashSet<Client>();

    private static final Logger logger = Logger.getLogger("ClientDAO");

    private void getAllClientsFromDB() {
        Client newClient;
        long id = 0;
        String name;
        String login;
        String password;
        String sqlRequest = "SELECT * FROM CLIENT";
        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sqlRequest);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getLong("ID");
                name = resultSet.getString("NAME");
                login = resultSet.getString("LOGIN");
                password = resultSet.getString("PASSWORD");
                newClient = new Client(id, name, login, password);
                clientsSet.add(newClient);
            }

        } catch (SQLException e1) {
            logger.error("Smth's going wrong", e1.getCause());
        }

    }

    public Set<Client> getAllClients() {
        getAllClientsFromDB();
        return clientsSet;
    }

    public Client getClientById(long id) {
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
            newClient = new Client(id, name, login, password);
            clientsSet.add(newClient);
        } catch (SQLException e) {
            logger.error("Something's going bad in getClientById");
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
            neededClient = getClientById(results.getLong("ID"));
        } catch (SQLException e) {
            logger.debug("There is no client with this login that's why I can add it");
        }
        return neededClient;
    }

    public Client addNewClient(Client newClient) {
        String sql = "INSERT INTO CLIENT (ID, NAME, LOGIN, PASSWORD) VALUES(?,?,?,?)";

        Client neededClient = getClientByLogin(newClient.getLogin());
        if (neededClient != null) {
            logger.debug("This login's Client already exists");
            return neededClient;
        } else {
            try {
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setLong(1, newClient.getID());
                statement.setString(2, newClient.getName());
                statement.setString(3, newClient.getLogin());
                statement.setString(4, newClient.getPassword());
                statement.execute();
                return getClientById(newClient.getID());
            } catch (SQLException e) {
                logger.error("SQL request insert error", e);
            }
        }
        return neededClient;

    }

}
