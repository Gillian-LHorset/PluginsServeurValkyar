package fr.Valkyar.displayingPlugin.Listener;

import fr.Valkyar.displayingPlugin.DisplayingPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static fr.Valkyar.displayingPlugin.DisplayingPlugin.activeBossBars;

public class JoinListener implements Listener {

    private DisplayingPlugin _plugin;

    public JoinListener(DisplayingPlugin plugin) {
        _plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // TODO : extraire la méthode de DisplayTask pour la reporter ici
        Player p = e.getPlayer();
        String bossBarKey = p.getUniqueId().toString().replace("-", "");

        NamespacedKey key = new NamespacedKey(_plugin, bossBarKey);
        KeyedBossBar hudDisplay = Bukkit.getBossBar(key);

        if (hudDisplay == null) {
            hudDisplay = Bukkit.createBossBar(key, "", BarColor.PINK, BarStyle.SOLID);
        } else {
            hudDisplay.setColor(BarColor.PINK);
            hudDisplay.setStyle(BarStyle.SOLID);
        }

        hudDisplay.setProgress(0);
        hudDisplay.addPlayer(p);

        activeBossBars.put(p.getUniqueId(), hudDisplay);
    }
}
