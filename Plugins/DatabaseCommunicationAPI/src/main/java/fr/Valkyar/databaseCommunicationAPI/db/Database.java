package fr.Valkyar.databaseCommunicationAPI.db;

import fr.Valkyar.databaseCommunicationAPI.Models.*;
import org.bukkit.entity.Player;
import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Database {
    private Connection _connection;

    //TODO need to be changed for the final server

    private DatabaseCommunicationAPI _plugin;

    public Database(DatabaseCommunicationAPI plugin) {
        this._plugin = plugin;
    }

    /** Make a connexion to the db.
     * @return Return a connexion to the db.
     * @throws SQLException In the case of the connexion with the db cannot be done, the methode throw an exception.
     */
    public Connection getConnection() throws SQLException {

        final String _urlConnexion = "jdbc:mysql://localhost:6033/db_valkyar_plugin";
        final String _username = "root";
        final String _password = "root";

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
    public Double getBalancePlayerByName(String player) {
        try {
            PreparedStatement statement =  _plugin.databaseLink._connection
                    .prepareStatement("SELECT balance FROM t_players WHERE name = ?");
            statement.setString(1, player);

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getDouble("balance");
            }
            statement.close();
        } catch (SQLException e) {
            DatabaseCommunicationAPI.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT,
                    "[DatabaseCommunicationAPI] : Erreur de connexion 'getBalancePlayerByName()'");

        }
        return null;
    }

    /** Get balance from a player.
     * @param player This is the player who need to get the balance.
     * @return In the case the player isn't be found, return null else return his
     * @throws SQLException In the case of the connexion with the db cannot be done, the methode throw an exception.
     */
    public Double getPlayerBalanceByUUID(Player player) throws SQLException{
        try {
            PreparedStatement statement =  _plugin.databaseLink._connection
                    .prepareStatement("SELECT balance FROM t_players WHERE uuid = ?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getDouble("balance");
            }

            statement.close();
        } catch (SQLException e) {
            DatabaseCommunicationAPI.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT,
                    "[DatabaseCommunicationAPI] : Erreur de connexion 'getPlayerBalanceByUUID()'");
        }
        return null;
    }

    public void setPlayerBalance(Player player, Double amount) {
        try {
         PreparedStatement statement = _plugin.databaseLink._connection
                 .prepareStatement("UPDATE t_players SET balance = ? WHERE uuid = ?");
             statement.setString(1, amount.toString());
             statement.setString(2, player.getUniqueId().toString());

             statement.executeUpdate();
             statement.close();
        } catch (SQLException e) {
            DatabaseCommunicationAPI.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT,
                    "[DatabaseCommunicationAPI] : Erreur de connexion 'setPlayerBalance()'");
        }
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

    public List<String> getAllJobsName() {
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
            DatabaseCommunicationAPI.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT, "[DatabaseCommunicationAPI] : Erreur de connexion 'getAllJobsName()'");
        }
        return null;
    }
    
    public List<Job> getAllJobsInfos() {
        try {
            List<Job> jobsList = new ArrayList<>();

            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT * FROM t_jobs ORDER BY job_id");
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                jobsList.add(new Job(
                        results.getInt("job_id"),
                        results.getString("name"),
                        results.getBoolean("isMain")));
            }

            return jobsList;
        }  catch (SQLException e) {
            DatabaseCommunicationAPI.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT, "[DatabaseCommunicationAPI] : Erreur de connexion 'getAllJobsInfos()'");
        }
        return null;
    }
    
    public Job getJobInfos(int jobId) {
        try {
            Job job = null;
            
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT * FROM t_jobs WHERE job_id = ? ORDER BY job_id");
            statement.setInt(1, jobId);
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                job = new Job(
                        results.getInt("job_id"),
                        results.getString("name"),
                        results.getBoolean("isMain"));
            }

            return job;
        }  catch (SQLException e) {
            DatabaseCommunicationAPI.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT, "[DatabaseCommunicationAPI] : Erreur de connexion 'getJobInfos()'");
        }
        return null;
    }

    public List<Job> getPlayerJobs(Player p) {
        try {
            List<Job> job = new ArrayList<>();

            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT * FROM t_jobs WHERE job_id = ? OR job_id = ? ORDER BY job_id");
            statement.setInt(1, getPlayerMainJobFk(p));
            statement.setInt(2, getPlayerSecondJobFk(p));
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                job.add(new Job(
                        results.getInt("job_id"),
                        results.getString("name"),
                        results.getBoolean("isMain")));
            }

            return job;
        }  catch (SQLException e) {
            DatabaseCommunicationAPI.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT, "[DatabaseCommunicationAPI] : Erreur de connexion 'getPlayerJobs()'");
        }
        return null;
    }



    public List<String> getAllMainJobsName() {
        try {
            List<String> jobsList = new ArrayList<>();

            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT name FROM t_jobs WHERE ismain = TRUE ORDER BY job_id");
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                String jobName = results.getString("name");
                jobsList.add(jobName);
            }

            return jobsList;
        }  catch (SQLException e) {
            DatabaseCommunicationAPI.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT, "[DatabaseCommunicationAPI] : Erreur de connexion 'getAllMainJobsName()'");
        }
        return null;
    }

    public List<String> getAllSecondJobsName() {
        try {
            List<String> jobsList = new ArrayList<>();

            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT name FROM t_jobs WHERE ismain = FALSE ORDER BY job_id");
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                String jobName = results.getString("name");
                jobsList.add(jobName);
            }

            return jobsList;
        }  catch (SQLException e) {
            DatabaseCommunicationAPI.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT, "[DatabaseCommunicationAPI] : Erreur de connexion 'getAllSecondJobsName()'");
        }
        return null;
    }

    public void setPlayerMainJob(Player player, Integer jobsId) throws SQLException{
        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement("UPDATE t_players SET main_job_fk = ? WHERE uuid = ?");
        if (jobsId == null) {
            statement.setNull(1, Types.INTEGER);
        } else {
            statement.setInt(1, jobsId);
        }
        statement.setString(2, player.getUniqueId().toString());

        statement.executeUpdate();
        statement.close();
    }



    public void setPlayerSecondJob(Player player, Integer jobsId) throws SQLException{
        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement("UPDATE t_players SET second_job_fk = ? WHERE uuid = ?");
        if (jobsId == null) {
            statement.setNull(1, Types.INTEGER);
        } else {
            statement.setInt(1, jobsId);
        }
        statement.setString(2, player.getUniqueId().toString());

        statement.executeUpdate();
        statement.close();
    }

    public Integer findJobIdByJobName(String jobName) {
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement(  "SELECT job_id " +
                            "FROM t_jobs " +
                            "WHERE name = ? ");
            statement.setString(1, jobName);

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getInt("job_id");
            }

            statement.close();
        } catch (SQLException e) {

            return null;
        }
        return null;
    }

    public String getPlayerMainJobName(Player player){
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement(  "SELECT j.name " +
                                            "FROM t_players AS p " +
                                            "LEFT JOIN t_jobs AS j ON p.main_job_fk = j.job_id " +
                                            "WHERE p.uuid = ? ");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getString("name");
            }

            statement.close();
        } catch (SQLException error) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'getPlayerMainJobName()'");
            return null;
        }
        return null;
    }

    public String getPlayerSecondJobName(Player player){
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement(  "SELECT j.name " +
                                            "FROM t_players AS p " +
                                            "LEFT JOIN t_jobs AS j ON p.second_job_fk = j.job_id " +
                                            "WHERE p.uuid = ? ");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getString("name");
            }

            statement.close();
        } catch (SQLException error) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'getPlayerSecondJobName()'");
            return null;
        }
        return null;
    }

    public Integer getPlayerMainJobFk(Player player){
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement(  "SELECT main_job_fk " +
                                            "FROM t_players " +
                                            "WHERE uuid = ? ");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getInt("main_job_fk");
            }

            statement.close();
        } catch (SQLException error) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'getPlayerMainJobFk()'");
            return null;
        }
        return null;
    }

    public Integer getPlayerSecondJobFk(Player player){
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement(  "SELECT second_job_fk " +
                                            "FROM t_players " +
                                            "WHERE uuid = ? ");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getInt("second_job_fk");
            }

            statement.close();
        } catch (SQLException error) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'getPlayerSecondJobFk()'");
            return null;
        }
        return null;
    }

    public List<ResourceData> getResourceByJob(int jobFk, String actionType) {
        List<ResourceData> breakableBlocksList = new ArrayList<>();
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement(  "SELECT block_id, minecraft_identifier, money_bringing, exp_bringing, action ,job_fk " +
                            "FROM t_action_type " +
                            "WHERE job_fk = ? AND action = ?");
            statement.setInt(1, jobFk);
            statement.setString(2, actionType);

            ResultSet results = statement.executeQuery();

            while (results.next()) {
                breakableBlocksList.add( new ResourceData(results.getInt("block_id"),
                        results.getString("minecraft_identifier"),
                        results.getDouble("money_bringing"),
                        results.getDouble("exp_bringing"),
                        results.getString("action"),
                        results.getInt("job_fk")));
            }

            statement.close();
        } catch (SQLException error) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'getBreakableBlocksNameByJob()'");
            return null;
        }

        return breakableBlocksList;
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

    public Town getPlayerTown(Player p) {
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement(  "SELECT t.name, t.balance " +
                            "FROM t_town AS t " +
                            "JOIN t_players AS p " +
                            "ON p.town_fk = t.town_id " +
                            "WHERE uuid = ? ");
            statement.setString(1, p.getUniqueId().toString());

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return new Town(
                        results.getString("t.name"),
                        results.getDouble("balance")
                        );
            }

            statement.close();
        } catch (SQLException error) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'GetPlayerTown()'");
        }
        return null;
    }

    public void setPlayerTownByName(Player player, String name) {
        try {
            PreparedStatement statement;
            if (name != null) {
                statement = _plugin.databaseLink._connection
                        .prepareStatement("UPDATE t_players AS p " +
                                "JOIN t_town AS t " +
                                "ON t.name = ?" +
                                "SET p.town_fk = t.town_id " +
                                "WHERE p.uuid = ? ");
                statement.setString(1, name);

            } else {
                statement = _plugin.databaseLink._connection
                        .prepareStatement("UPDATE t_players AS p " +
                                "SET p.town_fk = ? " +
                                "WHERE p.uuid = ? ");
                statement.setString(1, null);

            }
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();
            statement.close();
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
                                            "WHERE t.name = ? ");
            statement.setString(1, townName);

            ResultSet results = statement.executeQuery();

            while (results.next()) {
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
                .prepareStatement("INSERT INTO t_permission_list (mayor, second, claim, withdraw, unclaim, invite, kick, player_fk) " +
                                      "VALUE (?, ?, ?, ?, ?, ?, ?, ?)");

        statement.setBoolean(1, false);
        statement.setBoolean(2, false);
        statement.setBoolean(3, false);
        statement.setBoolean(4, false);
        statement.setBoolean(5, false);
        statement.setBoolean(6, false);
        statement.setBoolean(7, false);
        statement.setInt(8, getPlayerFk(player));

        statement.executeUpdate();
        statement.close();
    }

    public Map<String, Boolean> getAllPermissionOfAPlayer(Player p) throws SQLException{
        Map<String, Boolean> permissions = new HashMap<>();

        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement(  "SELECT mayor, second, claim, withdraw, unclaim, invite, kick " +
                                        "FROM t_permission_list " +
                                        "WHERE player_fk = ? ");

        statement.setInt(1, getPlayerFk(p));

        ResultSet results = statement.executeQuery();

        if (!results.next()) {
            return null;
        }

        permissions.put("mayor", results.getBoolean("mayor"));
        permissions.put("second", results.getBoolean("second"));
        permissions.put("claim", results.getBoolean("claim"));
        permissions.put("withdraw", results.getBoolean("withdraw"));
        permissions.put("unclaim", results.getBoolean("unclaim"));
        permissions.put("invite", results.getBoolean("invite"));
        permissions.put("kick", results.getBoolean("kick"));

        return permissions;
    }

    public void setAllPermissionsOfAPlayerToFalse(Player p) throws SQLException{
        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement(  "UPDATE t_permission_list SET " +
                        "mayor = FALSE, " +
                        "second = FALSE, " +
                        "claim = FALSE, " +
                        "withdraw = FALSE, " +
                        "unclaim = FALSE, " +
                        "invite = FALSE, " +
                        "kick = FALSE " +
                        "WHERE player_fk = ? ");
        statement.setInt(1, getPlayerFk(p));

        statement.executeUpdate();
        statement.close();
    }

    public void setAllPermissionsOfAPlayerToTrue(Player p) throws SQLException{
        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement(  "UPDATE t_permission_list SET " +
                        "mayor = TRUE, " +
                        "second = TRUE, " +
                        "claim = TRUE, " +
                        "withdraw = TRUE, " +
                        "unclaim = TRUE, " +
                        "invite = TRUE, " +
                        "kick = TRUE " +
                        "WHERE player_fk = ? ");
        statement.setInt(1, getPlayerFk(p));

        statement.executeUpdate();
        statement.close();
    }

    public void setPermissionOfAPlayerToTrue(Player p, String permissionToGive) throws SQLException{
        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement("UPDATE t_permission_list SET " + permissionToGive + " = TRUE WHERE player_fk = ?");
        statement.setInt(1, getPlayerFk(p));

        statement.executeUpdate();
        statement.close();
    }

    public void setPermissionOfAPlayerToFalse(Player p, String permissionToGive) throws SQLException{
        PreparedStatement statement = _plugin.databaseLink._connection
                .prepareStatement("UPDATE t_permission_list SET " + permissionToGive +" = FALSE WHERE player_fk = ?");
        statement.setInt(1, getPlayerFk(p));

        statement.executeUpdate();
        statement.close();
    }

    public List<String> getAllTownsNames() {
        List<String> townsNames  = new ArrayList<>();
        try {
            PreparedStatement statement =  _plugin.databaseLink._connection
                    .prepareStatement("SELECT name " +
                            "FROM t_town ");

            ResultSet results = statement.executeQuery();
            int i = 1;
            while(results.next()) {
                townsNames.add(results.getString(i));
                i++;
            }

            return townsNames;



        } catch (SQLException e) {
            System.out.println("[Database API] - " + e);
        }
        return null;
    }

    public void KickPlayerOfTown(Player p) {
        try {
            PreparedStatement setTownStatement = _plugin.databaseLink._connection
                    .prepareStatement("UPDATE t_players SET town_fk = null " +
                            "WHERE uuid = ?");
            setTownStatement.setString(1, p.getUniqueId().toString());

            setAllPermissionsOfAPlayerToFalse(p);

            setTownStatement.executeUpdate();
            setTownStatement.close();
        } catch (SQLException e) {
            System.out.println("[Database API] - " + e);
        }
    }

    public boolean HaveAPlayerAPerm(Player p, String permToCheck) {
        try {
            PreparedStatement statement =  _plugin.databaseLink._connection
                    // there no sql injection possible, because the string is checked for to be in the perms list
                    .prepareStatement("SELECT " + permToCheck + " FROM t_permission_list WHERE player_fk = ?");
            statement.setInt(1, getPlayerFk(p));

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getBoolean(permToCheck);
            }



        } catch (SQLException e) {
            System.out.println("[Database API] - " + "HaveAPlayerAPerm - " + e);
        }
        return false;
    }

    public List<String> getAllSecondFromTown(String townName) {
        List<String> seconds = new ArrayList<>();
        try {
            PreparedStatement statement =  _plugin.databaseLink._connection
                    // there no sql injection possible, because the string is checked for to be in the perms list
                    .prepareStatement("SELECT p.name " +
                            "FROM t_permission_list AS pl " +
                            "JOIN t_players AS p ON p.player_id = pl.player_fk " +
                            "JOIN t_town AS t ON t.town_id = p.town_fk " +
                            "WHERE t.name = ?  " +
                            "AND pl.second = true");
            statement.setString(1, townName);

            ResultSet results = statement.executeQuery();

            int i = 1;
            while(results.next()) {
                seconds.add(results.getString(i));
                i++;
            }

            return seconds;

        } catch (SQLException e) {
            System.out.println("[Database API] - " + "getAllSecondFromTown - " + e);
        }
        return null;
    }

    public Integer[] getSpawnTown(String tName) {
        Integer[] spawnTown = new Integer[3];
        try {
            PreparedStatement statement =  _plugin.databaseLink._connection
                    // there no sql injection possible, because the string is checked for to be in the perms list
                    .prepareStatement("SELECT spawn_x, spawn_y, spawn_z " +
                            "FROM t_town " +
                            "WHERE name = ?  ");
            statement.setString(1, tName);

            ResultSet results = statement.executeQuery();

            if (results.next()) {

                spawnTown[0] = results.getObject("spawn_x", Integer.class);
                spawnTown[1] = results.getObject("spawn_y", Integer.class);
                spawnTown[2] = results.getObject("spawn_z", Integer.class);

            }

            return spawnTown;

        } catch (SQLException e) {
            System.out.println("[Database API] - " + "getSpawnTown - " + e);
        }
        return null;
    }

    public void setSpawnTown(String tName, Integer[]spawnPos) {
        try {
            PreparedStatement statement =  _plugin.databaseLink._connection
                    // there no sql injection possible, because of the string is checked for to be in the perms list
                    .prepareStatement("UPDATE t_town SET spawn_x = ?, " +
                            "spawn_y = ?, " +
                            "spawn_z = ? " +
                            "WHERE name = ?  ");
            statement.setInt(1, spawnPos[0]);
            statement.setInt(2, spawnPos[1]);
            statement.setInt(3, spawnPos[2]);
            statement.setString(4, tName);

            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            System.out.println("[Database API] - " + "setSpawnTown - " + e);
        }
    }

    public void CreatePlayerJobInDb(Player p, int jobFk) {
        int playerId = getPlayerFk(p);
        try {
            PreparedStatement statement =  _plugin.databaseLink._connection
                    .prepareStatement("INSERT INTO t_player_jobs (player_fk, job_fk) VALUES (?, ?)");
            statement.setInt(1, playerId);
            statement.setInt(2, jobFk);

            statement.execute();
            statement.close();

        } catch (SQLException e) {
            System.out.println("[Database API] - " + "CreatePlayerJobInDb - " + e);
        }
    }

    public void IncreasePlayerJobExp(Player p, int jobFk, Double exp) {
        int playerId = getPlayerFk(p);
        try {
            PreparedStatement statement =  _plugin.databaseLink._connection
                    .prepareStatement("UPDATE t_player_jobs SET current_exp = current_exp + ? WHERE player_fk = ? AND job_fk = ?");
            statement.setDouble(1, exp);
            statement.setInt(2, playerId);
            statement.setInt(3, jobFk);

            statement.execute();
            statement.close();

        } catch (SQLException e) {
            System.out.println("[Database API] - " + "IncreasePlayerJobExp - " + e);
        }
    }

    public void IncreasePlayerJobLvl(Player p, int jobFk) {
        int playerId = getPlayerFk(p);
        try {
            PreparedStatement statement =  _plugin.databaseLink._connection
                    .prepareStatement("UPDATE t_player_jobs SET level = level + 1 WHERE player_fk = ? AND job_fk = ?");
            statement.setInt(1, playerId);
            statement.setInt(2, jobFk);

            statement.execute();
            statement.close();

        } catch (SQLException e) {
            System.out.println("[Database API] - " + "IncreasePlayerJobLvl - " + e);
        }
    }

    public void ResetPlayerJobExp(Player p, int jobFk) {
        int playerId = getPlayerFk(p);
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("UPDATE t_player_jobs SET current_exp = 0 WHERE player_fk = ? AND job_fk = ?");
            statement.setInt(1, playerId);
            statement.setInt(2, jobFk);

            statement.execute();
            statement.close();

        } catch (SQLException e) {
            System.out.println("[Database API] - " + "ResetPlayerJobExp - " + e);
        }
    }

    public void SetPlayerJobLvl(Player p, int jobFk, int level) {
        int playerId = getPlayerFk(p);
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("UPDATE t_player_jobs SET level = ? WHERE player_fk = ? AND job_fk = ?");
            statement.setInt(1, level);
            statement.setInt(2, playerId);
            statement.setInt(3, jobFk);

            statement.execute();
            statement.close();

        } catch (SQLException e) {
            System.out.println("[Database API] - " + "SetPlayerJobLvl - " + e);
        }
    }

    public JobProgression getJobProgression(Player p, int jobFk) {
        int playerId = getPlayerFk(p);
        try {
            PreparedStatement statement =  _plugin.databaseLink._connection
                    .prepareStatement("SELECT level, current_exp FROM t_player_jobs WHERE player_fk = ? AND job_fk = ?");
            statement.setInt(1, playerId);
            statement.setInt(2, jobFk);

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                int level = results.getInt("level");
                double currentExp = results.getDouble("current_exp");

                return new JobProgression(jobFk, currentExp, level, getJobInfos(jobFk));
            }
            statement.close();

        } catch (SQLException e) {
            System.out.println("[Database API] - " + "FindJobPlayerInfo - " + e);
        }
        return null;
    }


    public void setProgress(Player p,JobProgression jobProgression) {
        int playerId = getPlayerFk(p);

        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("UPDATE t_player_jobs SET current_exp = ?, level = ? WHERE player_fk = ? AND job_fk = ?");
            statement.setDouble(1, jobProgression.getExperience());
            statement.setInt(2, jobProgression.getLevel());
            statement.setInt(3, playerId);
            statement.setInt(4, jobProgression.getJobId());

            statement.execute();
            statement.close();

        } catch (SQLException e) {
            System.out.println("[Database API] - " + "setProgress - " + e);
        }
    }

    public void UpdateLastLeaveJob(Player p, int jobFk) {
        int playerId = getPlayerFk(p);

        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("UPDATE t_player_jobs SET last_leave = NOW() WHERE player_fk = ? AND job_fk = ?");
            statement.setInt(1, playerId);
            statement.setInt(2, jobFk);

            statement.execute();
            statement.close();

        } catch (SQLException e) {
            System.out.println("[Database API] - " + "UpdateLastLeaveJob - " + e);
        }
    }

    public LocalDateTime getLastLeaveTime(Player p, int jobFk) {
        int playerId = getPlayerFk(p);

        try (PreparedStatement statement = _connection
                .prepareStatement("SELECT last_leave FROM t_player_jobs WHERE player_fk = ? AND job_fk = ?")) {
            statement.setInt(1, playerId);
            statement.setInt(2, jobFk);

            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    return results.getObject("last_leave", LocalDateTime.class);
                }
            }
        } catch (SQLException e) {
            System.out.println("[Database API] - " + "getLastLeaveTime - " + e);
        }
        return null;
    }

    public void InsertBlockPlaced(BlockData block) {
        // exécute la requete en arrière-plan de manière asynchrone
        org.bukkit.Bukkit.getScheduler().runTaskAsynchronously(_plugin, () -> {
            String query = "INSERT INTO t_player_placed_blocks (player_uuid, world, x, y, z, block_type) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = _connection.prepareStatement(query)) {
                statement.setString(1, block.getPlayer().toString());
                statement.setString(2, block.getWorld());
                statement.setInt(3, block.getX());
                statement.setInt(4, block.getY());
                statement.setInt(5, block.getZ());
                statement.setString(6, block.getBlockType());

                statement.executeUpdate();

            } catch (SQLException e) {
                System.out.println("[Database API] - InsertBlockPlaced - " + e.getMessage());
            }
        });
    }

    public void insertBlockBatch(Collection<BlockData> blocks) {
        if (blocks == null || blocks.isEmpty()) {
            return;
        }

        org.bukkit.Bukkit.getScheduler().runTaskAsynchronously(_plugin, () -> {
            String query = "INSERT INTO t_player_placed_blocks (player_uuid, world, x, y, z, block_type) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = _connection.prepareStatement(query)) {

                for (BlockData block : blocks) {
                    statement.setString(1, block.getPlayer().toString());
                    statement.setString(2, block.getWorld());
                    statement.setInt(3, block.getX());
                    statement.setInt(4, block.getY());
                    statement.setInt(5, block.getZ());
                    statement.setString(6, block.getBlockType());

                    // permet de tout stocker dans un Batch avant d'envoyer le packet en db
                    statement.addBatch();
                }

                statement.executeBatch();

            } catch (SQLException e) {
                System.out.println("[Database API] - insertBlockBatch - " + e.getMessage());
            }
        });
    }

    public CompletableFuture<BlockData> getPlacedBlockByLocation(int x, int y, int z, String worldName) {
        // CompletableFuture permet un retour de manière asynchrone
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT * FROM t_player_placed_blocks WHERE x = ? AND y = ? AND z = ? AND world = ?";

            try (PreparedStatement statement = _connection.prepareStatement(query)) {
                statement.setInt(1, x);
                statement.setInt(2, y);
                statement.setInt(3, z);
                statement.setString(4, worldName);

                try (ResultSet results = statement.executeQuery()) {
                    if (results.next()) {
                        return new BlockData(
                                UUID.fromString(results.getString("player_uuid")),
                                results.getString("world"),
                                results.getInt("x"),
                                results.getInt("y"),
                                results.getInt("z"),
                                results.getString("block_type")
                                //results.getTimestamp("date") != null ? Timestamp.valueOf(results.getTimestamp("date").toLocalDateTime()) : null
                        );
                    }
                }
            } catch (SQLException e) {
                System.out.println("[Database API] - getPlacedBlockByLocation - " + e.getMessage());
            }

            return null;
        });
    }

    public CompletableFuture<BlockData> getPlacedBlockByLocation(DatabaseCommunicationAPI.BlockPosition blockPosition) {
        // CompletableFuture permet un retour de manière asynchrone
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT * FROM t_player_placed_blocks WHERE x = ? AND y = ? AND z = ? AND world = ?";

            try (PreparedStatement statement = _connection.prepareStatement(query)) {
                statement.setInt(1, blockPosition.x());
                statement.setInt(2, blockPosition.y());
                statement.setInt(3, blockPosition.z());
                statement.setString(4, blockPosition.worldName());

                try (ResultSet results = statement.executeQuery()) {
                    if (results.next()) {
                        return new BlockData(
                                UUID.fromString(results.getString("player_uuid")),
                                results.getString("world"),
                                results.getInt("x"),
                                results.getInt("y"),
                                results.getInt("z"),
                                results.getString("block_type")
                                //results.getTimestamp("date") != null ? Timestamp.valueOf(results.getTimestamp("date").toLocalDateTime()) : null
                        );
                    }
                }
            } catch (SQLException e) {
                System.out.println("[Database API] - getPlacedBlockByLocation - " + e.getMessage());
            }

            return null;
        });
    }

}
