package fr.Valkyar.valkyarVaultPlugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class joinListener implements org.bukkit.event.Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        if (!player.hasPlayedBefore()) {

        }
    }

}
