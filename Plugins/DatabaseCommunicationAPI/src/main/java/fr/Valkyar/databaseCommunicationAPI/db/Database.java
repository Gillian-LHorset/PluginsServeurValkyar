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

    private DatabaseCommunicationAPI _plugin;

    public Database(DatabaseCommunicationAPI plugin) {
        this._plugin = plugin;
    }

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
        PreparedStatement statement = _plugin.databaseLink._connection
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
        PreparedStatement statement =  _plugin.databaseLink._connection
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
    public Double getBalancePlayerByName(String player) throws SQLException{

        PreparedStatement statement =  _plugin.databaseLink._connection
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
        PreparedStatement statement =  _plugin.databaseLink._connection
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

        PreparedStatement statement = _plugin.databaseLink._connection
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
        Double currentBalance = getBalancePlayerByName(player.getDisplayName());
        currentBalance += amount;

        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement("UPDATE t_players SET balance = ? WHERE name = ?");

        statement.setString(1, currentBalance.toString());
        statement.setString(2, player.getDisplayName());

        statement.executeUpdate();
        statement.close();
    }

    public List<String> getAllJobs() {
        try {
            List<String> jobsList = new ArrayList<>();

            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT name FROM t_jobs ORDER BY job_id");
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
        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement("UPDATE t_players SET job_fk = ? WHERE uuid = ?");
        statement.setString(1, jobsId);
        statement.setString(2, player.getUniqueId().toString());

        statement.executeUpdate();
        statement.close();
    }

    public String getPlayerJob(Player player){
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
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
    public Integer getPlayerJobFk(Player player){
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement(  "SELECT job_fk " +
                                            "FROM t_players " +
                                            "WHERE uuid = ? ");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getInt("job_fk");
            }

            statement.close();
        } catch (SQLException error) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'getPlayerJobFk()'");
            return null;
        }
        return null;
    }

    public List<String> getAllBreakableBlocksByJobs(Player player) {
        List<String> breakableBlocksList = new ArrayList<>();
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement(  "SELECT minecraft_identifier " +
                                            "FROM t_breakable_blocks AS b " +
                                            "JOIN t_jobs AS j ON b.job_fk = j.job_id " +
                                            "JOIN t_players AS p ON p.job_fk = j.job_id " +
                                            "WHERE p.uuid = ?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet results = statement.executeQuery();

            while (results.next()) {
                breakableBlocksList.add(results.getString("minecraft_identifid"));
            }

            statement.close();
        } catch (SQLException error) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'getAllBreakableBlocksByJobs()'");
            return null;
        }

        return breakableBlocksList;
    }

    public void setPlayerBalanceJob(Player player, String breakedBlock) throws SQLException{
        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement(  "UPDATE t_players AS p " +
                                        "JOIN t_jobs AS j ON p.job_fk = j.job_id " +
                                        "JOIN t_breakable_blocks AS b ON b.job_fk = j.job_id " +
                                        "SET p.balance = p.balance + b.money_bringing " +
                                        "WHERE p.uuid = ? " +
                                        "AND b.minecraft_identifier = ?");
        statement.setString(1, player.getUniqueId().toString());
        statement.setString(2, breakedBlock);

        statement.executeUpdate();
        statement.close();
    }

    public void setPlayerBalanceDecrease(Player player, Double amount) throws SQLException {
        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement(  "UPDATE t_players " +
                                        "SET balance = balance - ? " +
                                        "WHERE uuid = ? ");
        statement.setDouble(1, amount);
        statement.setString(2, player.getUniqueId().toString());

        statement.executeUpdate();
        statement.close();
    }

    public void CreateTown(Player player, String name){
        try {
            PreparedStatement createTownStatement = _plugin.databaseLink._connection
                    .prepareStatement(  "INSERT INTO t_town (name) " +
                                            "VALUE (?)");

            createTownStatement.setString(1, name);

            createTownStatement.executeUpdate();
            createTownStatement.close();

            setPlayerTownByName(player, name);

        } catch (Exception err) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'CreateTown()' 'INSERT INTO t_town'");
            System.out.println(err.getMessage());
        }
    }

    public Integer getPlayerFk(Player player){
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement(  "SELECT player_id " +
                            "FROM t_players " +
                            "WHERE uuid = ? ");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getInt("player_id");
            }

            statement.close();
        } catch (SQLException error) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'GetPlayerFk()'");
        }
        return null;
    }

    public String getPlayerTown(Player player){
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement(  "SELECT t.name " +
                            "FROM t_town AS t " +
                            "JOIN t_players AS p " +
                            "ON p.town_fk = t.town_id " +
                            "WHERE uuid = ? ");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getString("t.name");
            }

            statement.close();
        } catch (SQLException error) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'GetPlayerTown()'");
        }
        return null;
    }

    public void setPlayerTownByName(Player player, String name) {
        try {
            if (name != null) {
                PreparedStatement statement = _plugin.databaseLink._connection
                        .prepareStatement("UPDATE t_players AS p " +
                                "JOIN t_town AS t " +
                                "ON t.name = ?" +
                                "SET p.town_fk = t.town_id " +
                                "WHERE p.uuid = ? ");
                statement.setString(1, name);
                statement.setString(2, player.getUniqueId().toString());

                statement.executeUpdate();
                statement.close();
            } else {
                PreparedStatement statement = _plugin.databaseLink._connection
                        .prepareStatement("UPDATE t_players AS p " +
                                "SET p.town_fk = ? " +
                                "WHERE p.uuid = ? ");
                statement.setString(1, null);
                statement.setString(2, player.getUniqueId().toString());

                statement.executeUpdate();
                statement.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getNumberOfPlayersInTown(String townName){
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement(  "SELECT COUNT(p.uuid) " +
                            "FROM t_town AS t " +
                            "JOIN t_players AS p " +
                            "ON p.town_fk = t.town_id " +
                            "WHERE t.name = ? ");
            statement.setString(1, townName);

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getInt("COUNT(p.uuid)");
            }

            statement.close();
        } catch (SQLException err) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'getNumberOfPlayersInTown()'");
        }
        return null;
    }

    public void deleteTownByName(String townName){
        try {
            deleteAllChunksByTownName(townName);
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement(  "DELETE FROM t_town " +
                                            "WHERE name = ? ");
            statement.setString(1, townName);

            statement.executeUpdate();
            statement.close();
        } catch (Exception err) {

        }
    }

    public void deleteAllChunksByTownName(String townName){
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement(  "DELETE FROM t_chunk_town " +
                                            "WHERE town_fk = ? ");
            statement.setInt(1, getTownFk(townName));

            statement.executeUpdate();
            statement.close();
        } catch (Exception err) {

        }
    }

    public List<String> getAllPlayerOfTownByName (String townName){
        List<String> allPlayers = new ArrayList<>();
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement(  "SELECT p.name " +
                                            "FROM t_town AS t " +
                                            "JOIN t_players AS p " +
                                            "ON t.town_id = p.town_fk " +
                                            "WHERE t.name = ? " +
                                            "ORDER BY p.name");
            statement.setString(1, townName);

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                allPlayers.add(results.getString("p.name"));
            }

            statement.close();
        } catch (Exception err) {

        }
        return allPlayers;
    }

    public void registerChunk(int[] chunks, String townName){
        try {
            PreparedStatement createTownStatement = _plugin.databaseLink._connection
                    .prepareStatement(  "INSERT INTO t_chunk_town   (chunk_identifier_x, chunk_identifier_z, town_fk) " +
                                            "VALUE                      (?, ?, ?)");

            createTownStatement.setInt(1, chunks[0]);
            createTownStatement.setInt(2, chunks[1]);
            createTownStatement.setInt(3, getTownFk(townName));

            createTownStatement.executeUpdate();
            createTownStatement.close();
        } catch (Exception err) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'registerChunk()'");
            System.out.println(err.getMessage());
        }
    }

    public Integer getTownFk(String townName){
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement(  "SELECT town_id " +
                            "FROM t_town " +
                            "WHERE name = ? ");

            statement.setString(1, townName);

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getInt("town_id");
            }

            statement.close();
        } catch (SQLException error) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'getTownFk()'");
        }
        return null;
    }

    public Double getBalanceTownByName(String townName) throws SQLException{

        PreparedStatement statement =  _plugin.databaseLink._connection
                .prepareStatement("SELECT balance FROM t_town WHERE name = ?");
        statement.setString(1, townName);

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            return results.getDouble("balance");
        }


        statement.close();

        return null;
    }

    public Integer[] getChunkById(int[] giveId) throws SQLException{

        Integer[] chunkProrities = new Integer[3];

        PreparedStatement statement =  _plugin.databaseLink._connection
                .prepareStatement("SELECT chunk_identifier_x, chunk_identifier_z, town_fk " +
                        "FROM t_chunk_town " +
                        "WHERE chunk_identifier_x = ? " +
                        "AND chunk_identifier_z = ?");
        statement.setString(1, String.valueOf(giveId[0]));
        statement.setString(2, String.valueOf(giveId[1]));

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            chunkProrities[0] = results.getInt("chunk_identifier_x");
            chunkProrities[1] = results.getInt("chunk_identifier_z");
            chunkProrities[2] = results.getInt("town_fk");

            return chunkProrities;
        }


        statement.close();

        return null;
    }

    public void setTownBalanceDecreaseByName(String townName, Double amount) throws SQLException {
        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement(  "UPDATE t_town " +
                        "SET balance = balance - ? " +
                        "WHERE name = ? ");
        statement.setDouble(1, amount);
        statement.setString(2, townName);

        statement.executeUpdate();
        statement.close();
    }

    public Integer[][] getAllChunkOfTown(String townName) throws SQLException{

        Integer[][] allChunks = new Integer[2][];
        int i = 0;

        PreparedStatement statement =  _plugin.databaseLink._connection
                .prepareStatement("SELECT chunk_identifier_x, chunk_identifier_z " +
                        "FROM t_chunk_town " +
                        "WHERE town_fk = ? ");
        statement.setInt(1, getTownFk(townName));

        ResultSet results = statement.executeQuery();

        while (results.next()) {
            allChunks[0][i] = results.getInt("chunk_identifier_x");
            allChunks[1][i] = results.getInt("chunk_identifier_z");

            i++;
        }

        return allChunks;
    }

    public boolean isChunkFromTown(String townName, int[] chunk) {
        try {
            PreparedStatement statement =  _plugin.databaseLink._connection
                    .prepareStatement("SELECT * " +
                            "FROM t_chunk_town " +
                            "WHERE town_fk = ? " +
                            "AND chunk_identifier_x = ? " +
                            "AND chunk_identifier_z = ?");
            statement.setInt(1, getTownFk(townName));
            statement.setInt(2, chunk[0]);
            statement.setInt(3, chunk[1]);

            ResultSet results = statement.executeQuery();

            return (results.next());



        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteOneChunksById(String townName, int[] chunk){
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement(  "DELETE FROM t_chunk_town " +
                            "WHERE town_fk = ? " +
                            "AND chunk_identifier_x = ? " +
                            "AND chunk_identifier_z = ?");
            statement.setInt(1, getTownFk(townName));
            statement.setInt(2, chunk[0]);
            statement.setInt(3, chunk[1]);

            statement.executeUpdate();
            statement.close();
        } catch (Exception err) {

        }
    }

    /** Withdraw money from the player for increase money of a town.
     * @param townName Name of the town who receive money.
     * @param amount Amount of the transaction.
     * @param p The player who send money.
     * @throws SQLException In the case of the connexion with the db cannot be done, the methode throw an exception.
     */
    public void setWithdrawBalanceToTown(String townName, Double amount, Player p) throws SQLException{
        Double currentBalance = getBalanceTownByName(townName);
        currentBalance += amount;

        setPlayerBalanceDecrease(p, amount);

        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement("UPDATE t_town SET balance = ? WHERE name = ?");
        statement.setDouble(1, currentBalance);
        statement.setString(2, townName);

        statement.executeUpdate();
        statement.close();
    }

    public void registerPermissionPlayer(Player player) throws SQLException{
        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement("INSERT INTO t_permission_list (mayor, claim, unclaim, withdraw, disband, player_fk) VALUE (?, ?, ?, ?, ?, ?)");

        statement.setString(1, "FALSE");
        statement.setString(2, "FALSE");
        statement.setString(3, "FALSE");
        statement.setString(4, "FALSE");
        statement.setString(5, "FALSE");
        statement.setInt(   6, getPlayerFk(player));

        statement.executeUpdate();
        statement.close();
    }

    public List<String> getAllPermissionOfAPlayer(Player p) throws SQLException{
        List<String> permissions = new ArrayList<>();

        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement(  "SELECT mayor, claim, unclaim, withdraw, disband " +
                                        "FROM t_permission_list " +
                                        "WHERE player_fk = ? ");

        statement.setInt(1, getPlayerFk(p));

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            if (results.getBoolean("mayor")) permissions.add("town.mayor");
            if (results.getBoolean("claim")) permissions.add("town.claim");
            if (results.getBoolean("unclaim")) permissions.add("town.unclaim");
            if (results.getBoolean("withdraw")) permissions.add("town.withdraw");
            if (results.getBoolean("disband")) permissions.add("town.disband");
        }

        return permissions;
    }

    public void deleteAllPermissionsOfAPlayer(Player p) throws SQLException{
        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement(  "DELETE FROM t_permission_list " +
                        "WHERE player_fk = ? ");
        statement.setInt(1, getPlayerFk(p));

        statement.executeUpdate();
        statement.close();
    }

    public void setPermissionOfAPlayerToTrue(Player p, String permissionToGive) throws SQLException{
        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement("UPDATE t_permission_list SET ? = TRUE WHERE player_fk = ?");
        statement.setString(1, permissionToGive);
        statement.setInt(2, getPlayerFk(p));

        statement.executeUpdate();
        statement.close();
    }

    public void setPermissionOfAPlayerToFalse(Player p, String permissionToGive) throws SQLException{
        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement("UPDATE t_permission_list SET ? = FALSE WHERE player_fk = ?");
        statement.setString(1, permissionToGive);
        statement.setInt(2, getPlayerFk(p));

        statement.executeUpdate();
        statement.close();
    }
}
