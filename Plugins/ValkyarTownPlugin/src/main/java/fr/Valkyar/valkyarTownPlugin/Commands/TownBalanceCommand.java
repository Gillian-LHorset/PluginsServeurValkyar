package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.Models.Town;
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
            Town _playerTown = db.getPlayerTown(p);
            if (_playerTown != null) {
                p.sendMessage(ChatColor.YELLOW + "Votre ville possède " + _playerTown.getBalance() + " $.");
            } else {
                p.sendMessage(ChatColor.RED + "Vous n'avez pas de ville.");
            }
        } else if (args.length == 2) {
            List<Town> allTowns = db.getAllTowns();
            if (allTowns != null && allTowns.contains(args[1])) {
                for (Town town : allTowns) {
                    if (town.getName().equals(args[1])) {
                        p.sendMessage("La ville de " + town + " possède " + town.getBalance() + "$.");
                        break;
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
