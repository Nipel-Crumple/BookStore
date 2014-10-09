package ru.bookstore.DAO;

/**
 * Created by Johnny D on 07.10.2014.
 */

import ru.bookstore.POJO.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO extends BookStoreAccess {

    public List<Client> clientsList = new ArrayList<Client>();

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
            newClient = new Client(id, name, login, password);
//            System.out.println(newClient.getName());
            clientsList.add(newClient);
        } catch (SQLException e) {
            System.out.println("Shout!");
            e.printStackTrace();
        }

        return newClient;
    }

    public List<Client> readAllClientsFromDB() {
        Client newClient = null;
        int id;
        String name;
        String login;
        String password;
        String sqlRequest = "SELECT * from CLIENT";
        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sqlRequest);
            resultSet = preparedStatement.executeQuery(sqlRequest);
            while (resultSet.next()) {
                id = resultSet.getInt("ID");
                name = resultSet.getString("NAME");
                login = resultSet.getString("LOGIN");
                password = resultSet.getString("PASSWORD");
                newClient = new Client(id, name, login, password);
                //            System.out.println(newClient.getName());
                clientsList.add(newClient);
            }
        } catch (SQLException e) {
            System.out.println("Shout!");
            e.printStackTrace();
        }

        return clientsList;

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
            e.printStackTrace();
        }
        return neededClient;
    }

    public static void main(String[] args) {
        ClientDAO cl = new ClientDAO();
        System.out.println(cl.getClientByLogin("CptNipel"));
        List<Client> list = cl.readAllClientsFromDB();
        for (Client tempClient : list) {
            System.out.print(tempClient.getName() + " ");
            System.out.print(tempClient.getID() + " ");
            System.out.println(tempClient.getLogin());
        }
    }

}
