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
        Player player = e.getPlayer();

        if (!player.hasPlayedBefore()) {
            try {
                // add the player to the db
                this._plugin.databaseLink.firstTimeJoinStatement(player);
                System.out.println(GREEN_OUTPUT + "[DatabaseCommunicationAPI] : The player is correctly registered in the DB");
            } catch (SQLException err) {
                OutputColoredText(RED_OUTPUT,"[DatabaseCommunicationAPI] : L'insersion du nouveau joueur dans la DATABASE à échouée");
                System.out.println("[DatabaseCommunicationAPI] : " + err.getMessage());
            }
        }

        try {
            if (this._plugin.databaseLink.getUUIDofPlayer(player) == null) {
                // if the player has been detected like he already played, but he's not in the DB

                // add the player to the db
                this._plugin.databaseLink.firstTimeJoinStatement(player);
                OutputColoredText(RED_OUTPUT, "[DatabaseCommunicationAPI] : A player who has already played wasn't in the database.");
            }
        } catch (SQLException err) {
            OutputColoredText(RED_OUTPUT,"[DatabaseCommunicationAPI] : Exception detected");
        }


        Player p = e.getPlayer();

        PlayerProfile playerProfile = CreatePlayerProfile(p, _plugin.databaseLink);

        if (playerProfiles == null) {
            OutputColoredText(RED_OUTPUT, "Erreur création profil");
        };

        _plugin.playerProfiles.put(p.getUniqueId(), playerProfile);
    }
}
