package fr.Valkyar.valkyarTownPlugin;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.valkyarTownPlugin.CommandCompletion.ChatCommandCompletion;
import fr.Valkyar.valkyarTownPlugin.CommandCompletion.TownCommandCompletion;
import fr.Valkyar.valkyarTownPlugin.Commands.ChatCommand;
import fr.Valkyar.valkyarTownPlugin.Commands.TownCommand;
import fr.Valkyar.valkyarTownPlugin.JoinInvitationCommand.AcceptCommand;
import fr.Valkyar.valkyarTownPlugin.JoinInvitationCommand.RefuseCommand;
import fr.Valkyar.valkyarTownPlugin.Listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ValkyarTownPlugin extends JavaPlugin {

    public DatabaseCommunicationAPI databaseApi = DatabaseCommunicationAPI.getApi();
    /// the first UUID corresponds to the player invited UUID, and the second UUID corresponds to the player who invite
    public final Map<UUID, UUID> sentInvitations = new HashMap<>();
    public final Map<UUID, Boolean> leaveTown = new HashMap<>();
    public final Map<UUID, Boolean> newMayor = new HashMap<>();

    // define in which chat the player will talk
    public final Map<UUID, String> playerChat = new HashMap<>();


    @Override
    public void onEnable() {
        // Plugin startup logic
        // Register Commands
        getCommand("town").setExecutor(new TownCommand(this));
        getCommand("t").setExecutor(new TownCommand(this));
        getCommand("accept").setExecutor(new AcceptCommand(this));
        getCommand("refuse").setExecutor(new RefuseCommand(this));
        getCommand("chat").setExecutor(new ChatCommand(this));

        // Register tab completion
        getCommand("town").setTabCompleter(new TownCommandCompletion(this));
        getCommand("t").setTabCompleter(new TownCommandCompletion(this));
        getCommand("chat").setTabCompleter(new ChatCommandCompletion());

        // TODO : ajouter la protection contre les explosions et les dégats aux entités
        getServer().getPluginManager().registerEvents(new BreakBlockListenener(this), this);
        getServer().getPluginManager().registerEvents(new PlaceBlockListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new BucketListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerTalkListener(this), this);

        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
