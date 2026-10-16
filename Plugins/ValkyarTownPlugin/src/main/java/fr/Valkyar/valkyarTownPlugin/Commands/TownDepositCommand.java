package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.databaseCommunicationAPI.managers.TownManager;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class TownDepositCommand {

    private Town _playerTown;
    private Double _amountToDeposit;

    public TownDepositCommand(ValkyarTownPlugin plugin, Player p, String[] args) {

        if (args.length != 2) {
            p.sendMessage(ChatColor.RED + "Erreur de syntaxe.");
            return;
        }

        PlayerProfile pp = playerProfiles.get(p.getUniqueId());

        if (!pp.hasTown()) {
            p.sendMessage(ChatColor.RED + "Vous n'avez pas de ville.");
            return;
        }

        _playerTown = pp.getTown();

        try {
            _amountToDeposit = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            p.sendMessage(ChatColor.RED + "Le montant saisi n'est pas valide.");
            return;
        }

        if (pp.getCurrentMoney() < _amountToDeposit) {
            p.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent pour déposer cette somme.");
            return;
        }

        TownManager.PlayerDepositMoneyInTown(DatabaseCommunicationAPI.getApi(), _playerTown, _amountToDeposit, p);

        p.sendMessage(ChatColor.GREEN + "" + _amountToDeposit + "$ ont été déposé dans le compte de votre ville.");
    }
}
