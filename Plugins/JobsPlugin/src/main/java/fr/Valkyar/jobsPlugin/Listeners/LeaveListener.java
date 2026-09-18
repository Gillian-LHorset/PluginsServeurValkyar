package fr.Valkyar.jobsPlugin.Listeners;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import fr.Valkyar.jobsPlugin.Utility.PlayerProfileUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    private JobsPlugin _plugin;
    private Database db;

    public LeaveListener(JobsPlugin plugin) {
        _plugin = plugin;
        db = plugin.databaseApi.databaseLink;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p =  e.getPlayer();

        PlayerProfileUtility.UpdatePlayerProfileInDB(_plugin, p);

    }
}
