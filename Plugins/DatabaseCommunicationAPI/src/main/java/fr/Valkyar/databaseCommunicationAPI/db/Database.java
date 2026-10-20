package fr.Valkyar.databaseCommunicationAPI.db;

import fr.Valkyar.databaseCommunicationAPI.Models.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.ChunkCoord;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.TownMap;

public class Database {
    private Connection _connection;

    //TODO need to be changed for the final server

    private DatabaseCommunicationAPI _plugin;

    public Database(DatabaseCommunicationAPI plugin) {
        this._plugin = plugin;
    }

    /**
     * Make a connexion to the db.
     *
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


    /**
     * Add the name, uuid and balance of a player to the database
     *
     * @param player This is the player who the information are going to be added to the db
     * @throws SQLException In the case of the connexion with the db cannot be done, the methode throw an exception.
     */
    public void RegisterPlayerInDB(Player player) {
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("INSERT INTO t_players (uuid, name, balance) VALUE (?, ?, ?)");

            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getDisplayName());
            statement.setDouble(3, 0);

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {

            System.out.println("[Database API] - RegisterPlayerInDB - " + e);
        }
    }

    // plein de testo
    public PlayerProfile getBasePlayerProfile(Player player) {
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT uuid, balance, town_fk FROM t_players " +
                            "WHERE uuid = ?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return new PlayerProfile(UUID.fromString(results.getString("uuid")),
                        results.getDouble("balance"),
                        results.getObject("town_fk", Integer.class),
                        this);
            }

            statement.close();
        } catch (SQLException e) {
            System.out.println("[Database API] - RegisterPlayerInDB - " + e);
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
        } catch (SQLException e) {
            DatabaseCommunicationAPI.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT, "[DatabaseCommunicationAPI] : Erreur de connexion 'getAllJobsInfos()'");
        }
        return null;
    }

    public List<Job> getPlayerJobs(Player p) {
        try {
            List<Job> job = new ArrayList<>();

            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT * FROM t_jobs AS j " +
                            "JOIN t_players AS p " +
                            "ON p.main_job_fk = j.job_id OR p.second_job_fk = j.job_id " +
                            "WHERE p.uuid = ?");
            statement.setString(1, p.getUniqueId().toString());
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                job.add(new Job(
                        results.getInt("job_id"),
                        results.getString("name"),
                        results.getBoolean("isMain")));
            }

            return job;
        } catch (SQLException e) {
            DatabaseCommunicationAPI.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT, "[DatabaseCommunicationAPI] : Erreur de connexion 'getPlayerJobs()'");
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

    public void setPlayerMainJob(Player player, Integer jobsId) throws SQLException {
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

    public void setPlayerSecondJob(Player player, Integer jobsId) throws SQLException {
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

    public List<ResourceData> getResourceByJob(int jobFk, String actionType) {
        List<ResourceData> breakableBlocksList = new ArrayList<>();
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT block_id, minecraft_identifier, money_bringing, exp_bringing, action ,job_fk " +
                            "FROM t_action_type " +
                            "WHERE job_fk = ? AND action = ?");
            statement.setInt(1, jobFk);
            statement.setString(2, actionType);

            ResultSet results = statement.executeQuery();

            while (results.next()) {
                breakableBlocksList.add(new ResourceData(results.getInt("block_id"),
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

    public Town CreateTown(Player player, String newTownName) {
        try {
            PreparedStatement createTownStatement = _plugin.databaseLink._connection
                    .prepareStatement("INSERT INTO t_town (name) " +
                            "VALUE (?)", Statement.RETURN_GENERATED_KEYS);

            createTownStatement.setString(1, newTownName);

            createTownStatement.executeUpdate();

            try (ResultSet generatedKeys = createTownStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int townId = generatedKeys.getInt(1);

                    return new Town(
                            townId,
                            newTownName,
                            0.00,
                            null,
                            List.of(player.getUniqueId()),
                            null,
                            this);
                }
            }
            createTownStatement.close();
        } catch (Exception err) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'CreateTown()' 'INSERT INTO t_town'");
            System.out.println(err.getMessage());
        }
        return null;
    }

    public List<UUID> getUuidOfTownResidentsById(int townId) {
        List<UUID> uuidList = new ArrayList<>();
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT uuid " +
                            "FROM t_players " +
                            "WHERE town_fk = ? ");
            statement.setInt(1, townId);

            ResultSet results = statement.executeQuery();

            while (results.next()) {
                uuidList.add(UUID.fromString(results.getString("uuid")));
            }

            statement.close();

            return uuidList;
        } catch (SQLException error) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'GetPlayerFk()'");
        }
        return null;
    }

    public Integer getPlayerFk(Player player) {
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT player_id " +
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
        Town town = null;

        String townQuery = "SELECT t.town_id, t.name, t.balance " +
                "FROM t_town AS t " +
                "JOIN t_players AS p " +
                "ON p.town_fk = t.town_id " +
                "WHERE p.uuid = ?";
        try (PreparedStatement stmt = _plugin.databaseLink._connection.prepareStatement(townQuery)) {
            stmt.setString(1, p.getUniqueId().toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    town = new Town(
                            rs.getInt("town_id"),
                            rs.getString("name"),
                            rs.getDouble("balance"),
                            new HashSet<>(),
                            getUuidOfTownResidentsById(rs.getInt("town_id")),
                            getSpawnTown(rs.getInt("town_id")),
                            this
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        if (town == null) return null;

        String chunkQuery = "SELECT chunk_identifier_x, chunk_identifier_z " +
                "FROM t_chunk_town " +
                "WHERE town_fk = ?";
        try (PreparedStatement stmt = _plugin.databaseLink._connection.prepareStatement(chunkQuery)) {
            stmt.setInt(1, town.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    town.addChunk(new ChunkCoord(
                            rs.getInt("chunk_identifier_x"),
                            rs.getInt("chunk_identifier_z")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return town;
    }

    public void setPlayerTown(Player player, Town town) {
        try {
            PreparedStatement statement;
            if (town != null) {
                statement = _plugin.databaseLink._connection
                        .prepareStatement("UPDATE t_players " +
                                "SET town_fk = ? " +
                                "WHERE uuid = ? ");
                statement.setInt(1, town.getId());

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

    public Integer getNumberOfPlayersInTown(Town town) {
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT COUNT(p.uuid) " +
                            "FROM t_town AS t " +
                            "JOIN t_players AS p " +
                            "ON p.town_fk = t.town_id " +
                            "WHERE t.town_id = ? ");
            statement.setInt(1, town.getId());

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

    public void deleteTown(Town town) {
        try {
            deleteAllChunksOfTown(town);
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("DELETE FROM t_town " +
                            "WHERE town_id = ? ");
            statement.setInt(1, town.getId());

            statement.executeUpdate();
            statement.close();
        } catch (Exception err) {

        }
    }

    // TODO : vérifier si c'est onCascadDelete
    public void deleteAllChunksOfTown(Town town) {
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("DELETE FROM t_chunk_town " +
                            "WHERE town_fk = ? ");
            statement.setInt(1, town.getId());

            statement.executeUpdate();
            statement.close();
        } catch (Exception err) {

        }
    }

    public List<String> getAllPlayerOfTown(Town town) {
        List<String> allPlayers = new ArrayList<>();
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT p.name " +
                            "FROM t_town AS t " +
                            "JOIN t_players AS p " +
                            "ON t.town_id = p.town_fk " +
                            "WHERE t.name = ? ");
            statement.setInt(1, town.getId());

            ResultSet results = statement.executeQuery();

            while (results.next()) {
                allPlayers.add(results.getString("p.name"));
            }

            statement.close();
        } catch (Exception err) {

        }
        return allPlayers;
    }

    public void registerChunk(ChunkCoord chunks, Town town) {
        try {
            PreparedStatement createTownStatement = _plugin.databaseLink._connection
                    .prepareStatement("INSERT INTO t_chunk_town   (chunk_identifier_x, chunk_identifier_z, town_fk) " +
                            "VALUE                      (?, ?, ?)");

            createTownStatement.setInt(1, chunks.x());
            createTownStatement.setInt(2, chunks.z());
            createTownStatement.setInt(3, town.getId());

            createTownStatement.executeUpdate();
            createTownStatement.close();
        } catch (Exception err) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'registerChunk()'");
            System.out.println(err.getMessage());
        }
    }

    public Integer getTownFkByName(String townName) {
        try {
            ChunkCoord chunkProrities;

            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT town_id " +
                            "FROM t_town " +
                            "WHERE name = ? ");
            statement.setString(1, townName);

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getInt("town_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ChunkCoord getChunkById(ChunkCoord giveId, Town town) {
        try {
            ChunkCoord chunkProrities;

            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT chunk_identifier_x, chunk_identifier_z, town_fk " +
                            "FROM t_chunk_town " +
                            "WHERE chunk_identifier_x = ? " +
                            "AND chunk_identifier_z = ?");
            statement.setInt(1, giveId.x());
            statement.setInt(2, giveId.z());

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                chunkProrities = new ChunkCoord(
                        results.getInt("chunk_identifier_x"),
                        results.getInt("chunk_identifier_z")
                );

                return chunkProrities;
            }

            statement.close();
        } catch (SQLException e) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion 'getChunkById()'" + e.getMessage());
        }

        return null;
    }


    /**
     * @param town The town for which we want to get the chunks.
     * @return A list of coordinate of chunk who is not an outpost
     */
    public Set<ChunkCoord> getAllTownChunkOfTown(Town town) {
        try {
            Set<ChunkCoord> allChunks = new HashSet<>();

            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT chunk_identifier_x, chunk_identifier_z " +
                            "FROM t_chunk_town " +
                            "WHERE town_fk = ? ");
            statement.setInt(1, town.getId());

            ResultSet results = statement.executeQuery();

            while (results.next()) {
                allChunks.add(new ChunkCoord(
                        results.getInt("chunk_identifier_x"),
                        results.getInt("chunk_identifier_z")
                ));
            }

            return allChunks;
        } catch (SQLException e) {
            System.out.println("[Database API] - getAllChunkOfTown : " + e);
        }
        return null;
    }

    public boolean isChunkFromTown(Town town, ChunkCoord chunk) {
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT * " +
                            "FROM t_chunk_town " +
                            "WHERE town_fk = ? " +
                            "AND chunk_identifier_x = ? " +
                            "AND chunk_identifier_z = ?");
            statement.setInt(1, town.getId());
            statement.setInt(2, chunk.x());
            statement.setInt(3, chunk.z());

            ResultSet results = statement.executeQuery();

            return (results.next());


        } catch (SQLException e) {
            System.out.println("[Database API] - isChunkFromTown : " + e);
        }
        return false;
    }

    public void deleteAChunksById(ChunkCoord chunk, Town town) {
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("DELETE FROM t_chunk_town " +
                            "WHERE town_fk = ? " +
                            "AND chunk_identifier_x = ? " +
                            "AND chunk_identifier_z = ?");
            statement.setInt(1, town.getId());
            statement.setInt(2, chunk.x());
            statement.setInt(3, chunk.z());

            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            System.out.println("[Database API] - deleteAChunksById : " + e);
        }
    }

    public void registerPermissionPlayer(Player player) {
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("INSERT INTO t_permission_list (town_mayor, town_second, town_claim, town_withdraw, town_unclaim, town_invite, town_kick, player_fk) " +
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
        } catch (SQLException e) {
            System.out.println("[Database API] - registerPermissionPlayer : " + e);
        }
    }

    public HashMap<String, Boolean> getAllPermissionOfAPlayer(Player p) {
        try {
            HashMap<String, Boolean> permissions = new HashMap<>();

            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT town_mayor, town_second, town_claim, town_withdraw, town_unclaim, town_invite, town_kick " +
                            "FROM t_permission_list " +
                            "WHERE player_fk = ? ");

            statement.setInt(1, getPlayerFk(p));

            ResultSet results = statement.executeQuery();

            if (!results.next()) {
                return null;
            }

            permissions.put("town_mayor", results.getBoolean("town_mayor"));
            permissions.put("town_second", results.getBoolean("town_second"));
            permissions.put("town_claim", results.getBoolean("town_claim"));
            permissions.put("town_withdraw", results.getBoolean("town_withdraw"));
            permissions.put("town_unclaim", results.getBoolean("town_unclaim"));
            permissions.put("town_invite", results.getBoolean("town_invite"));
            permissions.put("town_kick", results.getBoolean("town_kick"));

            return permissions;
        } catch (SQLException e) {
            System.out.println("[Database API] - getAllPermissionOfAPlayer : " + e);
        }
        return null;
    }

    public void setAllPermissionsOfAPlayerToFalse(Player p) {
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("UPDATE t_permission_list SET " +
                            "town_mayor = FALSE, " +
                            "town_second = FALSE, " +
                            "town_claim = FALSE, " +
                            "town_withdraw = FALSE, " +
                            "town_unclaim = FALSE, " +
                            "town_invite = FALSE, " +
                            "town_kick = FALSE " +
                            "WHERE player_fk = ? ");
            statement.setInt(1, getPlayerFk(p));

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            System.out.println("[Database API] - setAllPermissionsOfAPlayerToFalse : " + e);
        }
    }

    public void setAllPermissionsOfAPlayerToTrue(Player p) {
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("UPDATE t_permission_list SET " +
                            "town_mayor = TRUE, " +
                            "town_second = TRUE, " +
                            "town_claim = TRUE, " +
                            "town_withdraw = TRUE, " +
                            "town_unclaim = TRUE, " +
                            "town_invite = TRUE, " +
                            "town_kick = TRUE " +
                            "WHERE player_fk = ? ");
            statement.setInt(1, getPlayerFk(p));

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            System.out.println("[Database API] - setAllPermissionsOfAPlayerToTrue : " + e);
        }
    }

    public void setPermissionOfAPlayerToTrue(Player p, String permissionToGive) {
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("UPDATE t_permission_list SET " + permissionToGive + " = TRUE WHERE player_fk = ?");
            statement.setInt(1, getPlayerFk(p));

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            System.out.println("[Database API] - setPermissionOfAPlayerToTrue : " + e);
        }
    }

    public void setPermissionOfAPlayerToFalse(Player p, String permissionToGive) {
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("UPDATE t_permission_list SET " + permissionToGive + " = FALSE WHERE player_fk = ?");
            statement.setInt(1, getPlayerFk(p));

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            System.out.println("[Database API] - setPermissionOfAPlayerToFalse : " + e);
        }
    }

    public List<Town> getAllTownsBasics() { //TODO : récuperer les chunk et les avantgarde chunks
        Map<Integer, Town> townMap = new HashMap<>();

        String query = "SELECT town_id, name, balance " +
                "FROM t_town ";

        try (PreparedStatement statement = _plugin.databaseLink._connection.prepareStatement(query);
             ResultSet results = statement.executeQuery()) {

            while (results.next()) {
                int townId = results.getInt("town_id");

                Town town = new Town(
                        townId,
                        results.getString("name"),
                        results.getDouble("balance"),
                        new HashSet<>(),
                        getUuidOfTownResidentsById(townId),
                        getSpawnTown(townId),
                        this);

                townMap.put(townId, town);

            }

            return new ArrayList<>(townMap.values());

        } catch (SQLException e) {
            System.out.println("[Database API] - getAllTowns - " + e);
            e.printStackTrace();
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
            PreparedStatement statement = _plugin.databaseLink._connection
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
            PreparedStatement statement = _plugin.databaseLink._connection
                    // there no sql injection possible, because the string is checked for to be in the perms list
                    .prepareStatement("SELECT p.name " +
                            "FROM t_permission_list AS pl " +
                            "JOIN t_players AS p ON p.player_id = pl.player_fk " +
                            "JOIN t_town AS t ON t.town_id = p.town_fk " +
                            "WHERE t.name = ?  " +
                            "AND pl.town_second = true");
            statement.setString(1, townName);

            ResultSet results = statement.executeQuery();

            int i = 1;
            while (results.next()) {
                seconds.add(results.getString(i));
                i++;
            }

            return seconds;

        } catch (SQLException e) {
            System.out.println("[Database API] - " + "getAllSecondFromTown - " + e);
        }
        return null;
    }

    public Location getSpawnTown(Town town) {
        Location spawnTown;
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    // there no sql injection possible, because the string is checked for to be in the perms list
                    .prepareStatement("SELECT spawn_x, spawn_y, spawn_z " +
                            "FROM t_town " +
                            "WHERE town_id = ?  ");
            statement.setInt(1, town.getId());

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return spawnTown = new Location(
                        Bukkit.getWorld("world"),
                        results.getObject("spawn_x", Integer.class),
                        results.getObject("spawn_y", Integer.class),
                        results.getObject("spawn_z", Integer.class)
                );

            }
        } catch (SQLException e) {
            System.out.println("[Database API] - " + "getSpawnTown - " + e);
        }
        return null;
    }

    public Location getSpawnTown(int townId) {
        Location spawnTown;
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    // there no sql injection possible, because the string is checked for to be in the perms list
                    .prepareStatement("SELECT spawn_x, spawn_y, spawn_z " +
                            "FROM t_town " +
                            "WHERE town_id = ?  ");
            statement.setInt(1, townId);

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return spawnTown = new Location(
                        Bukkit.getWorld("world"),
                        results.getObject("spawn_x", Integer.class),
                        results.getObject("spawn_y", Integer.class),
                        results.getObject("spawn_z", Integer.class)
                );

            }
        } catch (SQLException e) {
            System.out.println("[Database API] - " + "getSpawnTown - " + e);
        }
        return null;
    }

    public void setSpawnTown(Town town, Location spawnPos) {
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    // there no sql injection possible, because of the string is checked for to be in the perms list
                    .prepareStatement("UPDATE t_town SET spawn_x = ?, " +
                            "spawn_y = ?, " +
                            "spawn_z = ? " +
                            "WHERE town_id = ?  ");
            statement.setInt(1, spawnPos.getBlockX());
            statement.setInt(2, spawnPos.getBlockY());
            statement.setInt(3, spawnPos.getBlockZ());
            statement.setInt(4, town.getId());

            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            System.out.println("[Database API] - " + "setSpawnTown - " + e);
        }
    }

    public void CreatePlayerJobInDb(Player p, int jobFk) {
        int playerId = getPlayerFk(p);
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("INSERT INTO t_player_jobs (player_fk, job_fk) VALUES (?, ?)");
            statement.setInt(1, playerId);
            statement.setInt(2, jobFk);

            statement.execute();
            statement.close();

        } catch (SQLException e) {
            System.out.println("[Database API] - " + "CreatePlayerJobInDb - " + e);
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

    public JobProgression getJobProgression(Player p, Job playerJob) {
        int playerId = getPlayerFk(p);
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("SELECT job_fk ,level, current_exp FROM t_player_jobs WHERE player_fk = ? AND job_fk = ?");
            statement.setInt(1, playerId);
            statement.setInt(2, playerJob.getId());

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                int jobFk = results.getInt("job_fk");
                int level = results.getInt("level");
                double currentExp = results.getDouble("current_exp");

                return new JobProgression(jobFk, currentExp, level, playerJob);
            }
            statement.close();

        } catch (SQLException e) {
            System.out.println("[Database API] - " + "FindJobPlayerInfo - " + e);
        }
        return null;
    }


    public void setProgress(Player p, JobProgression jobProgression) {
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

    public void setTownBalance(Town town) {
        try {
            PreparedStatement statement = _plugin.databaseLink._connection
                    .prepareStatement("UPDATE t_town SET balance = ? WHERE town_id = ?");
            statement.setDouble(1, town.getBalance());
            statement.setInt(2, town.getId());

            statement.execute();
            statement.close();

        } catch (SQLException e) {
            System.out.println("[Database API] - " + "setTownBalance - " + e);
        }
    }

    public List<AvantGarde> getTownAvantGardes(Town town) {
        List<AvantGarde> avantGardes = new ArrayList<>();

        String query = "SELECT avant_garde_id, avant_garde_name, spawn_x, spawn_y, spawn_z FROM t_avant_garde WHERE town_fk = ?";
        try (PreparedStatement statement = _connection.prepareStatement(query)) {
            statement.setInt(1, town.getId());

            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    avantGardes.add(new AvantGarde(
                            results.getInt("avant_garde_id"),
                            results.getString("avant_garde_name"),
                            new Location(Bukkit.getWorld("world"),
                                    results.getInt("spawn_x"),
                                    results.getInt("spawn_y"),
                                    results.getInt("spawn_z")
                            ),
                            null
                    ));
                }
                return avantGardes;
            }
        } catch (SQLException e) {
            System.out.println("[Database API] - getTownAvantGardes - " + e.getMessage());
        }
        return null;
    }


    public Set<ChunkCoord> getAvantGardeChunks(AvantGarde avantGarde) {
        String query = "SELECT * FROM t_chunk_town AS c " +
                "JOIN t_avant_garde AS a " +
                    "ON c.chunk_id = a.chunk_fk " +
                "WHERE a.town_fk = ? " +
                "GROUP BY a.avant_garde_name";

        try (PreparedStatement statement = _connection.prepareStatement(query)) {
            statement.setInt(1, blockPosition.x());
            statement.setInt(2, blockPosition.y());
            statement.setInt(3, blockPosition.z());
            statement.setString(4, blockPosition.worldName());

            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {

                }
            }
        } catch (SQLException e) {
            System.out.println("[Database API] - getAvantGardeChunks - " + e.getMessage());
        }
    }
}
