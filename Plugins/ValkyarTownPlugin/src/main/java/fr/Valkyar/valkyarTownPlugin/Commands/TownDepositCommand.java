package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TownDepositCommand {

    private Database db;
    private ValkyarTownPlugin _plugin;

    private String _playerTown;

    private Double _playerBalance;
    private Double _amountToDeposit;

    public TownDepositCommand(ValkyarTownPlugin plugin, Player p, String[] args) {

        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;
        if (args.length == 2) {
            try {
                try {
                    _playerTown = db.getPlayerTown(p);
                    _playerBalance = db.getPlayerBalanceByUUID(p);
                    _amountToDeposit = Double.parseDouble(args[1]);
                    if (_playerBalance > _amountToDeposit) {
                        db.setWithdrawBalanceToTown(_playerTown, _amountToDeposit, p);
                        p.sendMessage(ChatColor.GREEN + "" + _amountToDeposit + "$ ont été déposé dans le compte de votre ville.");
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
