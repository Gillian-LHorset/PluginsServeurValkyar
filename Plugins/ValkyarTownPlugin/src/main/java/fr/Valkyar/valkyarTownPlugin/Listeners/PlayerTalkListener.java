package fr.Valkyar.valkyarTownPlugin.Listeners;

import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class PlayerTalkListener implements Listener {

    private ValkyarTownPlugin _plugin;
    private Database db;

    public PlayerTalkListener(ValkyarTownPlugin plugin) {
        _plugin = plugin;
        db = plugin.databaseApi.databaseLink;
    }

    // TODO : recréer le système de msg et le système de parlé

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (_plugin.playerChat.get(p.getUniqueId()).equalsIgnoreCase("ville")) {
            e.setCancelled(true);
            PlayerProfile pp = playerProfiles.get(p.getUniqueId());
            Town town = pp.getTown();
            List<Player> playersTown = town.getTownPlayers();

            for (Player playerTown : playersTown) {
                playerTown.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + town.getName() + " - "
                        + p.getName() + " : " + ChatColor.RESET
                        + e.getMessage());
            }

        }

    }
}
