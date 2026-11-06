package fr.Valkyar.valkyarVaultPlugin;

import fr.Valkyar.valkyarVaultPlugin.BlackMarket.BlackMerchant;
import fr.Valkyar.valkyarVaultPlugin.BlackMarket.ManageBlackMerchantCommand;
import fr.Valkyar.valkyarVaultPlugin.Commands.BalanceCommand;
import fr.Valkyar.valkyarVaultPlugin.Commands.PayCommand;
import fr.Valkyar.valkyarVaultPlugin.TabCompletion.PayTabCompletion;
import org.bukkit.plugin.java.JavaPlugin;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;

public final class ValkyarVaultPlugin extends JavaPlugin {

    // color for the output messages
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String RED_OUTPUT = "\u001B[31m";
    public static final String GREEN_OUTPUT = "\u001B[32m";

    public DatabaseCommunicationAPI api = DatabaseCommunicationAPI.getApi();

    @Override
    public void onEnable() {
        // Register Commands
        getCommand("pay").setExecutor(new PayCommand(this));
        getCommand("balance").setExecutor(new BalanceCommand(this));
        getCommand("blackmerchant").setExecutor(new ManageBlackMerchantCommand()); // only for test

        // Register tab completion
        getCommand("pay").setTabCompleter(new PayTabCompletion());

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
