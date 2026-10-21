package fr.Valkyar.databaseCommunicationAPI;

import fr.Valkyar.databaseCommunicationAPI.Models.BlockData;
import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.listeners.ItemProtectionListener;
import fr.Valkyar.databaseCommunicationAPI.listeners.JoinListener;
import fr.Valkyar.databaseCommunicationAPI.listeners.LeaveListener;
import fr.Valkyar.databaseCommunicationAPI.Models.Job;
import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.databaseCommunicationAPI.listeners.PlaceBlockListener;
import fr.Valkyar.databaseCommunicationAPI.tasks.StockPlacedBlockTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static fr.Valkyar.databaseCommunicationAPI.Utilities.PlayerProfileUtility.RegisterPlayerProfile;
import static fr.Valkyar.databaseCommunicationAPI.Utilities.TownUtility.getAllTowns;

public final class DatabaseCommunicationAPI extends JavaPlugin {

    // color for the output messages
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String RED_OUTPUT = "\u001B[31m";
    public static final String GREEN_OUTPUT = "\u001B[32m";

    private DatabaseCommunicationAPI _plugin;

    public Database databaseLink;
    public Job job;

    public static DatabaseCommunicationAPI getApi() {

        return JavaPlugin.getPlugin(DatabaseCommunicationAPI.class);
    }

    public record BlockPosition(String worldName, int x, int y, int z) {}
    public record ChunkCoord(int x, int z) {}

    public static double createTownPrice = 1.00;
    public static double claimPrice = 1.00;

    public static HashMap<UUID, PlayerProfile> playerProfiles = new HashMap<>();
    public static HashMap<BlockPosition, BlockData> PlacedBlocks = new HashMap<>();
    public static HashMap<Integer, Town> TownMap = new HashMap<>();

    public StockPlacedBlockTask stockPlacedBlockTask;

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            databaseLink = new Database(this);

            databaseLink.getConnection();

        } catch (SQLException error) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion");
            System.err.println("[DatabaseCommunicationAPI] : " + error.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }

        // Register listeners
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new LeaveListener(this), this);
        getServer().getPluginManager().registerEvents(new PlaceBlockListener(), this);
        getServer().getPluginManager().registerEvents(new ItemProtectionListener(), this);

        stockPlacedBlockTask = new StockPlacedBlockTask(this);
        // attend 1 minute (1200 ticks) avant la première exécution
        // puis se répète toutes les 10 minutes (12000 ticks)
        stockPlacedBlockTask.runTaskTimer(this, 1200L, 12000L);

        for (Player p : Bukkit.getOnlinePlayers()) {

            RegisterPlayerProfile(p, this);
        }

        getAllTowns(databaseLink).forEach(t -> {
            TownMap.put(t.getId(), t);
            System.out.println(t.getName());
        });

        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Map<BlockPosition, BlockData> getPlacedBlocks() {
        return PlacedBlocks;
    }

    public static void OutputColoredText (String color, String message) {
        System.out.println(color + message + ANSI_RESET + "");
    }
}
