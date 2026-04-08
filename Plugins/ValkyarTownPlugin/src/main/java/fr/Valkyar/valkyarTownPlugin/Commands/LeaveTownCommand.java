package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.Utility.AssignPerms;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class LeaveTownCommand {
    Database db;
    public LeaveTownCommand(ValkyarTownPlugin plugin, Player p, String[] args) {
        try {
            db = plugin.databaseApi.databaseLink;
            String playerTown = db.getPlayerTown(p);
            if (playerTown != null) {
                if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
                    if (!plugin.leaveTown.containsKey(p.getUniqueId())) {
                        p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Êtes vous sûr de vouloir quitter la ville ?\nRéexécutez la commande dans la minute pour confirmer.");
                        plugin.leaveTown.put(p.getUniqueId(), null);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (plugin.leaveTown.containsKey(p.getUniqueId())) {
                                    plugin.leaveTown.remove(p.getUniqueId());
                                    p.sendMessage(ChatColor.RED + "La confirmation de la commande a expiré.");

                                }
                            }
                        }.runTaskLater(plugin, 20L * 60);

                    } else {
                        plugin.leaveTown.remove(p.getUniqueId());

                        // if the town has no player anymore
                        if (db.getNumberOfPlayersInTown(playerTown) - 1 < 1) {
                            // delete the town
                            db.deleteTownByName(playerTown);
                            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "La ville de " + playerTown + " viens d'être détruite par faute de membres !");
                            // if the player is the major of his town
                        } else if (p.hasPermission("town.mayor")) {

                            // // // // // // // // // // // // // // //
                            // revoke the player town fk from the db
                            db.setPlayerTownByName(p, null);
                            // // // // // // // // // // // // // // //

                            List<String> allPlayers = db.getAllPlayerOfTownByName(playerTown);
                            // get all players of the town with the permission "second"
                            List<String> second = db.getAllSecondFromTown(playerTown);

                            Player newMajor;

                            if (!second.isEmpty()) {
                                newMajor = Bukkit.getPlayer(second.getFirst());

                                // set major the first of the list
                                db.setPermissionOfAPlayerToTrue(newMajor, "mayor");

                                // inform all player about the change
                                for (String pName : allPlayers) {
                                    Bukkit.getPlayer(pName).sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Le joueur " + newMajor.getName() + " est désormait le maire de votre ville.");
                                }
                            } else {
                                // set a random of the town new major
                                newMajor = Bukkit.getPlayer(allPlayers.getFirst());

                                // set all permission to true to the new major
                                db.setPermissionOfAPlayerToTrue(newMajor, "mayor");

                                // inform all player about the change
                                for (String pName : allPlayers) {
                                    Bukkit.getPlayer(pName).sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Le joueur " + newMajor.getName() + " est désormait le maire de votre ville.");
                                }
                            }
                            AssignPerms.assignPerms(newMajor, db, plugin);
                        }

                        // disable all permission of players
                        for (String pTownName : db.getAllPlayerOfTownByName(playerTown)) {
                            if (!pTownName.equals(p.getName())) {
                                Bukkit.getPlayer(pTownName).sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BOLD + p.getDisplayName() + ChatColor.RESET + "" + ChatColor.RED + " a quitté la ville.");
                            }
                        }

                        // revoke the player town fk from the db
                        db.setPlayerTownByName(p, null);

                        p.sendMessage(ChatColor.RED + "Vous avez quitter la ville de " + playerTown + ".");

                        db.setAllPermissionsOfAPlayerToFalse(p);

                        // refresh perms
                        AssignPerms.assignPerms(p, db, plugin);
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Erreur dans la syntaxe de la commande.");
                    p.sendMessage(ChatColor.RED + "Utilisation correcte :\n/<commande> <argument>");
                }
            } else {
                p.sendMessage(ChatColor.RED + "Vous n'avez pas de ville.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
