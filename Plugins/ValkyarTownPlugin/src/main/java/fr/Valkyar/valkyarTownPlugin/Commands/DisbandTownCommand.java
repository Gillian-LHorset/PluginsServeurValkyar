package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.Utility.AssignPerms;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DisbandTownCommand{

    Database db;

    private ValkyarTownPlugin _plugin;
    public DisbandTownCommand(ValkyarTownPlugin plugin, CommandSender sender, Command command, String s, String[] args) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        if (sender instanceof Player player) {
                try {
                    String playerTown = db.getPlayerTown(player);
                    if (args.length == 1 && args[0].equalsIgnoreCase("disband") && playerTown != null) {
                        if (!_plugin.leaveTown.containsKey(player.getUniqueId())) {
                            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Êtes vous sûr de vouloir supprimer la ville ?\nRéexécutez la commande dans les 10 secondes pour confirmer.");
                            _plugin.leaveTown.put(player.getUniqueId(), null);

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (plugin.leaveTown.containsKey(player.getUniqueId())) {
                                        plugin.leaveTown.remove(player.getUniqueId());
                                        player.sendMessage(ChatColor.RED + "La confirmation de la commande a expiré.");

                                    }
                                }
                            }.runTaskLater(plugin, 20L * 10);

                        } else {
                            plugin.leaveTown.remove(player.getUniqueId());
                            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "La ville de " + playerTown + " vient d'être dissoute !");


                            // disable all permission of players
                            for (String pTown : db.getAllPlayerOfTownByName(playerTown)) {
                                db.setAllPermissionsOfAPlayerToFalse(Bukkit.getPlayer(pTown));

                                // refresh perms
                                AssignPerms.assignPerms(player, db, _plugin);
                            }

                            // delete on cascade all player from town
                            db.deleteTownByName(playerTown);

                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Erreur dans la syntaxe de la commande.");
                        player.sendMessage(ChatColor.RED + "Utilisation correcte :\n/<commande> <argument> <argument>");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

        }
    }
}
