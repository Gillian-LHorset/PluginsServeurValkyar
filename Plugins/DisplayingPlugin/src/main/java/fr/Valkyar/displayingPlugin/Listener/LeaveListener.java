package fr.Valkyar.displayingPlugin.Listener;

import fr.Valkyar.displayingPlugin.DisplayingPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static fr.Valkyar.displayingPlugin.DisplayingPlugin.activeBossBars;

public class LeaveListener implements Listener {

    private DisplayingPlugin _plugin;

    public LeaveListener(DisplayingPlugin plugin) {
        this._plugin = plugin;
    }

    public void onPlayerLeave(PlayerQuitEvent e) {
        Bukkit.removeBossBar(activeBossBars.get(e.getPlayer().getUniqueId()).getKey());
        activeBossBars.remove(e.getPlayer().getUniqueId());
    }
}
