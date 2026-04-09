package fr.Valkyar.valkyarTownPlugin.Utility;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayerPermissionLogic {
    public static boolean IsPlayerPermissionInClaim(Player p, int[] chunk, Database db) {
        if (p.getGameMode() == GameMode.CREATIVE) {
            return true;
        }
        if (p.getWorld().getName().equalsIgnoreCase("world")) {
            String _playerTown = db.getPlayerTown(p);
            if (_playerTown != null) {
                int[] breakChunk = ChunkSystem.getChunkId(chunk[0], chunk[1]);
                return db.isChunkFromTown(_playerTown, breakChunk);
            } else {
                return false;
            }
        } else {
            // CrashTest - vérification dangereurse, test à faire
            return true;
        }
    }
}
