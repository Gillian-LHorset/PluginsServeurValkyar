package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class GivePermissionCommand {
    private Database db;
    private ValkyarTownPlugin _plugin;
    private PlayerProfile pp;

    private final List<String> _assignablePermList = Arrays.asList("second", "claim", "unclaim", "withdraw", "invite", "kick");

    public GivePermissionCommand(ValkyarTownPlugin plugin, Player p, String[] args) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        pp = playerProfiles.get(p.getUniqueId());

        if (args.length != 4) {
            p.sendMessage(ChatColor.RED + "Erreur de syntaxe, utilisation correcte de la commande :\n/town permission <argument> <permission> <joueur>");
        }
        Player pTarget = Bukkit.getPlayerExact(args[3]);
        if (pTarget == null) {
            p.sendMessage(ChatColor.RED + "Ce joueur n'existe pas.");
            return;
        }

        if (!args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("remove")) {
            p.sendMessage(ChatColor.RED + "Erreur dans l'argument de la commande.");
            return;
        }

        // add a permission to a player logic
        if (args[1].equalsIgnoreCase("add")) {
            AddPermission(p, args, pTarget);
            return;
        }
        if (args[1].equalsIgnoreCase("remove")) {
            RemovePermission(p, args, pTarget);
            return;
        }

    }


    private void AddPermission(Player p, String[] args, Player pTarget) {
        String permToGive = args[2].toLowerCase();

        // Check if the target is online
        if (!Bukkit.getOnlinePlayers().contains(pTarget)) {
            p.sendMessage(ChatColor.RED + "Le joueur ciblé n'est pas en ligne.");
            return;
        }

        // check if the permission to give is the mayor permission
        if (!permToGive.equalsIgnoreCase("mayor")) {

            // Check if the permission exist
            if (!_assignablePermList.contains(permToGive)) {
                p.sendMessage(ChatColor.RED + "Cette permission n'existe pas ou n'est pas assignable.");
                return;
            }

            // Check if the target is in the same town as the sender
            if (!pp.getTown().getTownPlayers().contains(pTarget)) {
                p.sendMessage(ChatColor.RED + "Le joueur ciblé n'est pas dans votre ville.");
                return;
            }

            // check if the player targeted already have the perm
            if (playerProfiles.get(pTarget.getUniqueId()).hasPermission("town_" + permToGive)) {
                p.sendMessage(ChatColor.RED + "Le joueur ciblé possède déjà cette permission.");
                return;
            }

            // set the permission to true
            playerProfiles.get(pTarget.getUniqueId()).addPermission(permToGive);
            pTarget.sendMessage(ChatColor.GREEN + "Vous avez désormais la permission ''" + permToGive + "''");
            p.sendMessage(ChatColor.GREEN + "Permission accordée.");


        } else {
            // set a new mayor
            if (pTarget.getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString())) {
                p.sendMessage(ChatColor.RED + "Vous ne pouvez pas vous assigner cette permission.");
                return;
            }

            // Check if the target is in the same town as the sender
            if (pp.getTown().getTownPlayers().contains(pTarget)) {
                p.sendMessage(ChatColor.RED + "Le joueur ciblé n'est pas dans votre ville.");
                return;
            }

            if (!_plugin.newMayor.containsKey(p.getUniqueId())) {
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Êtes vous sûr de vouloir abdiquer de votre rôle de chef de la ville au profit de " + pTarget.getName() + " ?" +
                        "\nRéexécutez la commande dans la minute pour confirmer.");
                _plugin.newMayor.put(p.getUniqueId(), null);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (_plugin.newMayor.containsKey(p.getUniqueId())) {
                            _plugin.newMayor.remove(p.getUniqueId());
                            p.sendMessage(ChatColor.RED + "La confirmation de la commande a expiré.");

                        }
                    }
                }.runTaskLater(_plugin, 20L * 60);

            } else { // confirme case
                _plugin.newMayor.remove(p.getUniqueId());
                // set the permission to true in the db
                playerProfiles.get(pTarget.getUniqueId()).setPermissionsTrue();
                playerProfiles.get(p.getUniqueId()).ResetPermissions();

                pTarget.sendMessage(ChatColor.GREEN + "Vous êtes désormais chef de la ville.");
                p.sendMessage(ChatColor.GREEN + "Vous avez abdiquer avec succès.");
                for (String pTown : db.getAllPlayerOfTown(db.getPlayerTown(pTarget))) {
                    Bukkit.getPlayer(pTown).sendMessage(ChatColor.GREEN + p.getName() + " viens d'abdiquer de son rôle de chef au profit de " + pTarget.getName() + ".");
                }
            }
        }
    }

    private void RemovePermission(Player p, String[] args, Player pTarget) {
        //logic to remove a permission
        String permToRemove = args[2];
        // check if the permission is the mayor permission
        if (permToRemove.equalsIgnoreCase("mayor")) {
            p.sendMessage(ChatColor.RED + "Vous ne pouvez pas retirer cette permission.");
            return;
        }

        // Check if the permission exist
        if (!_assignablePermList.contains(permToRemove)) {
            p.sendMessage(ChatColor.RED + "Cette permission n'existe pas.");
            return;
        }
        // Check if the target is online
        if (!Bukkit.getOnlinePlayers().contains(pTarget)) {
            p.sendMessage(ChatColor.RED + "Le joueur ciblé n'est pas en ligne.");
            return;
        }

        // Check if the target is in the same town as the sender
        if (!playerProfiles.get(p.getUniqueId()).getTown().getTownPlayers().contains(pTarget)) {
            p.sendMessage(ChatColor.RED + "Le joueur ciblé n'est pas dans votre ville.");
            return;
        }
        // check if the player targeted already have the perm
        if (!playerProfiles.get(pTarget.getUniqueId()).hasPermission(permToRemove)) {
            p.sendMessage(ChatColor.RED + "Le joueur ciblé ne possède pas cette permission.");
            return;
        }
        playerProfiles.get(pTarget.getUniqueId()).removePermission(permToRemove);

        pTarget.sendMessage(ChatColor.RED + "Vous n'avez désormais plus la permission ''" + permToRemove + "''");
        p.sendMessage(ChatColor.GREEN + "La permission à correctement été retirer.");

    }
}
