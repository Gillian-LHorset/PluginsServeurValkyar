package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

import static fr.Valkyar.valkyarTownPlugin.Utility.AssignPerms.assignPerms;

public class KickTownCommand {
    private Database db;
    private ValkyarTownPlugin _plugin;

    private List<String> _playersInTown;

    public KickTownCommand(ValkyarTownPlugin plugin, Player p, String[] args) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;

        if (args.length == 2) {
            Player pKicked = Bukkit.getPlayer(args[1]);
            _playersInTown = db.getAllPlayerOfTown(db.getPlayerTown(p));
            if (_playersInTown.contains(args[1]) && pKicked != null) {
                // if the player who kick is the mayor
                if (p.hasPermission("town.mayor")) {
                    KickPlayerTown(p, pKicked);
                } else if (p.hasPermission("town.second")) {
                    // if the player who kick have the perm "second"
                    if (!db.HaveAPlayerAPerm(pKicked, "second") || !db.HaveAPlayerAPerm(pKicked, "mayor") ) {
                        KickPlayerTown(p, pKicked);
                    } else {
                        p.sendMessage(ChatColor.RED + "Vous ne pouvez pas kick quelqu'un d'un rôle équivalent ou supérieur au vôtre.");
                    }
                } else if (p.hasPermission("town.kick")) {
                    // if the player who kick have the perm "kick"
                    if (!db.HaveAPlayerAPerm(pKicked, "second") || !db.HaveAPlayerAPerm(pKicked, "mayor") ) {
                        KickPlayerTown(p,  pKicked);
                    } else {
                        p.sendMessage(ChatColor.RED + "Vous ne pouvez pas kick quelqu'un qui a un rôle supérieur au vôtre.");
                    }
                }
            } else {
                p.sendMessage(ChatColor.RED + "Le joueur ciblé n'a pas été trouvé.");
            }
        } else {
            p.sendMessage(ChatColor.RED + "Erreur de syntaxe.");
            p.sendMessage(ChatColor.RED + "Syntaxe correcte :");
            p.sendMessage("/town kick <player>");
        }

    }

    private void KickPlayerTown(Player p, Player pKicked) {
        if (!p.getUniqueId().toString().equals(pKicked.getUniqueId().toString())) {
            db.KickPlayerOfTown(pKicked);
            for (String player : db.getAllPlayerOfTown(db.getPlayerTown(p))) {
                Bukkit.getPlayer(player).sendMessage(ChatColor.RED + "Le joueur " + pKicked.getName() + " à été expulsé de la ville par " + p.getName() + ".");
            }
            pKicked.sendMessage(ChatColor.RED + "Vous avez été explusé de votre ville par " + p.getName() + ".");
            assignPerms(pKicked, db, _plugin);
        } else {
            p.sendMessage(ChatColor.RED + "Vous ne pouvez pas vous expulser vous même !");
        }
    }
}
