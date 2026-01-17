package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.Utility.ChunkSystem;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class CreateTownCommand {

    private final int createTownPrice = 1;

    private ValkyarTownPlugin _plugin;
    private Database db;

    public CreateTownCommand(ValkyarTownPlugin plugin, CommandSender sender, Command command, String s, String[] args) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        if (sender instanceof Player player) {
            try {
                Double playerBalance = db.getPlayerBalanceByUUID(player);
                String playerTown = db.getPlayerTown(player);
                if (args.length == 2 && args[0].equalsIgnoreCase("create") && playerBalance >= createTownPrice) {

                    db.setPlayerBalanceDecrease(player, createTownPrice);
                    db.CreateTown(player, args[1]);
                    db.registerChunk(ChunkSystem.getChunkId(player.getLocation().getBlockX(), player.getLocation().getBlockZ()), args[1]);
                    Bukkit.broadcastMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Félicitation à " + player.getName() + " qui viens de créer la ville de "+args[1] + " !");

                } else if (args.length == 1 && args[0].equalsIgnoreCase("leave") && playerTown != null) {

                    db.setPlayerTownByName(player, null);
                    player.sendMessage("Vous avec quitté votre ville.");
                    //TODO implémenter logique de sortie de ville (destruction, role, si le maire quitte)
                    if (db.getNumberOfPlayersInTown(playerTown) < 1) {

                        db.deleteTownByName(playerTown);
                        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "La ville de " + playerTown + " viens d'être détruite par faut de membres !");
                    }
                } else if (args.length == 1 && args[0].equalsIgnoreCase("leave") && playerTown == null) {
                    player.sendMessage(ChatColor.RED + "Vous n'avez aucune ville.");
                } else if (args.length == 2 && args[0].equalsIgnoreCase("create") && db.getPlayerBalanceByUUID(player) <= createTownPrice) {
                    player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent pour créer une ville.");
                } else {
                    player.sendMessage(ChatColor.RED + "Erreur dans la syntaxe de la commande.");
                    player.sendMessage(ChatColor.RED + "Utilisation correcte :\n/<commande> <argument> <argument");
                }
            } catch (SQLException err) {
                System.out.println("[ValkyarTownPlugin] - Erreur de connexion");
                System.out.println(err.getMessage());
            }
        }
    }
}
