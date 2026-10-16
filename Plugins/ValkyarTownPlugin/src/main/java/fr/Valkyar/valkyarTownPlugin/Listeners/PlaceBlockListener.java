package fr.Valkyar.valkyarTownPlugin.Listeners;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static fr.Valkyar.valkyarTownPlugin.Utility.PlayerPermissionLogic.IsPlayerPermissionInClaim;

public class PlaceBlockListener implements Listener {

    private Database db;
    private ValkyarTownPlugin _plugin;
    public PlaceBlockListener(ValkyarTownPlugin  plugin) {
        _plugin = plugin;
        db = plugin.databaseApi.databaseLink;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        if (!IsPlayerPermissionInClaim(p, e.getBlock().getLocation())) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "Vous n'avez pas la permission de poser ce block.");
        }
    }
}
