package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.databaseCommunicationAPI.managers.TownManager;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.TownMap;
import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;
import static fr.Valkyar.databaseCommunicationAPI.managers.TownManager.LeaveTown;

public class DisbandTownCommand{

    Database db;

    private ValkyarTownPlugin _plugin;
    public DisbandTownCommand(ValkyarTownPlugin plugin, CommandSender sender, Command command, String s, String[] args) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        if (sender instanceof Player player) {
            Town playerTown = playerProfiles.get(player.getUniqueId()).getTown();

            if (args.length != 1) {
                player.sendMessage(ChatColor.RED + "Erreur dans la commande.");
                return;
            }

            if (!args[0].equalsIgnoreCase("disband")) {
                player.sendMessage(ChatColor.RED + "Erreur dans le paramètre de la commande.");
                return;
            }

            if (playerTown == null) {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas de ville.");
                return;
            }

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
                // 10 * 20 ticks = 10 secondes
                }.runTaskLater(plugin, 20L * 10);

            } else {
                plugin.leaveTown.remove(player.getUniqueId());
                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "La ville de " + playerTown.getName() + " vient d'être dissoute !");

                LeaveTown(db, player, playerTown);

                for (Player p : TownMap.get(playerTown.getId()).getTownPlayers()) {
                    p.sendMessage(ChatColor.RED + "Votre ville a été dissoute par " + player.getName());
                    // disable all permission of players
                    LeaveTown(db, p, playerTown);
                }

                // delete on cascade all player from town
                TownManager.DeleteTown(db, playerTown);
            }
        }
    }
}
