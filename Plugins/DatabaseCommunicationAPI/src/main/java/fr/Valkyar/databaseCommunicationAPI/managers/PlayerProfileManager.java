package fr.Valkyar.databaseCommunicationAPI.managers;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class PlayerProfileManager {

    public static void IncreaseMoney(DatabaseCommunicationAPI plugin, Player p, Double amount) {
        playerProfiles.get(p.getUniqueId()).increaseMoney(amount);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.databaseLink.setPlayerBalance(p, playerProfiles.get(p.getUniqueId()).getCurrentMoney());
        });
    }

    public static void DecreaseMoney(DatabaseCommunicationAPI plugin,Player p, Double amount) {
        playerProfiles.get(p.getUniqueId()).decreaseMoney(amount);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.databaseLink.setPlayerBalance(p, playerProfiles.get(p.getUniqueId()).getCurrentMoney());
        });
    }
}
