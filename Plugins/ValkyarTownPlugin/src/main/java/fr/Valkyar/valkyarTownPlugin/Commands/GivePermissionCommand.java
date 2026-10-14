package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.Utility.AssignPerms;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class GivePermissionCommand {
    private Database db;
    private ValkyarTownPlugin _plugin;

    private final List<String> _assignablePermList = Arrays.asList("second", "claim", "unclaim", "withdraw", "invite", "kick");

    public GivePermissionCommand(ValkyarTownPlugin plugin, Player p, String[] args) {

        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        try {
            if (args.length == 4) {
                Player pTarget = Bukkit.getPlayerExact(args[3]);
                if (pTarget == null) {
                    p.sendMessage(ChatColor.RED + "Ce joueur n'existe pas.");
                    return;
                }
                // add a permission to a player logic
                if (args[1].equalsIgnoreCase("add")) {
                    String permToGive = args[2];
                    // check if the permission is the mayor permission
                    if (!permToGive.equalsIgnoreCase("mayor")) {
                        // Check if the permission exist
                        if (_assignablePermList.contains(permToGive)) {
                            // Check if the target exist
                            if (Bukkit.getOnlinePlayers().contains(pTarget)) {
                                // Check if the target is in the same town as the sender
                                if (db.getAllPlayerOfTown(db.getPlayerTown(pTarget)).contains(pTarget.getName())) {
                                    // check if the player targeted already have the perm
                                    if (!db.HaveAPlayerAPerm(pTarget, permToGive)) {
                                        // set the permission to true in the db
                                        db.setPermissionOfAPlayerToTrue(pTarget, permToGive);
                                        // update the perm in game
                                        AssignPerms.assignPerms(pTarget, db, _plugin);
                                        pTarget.sendMessage(ChatColor.GREEN + "Vous avez désormais la permission ''" + permToGive + "''");
                                        p.sendMessage(ChatColor.GREEN + "Permission accordée.");
                                    } else {
                                        p.sendMessage(ChatColor.RED + "Le joueur ciblé possède déjà cette permission.");
                                    }
                                } else {
                                    p.sendMessage(ChatColor.RED + "Le joueur ciblé n'est pas dans votre ville.");
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "Le joueur ciblé n'est pas en ligne ou n'existe pas.");
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "Cette permission n'existe pas ou n'est pas assignable.");
                        }
                    } else {
                        // set a new mayor
                        if (!pTarget.getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString())) {
                            // Check if the target exist
                            if (Bukkit.getOnlinePlayers().contains(pTarget)) {
                                // Check if the target is in the same town as the sender
                                if (db.getAllPlayerOfTown(db.getPlayerTown(pTarget)).contains(pTarget.getName())) {

                                    if (!plugin.newMayor.containsKey(p.getUniqueId())) {
                                        p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Êtes vous sûr de vouloir abdiquer de votre rôle de chef de la ville au profit de " + pTarget.getName() + " ?\nRéexécutez la commande dans la minute pour confirmer.");
                                        plugin.newMayor.put(p.getUniqueId(), null);

                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                if (plugin.newMayor.containsKey(p.getUniqueId())) {
                                                    plugin.newMayor.remove(p.getUniqueId());
                                                    p.sendMessage(ChatColor.RED + "La confirmation de la commande a expiré.");

                                                }
                                            }
                                        }.runTaskLater(plugin, 20L * 60);

                                    } else {
                                        plugin.newMayor.remove(p.getUniqueId());
                                        // set the permission to true in the db
                                        db.setAllPermissionsOfAPlayerToTrue(pTarget);
                                        db.setAllPermissionsOfAPlayerToFalse(p);
                                        // update the perm in game
                                        AssignPerms.assignPerms(pTarget, db, _plugin);

                                        AssignPerms.assignPerms(p, db, _plugin);
                                        pTarget.sendMessage(ChatColor.GREEN + "Vous êtes désormais chef de la ville.");
                                        p.sendMessage(ChatColor.GREEN + "Vous avez abdiquer avec succès.");
                                        for (String pTown : db.getAllPlayerOfTown(db.getPlayerTown(pTarget))) {
                                            Bukkit.getPlayer(pTown).sendMessage(ChatColor.GREEN + p.getName() + " viens d'abdiquer de son rôle de chef au profit de " + pTarget.getName() + ".");
                                        }
                                    }
                                } else {
                                    p.sendMessage(ChatColor.RED + "Le joueur ciblé n'est pas dans votre ville.");
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "Le joueur ciblé n'est pas en ligne ou n'existe pas.");
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "Vous ne pouvez pas vous assigner cette permission." );
                        }
                    }
                } else if (args[1].equalsIgnoreCase("remove")) {
                    //logic to remove a permission
                    String permToRemove = args[2];
                    // check if the permission is the mayor permission
                    if (!permToRemove.equalsIgnoreCase("mayor")) {
                        // Check if the permission exist
                        if (_assignablePermList.contains(permToRemove)) {
                            // Check if the target exist
                            if (Bukkit.getOnlinePlayers().contains(pTarget)) {
                                // Check if the target is in the same town as the sender
                                if (db.getAllPlayerOfTown(db.getPlayerTown(pTarget)).contains(pTarget.getName())) {
                                    // check if the player targeted already have the perm
                                    if (db.HaveAPlayerAPerm(pTarget, permToRemove)) {
                                        // set the permission to true in the db
                                        db.setPermissionOfAPlayerToFalse(pTarget, permToRemove);
                                        // update the perm in game
                                        AssignPerms.assignPerms(pTarget, db, _plugin);
                                        pTarget.sendMessage(ChatColor.RED + "Vous n'avez désormais plus la permission ''" + permToRemove + "''");
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
