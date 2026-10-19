package fr.Valkyar.valkyarTownPlugin.Utility;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.Utilities.ChunkSystem;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class PlayerPermissionLogic {
    public static boolean hasPlayerPermissionInClaim(Player p, Location location) {
        if (p.getGameMode() == GameMode.CREATIVE) {
            return true;
        }

        if (!p.getWorld().getName().equalsIgnoreCase("world")) {
            // CrashTest - vérification dangereurse, test à faire
            return true;
        }

        PlayerProfile pp = playerProfiles.get(p.getUniqueId());
        if (!pp.hasTown()) {
            return false;
        }

        Town _playerTown = playerProfiles.get(p.getUniqueId()).getTown();

        DatabaseCommunicationAPI.ChunkCoord breakChunk = ChunkSystem.getChunkId(location);
        return _playerTown.hasChunk(breakChunk);
    }
}
