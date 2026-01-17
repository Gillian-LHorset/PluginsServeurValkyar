package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisbandTownCommand{

    Database db;

    private ValkyarTownPlugin _plugin;
    public DisbandTownCommand(ValkyarTownPlugin plugin, CommandSender sender, Command command, String s, String[] args) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        if (sender instanceof Player player) {
            try {
                //TODO player a la permission de detruire la ville
                String playerTown = db.getPlayerTown(player);
                if (args.length == 1 && args[0].equalsIgnoreCase("disband") && playerTown != null) {

                    db.deleteTownByName(playerTown);
                    Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "La ville de " + playerTown + " vient d'être dissoute !");
                } else {
                    player.sendMessage(ChatColor.RED + "Erreur dans la syntaxe de la commande.");
                    player.sendMessage(ChatColor.RED + "Utilisation correcte :\n/<commande> <argument> <argument");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
