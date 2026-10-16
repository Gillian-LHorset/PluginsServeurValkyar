package fr.Valkyar.valkyarTownPlugin.Listeners;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private Database db;
    private ValkyarTownPlugin _plugin;
    public JoinListener(ValkyarTownPlugin  plugin) {
        _plugin = plugin;
        db = plugin.databaseApi.databaseLink;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        // define the player to talk by default to the global chat
        _plugin.playerChat.put(p.getUniqueId(), "global");
    }
}
