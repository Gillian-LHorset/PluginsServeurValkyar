package fr.Valkyar.valkyarVaultPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.valkyarVaultPlugin.ValkyarVaultPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



import java.sql.SQLException;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class PayCommand implements CommandExecutor {

    private ValkyarVaultPlugin _plugin;

    public PayCommand(ValkyarVaultPlugin plugin) {
        this._plugin = plugin;
    }

    private String _targetName;
    public Double amountOfTransaction;

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        try {
            amountOfTransaction = Double.parseDouble(args[1]);

        } catch (Exception e) {
            amountOfTransaction = null;
        }

        if (commandSender instanceof Player player) {
            PlayerProfile profile = playerProfiles.get(player.getUniqueId());
            if (args.length != 2 || amountOfTransaction == null) {
                player.sendMessage(ChatColor.RED + "Utilisation de la commande :"+ ChatColor.YELLOW +"\n/pay <player> <montant>.");
            } else if (amountOfTransaction < 0.01) {
                player.sendMessage(ChatColor.RED + "Le montant envoyer est incorrecte.");
            } else {
                _targetName = args[0];
                if (Bukkit.getPlayer(_targetName) != null && Bukkit.getPlayer(_targetName).isOnline() && !_targetName.equalsIgnoreCase(player.getName())) {
                    Player targetPlayer = Bukkit.getPlayer(_targetName);
                    PlayerProfile targetProfile = playerProfiles.get(targetPlayer.getUniqueId());
                        Double senderBalance = profile.getCurrentMoney();
                        if (senderBalance >= amountOfTransaction) {
                            // if the money of the sender is bigger than the amount of sent money

                            // Withdraw the amount of transaction to the sender
                            profile.decreaseMoney(amountOfTransaction);
                            targetProfile.increaseMoney(amountOfTransaction);

                            player.sendMessage(ChatColor.GREEN + "Transaction effectuée.");
                            targetPlayer.sendMessage( ChatColor.GREEN + player.getDisplayName() + " vous a envoyé " + amountOfTransaction + " $.");
                        } else {
                            player.sendMessage("Vous n'avez pas l'argent suffisante pour effecter la transaction.\nIl vous manque " + ChatColor.RED + (amountOfTransaction - senderBalance) + ChatColor.WHITE + " $.");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Le joueur ciblé n'est pas en ligne.");
                    }
                }
            }




        return true;
    }
}
