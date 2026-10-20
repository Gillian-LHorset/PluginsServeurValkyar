package fr.Valkyar.databaseCommunicationAPI.Models;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import org.bukkit.Location;


import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.ChunkCoord;

import java.util.List;

public class AvantGarde {
    private final int id;
    private final String name;
    private Location spawnLocation;
    private final List<ChunkCoord> avantGardeChunks;

    public AvantGarde(int id, String name, Location spawnLocation, List<ChunkCoord> chunks) {
        this.id = id;
        this.name = name;
        this.spawnLocation = spawnLocation;
        this.avantGardeChunks = chunks;
    }

    public String getName() { return name; }
    public Location getSpawnLocation() { return spawnLocation; }
    public List<ChunkCoord> getChunks() { return avantGardeChunks; }

    public boolean addChunk(ChunkCoord chunk) {
        return this.avantGardeChunks.add(chunk);
    }

    public void registerChunk(ChunkCoord chunk) {
        this.avantGardeChunks.add(chunk);
    }

    public void deleteChunk(ChunkCoord chunk) {
        this.avantGardeChunks.remove(chunk);
    }

    public boolean hasChunk(ChunkCoord coord) {
        return this.avantGardeChunks.contains(coord);
    }

    public boolean hasChunk(int x, int z) {
        return this.avantGardeChunks.contains(new ChunkCoord(x, z));
    }

    public boolean hasSpawn() {return this.spawnLocation != null;}

    public void setSpawnLocation(Location spawnLocation) { this.spawnLocation = spawnLocation; }
}
