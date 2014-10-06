package ru.bookstore.mypackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
/**
 * Created by Johnny D on 06.10.2014.
 */
public abstract class BookStoreDAO {
    public static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    public static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:ORADB10G";
    public static final String LOGIN = "VADIM";
    public static final String PASSWORD = "vadim";
    public static Connection con;

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            con =  DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
