package fr.Valkyar.moderatorPlugin;

import fr.Valkyar.moderatorPlugin.Commands.VanishCommand;
import fr.Valkyar.moderatorPlugin.Commands.VanishTabCompletion;
import fr.Valkyar.moderatorPlugin.Listener.JoinListener;
import fr.Valkyar.moderatorPlugin.Listener.LeaveListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public final class ModeratorPlugin extends JavaPlugin {

    // list of vanish player in the server
    public ArrayList<Player> VanishList = new ArrayList<>();

    // a infinite effect of glowing
    public static PotionEffect InfiniteGlowing = new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, false);


    @Override
    public void onEnable() {
        // Plugin startup logic

        // add Listener class to the plugin
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new LeaveListener(this), this);

        // add commands to the plugin
        getCommand("vanish").setExecutor(new VanishCommand(this));
        getCommand("vanish").setTabCompleter(new VanishTabCompletion());

        getConfig().options().copyDefaults();
        saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
