package fr.Valkyar.jobsPlugin.Listeners.JobListeners;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class CraftItemListener implements Listener {

    private JobsPlugin _plugin;

    private Database db;

    public CraftItemListener(JobsPlugin plugin) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;
    }


    @EventHandler
    public void onPlayerCraft(CraftItemEvent event) {
        // pour forgemage (créer armure/arme, réparer table de craft, réparer enclume)
        // pour cuisinier (fabriquer nourriture)

    }
}
