package fr.Valkyar.valkyarVaultPlugin.listeners;

import fr.Valkyar.valkyarVaultPlugin.ValkyarVaultPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.util.Date;

import static fr.Valkyar.valkyarVaultPlugin.ValkyarVaultPlugin.*;

public class JoinListener implements org.bukkit.event.Listener {

    private ValkyarVaultPlugin _plugin;

    public JoinListener(ValkyarVaultPlugin plugin) {
        this._plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        ValkyarVaultPlugin.OutputColoredText(ValkyarVaultPlugin.RED_OUTPUT, "ta mere la pute1");
        if (!player.hasPlayedBefore()) {
            try {
                //TODO the creation of the player in the db need to be in another plugin
                this._plugin.databaseLink.firstTimeJoinStatement(player);
                System.out.println(GREEN_OUTPUT + "[ValkyarVaultPlugin] : The player is correctly registered in the DB");
            } catch (SQLException err) {
                OutputColoredText(RED_OUTPUT,"[ValkyarVaultPlugin] : L'insersion du nouveau joueur dans la DATABASE à échouée");
                System.out.println("[ValkyarVaultPlugin] : " + err.getMessage());
            }
        }
        ValkyarVaultPlugin.OutputColoredText(ValkyarVaultPlugin.RED_OUTPUT, "ta mere la pute");

        try {
            if (this._plugin.databaseLink.getUUIDofPlayer(player) == null) {
                ValkyarVaultPlugin.OutputColoredText(ValkyarVaultPlugin.RED_OUTPUT, "ta mere la pute3");

                this._plugin.databaseLink.firstTimeJoinStatement(player);
                OutputColoredText(RED_OUTPUT, "[ValkyarVaultPlugin] : A player who has already played wasn't in the database.");
            }
        } catch (SQLException err) {
            OutputColoredText(RED_OUTPUT,"[ValkyarVaultPlugin] : Exception detected");
        }


    }

}
