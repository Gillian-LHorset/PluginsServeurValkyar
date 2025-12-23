package fr.Valkyar.valkyarVaultPlugin.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private Connection _connection;

    // need to be changed for the final server
    private String _urlConnexion = "jdbc:mysql://localhost:6033/";
    private String _username = "root";
    private String _password = "root";

    public Connection getConnection() {

        if (_connection != null) {
            return _connection;
        }


        try {
            this._connection = DriverManager.getConnection(_urlConnexion, _username, _password);

            System.out.println("Connection established");

        } catch (SQLException e) {
            System.out.println("Connection unestablished");
            e.printStackTrace();
        }

        return this._connection;

    }

}
