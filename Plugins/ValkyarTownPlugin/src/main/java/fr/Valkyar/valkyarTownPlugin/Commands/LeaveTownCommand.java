package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.databaseCommunicationAPI.managers.TownManager;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.TownMap;
import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;
import static java.lang.Boolean.TRUE;

public class LeaveTownCommand {
    Database db;
    ValkyarTownPlugin _plugin;

    private boolean _wasMayor = false;

    public LeaveTownCommand(ValkyarTownPlugin plugin, Player p, String[] args) {
        _plugin = plugin;
        db = plugin.databaseApi.databaseLink;
        PlayerProfile pp = playerProfiles.get(p.getUniqueId());

        if (!pp.hasTown()) {
            p.sendMessage(ChatColor.RED + "Vous n'avez pas de ville.");
            return;
        }

        Town playerTown = TownMap.get(pp.getTown().getId());

        if (args.length != 1) {
            p.sendMessage(ChatColor.RED + "Erreur dans la syntaxe de la commande.");
            p.sendMessage(ChatColor.RED + "Utilisation correcte :\n/<commande> <argument>");
            return;
        }

        if (!plugin.leaveTown.containsKey(p.getUniqueId())) {
            p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Êtes vous sûr de vouloir quitter votre ville ?\nRéexécutez la commande dans la minute pour confirmer.");
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

            p.sendMessage(ChatColor.RED + "Vous avez quitter la ville de " + playerTown.getName() + ".");

            if (pp.hasPermission("town_mayor")) {
                _wasMayor = TRUE;
            }

            TownManager.LeaveTown(db, p, playerTown);

            for (Player player : playerTown.getTownPlayers()) {
                player.sendMessage(ChatColor.RED + "Le joueur " + ChatColor.BOLD + p.getDisplayName() + ChatColor.RESET + "" + ChatColor.RED + " a quitté la ville.");
            }

            // if the town has no player anymore
            if (playerTown.getTownPlayers().isEmpty()) {
                // delete the town
                TownManager.DeleteTown(db, playerTown);
                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "La ville de " + playerTown.getName() + " viens d'être détruite par faute de membres !");
                return;
            }
            // if the player is the major of his town

            if (_wasMayor) {
                setNewMayor(playerTown);
            }
        } // end of : plugin.leaveTown.containsKey(p.getUniqueId())
    }

    private void setNewMayor(Town town) {
        List<Player> allPlayers = town.getTownPlayers();
        // get all players of the town with the permission "second"
        List<String> second = db.getAllSecondFromTown(town.getName());

        Player newMayor;

        if (!second.isEmpty()) {
            newMayor = Bukkit.getPlayer(second.getFirst());
        } else { //if (second.isEmpty())
            // set a random of the town new major
            newMayor = allPlayers.getFirst();
        }
        // set all permission to true to the new major
        db.setPermissionOfAPlayerToTrue(newMayor, "mayor");
        playerProfiles.get(newMayor.getUniqueId()).setPermissionsTrue();

        // inform all player about the change
        for (Player player : allPlayers) {
            player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Le joueur " + newMayor.getName() + " est désormait le maire de votre ville.");
        }
    }

}
