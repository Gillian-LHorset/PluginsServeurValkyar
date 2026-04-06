package fr.Valkyar.valkyarTownPlugin.Listeners;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static fr.Valkyar.valkyarTownPlugin.Utility.AssignPerms.assignPerms;

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
        assignPerms(p, db, _plugin);
    }
}
