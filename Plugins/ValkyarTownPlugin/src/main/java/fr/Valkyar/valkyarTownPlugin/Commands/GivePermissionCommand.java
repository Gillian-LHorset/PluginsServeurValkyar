package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.Utility.AssignPerms;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.sql.SQLException;
import java.util.Set;

public class GivePermissionCommand {
    private Database db;
    private ValkyarTownPlugin _plugin;

    public GivePermissionCommand(ValkyarTownPlugin plugin, Player p, String[] args) {

        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        Player pTarget;
        try {
            if (args.length == 4) {
                pTarget = Bukkit.getPlayer(args[3]);
                // add a permission to a player logic
                if (args[1].equalsIgnoreCase("add")) {
                    String permToGive = args[2];
                    // check if the permission is the major permission
                    if (!permToGive.equalsIgnoreCase("major")) {
                        // Check if the permission exist
                        if (Bukkit.getPluginManager().getPermission("town." + permToGive) != null) {
                            // Check if the target exist
                            if (Bukkit.getOnlinePlayers().contains(pTarget)) {
                                // Check if the target is in the same town as the sender
                                if (db.getAllPlayerOfTownByName(db.getPlayerTown(pTarget)).contains(pTarget.getName())) {
                                    // check if the player targeted already have the perm
                                    if (!db.HaveAPlayerAPerm(pTarget, permToGive)) {
                                        // set the permission to true in the db
                                        db.setPermissionOfAPlayerToTrue(pTarget, permToGive);
                                        // update the perm in game
                                        AssignPerms.assignPerms(pTarget, db, _plugin);
                                        p.sendMessage(ChatColor.GREEN + "Permission accordée.");
                                    } else {
                                        p.sendMessage("Le joueur ciblé possède déjà cette permission.");
                                    }
                                } else {
                                    p.sendMessage(ChatColor.RED + "Le joueur ciblé n'est pas dans votre ville.");
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "Le joueur ciblé n'est pas en ligne ou n'existe pas.");
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "Cette permission n'existe pas.");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Vous ne pouvez pas assigner cette permission !"); // TODO : faire en sorte que le joueur puisse abdiquer de son role de maire
                    }
                } else if (args[1].equalsIgnoreCase("remove")) {
                    //logic to remove a permission
                    String permToRemove = args[2];
                    // check if the permission is the major permission
                    if (!permToRemove.equalsIgnoreCase("major")) {
                        // Check if the permission exist
                        if (Bukkit.getPluginManager().getPermission("town." + permToRemove) != null) {
                            // Check if the target exist
                            if (Bukkit.getOnlinePlayers().contains(pTarget)) {
                                // Check if the target is in the same town as the sender
                                if (db.getAllPlayerOfTownByName(db.getPlayerTown(pTarget)).contains(pTarget.getName())) {
                                    // check if the player targeted already have the perm
                                    if (db.HaveAPlayerAPerm(pTarget, permToRemove)) {
                                        // set the permission to true in the db
                                        db.setPermissionOfAPlayerToFalse(pTarget, permToRemove);
                                        // update the perm in game
                                        AssignPerms.assignPerms(pTarget, db, _plugin);
                                        p.sendMessage(ChatColor.GREEN + "La permission à correctement été retirer.");
                                    } else {
                                        p.sendMessage("Le joueur ciblé ne possède pas cette permission.");
                                    }
                                } else {
                                    p.sendMessage(ChatColor.RED + "Le joueur ciblé n'est pas dans votre ville.");
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "Le joueur ciblé n'est pas en ligne ou n'existe pas.");
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "Cette permission n'existe pas.");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Vous ne pouvez pas retirer cette permission !");
                    }

                } else {
                    p.sendMessage(ChatColor.RED + "Erreur d'argument.");
                }
            } else {
                p.sendMessage(ChatColor.RED + "Erreur de syntaxe, utilisation correcte de la commande :\n/town permission <argument> <permission> <joueur>");
            }
        } catch (SQLException e) {
            System.out.println("Ta mere la pute");
            System.out.println(e.getMessage());
        }
    }
}
