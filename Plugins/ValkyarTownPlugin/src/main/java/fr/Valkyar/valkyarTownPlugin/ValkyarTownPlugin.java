package fr.Valkyar.valkyarTownPlugin;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.valkyarTownPlugin.CommandCompletion.TownCommandCompletion;
import fr.Valkyar.valkyarTownPlugin.Commands.TownCommand;
import fr.Valkyar.valkyarTownPlugin.Listeners.*;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ValkyarTownPlugin extends JavaPlugin {

    public DatabaseCommunicationAPI databaseApi = DatabaseCommunicationAPI.getApi();

    @Override
    public void onEnable() {
        // Plugin startup logic
        // Register Commands
        getCommand("town").setExecutor(new TownCommand(this));

        // Register tab completion
        getCommand("town").setTabCompleter(new TownCommandCompletion(this));

        getServer().getPluginManager().registerEvents(new BreakBlockListenener(this), this);
        getServer().getPluginManager().registerEvents(new PlaceBlockListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new BucketListener(this), this);

        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
