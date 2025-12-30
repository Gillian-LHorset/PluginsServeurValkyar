package fr.Valkyar.displayingPlugin.Tasks;

import fr.Valkyar.displayingPlugin.DisplayingPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GetPlayerBalance extends BukkitRunnable {

    private DisplayingPlugin _plugin;

    public final Map<UUID, BossBar> activeBossBars = new HashMap<>();

    public GetPlayerBalance(DisplayingPlugin plugin) {
        this._plugin = plugin;
    }
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String RED_OUTPUT = "\u001B[31m";

    public void removeBar(Player p) {
        if (activeBossBars.containsKey(p.getUniqueId())) {
            activeBossBars.get(p.getUniqueId()).removeAll();
            activeBossBars.remove(p.getUniqueId());
        }
    }

    private DecimalFormat _df = new DecimalFormat("#,###.##");
    @Override
    public void run() {
        //TODO : séparer le code en méthodes
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p != null && p.isOnline()) {
                    try {
                        Double balance = _plugin.databaseApi.databaseLink.getPlayerBalanceByUUID(p);

                        String formattedBalance = _df.format(balance);

                        String title = _plugin.moneyIcon + ChatColor.YELLOW + " " + formattedBalance;

                        Integer playerJob = _plugin.databaseApi.databaseLink.getPlayerJobFk(p);

                        String playerTown = _plugin.databaseApi.databaseLink.GetPlayerTown(p);

                        if (playerJob != null) {
                            switch (playerJob) {
                                case 1:
                                    title += "   " + _plugin.mineurIcon + ChatColor.RED + " " + "Mineur";
                                    break;
                                case 2:
                                    title += "   " + _plugin.bucheronIcon + ChatColor.RED + " " + "Bucheron";
                                    break;
                                case 3:
                                    title += "   " + _plugin.farmerIcon + ChatColor.RED + " " + "Paysan";
                                    break;
                            }
                        }

                        if (playerTown != null) {
                            title += "   " + _plugin.townIcon + ChatColor.GREEN + " " + playerTown;
                        }

                        UUID uuid = p.getUniqueId();

                        if (activeBossBars.containsKey(uuid)) {
                            BossBar currentBar = activeBossBars.get(uuid);
                            currentBar.setTitle(title);
                        } else {
                            BossBar hudDisplay = Bukkit.createBossBar(title, BarColor.PINK, BarStyle.SOLID);
                            hudDisplay.setProgress(0);
                            hudDisplay.addPlayer(p);

                            activeBossBars.put(uuid, hudDisplay);
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
