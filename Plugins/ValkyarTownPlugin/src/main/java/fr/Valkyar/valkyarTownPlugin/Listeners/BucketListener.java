package fr.Valkyar.valkyarTownPlugin.Listeners;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import static fr.Valkyar.valkyarTownPlugin.Utility.PlayerPermissionLogic.hasPlayerPermissionInClaim;

public class BucketListener implements Listener {

    private Database db;
    private ValkyarTownPlugin _plugin;
    public BucketListener(ValkyarTownPlugin  plugin) {
        _plugin = plugin;
        db = plugin.databaseApi.databaseLink;
    }

    private Location _breakChunk;

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent e) {
        Player p = e.getPlayer();

        if (!hasPlayerPermissionInClaim(p, e.getBlock().getLocation())) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "Vous n'avez pas le droit de vider un seau ici.");
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketFillEvent e) {
        Player p = e.getPlayer();

        if (!hasPlayerPermissionInClaim(p, e.getBlock().getLocation())) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "Vous n'avez pas le droit de remplir un seau ici.");
        }
    }
}
