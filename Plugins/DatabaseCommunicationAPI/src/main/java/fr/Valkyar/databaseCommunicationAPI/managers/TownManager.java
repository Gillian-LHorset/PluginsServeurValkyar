package fr.Valkyar.databaseCommunicationAPI.managers;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.Utilities.ChunkSystem;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.*;

public class TownManager {

    public static void CreateTown(Database db, Player p, String name) {
        DatabaseCommunicationAPI.ChunkCoord originChunk = ChunkSystem.getChunkId(p.getLocation());
        Town tempTown = db.CreateTown(p, name);
        db.registerChunk(originChunk, tempTown);
        db.setSpawnTown(tempTown, p.getLocation());
        db.setPlayerTown(p, tempTown);

        // the player pay the creation of the town
        PlayerProfile pp = playerProfiles.get(p.getUniqueId());
        pp.decreaseMoney(createTownPrice);
        playerProfiles.get(p.getUniqueId()).setTown(tempTown);

        // give him all permissions
        pp.setPermissionsTrue();

        TownMap.put(tempTown.getId(), db.getPlayerTown(p));

        Bukkit.broadcastMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Félicitation à " + p.getName() + " qui viens de créer la ville de " + name + " !");
        p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
    }

    public static void DeleteTown(Database db, Town town) {
        db.deleteTown(town);
        TownMap.remove(town.getId());
    }

    public static void LeaveTown(Database db, Player p, Town town) {
        PlayerProfile pp = playerProfiles.get(p.getUniqueId());
        town.RemovePlayerTown(p);
        pp.setTown(null);
        pp.ResetPermissions();
    }

    public static void PlayerDepositMoneyInTown(DatabaseCommunicationAPI plugin, Town town, Double amount, Player p) {
        PlayerProfileManager.DecreaseMoney(plugin, p, amount);
        town.DepositMoney(amount);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.databaseLink.setTownBalance(town);
        });
    }

    public static void PlayerWithdrawMoneyInTown(DatabaseCommunicationAPI plugin, Town town, Double amount, Player p) {
        PlayerProfileManager.IncreaseMoney(plugin, p, amount);
        town.WithdrawMoney(amount);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.databaseLink.setTownBalance(town);
        });
    }

    public static void TownClaim(DatabaseCommunicationAPI plugin, Town town, Double claimPrice, ChunkCoord chunk) {
        town.registerChunk(chunk);
        town.WithdrawMoney(claimPrice);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.databaseLink.setTownBalance(town);
            plugin.databaseLink.registerChunk(chunk, town);
        });
    }

    public static void TownUnclaim(DatabaseCommunicationAPI plugin, Town town, ChunkCoord chunk) {
        town.deleteChunk(chunk);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.databaseLink.deleteAChunksById(chunk, town);
        });
    }
}
