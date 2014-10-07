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

public class ClientDAO extends BookStoreAccess{

    public List<Client> clientsList = new ArrayList<Client>();
    public List<Client> readAllClientsFromDB() {
        String name = null;
        String login = null;
        String password = null;
        int id = 0;
        String sqlRequest = "SELECT * from CLIENT";
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sqlRequest);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                id = resultSet.getInt("ID");
                name = resultSet.getString("NAME");
                login = resultSet.getString("LOGIN");
                password = resultSet.getString("PASSWORD");
                Client newClient = new Client(id, name, login, password);
                System.out.println(newClient.getName());
                clientsList.add(newClient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientsList;
    }

    public static void main(String[] args){
        ClientDAO cl = new ClientDAO();
        List<Client> list = cl.readAllClientsFromDB();
        for (Client tempClient : list) {
            System.out.print(tempClient.getName()+" ");
            System.out.print(tempClient.getID()+ " ");
            System.out.println(tempClient.getLogin());
        }
    }

}
