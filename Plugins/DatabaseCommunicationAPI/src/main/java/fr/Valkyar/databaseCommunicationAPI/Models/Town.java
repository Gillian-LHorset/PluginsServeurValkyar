package fr.Valkyar.databaseCommunicationAPI.Models;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Town {// TODO : à terme déplacer tous les appels db vers TownManager
    private final int id;
    private String name;
    private Double balance;
    private Set<DatabaseCommunicationAPI.ChunkCoord> claimedChunk = new HashSet<>();
    private List<UUID> townResidentsId = new ArrayList<>();
    private Location spawnLocation;
    private Database db;

    public Town(int id, String name, Double balance,
                Set<DatabaseCommunicationAPI.ChunkCoord> claimedChunk, List<UUID> townResidentsId,
                Location spawnLocation, Database db) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.claimedChunk = claimedChunk;
        this.townResidentsId = townResidentsId;
        this.spawnLocation = spawnLocation;
        this.db = db;
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public Double getBalance() {return balance;}
    public Set<DatabaseCommunicationAPI.ChunkCoord> getclaimedChunk() {return claimedChunk;}
    public List<Player> getTownPlayers() {
        List<Player> townPlayers = new ArrayList<>();
        for (UUID uuid : townResidentsId) {
            townPlayers.add(Bukkit.getPlayer(uuid));
        }
        return townPlayers;
    }

    public void setTownSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
        db.setSpawnTown(this, spawnLocation);
    }

    public Location getSpawnLocation() {return spawnLocation;}

    public void AddPlayerTown(Player player) {
        townResidentsId.add(player.getUniqueId());
        db.setPlayerTown(player, this);
    }

    public void RemovePlayerTown(Player player) {
        townResidentsId.remove(player.getUniqueId());
        db.setPlayerTown(player, null);
    }

    public boolean addChunk(DatabaseCommunicationAPI.ChunkCoord chunk) {
        return this.claimedChunk.add(chunk);
    }

    public void registerChunk(DatabaseCommunicationAPI.ChunkCoord chunk) {
        this.claimedChunk.add(chunk);
    }

    public void deleteChunk(DatabaseCommunicationAPI.ChunkCoord chunk) {
        this.claimedChunk.remove(chunk);
    }

    public boolean hasChunk(DatabaseCommunicationAPI.ChunkCoord coord) {
        return this.claimedChunk.contains(coord);
    }

    public boolean hasChunk(int x, int z) {
        return this.claimedChunk.contains(new DatabaseCommunicationAPI.ChunkCoord(x, z));
    }

    public boolean hasSpawn() {return this.spawnLocation != null;}

    public void DepositMoney(Double amount) {
        balance += amount;
    }
    public void WithdrawMoney(Double amount) {
        balance -= amount;
    }
}
