package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.TownMap;
import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class TownBalanceCommand {

    private Database db;

    public TownBalanceCommand(ValkyarTownPlugin plugin, Player p, String[] args) {
        db = plugin.databaseApi.databaseLink;

        PlayerProfile pp = playerProfiles.get(p.getUniqueId());

        if (args.length != 1 && args.length != 2) {
            p.sendMessage(ChatColor.RED + "Erreur de syntaxe.");
            return;
        }

        if (args.length == 1) {
            if (!pp.hasTown()) {
                p.sendMessage(ChatColor.RED + "Vous n'avez pas de ville.");
                return;
            }

            Town _playerTown = pp.getTown();
            p.sendMessage(ChatColor.YELLOW + "Votre ville possède " + _playerTown.getBalance() + " $.");
            return;
        }

        if (args.length == 2) {
            List<Town> allTowns = new ArrayList<>(TownMap.values());
            for (Town town : allTowns) {
                if (town.getName().equals(args[1])) {
                    p.sendMessage("La ville de " + town.getName() + " possède " + town.getBalance() + "$.");
                    return;
                }
            }

            p.sendMessage(ChatColor.RED + "La ville ciblée n'a pas été trouvé.");
        }
    }
}
