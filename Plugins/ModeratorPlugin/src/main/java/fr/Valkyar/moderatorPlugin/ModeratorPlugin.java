package fr.Valkyar.moderatorPlugin;

import fr.Valkyar.moderatorPlugin.Commands.VanishCommand;
import fr.Valkyar.moderatorPlugin.Listener.JoinListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class ModeratorPlugin extends JavaPlugin {

    public ArrayList<Player> VanishList = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic

        // ajoute les classes Listener à la lecture du plugin
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);

        // ajoute les classes des commandes
        getCommand("vanish").setExecutor(new VanishCommand(this));

        getConfig().options().copyDefaults();
        saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
