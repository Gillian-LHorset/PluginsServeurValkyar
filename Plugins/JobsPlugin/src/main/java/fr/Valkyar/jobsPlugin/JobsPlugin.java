package fr.Valkyar.jobsPlugin;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Job;
import fr.Valkyar.jobsPlugin.Commands.JobCommand;
import fr.Valkyar.jobsPlugin.Listeners.JobListeners.*;
import fr.Valkyar.jobsPlugin.Listeners.JoinListener;
import fr.Valkyar.jobsPlugin.Listeners.LeaveListener;
import fr.Valkyar.jobsPlugin.TabCompletion.JobCommandCompletion;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class JobsPlugin extends JavaPlugin {

    public DatabaseCommunicationAPI databaseApi = DatabaseCommunicationAPI.getApi();
    // attention : case-sensitive
    public Map<String, Job> jobsList;

    public final JobsRegister jobsRegister = new JobsRegister();
    public HashMap<UUID, PlayerProfile> playerProfiles = new HashMap<>();

    @Override
    public void onEnable() {

        jobsRegister.loadJobsFromDatabase(databaseApi.databaseLink);
        jobsList = jobsRegister.getAllJobs();

        // Register Commands
        getCommand("jobs").setExecutor(new JobCommand(this));

        // Register tab completion
        getCommand("jobs").setTabCompleter(new JobCommandCompletion(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new LeaveListener(this), this);

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

        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
