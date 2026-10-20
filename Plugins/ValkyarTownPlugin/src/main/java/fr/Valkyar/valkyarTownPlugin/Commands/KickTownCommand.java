package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.databaseCommunicationAPI.managers.TownManager;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.TownMap;
import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class KickTownCommand {
    private Database db;
    private ValkyarTownPlugin _plugin;

    Town town;
    private List<Player> _playersInTown;

    public KickTownCommand(ValkyarTownPlugin plugin, Player p, String[] args) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        if (args.length != 2) {
            p.sendMessage(ChatColor.RED + "Erreur de syntaxe.");
            p.sendMessage(ChatColor.RED + "Syntaxe correcte :");
            p.sendMessage("/town kick <player>");
            return;
        }

        Player pKicked = Bukkit.getPlayer(args[1]);
        if (pKicked == null) {
            p.sendMessage(ChatColor.RED + "Le joueur ciblé n'existe pas.");
            return;
        }

        PlayerProfile senderProfile = playerProfiles.get(p.getUniqueId());

        if (!senderProfile.hasTown()) {
            p.sendMessage(ChatColor.RED + "Vous n'avez pas de ville.");
        }

        town = senderProfile.getTown();
        _playersInTown = town.getTownPlayers();

        if (!_playersInTown.contains(pKicked)) {
            p.sendMessage(ChatColor.RED + "Le joueur ciblé n'est pas dans votre ville.");
            return;
        }

        if (!senderProfile.hasPermission("town_kick") && !senderProfile.hasHighPermission()) {
            p.sendMessage(ChatColor.RED + "Vous n'avez pas la permission pour virer quelqu'un de la ville.");
            return;
        }

        // if the player who kick is the mayor
        if (senderProfile.hasPermission("town_mayor")) {
            KickPlayerTown(p, pKicked, town);
            return;
        }

        PlayerProfile kickedProfile = playerProfiles.get(pKicked.getUniqueId());

        if (senderProfile.hasPermission("town_second")) {
            // if the player who kick have the perm "second"
            if (!senderProfile.hasHighPermission()) {
                KickPlayerTown(p, pKicked, town);
            } else {
                p.sendMessage(ChatColor.RED + "Vous ne pouvez pas kick quelqu'un d'un rôle équivalent ou supérieur au vôtre.");
            }
            return;
        }

        if (senderProfile.hasPermission("town_kick")) { // TODO : quelqu'un qui peut kick ne peut pas se faire kick par quelqu'un qui à la meme permission
            // if the player who kick have the perm "kick"
            if (!db.HaveAPlayerAPerm(pKicked, "second") || !db.HaveAPlayerAPerm(pKicked, "mayor")) {
                KickPlayerTown(p, pKicked, town);
            } else {
                p.sendMessage(ChatColor.RED + "Vous ne pouvez pas kick quelqu'un qui a un rôle supérieur au vôtre.");
            }
            return;
        }
    }


    private void KickPlayerTown(Player p, Player pKicked, Town town) {
        if (p.getUniqueId().toString().equals(pKicked.getUniqueId().toString())) {
            p.sendMessage(ChatColor.RED + "Vous ne pouvez pas vous expulser vous même !");
            return;
        }

        TownManager.LeaveTown(db, pKicked, town);
        pKicked.sendMessage(ChatColor.RED + "Vous avez été explusé de votre ville par " + p.getName() + ".");

        for (Player player : town.getTownPlayers()) {
            player.sendMessage(ChatColor.RED + "Le joueur " + pKicked.getName() + " à été expulsé de la ville par " + p.getName() + ".");
        }

    }
}
