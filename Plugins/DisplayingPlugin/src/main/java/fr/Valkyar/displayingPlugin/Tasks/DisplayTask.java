package fr.Valkyar.displayingPlugin.Tasks;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.displayingPlugin.DisplayingPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DisplayTask extends BukkitRunnable {

    private DisplayingPlugin _plugin;
    private Database db;

    public final Map<UUID, BossBar> activeBossBars = new HashMap<>();

    public DisplayTask(DisplayingPlugin plugin) {
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
            db = _plugin.databaseApi.databaseLink;
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p != null && p.isOnline()) {
                    try {
                        String playerTown = db.getPlayerTown(p);

                        Double balance = db.getPlayerBalanceByUUID(p);
                        Double townBalance = db.getBalanceTownByName(playerTown);

                        String formattedBalance = _df.format(balance);

                        String title = _plugin.moneyIcon + ChatColor.YELLOW + " " + formattedBalance;

                        Integer playerJob = db.getPlayerMainJobFk(p);
                        // TODO : AJOUTER job secondaire


                        // TODO : mettre ce système au propre
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
                            title += "   " + _plugin.townIcon + ChatColor.YELLOW + " " + townBalance;
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
