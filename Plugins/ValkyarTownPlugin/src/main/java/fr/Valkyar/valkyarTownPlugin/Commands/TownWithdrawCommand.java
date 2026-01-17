package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TownWithdrawCommand {
    private Database db;
    private ValkyarTownPlugin _plugin;

    private String _playerTown;

    private Double _townBalance;
    private Double _amountToWithdraw;

    public TownWithdrawCommand(ValkyarTownPlugin plugin, Player p, String[] args) {

        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        if (args.length == 2) {
            try {
                try {
                    _playerTown = db.getPlayerTown(p);
                    _amountToWithdraw = Double.parseDouble(args[1]);
                    _townBalance = db.getBalanceTownByName(_playerTown);
                    if (_townBalance > _amountToWithdraw) {
                        db.setAddBalanceByName(p, _amountToWithdraw);
                        db.setTownBalanceDecreaseByName(_playerTown, _amountToWithdraw);
                        p.sendMessage(ChatColor.GREEN + "" + _amountToWithdraw + "$ ont été ajouté à votre compte.");
                    }
                } catch (Exception e) {
                    p.sendMessage(ChatColor.RED + "Montant invalide.");
                }
            } catch (NumberFormatException e) {
                p.sendMessage(ChatColor.RED + "Montant invalide.");
            }

        } else {
            p.sendMessage(ChatColor.RED + "Erreur de syntaxe.");
        }
    }
}
