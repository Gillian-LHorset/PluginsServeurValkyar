package fr.Valkyar.displayingPlugin.Listener;

import fr.Valkyar.displayingPlugin.DisplayingPlugin;
import fr.Valkyar.displayingPlugin.Tasks.GetPlayerBalance;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class JoinListener implements Listener {

    private DisplayingPlugin _plugin;

    public JoinListener(DisplayingPlugin plugin) {
        _plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        try {
            String balance = _plugin.databaseApi.databaseLink.getPlayerBalanceByUUID(p).toString();
            String title = _plugin.moneyIcon + ChatColor.YELLOW + " " + balance;

            BossBar hudDisplay = Bukkit.createBossBar(title, BarColor.PINK, BarStyle.SOLID);
            hudDisplay.setProgress(0);
            hudDisplay.addPlayer(p);

            _plugin.balanceTask.activeBossBars.put(p.getUniqueId(), hudDisplay);
        } catch (SQLException err) {
            err.printStackTrace();        }

    }
}
