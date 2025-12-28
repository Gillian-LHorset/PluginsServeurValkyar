package fr.Valkyar.displayingPlugin.Listener;

import fr.Valkyar.displayingPlugin.DisplayingPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    private DisplayingPlugin _plugin;

    public LeaveListener(DisplayingPlugin plugin) {
        this._plugin = plugin;
    }

    public void onPlayerLeave(PlayerQuitEvent e) {
        if (_plugin.balanceTask != null) {
             _plugin.balanceTask.removeBar(e.getPlayer());
        }
    }
}
