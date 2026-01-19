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
        try {
            List<String> permissionsList;
            permissionsList = db.getAllPermissionOfAPlayer(p);
            if (!permissionsList.isEmpty()) {
                for (String permission : permissionsList) {
                    PermissionAttachment attachment = p.addAttachment(_plugin);
                    attachment.setPermission(permission, true);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
