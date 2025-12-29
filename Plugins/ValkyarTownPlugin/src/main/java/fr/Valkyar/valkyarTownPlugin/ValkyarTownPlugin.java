package fr.Valkyar.valkyarTownPlugin;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.valkyarTownPlugin.CommandCompletion.CreateTownCommandCompletion;
import fr.Valkyar.valkyarTownPlugin.Commands.CreateTownCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class ValkyarTownPlugin extends JavaPlugin {

    public DatabaseCommunicationAPI databaseApi = DatabaseCommunicationAPI.getApi();

    @Override
    public void onEnable() {
        // Plugin startup logic
        // Register Commands
        getCommand("town").setExecutor(new CreateTownCommand(this));

        // Register tab completion
        getCommand("town").setTabCompleter(new CreateTownCommandCompletion());

        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
