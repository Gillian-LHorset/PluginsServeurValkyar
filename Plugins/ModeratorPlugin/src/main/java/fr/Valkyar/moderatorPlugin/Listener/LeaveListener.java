package fr.Valkyar.moderatorPlugin.Listener;

import fr.Valkyar.moderatorPlugin.Commands.VanishCommand;
import fr.Valkyar.moderatorPlugin.ModeratorPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    ModeratorPlugin plugin;

    public LeaveListener(ModeratorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();

        for (Player inVanishList : plugin.VanishList) {
            // if the player is in vanish when he leaves
            if (inVanishList.getDisplayName().equals(player.getDisplayName())) {
                // the player is not vanish anymore
                VanishCommand.OnVanishMod(player, false);
                VanishCommand.disableVanish(player, plugin);
            }
        }
    }
}
