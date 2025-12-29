package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class CreateTownCommand implements CommandExecutor {

    private final int createTownPrice = 10000;

    private ValkyarTownPlugin _plugin;
    public CreateTownCommand(ValkyarTownPlugin  plugin) {
        _plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            try {
                if (args.length == 2 && args[0].equalsIgnoreCase("create") && _plugin.databaseApi.databaseLink.getPlayerBalanceByUUID(player) >= createTownPrice) {
                        _plugin.databaseApi.databaseLink.setPlayerBalanceDecrease(player, createTownPrice);
                        _plugin.databaseApi.databaseLink.CreateTown(player, args[1]);
                        Bukkit.broadcastMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Félicitation à" + player.getName() + " qui viens de créer la ville de "+args[1] + " !");
                } else if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
                    _plugin.databaseApi.databaseLink.setPlayerTownByName(player, null);
                    player.sendMessage("Vous avec quitté votre ville.");
                }
                else if (_plugin.databaseApi.databaseLink.getPlayerBalanceByUUID(player) <= createTownPrice) {
                    player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent pour créer une ville.");
                } else {
                    player.sendMessage(ChatColor.RED + "Erreur dans la syntaxe de la commande.");
                    player.sendMessage(ChatColor.RED + "Utilisation correcte :\n/<commande> <argument> <argument");
                }

            }catch (SQLException err) {
                System.out.println("[ValkyarTownPlugin] - Erreur de connexion");
                System.out.println(err.getMessage());
            }
        }
        return true;
    }
}
