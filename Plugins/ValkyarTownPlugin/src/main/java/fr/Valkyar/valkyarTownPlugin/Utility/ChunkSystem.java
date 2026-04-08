package fr.Valkyar.valkyarTownPlugin.Utility;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import org.bukkit.entity.Player;

public class ChunkSystem {

    public static int[] getChunkId(Integer x, Integer z) {
        x = Math.floorDiv(x, 16) + 1;
        z = Math.floorDiv(z, 16) + 1;

        return new int[]{x ,z};
    }
    public static int[] getChunkId2(Integer[] positions) {
        positions[0] = Math.floorDiv(positions[0], 16) + 1;
        positions[1] = Math.floorDiv(positions[1], 16) + 1;

        return new int[]{positions[0] ,positions[1]};
    }

    public static Boolean IsChunkNextToAnother(Player p, Database db, int[] chunkId) {
        int[] chunkIdCopy = chunkId.clone();
        chunkIdCopy[0]--;
        if (db.isChunkFromTown(db.getPlayerTown(p), chunkIdCopy)) {
            return true;
        }

        chunkIdCopy[0] += 2;
        if (db.isChunkFromTown(db.getPlayerTown(p), chunkIdCopy)) {
            return true;
        }

        chunkIdCopy[0]--;
        chunkIdCopy[1]--;
        if (db.isChunkFromTown(db.getPlayerTown(p), chunkIdCopy)) {
            return true;
        }

        chunkIdCopy[1] += 2;
        return db.isChunkFromTown(db.getPlayerTown(p), chunkIdCopy);
    }

}
