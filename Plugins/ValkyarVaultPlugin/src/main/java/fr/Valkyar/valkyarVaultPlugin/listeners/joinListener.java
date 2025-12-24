package fr.Valkyar.valkyarVaultPlugin.listeners;

import fr.Valkyar.valkyarVaultPlugin.ValkyarVaultPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.util.Date;

import static fr.Valkyar.valkyarVaultPlugin.ValkyarVaultPlugin.*;

public class joinListener implements org.bukkit.event.Listener {

    private ValkyarVaultPlugin _plugin;

    public joinListener(ValkyarVaultPlugin plugin) {
        this._plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        if (!player.hasPlayedBefore()) {
            try {
                if (this._plugin.databaseLink.getUUIDofPlayer(player).equals(player.getUniqueId().toString())) {
                    // if the UUID of the player who has never played before has been detected
                    OutputColoredText(RED_OUTPUT,"[ValkyarVaultPlugin] : Un même id à été détecté");
                    player.ban("Un problème à été détecter avec votre compte.\nVeuillez contacter un responsable.", (Date) null, "Console");
                } else {
                    try {
                        this._plugin.databaseLink.firstTimeJoinStatement(player);
                        System.out.println(GREEN_OUTPUT + "[ValkyarVaultPlugin] : The player is correctly registered in the DB");
                    } catch (SQLException err) {
                        OutputColoredText(RED_OUTPUT,"[ValkyarVaultPlugin] : L'insersion du nouveau joueur dans la DATABASE à échouée");
                        System.out.println("[ValkyarVaultPlugin] : " + err.getMessage());
                    }
                }
            } catch (SQLException ex) {
                OutputColoredText(RED_OUTPUT,"[ValkyarVaultPlugin] : Exception detected");
            }
        }
        try {
            if (this._plugin.databaseLink.getUUIDofPlayer(player) == null) {
                this._plugin.databaseLink.firstTimeJoinStatement(player);
                OutputColoredText(RED_OUTPUT, "[ValkyarVaultPlugin] : A player who has already played wasn't in the database.");
            }
        } catch (SQLException err) {
            OutputColoredText(RED_OUTPUT,"[ValkyarVaultPlugin] : Exception detected");
        }


    }

}
