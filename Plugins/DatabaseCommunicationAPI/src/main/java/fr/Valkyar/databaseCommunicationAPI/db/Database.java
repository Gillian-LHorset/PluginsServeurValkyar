package fr.Valkyar.databaseCommunicationAPI.db;

import org.bukkit.entity.Player;
import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection _connection;

    //TODO need to be changed for the final server
    private String _urlConnexion = "jdbc:mysql://localhost:6033/db_valkyar_plugin";
    private String _username = "root";
    private String _password = "root";

    /** Make a connexion to the db.
     * @return Return a connexion to the db.
     * @throws SQLException In the case of the connexion with the db cannot be done, the methode throw an exception.
     */
    public Connection getConnection() throws SQLException {

        if (_connection != null) {
            System.out.println("[DatabaseCommunicationPlugin] : Connection is already established");
            return _connection;
        }

        try {
            this._connection = DriverManager.getConnection(_urlConnexion, _username, _password);

            System.out.println("[DatabaseCommunicationPlugin] : Connection established");

        } catch (SQLException e) {
            System.out.println("[DatabaseCommunicationPlugin] : Connection unestablished");
            System.out.println(e);
        }

        return this._connection;

    }


    /** Add the name, uuid and balance of a player to the database
     * @param player This is the player who the information are going to be added to the db
     * @throws SQLException In the case of the connexion with the db cannot be done, the methode throw an exception.
     */
    public void firstTimeJoinStatement(Player player) throws SQLException{
        PreparedStatement statement = getConnection()
                .prepareStatement("INSERT INTO t_players (uuid, name, balance) VALUE (?, ?, ?)");

        statement.setString(1, player.getUniqueId().toString());
        statement.setString(2, player.getDisplayName());
        statement.setDouble(3, 0);

        statement.executeUpdate();
        statement.close();
    }

    /** Get uuid from a player.
     * @param player This is the player who need to get the uuid.
     * @return In the case the player isn't be found, return null else return his uuid
     * @throws SQLException In the case of the connexion with the db cannot be done, the methode throw an exception.
     */
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

    /** Get balance from a player.
     * @param player This is the player who need to get the balance.
     * @return In the case the player isn't be found, return null else return his balance
     * @throws SQLException In the case of the connexion with the db cannot be done, the methode throw an exception.
     */
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

    /** Get balance from a player.
     * @param player This is the player who need to get the balance.
     * @return In the case the player isn't be found, return null else return his
     * @throws SQLException In the case of the connexion with the db cannot be done, the methode throw an exception.
     */
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

    /** Decrease the balance of the player to the specify amount.
     * @param player This is the player who pay.
     * @param amount This is how many the player pay for the transaction.
     * @throws SQLException In the case of the connexion with the db cannot be done, the methode throw an exception.
     */
    public void setWithdrawBalanceByUUID(Player player, Double amount) throws SQLException{
        Double currentBalance = getPlayerBalanceByUUID(player);
        currentBalance -= amount;

        PreparedStatement statement = getConnection()
                .prepareStatement("UPDATE t_players SET balance = ? WHERE uuid = ?");
        statement.setString(1, currentBalance.toString());
        statement.setString(2, player.getUniqueId().toString());

        statement.executeUpdate();
        statement.close();
    }

    /** Increase the balance of the player to the specify amount.
     * @param player This is the player who receive.
     * @param amount This is how many the player receive from the transaction.
     * @throws SQLException In the case of the connexion with the db cannot be done, the methode throw an exception.
     */
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

    public List<String> getAllJobs() {
        try {
            List<String> jobsList = new ArrayList<>();

            PreparedStatement statement = getConnection()
                    .prepareStatement("SELECT name FROM t_jobs");
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                String jobName = results.getString("name");
                jobsList.add(jobName);
            }

            return jobsList;
        }  catch (SQLException e) {
            DatabaseCommunicationAPI.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT, "[DatabaseCommunicationAPI] : Erreur de connexion 'getAllJobs()'");
        }
        return null;
    }

    public void setPlayerJob(Player player, String jobsId) throws SQLException{
        PreparedStatement statement = getConnection()
                .prepareStatement("UPDATE t_players SET job_fk = ? WHERE uuid = ?");
        statement.setString(1, jobsId);
        statement.setString(2, player.getUniqueId().toString());

        statement.executeUpdate();
        statement.close();
    }

    public String getPlayerJob(Player player){
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement(  "SELECT j.name " +
                                            "FROM t_players AS p " +
                                            "LEFT JOIN t_jobs AS j ON p.job_fk = j.job_id " +
                                            "WHERE p.uuid = ? ");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getString("name");
            }

            statement.close();
        } catch (SQLException error) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'getPlayerJob()'");
            return null;
        }
        return null;
    }
}
