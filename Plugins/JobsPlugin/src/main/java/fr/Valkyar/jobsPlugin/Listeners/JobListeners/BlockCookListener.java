package fr.Valkyar.jobsPlugin.Listeners.JobListeners;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;

public class BlockCookListener implements Listener {


    private JobsPlugin _plugin;

    private Database db;

    public BlockCookListener(JobsPlugin plugin) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;
    }


    @EventHandler
    public void onFoodCook(BlockCookEvent event) {
        // pour cuisinier

    }
}
