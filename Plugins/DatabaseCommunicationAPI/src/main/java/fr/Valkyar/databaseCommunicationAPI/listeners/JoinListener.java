package fr.Valkyar.databaseCommunicationAPI.listeners;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.*;
import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.RED_OUTPUT;
import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.GREEN_OUTPUT;
import static fr.Valkyar.databaseCommunicationAPI.Utilities.PlayerProfileUtility.CreatePlayerProfile;

public class JoinListener implements Listener {

    private DatabaseCommunicationAPI _plugin;
    public JoinListener(DatabaseCommunicationAPI plugin) {
        this._plugin = plugin;
    }

    /** If the player isn't in the db, he is added to.
     * @param e Detect when a player join the game.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        if (!p.hasPlayedBefore()) {
            // add the player to the db
            this._plugin.databaseLink.RegisterPlayerInDB(p);
        }

        RegisterPlayerProfile(p, _plugin);

    }

    public static void RegisterPlayerProfile(Player p, DatabaseCommunicationAPI plugin) {

        PlayerProfile playerProfile = CreatePlayerProfile(p, plugin.databaseLink);

        if (playerProfiles == null) {
            OutputColoredText(RED_OUTPUT, "Erreur création profil");
        };

        plugin.playerProfiles.put(p.getUniqueId(), playerProfile);
    }
}
