package fr.Valkyar.valkyarVaultPlugin;

import fr.Valkyar.valkyarVaultPlugin.db.Database;
import fr.Valkyar.valkyarVaultPlugin.listeners.joinListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ValkyarVaultPlugin extends JavaPlugin {

    // color for the output messages
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String RED_OUTPUT = "\u001B[31m";
    public static final String GREEN_OUTPUT = "\u001B[32m";

    public Database databaseLink;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new joinListener(this), this);

        // Plugin startup logic

        try {
            databaseLink = new Database(this);
            databaseLink.getConnection();
        } catch (SQLException error) {
            System.out.println("[ValkyarVaultPlugin] : Erreur de connexion");
            System.err.println("[ValkyarVaultPlugin] : " + error.getMessage());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void OutputColoredText (String color, String message) {
        System.out.println(color + message + ANSI_RESET + "");
    }
}
