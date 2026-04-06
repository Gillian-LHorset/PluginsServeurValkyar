package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;

public class TownBalanceCommand {

    private Database db;

    public TownBalanceCommand(ValkyarTownPlugin plugin, Player p, String[] args) {
        db = plugin.databaseApi.databaseLink;

        if (args.length == 1) {
            String _playerTown = db.getPlayerTown(p);
            if (_playerTown != null) {
                try {
                    p.sendMessage(ChatColor.YELLOW + "Votre ville possède " + db.getBalanceTownByName(_playerTown) + " $.");
                } catch (SQLException e) {
                    System.err.println("[TownBalanceCommand] - " + e.getMessage());
                }
            } else {
                p.sendMessage(ChatColor.RED + "Vous n'avez pas de ville.");
            }
        } else if (args.length == 2) {
            List<String> allTowns = db.getAllTownsNames();
            if (allTowns != null && allTowns.contains(args[1])) {
                for (String town : allTowns) {
                    if (town.equals(args[1])) {
                        try {
                            p.sendMessage("La ville de " + town + " possède " + db.getBalanceTownByName(town) + "$.");
                            break;
                        } catch (SQLException e) {
                            System.err.println("[TownBalanceCommand] - " + e.getMessage());
                        }
                    }
                }
            } else {

                p.sendMessage(ChatColor.RED + "La ville ciblée n'a pas été trouvé.");
            }
        } else {
            p.sendMessage(ChatColor.RED + "Erreur de sytaxe.");
        }
    }
}
