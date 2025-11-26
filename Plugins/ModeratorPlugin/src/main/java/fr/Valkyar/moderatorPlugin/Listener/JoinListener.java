package fr.Valkyar.moderatorPlugin.Listener;

import fr.Valkyar.moderatorPlugin.ModeratorPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    ModeratorPlugin plugin;

    public JoinListener(ModeratorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // hide players in vanish to new player
        for (Player playerInVanish : plugin.VanishList) {
            player.hidePlayer(plugin, playerInVanish);
        }
    }
}
