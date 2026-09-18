package fr.Valkyar.jobsPlugin.Listeners;

import fr.Valkyar.databaseCommunicationAPI.Job;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import fr.Valkyar.jobsPlugin.PlayerProfile;
import fr.Valkyar.jobsPlugin.Utility.PlayerProfileUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

import static fr.Valkyar.jobsPlugin.Utility.PlayerProfileUtility.CreatePlayerProfile;

public class JoinListener implements Listener {

    private JobsPlugin _plugin;

    private Database db;

    public JoinListener(JobsPlugin plugin) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;
    }

    public PlayerProfile playerProfile;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        playerProfile = CreatePlayerProfile(p, db);

        _plugin.playerProfiles.put(p.getUniqueId(), playerProfile);
    }


}
