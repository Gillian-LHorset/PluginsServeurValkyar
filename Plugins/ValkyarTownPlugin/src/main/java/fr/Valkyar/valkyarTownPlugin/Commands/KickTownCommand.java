package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.Utility.ChunkSystem;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.SQLException;
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
            _playersInTown = db.getAllPlayerOfTownByName(db.getPlayerTown(p));
            if (_playersInTown.contains(args[1])) {
                if (!pKicked.hasPermission("town.mayor")) {
                    // when the player is kick, all perms are deleted
                    db.KickPlayerOfTown(pKicked);
                    for (String player : db.getAllPlayerOfTownByName(db.getPlayerTown(p))) {
                        Bukkit.getPlayer(player).sendMessage(ChatColor.RED + "Le joueur " + pKicked.getName() + " à été expulsé de la ville.");
                    }
                    pKicked.sendMessage(ChatColor.RED + "Vous avez été explusé de votre ville.");
                    assignPerms(pKicked, db, _plugin);
                } else {
                    p.sendMessage(ChatColor.RED + "Vous ne pouvez pas expulser le chef de la ville.");
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
}
