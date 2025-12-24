package fr.Valkyar.valkyarVaultPlugin.db;

import fr.Valkyar.valkyarVaultPlugin.ValkyarVaultPlugin;
import org.bukkit.entity.Player;

import java.sql.*;

public class Database {

    private Connection _connection;

    // need to be changed for the final server
    private String _urlConnexion = "jdbc:mysql://localhost:6033/db_valkyar_plugin";
    private String _username = "root";
    private String _password = "root";

    private ValkyarVaultPlugin _plugin;

    public Database(ValkyarVaultPlugin plugin) {
        this._plugin = plugin;
    }


    public Connection getConnection() throws SQLException{

        if (_connection != null) {
            System.out.println("[ValkyarVaultPlugin] Connection is already established");
            return _connection;
        }

        try {
            this._connection = DriverManager.getConnection(_urlConnexion, _username, _password);

            System.out.println("[ValkyarVaultPlugin] Connection established");

        } catch (SQLException e) {
            System.out.println("[ValkyarVaultPlugin] Connection unestablished");
            System.out.println(e);
        }

        return this._connection;

    }

    public void firstTimeJoinStatement(Player player) throws SQLException{
        PreparedStatement statement = getConnection()
                .prepareStatement("INSERT INTO t_players (uuid, name, balance) VALUE (?, ?, ?)");

        statement.setString(1, player.getUniqueId().toString());
        statement.setString(2, player.getDisplayName());
        statement.setDouble(3, 0);

        statement.executeUpdate();
        statement.close();
    }

    public String getUUIDofPlayer(Player player) throws SQLException{
        PreparedStatement statement =  getConnection()
                .prepareStatement("SELECT uuid FROM t_players WHERE uuid = ?");
        statement.setString(1, player.getUniqueId().toString());

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            return results.getString("uuid");
        }


        statement.close();

        return null;
    }

    public Double getBalanceByName(String player) throws SQLException{

        PreparedStatement statement =  getConnection()
                .prepareStatement("SELECT balance FROM t_players WHERE name = ?");
        statement.setString(1, player);

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            return results.getDouble("balance");
        }


        statement.close();

        return null;
    }

    public Double getPlayerBalanceByUUID(Player player) throws SQLException{
        PreparedStatement statement =  getConnection()
                .prepareStatement("SELECT balance FROM t_players WHERE uuid = ?");
        statement.setString(1, player.getUniqueId().toString());

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            return results.getDouble("balance");
        }

        statement.close();

        return null;
    }

    public void setWithdrawBalanceByUUID(Player player, Double amount) throws SQLException{


        ValkyarVaultPlugin.OutputColoredText(ValkyarVaultPlugin.RED_OUTPUT, "ta mere la pute");
        Double currentBalance = getPlayerBalanceByUUID(player);
        currentBalance -= amount;

        PreparedStatement statement = getConnection()
                .prepareStatement("UPDATE t_players SET balance = ? WHERE uuid = ?");
        statement.setString(1, currentBalance.toString());
        statement.setString(2, player.getUniqueId().toString());

        statement.executeUpdate();
        statement.close();
    }

    public void setAddBalanceByName(Player player, Double amount) throws SQLException{
        Double currentBalance = getBalanceByName(player.getDisplayName());
        currentBalance += amount;

        PreparedStatement statement = getConnection()
                .prepareStatement("UPDATE t_players SET balance = ? WHERE name = ?");

        statement.setString(1, currentBalance.toString());
        statement.setString(2, player.getDisplayName());

        statement.executeUpdate();
        statement.close();
    }

}
