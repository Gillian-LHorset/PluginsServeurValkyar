package fr.Valkyar.jobsPlugin.Listeners;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.SQLException;

public class MainListener implements Listener {

    private JobsPlugin _plugin;

    public MainListener(JobsPlugin _plugin) {
        this._plugin = _plugin;
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (_plugin.databaseApi.databaseLink.getPlayerJob(player) != null) {
            String breakedBlock = event.getBlock().getBlockData().toString().substring(25, event.getBlock().getBlockData().toString().length() - 1);
            //TODO : verifier le block cassé seulement si le block est différent du dernier cassé
            for ( String breakableBlock : _plugin.databaseApi.databaseLink.getAllBreakableBlocksByJobs(player)) {
                if (breakableBlock.equalsIgnoreCase(breakedBlock)) {
                    try {
                        _plugin.databaseApi.databaseLink.setPlayerBalanceJob(player, breakableBlock);
                    } catch (SQLException e) {
                        _plugin.databaseApi.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT, "[DatabaseCommunicationAPI] - Can't increase balance of player after breaking block.");
                    }
                    break;
                }
            }
        }
    }
}
