package fr.Valkyar.databaseCommunicationAPI;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.databaseCommunicationAPI.listeners.JoinListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class DatabaseCommunicationAPI extends JavaPlugin {

    // color for the output messages
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String RED_OUTPUT = "\u001B[31m";
    public static final String GREEN_OUTPUT = "\u001B[32m";

    private DatabaseCommunicationAPI _plugin;

    public Database databaseLink;

    public static DatabaseCommunicationAPI getApi() {

        return JavaPlugin.getPlugin(DatabaseCommunicationAPI.class);
    }

    @Override
    public void onEnable() {

        // Plugin startup logic
        try {
            databaseLink = new Database(this);
            databaseLink.getConnection();
        } catch (SQLException error) {
            System.out.println("[DatabaseCommunicationAPI] : Erreur de connexion");
            System.err.println("[DatabaseCommunicationAPI] : " + error.getMessage());
        }

        getServer().getPluginManager().registerEvents(new JoinListener(this), this);

        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void OutputColoredText (String color, String message) {
        System.out.println(color + message + ANSI_RESET + "");
    }
}
