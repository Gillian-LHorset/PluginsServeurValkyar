package fr.Valkyar.databaseCommunicationAPI.listeners;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Utilities.PlayerProfileUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    private final DatabaseCommunicationAPI _plugin;

    public LeaveListener(DatabaseCommunicationAPI plugin) {
        _plugin = plugin;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p =  e.getPlayer();

        PlayerProfileUtility.UpdatePlayerProfileInDB(p, _plugin.databaseLink, _plugin.playerProfiles);

    }
}