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

import static fr.Valkyar.valkyarTownPlugin.Utility.PlayerPermissionLogic.IsPlayerPermissionInClaim;

public class PlayerInteractListener implements Listener {

    private Database db;
    private ValkyarTownPlugin _plugin;
    public PlayerInteractListener(ValkyarTownPlugin  plugin) {
        _plugin = plugin;
        db = plugin.databaseApi.databaseLink;
    }

    private int[] _breakChunk;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getClickedBlock() != null) {
            _breakChunk = new int[]{
                    (int) Math.floor(e.getClickedBlock().getLocation().getX()),
                    (int) Math.floor(e.getClickedBlock().getLocation().getZ())
            };
        } else {
            return;
        }

        if (!IsPlayerPermissionInClaim(p, _breakChunk, db)) {
            try {
                if (e.getClickedBlock().getState() instanceof InventoryHolder
                        || e.getClickedBlock().getBlockData() instanceof Openable) {
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'interagir avec block.");
                }
            } catch (Exception ex) {

            }
        }

    }
}
