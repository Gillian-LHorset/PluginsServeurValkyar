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

public class TownWithdrawCommand {

    private Town _playerTown;

    private Double _townBalance;
    private Double _amountToWithdraw;

    public TownWithdrawCommand(ValkyarTownPlugin plugin, Player p, String[] args) {


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
            _amountToWithdraw = Double.parseDouble(args[1]);
        } catch (Exception e) {
            p.sendMessage(ChatColor.RED + "Montant invalide.");
            return;
        }

        _townBalance = _playerTown.getBalance();
        if (_townBalance < _amountToWithdraw) {
            p.sendMessage(ChatColor.RED + "La ville n'a pas assez d'argent.");
            return;
        }

        TownManager.PlayerWithdrawMoneyInTown(DatabaseCommunicationAPI.getApi(), _playerTown, _amountToWithdraw, p);

        p.sendMessage(ChatColor.GREEN + "" + _amountToWithdraw + "$ ont été ajouté à votre compte.");
    }
}
