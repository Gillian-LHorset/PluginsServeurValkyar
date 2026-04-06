package fr.Valkyar.valkyarTownPlugin.JoinInvitationCommand;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AcceptCommand implements CommandExecutor {

    private ValkyarTownPlugin _plugin;
    private Database db;

    public AcceptCommand(ValkyarTownPlugin plugin) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player p) {
            if (db.getPlayerTown(p) == null) {
                // if an invitation was sended
                if (_plugin.sendedInvitations.containsKey(p.getUniqueId())) {
                    UUID pUUID =  p.getUniqueId();
                    Player pSender = Bukkit.getPlayer(_plugin.sendedInvitations.get(pUUID));

                    String town = db.getPlayerTown(pSender);

                    // add the player to the town
                    db.setPlayerTownByName(p, town);
                    p.sendMessage(ChatColor.GREEN + "Vous avez accepté l'invitation.");
                    pSender.sendMessage(ChatColor.GREEN + "" + p.getDisplayName() + " a accepté l'invitation.");

                    for (String player : db.getAllPlayerOfTownByName(town)) {
                        Bukkit.getPlayer(player).sendMessage(ChatColor.GREEN + "" + p.getDisplayName() + " a rejoint votre ville.");
                    }

                    _plugin.sendedInvitations.remove(pUUID);
                } else {
                    p.sendMessage(ChatColor.RED + "Vous n'avez pas d'invitation en cours.");
                }
            } else {
                p.sendMessage(ChatColor.RED + "Vous ne pouvez pas accepter cette invitation, car vous appartenez déjà à une ville.");
            }

            return true;
        }
        return false;
    }
}
