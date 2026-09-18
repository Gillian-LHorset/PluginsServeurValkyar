package fr.Valkyar.jobsPlugin.Listeners.JobListeners;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

public class SmeltListener implements Listener {

    private JobsPlugin _plugin;

    private Database db;

    public SmeltListener(JobsPlugin plugin) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;
    }


    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        // pour fondeur
    }
}
