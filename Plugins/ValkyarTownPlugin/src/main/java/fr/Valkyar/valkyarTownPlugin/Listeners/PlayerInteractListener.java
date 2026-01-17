package fr.Valkyar.valkyarTownPlugin.Listeners;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

import static fr.Valkyar.valkyarTownPlugin.Utility.PlayerPermissionLogic.IsPlayerPermissionInClaim;

public class PlayerInteractListener implements Listener {

    private Database db;
    private ValkyarTownPlugin _plugin;
    public PlayerInteractListener(ValkyarTownPlugin  plugin) {
        _plugin = plugin;
        db = plugin.databaseApi.databaseLink;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        int[] breakChunk = new int[]{ (int) Math.floor(e.getClickedBlock().getLocation().getX()), (int) Math.floor(e.getClickedBlock().getLocation().getZ())};

        if (!IsPlayerPermissionInClaim(p, breakChunk, db)) {
            if (e.getClickedBlock().getState() instanceof InventoryHolder
                    || e.getClickedBlock().getBlockData() instanceof Openable) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'interagir avec block.");
            }
        }

    }
}
