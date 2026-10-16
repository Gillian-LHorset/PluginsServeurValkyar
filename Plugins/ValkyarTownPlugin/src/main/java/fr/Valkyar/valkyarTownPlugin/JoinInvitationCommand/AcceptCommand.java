package fr.Valkyar.valkyarTownPlugin.JoinInvitationCommand;

import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

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
            PlayerProfile pp = playerProfiles.get(p.getUniqueId());
            if (pp.hasTown()) {
                p.sendMessage(ChatColor.RED + "Vous ne pouvez pas accepter d'invitation, car vous appartenez déjà à une ville.");
                return true;
            }

            // if an invitation was sended
            if (!_plugin.sentInvitations.containsKey(p.getUniqueId())) {
                p.sendMessage(ChatColor.RED + "Vous n'avez pas d'invitation en cours.");
                return true;
            }

            // the key of sentInvitations is the UUID of the invited
                // the value is the player who invite
            Player pSender = Bukkit.getPlayer(_plugin.sentInvitations.get(p.getUniqueId()));

            if (pSender == null) {
                p.sendMessage(ChatColor.RED + "Le joueur qui vous a envoyer l'invitation n'existe pas.");
                return true;
            }

            PlayerProfile profileSender = playerProfiles.get(pSender.getUniqueId());

            Town town = profileSender.getTown();

            pp.ResetPermissions();

            // add the player to the town
            db.setPlayerTown(p, town);
            town.AddPlayerTown(p);
            pp.setTown(town);

            p.sendMessage(ChatColor.GREEN + "Vous avez accepté l'invitation.");
            pSender.sendMessage(ChatColor.GREEN + p.getDisplayName() + " a accepté l'invitation.");

            for (Player player : town.getTownPlayers()) {
                player.sendMessage(ChatColor.GREEN + p.getDisplayName() + " a rejoint votre ville.");
            }

            _plugin.sentInvitations.remove(p.getUniqueId());
        }
        return true;
    }
}

