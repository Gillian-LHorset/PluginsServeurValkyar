package fr.Valkyar.valkyarVaultPlugin.listeners;

import fr.Valkyar.valkyarVaultPlugin.ValkyarVaultPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

import static fr.Valkyar.valkyarVaultPlugin.ValkyarVaultPlugin.*;

public class JoinListener implements org.bukkit.event.Listener {

    private ValkyarVaultPlugin _plugin;

    public JoinListener(ValkyarVaultPlugin plugin) {
        this._plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        if (!player.hasPlayedBefore()) {
            try {
                //TODO the creation of the player in the db need to be in another plugin

                // add the player to the db
                this._plugin.databaseLink.firstTimeJoinStatement(player);
                System.out.println(GREEN_OUTPUT + "[ValkyarVaultPlugin] : The player is correctly registered in the DB");
            } catch (SQLException err) {
                OutputColoredText(RED_OUTPUT,"[ValkyarVaultPlugin] : L'insersion du nouveau joueur dans la DATABASE à échouée");
                System.out.println("[ValkyarVaultPlugin] : " + err.getMessage());
            }
        }

        try {
            if (this._plugin.databaseLink.getUUIDofPlayer(player) == null) {
                // if the player has been detected like he already played, but he's not in the DB

                // add the player to the db
                this._plugin.databaseLink.firstTimeJoinStatement(player);
                OutputColoredText(RED_OUTPUT, "[ValkyarVaultPlugin] : A player who has already played wasn't in the database.");
            }
        } catch (SQLException err) {
            OutputColoredText(RED_OUTPUT,"[ValkyarVaultPlugin] : Exception detected");
        }


    }

}
