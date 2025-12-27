package fr.Valkyar.jobsPlugin;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.listeners.JoinListener;
import fr.Valkyar.jobsPlugin.Commands.JobCommand;
import fr.Valkyar.jobsPlugin.Listeners.MainListener;
import fr.Valkyar.jobsPlugin.TabCompletion.JobCommandCompletion;
import org.bukkit.plugin.java.JavaPlugin;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.ANSI_RESET;

public final class JobsPlugin extends JavaPlugin {

    public DatabaseCommunicationAPI databaseApi = DatabaseCommunicationAPI.getApi();

    @Override
    public void onEnable() {
        // Register Commands
        getCommand("jobs").setExecutor(new JobCommand(this));

        // Register tab completion
        getCommand("jobs").setTabCompleter(new JobCommandCompletion(this));

        // Register listener
        getServer().getPluginManager().registerEvents(new MainListener(this), this);

        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
