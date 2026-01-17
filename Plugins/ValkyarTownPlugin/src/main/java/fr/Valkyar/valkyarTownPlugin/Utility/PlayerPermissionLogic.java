package fr.Valkyar.valkyarTownPlugin.Utility;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayerPermissionLogic {
    public static boolean IsPlayerPermissionInClaim(Player p, int[] chunk, Database db) {
        if (p.getWorld().getName().equalsIgnoreCase("world") && p.getGameMode() != GameMode.CREATIVE) {
            String _playerTown = db.getPlayerTown(p);
            if (_playerTown != null) {
                int[] breakChunk = ChunkSystem.getChunkId(chunk[0], chunk[1]);
                return db.isChunkFromTown(_playerTown, breakChunk);
            } else {
                return false;
            }
        }
        return false;
    }
}
