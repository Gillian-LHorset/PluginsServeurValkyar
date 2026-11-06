package fr.Valkyar.valkyarVaultPlugin;

import fr.Valkyar.databaseCommunicationAPI.listeners.JoinListener;
import fr.Valkyar.valkyarVaultPlugin.BlackMarket.BlackMerchant;
import fr.Valkyar.valkyarVaultPlugin.BlackMarket.BlackMerchantInteractListener;
import fr.Valkyar.valkyarVaultPlugin.BlackMarket.ManageBlackMerchantCommand;
import fr.Valkyar.valkyarVaultPlugin.Commands.BalanceCommand;
import fr.Valkyar.valkyarVaultPlugin.Commands.PayCommand;
import fr.Valkyar.valkyarVaultPlugin.TabCompletion.PayTabCompletion;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;

public final class ValkyarVaultPlugin extends JavaPlugin {

    // color for the output messages
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String RED_OUTPUT = "\u001B[31m";
    public static final String GREEN_OUTPUT = "\u001B[32m";

    public static BlackMerchant blackMerchant;

    public DatabaseCommunicationAPI api = DatabaseCommunicationAPI.getApi();

    public static FileConfiguration config;

    @Override
    public void onEnable() {
        // Register Commands
        getCommand("pay").setExecutor(new PayCommand(this));
        getCommand("balance").setExecutor(new BalanceCommand(this));
        getCommand("blackmerchant").setExecutor(new ManageBlackMerchantCommand()); // only for test TODO : need to be protected or deleted

        // Register tab completion
        getCommand("pay").setTabCompleter(new PayTabCompletion());


        getServer().getPluginManager().registerEvents(new BlackMerchantInteractListener(), this);

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        config = this.getConfig();

        blackMerchant = new BlackMerchant(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void OutputColoredText(String color, String message) {
        System.out.println(color + message + ANSI_RESET + "");
    }
}
