package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.Utility.AssignPerms;
import fr.Valkyar.valkyarTownPlugin.Utility.ChunkSystem;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreateTownCommand {

    private final Double createTownPrice = 1.00;

    private ValkyarTownPlugin _plugin;
    private Database db;

    public CreateTownCommand(ValkyarTownPlugin plugin, CommandSender sender, Command command, String s, String[] args) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        if (sender instanceof Player player) {
            try {
                Double playerBalance = db.getPlayerBalanceByUUID(player);
                String playerTown = db.getPlayerTown(player);
                if (args.length == 2 && playerBalance >= createTownPrice) {
                    // if the player didn't have a town
                    if (playerTown == null) {
                        // if the town name don't exist already
                        if (!db.getAllTownsNames().contains(args[1])) {
                            // check if the chunk is already claimed
                            int[] claimedChunk = ChunkSystem.getChunkId(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
                            if (db.getChunkById(claimedChunk) == null) {
                                // the player pay the creation of the town
                                db.setPlayerBalanceDecrease(player, createTownPrice);

                                // insert the town in the db
                                db.CreateTown(player, args[1]);

                                // give him all permissions
                                db.setAllPermissionsOfAPlayerToTrue(player);

                                // register the chunk in the db
                                db.registerChunk(ChunkSystem.getChunkId(player.getLocation().getBlockX(), player.getLocation().getBlockZ()), args[1]);

                                AssignPerms.assignPerms(player, db, _plugin);

                                Bukkit.broadcastMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Félicitation à " + player.getName() + " qui viens de créer la ville de " + args[1] + " !");
                                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
                            } else {
                                player.sendMessage(ChatColor.RED + "Vous ne pouvez pas créer de ville sur un chunk déjà claim !");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Vous ne pouvez pas créer une ville avec ce nom.");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Vous ne pouvez pas créer de ville, vous êtes déjà une ville.");
                    }
                } else if (args.length == 2 && db.getPlayerBalanceByUUID(player) <= createTownPrice) {
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
