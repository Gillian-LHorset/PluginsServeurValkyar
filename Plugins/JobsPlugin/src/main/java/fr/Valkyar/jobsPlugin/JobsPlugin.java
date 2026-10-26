package fr.Valkyar.jobsPlugin;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Models.Job;
import fr.Valkyar.jobsPlugin.Commands.JobCommand;
import fr.Valkyar.jobsPlugin.Commands.PrestigeCommand;
import fr.Valkyar.jobsPlugin.Listeners.JobListeners.*;
import fr.Valkyar.jobsPlugin.Listeners.PrestigeShopMenuHandler;
import fr.Valkyar.jobsPlugin.TabCompletion.JobCommandCompletion;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

import static fr.Valkyar.databaseCommunicationAPI.Utilities.JobsUtilities.setupJobsList;

public final class JobsPlugin extends JavaPlugin {

    public DatabaseCommunicationAPI databaseApi = DatabaseCommunicationAPI.getApi();
    // attention : case-sensitive
    public Map<String, Job> jobsList;

    public FileConfiguration config;

    @Override
    public void onEnable() {

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        config = this.getConfig();

        jobsList = setupJobsList(databaseApi.databaseLink);

        // Register Commands
        getCommand("jobs").setExecutor(new JobCommand(this));
        getCommand("prestige").setExecutor(new PrestigeCommand(this, config));

        // Register tab completion
        getCommand("jobs").setTabCompleter(new JobCommandCompletion(this));

        getServer().getPluginManager().registerEvents(new AnvilListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockCookListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        getServer().getPluginManager().registerEvents(new BrewListener(this), this);
        getServer().getPluginManager().registerEvents(new CraftItemListener(this), this);
        getServer().getPluginManager().registerEvents(new EnchantItemListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityBreedListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerFishListener(this), this);
        getServer().getPluginManager().registerEvents(new SmeltListener(this), this);

        getServer().getPluginManager().registerEvents(new PrestigeShopMenuHandler(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
