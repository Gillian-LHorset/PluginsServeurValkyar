package fr.Valkyar.valkyarTownPlugin.Listeners;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static fr.Valkyar.valkyarTownPlugin.Utility.PlayerPermissionLogic.IsPlayerPermissionInClaim;

public class BreakBlockListenener implements Listener {

    private Database db;
    private ValkyarTownPlugin _plugin;
    public BreakBlockListenener(ValkyarTownPlugin  plugin) {
        _plugin = plugin;
        db = plugin.databaseApi.databaseLink;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        int[] breakChunk = new int[]{(int) Math.floor(e.getBlock().getLocation().getX()), (int) Math.floor(e.getBlock().getLocation().getZ())};

        if (!IsPlayerPermissionInClaim(p, breakChunk, db)) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "Vous n'avez pas la permission de casser ce block.");
        }
    }
}
