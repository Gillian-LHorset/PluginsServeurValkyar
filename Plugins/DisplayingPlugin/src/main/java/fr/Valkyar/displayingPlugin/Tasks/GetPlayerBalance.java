package fr.Valkyar.displayingPlugin.Tasks;

import fr.Valkyar.displayingPlugin.DisplayingPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GetPlayerBalance extends BukkitRunnable {

    private DisplayingPlugin _plugin;

    private final Map<UUID, BossBar> activeBossBars = new HashMap<>();

    public GetPlayerBalance(DisplayingPlugin plugin) {
        this._plugin = plugin;
    }
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String RED_OUTPUT = "\u001B[31m";

    public void removeBar(Player p) {
        if (activeBossBars.containsKey(p.getUniqueId())) {
            activeBossBars.get(p.getUniqueId()).removeAll(); // Enlève la barre de l'écran
            activeBossBars.remove(p.getUniqueId()); // Enlève de la mémoire
        }
    }

    @Override
    public void run() {
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p != null && p.isOnline()) {
                    try {
                        String balance = _plugin.databaseApi.databaseLink.getPlayerBalanceByUUID(p).toString();


                        String title = _plugin.moneyIcon + ChatColor.YELLOW + " " + balance;

                        UUID uuid = p.getUniqueId();

                        if (activeBossBars.containsKey(uuid)) {
                            BossBar currentBar = activeBossBars.get(uuid);
                            currentBar.setTitle(title);
                        } else {
                            BossBar newBar = Bukkit.createBossBar(title, BarColor.PINK, BarStyle.SOLID);
                            newBar.setProgress(0);
                            newBar.addPlayer(p);

                            activeBossBars.put(uuid, newBar);
                        }
                    } catch (SQLException err) {
                        System.out.println(RED_OUTPUT + "[ScreenDisplayingPlugin] - Error get balance from database." + ANSI_RESET);
                        err.getMessage();
                    }
                }
            }
        }
    }
}
