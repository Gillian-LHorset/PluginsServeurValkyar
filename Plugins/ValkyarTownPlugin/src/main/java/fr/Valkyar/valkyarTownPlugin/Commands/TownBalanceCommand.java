package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class TownBalanceCommand {

    private Database db;

    private String _playerTown;

    public TownBalanceCommand(ValkyarTownPlugin plugin, Player p, String[] args) {
        db = plugin.databaseApi.databaseLink;

        if (args.length == 1) {
            _playerTown = db.getPlayerTown(p);
            if (_playerTown != null) {
                try {
                    p.sendMessage(ChatColor.YELLOW + "Votre ville possède " + db.getBalanceTownByName(_playerTown) + " $.");
                } catch (SQLException e) {
                    System.err.println("[TownBalanceCommand] - " + e.getMessage());
                }
            } else {
                p.sendMessage(ChatColor.RED + "Vous n'avez pas de ville.");
            }
        } else {
            p.sendMessage(ChatColor.RED + "Erreur de sytaxe.");
        }
    }
}
