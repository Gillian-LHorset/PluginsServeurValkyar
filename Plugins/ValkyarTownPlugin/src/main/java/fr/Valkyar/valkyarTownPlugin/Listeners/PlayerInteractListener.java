package fr.Valkyar.valkyarTownPlugin.Listeners;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

import static fr.Valkyar.valkyarTownPlugin.Utility.PlayerPermissionLogic.hasPlayerPermissionInClaim;

public class PlayerInteractListener implements Listener {

    private Database db;
    private ValkyarTownPlugin _plugin;

    public PlayerInteractListener(ValkyarTownPlugin plugin) {
        _plugin = plugin;
        db = plugin.databaseApi.databaseLink;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getClickedBlock() == null) {
            return;
        }

        if (!hasPlayerPermissionInClaim(p, e.getClickedBlock().getLocation())) {
            if (e.getClickedBlock().getState() instanceof InventoryHolder
                    || e.getClickedBlock().getBlockData() instanceof Openable) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'interagir avec block.");
            }
        }
    }
}
