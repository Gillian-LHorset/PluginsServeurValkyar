package fr.Valkyar.valkyarTownPlugin;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.valkyarTownPlugin.CommandCompletion.TownCommandCompletion;
import fr.Valkyar.valkyarTownPlugin.Commands.TownCommand;
import fr.Valkyar.valkyarTownPlugin.JoinInvitationCommand.AcceptCommand;
import fr.Valkyar.valkyarTownPlugin.JoinInvitationCommand.RefuseCommand;
import fr.Valkyar.valkyarTownPlugin.Listeners.*;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ValkyarTownPlugin extends JavaPlugin {

    public DatabaseCommunicationAPI databaseApi = DatabaseCommunicationAPI.getApi();
    /// the first UUID corresponds to the player invited UUID, and the second UUID corresponds to the player who invite
    public final Map<UUID, UUID> sendedInvitations = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        // Register Commands
        getCommand("town").setExecutor(new TownCommand(this));
        getCommand("accept").setExecutor(new AcceptCommand(this));
        getCommand("refuse").setExecutor(new RefuseCommand(this));

        // Register tab completion
        getCommand("town").setTabCompleter(new TownCommandCompletion(this));

        // TODO : ajouter la protection contre les explosions
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
