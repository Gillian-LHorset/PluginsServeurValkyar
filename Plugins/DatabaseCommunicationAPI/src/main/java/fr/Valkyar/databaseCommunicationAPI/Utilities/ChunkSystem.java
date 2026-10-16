package fr.Valkyar.databaseCommunicationAPI.Utilities;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.TownMap;

public class ChunkSystem {

    public static DatabaseCommunicationAPI.ChunkCoord getChunkId(Integer x, Integer z) {
        return new DatabaseCommunicationAPI.ChunkCoord(
                Math.floorDiv(x, 16) + 1,
                Math.floorDiv(z, 16) + 1
        );
    }

    public static DatabaseCommunicationAPI.ChunkCoord getChunkId(Location location) {
        return new DatabaseCommunicationAPI.ChunkCoord(
                Math.floorDiv(location.getBlockX(), 16) + 1,
                Math.floorDiv(location.getBlockZ(), 16) + 1
        );
    }

    public static boolean isThisChunkClaim(DatabaseCommunicationAPI.ChunkCoord chunkCoord) {
        for (Town town : TownMap.values()) {
            if (town.hasChunk(chunkCoord)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChunkNextToAnotherTownClaim(Player p, Database db, DatabaseCommunicationAPI.ChunkCoord chunkId, Town town) {
        if (town == null) {
            return false;
        }

        int x = chunkId.x();
        int z = chunkId.z();

        DatabaseCommunicationAPI.ChunkCoord[] adjacentCoords = {
                new DatabaseCommunicationAPI.ChunkCoord(x + 1, z), // Est
                new DatabaseCommunicationAPI.ChunkCoord(x - 1, z), // Ouest
                new DatabaseCommunicationAPI.ChunkCoord(x, z + 1), // Sud
                new DatabaseCommunicationAPI.ChunkCoord(x, z - 1)  // Nord
        };

        for (DatabaseCommunicationAPI.ChunkCoord coord : adjacentCoords) {
            if (town.hasChunk(coord)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isChunkNextToAnother(Player p, Database db, DatabaseCommunicationAPI.ChunkCoord chunkId) {
        for (Town town : TownMap.values()) {
            if (town == null) {
                return false;
            }

            int x = chunkId.x();
            int z = chunkId.z();

            DatabaseCommunicationAPI.ChunkCoord[] adjacentCoords = {
                    new DatabaseCommunicationAPI.ChunkCoord(x + 1, z), // Est
                    new DatabaseCommunicationAPI.ChunkCoord(x - 1, z), // Ouest
                    new DatabaseCommunicationAPI.ChunkCoord(x, z + 1), // Sud
                    new DatabaseCommunicationAPI.ChunkCoord(x, z - 1)  // Nord
            };

            for (DatabaseCommunicationAPI.ChunkCoord coord : adjacentCoords) {
                if (db.isChunkFromTown(town, coord)) {
                    return true;
                }
            }

            return false;
        }
        return false;
    }

}
