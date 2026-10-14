package fr.Valkyar.displayingPlugin;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.displayingPlugin.Listener.JoinListener;
import fr.Valkyar.displayingPlugin.Listener.LeaveListener;
import fr.Valkyar.displayingPlugin.Tasks.DisplayTask;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class DisplayingPlugin extends JavaPlugin {

    // color for the output messages
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String RED_OUTPUT = "\u001B[31m";
    public static final String GREEN_OUTPUT = "\u001B[32m";

    public DatabaseCommunicationAPI databaseApi = DatabaseCommunicationAPI.getApi();

    public String debugIcon = "\uE000";
    public String moneyIcon = "\uE001";
    public String mineurIcon = "\uE002";
    public String bucheronIcon = "\uE003";
    public String farmerIcon = "\uE004";
    public String townIcon = "\uE005";

    public DisplayTask balanceTask;

    public static final Map<UUID, KeyedBossBar> activeBossBars = new HashMap<>();

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(new LeaveListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);

        balanceTask = new DisplayTask(this);
        balanceTask.runTaskTimer(this, 0, 40L);


        getConfig().options().copyDefaults();
        saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (BossBar bossBar : activeBossBars.values()) {
            bossBar.removeAll();
        }
    }

    public static void OutputColoredText (String color, String message) {
        System.out.println(color + message + ANSI_RESET + "");
    }
}
