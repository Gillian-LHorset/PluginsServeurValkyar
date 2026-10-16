package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class InviteCommand {

    public InviteCommand(ValkyarTownPlugin plugin, Player p, String[] args) {

        Database db = plugin.databaseApi.databaseLink;
        if (args.length != 2) {
            p.sendMessage(ChatColor.RED + "Erreur de syntaxe.\nSyntaxe correcte : /town invite <player>");
            return;
        }

        PlayerProfile pp = playerProfiles.get(p.getUniqueId());

        if (!pp.hasTown()) {
            p.sendMessage(ChatColor.RED + "Vous n'avez pas de ville.");
            return;
        }

        Town town = pp.getTown();

        Player pInvited = Bukkit.getPlayer(args[1]);
        if (pInvited == null) {
            p.sendMessage(ChatColor.RED + "Le joueur ciblé n'existe pas.");
            return;
        }

        if (!pInvited.isOnline()) {
            p.sendMessage(ChatColor.RED + "Le joueur ciblé n'est pas en ligne.");
            return;
        }

        if (playerProfiles.get(pInvited.getUniqueId()).hasTown()) {
            p.sendMessage(ChatColor.RED + "Le joueur ciblé possède déjà une ville.");
            return;
        }

        if (plugin.sentInvitations.get(pInvited.getUniqueId()) != null) {
            p.sendMessage(ChatColor.RED + "Le joueur ciblé a déjà une invitation en cours.");
            return;
        }

        p.sendMessage(ChatColor.GREEN + "L'invitation à été envoyer à " + pInvited.getDisplayName() + ".");
        p.sendMessage(ChatColor.GREEN + "L'invitation durera 2 minutes");

        // inform the player to the invitation
        pInvited.sendMessage(ChatColor.BLUE + p.getDisplayName() + " vous a invité à rejoindre la ville de " + town.getName() + " .");
        pInvited.sendMessage(ChatColor.BLUE + "L'invitation durera 2 minutes");
        pInvited.sendMessage(ChatColor.BLUE + "Utilisez la commande " + ChatColor.GREEN + "[/accept]" + ChatColor.BLUE + " pour rejoindre la ville.");
        pInvited.sendMessage(ChatColor.BLUE + "Utilisez la commande " + ChatColor.RED + "[/refuse]" + ChatColor.BLUE + " pour refuser l'invitation.");

        // put the UUID and the town name in the hash map for save that
        plugin.sentInvitations.put(pInvited.getUniqueId(), p.getUniqueId());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (plugin.sentInvitations.containsKey(pInvited.getUniqueId())) {
                    plugin.sentInvitations.remove(pInvited.getUniqueId());
                    pInvited.sendMessage(ChatColor.RED + "L'invitation de " + town.getName() + " a expiré.");
                    if (p.isOnline()) {
                        p.sendMessage(ChatColor.RED + "Votre invitation à " + pInvited.getName() + " a expiré.");
                    }
                }
            }
        }.runTaskLater(plugin, 20L * 120);

    }
}
