package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class ChatCommand implements CommandExecutor {

    private ValkyarTownPlugin _plugin;
    private Database db;

    public ChatCommand(ValkyarTownPlugin plugin) {
        _plugin = plugin;
        db = plugin.databaseApi.databaseLink;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        // Commande permet d'intervertir pour parler ou dans le chat global ou dans le chat de ville
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        PlayerProfile profile = playerProfiles.get(p.getUniqueId());
        if (!profile.hasTown()) {
            p.sendMessage(ChatColor.RED + "Vous n'avez pas de ville.");
            return  true;
        }

        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "Veuillez spécifier un chat dans lequel vous voulez parler.");
            return true;
        }

        if (args.length != 1) {
            p.sendMessage(ChatColor.RED + "Erreur de syntaxe.\nUtilisation correcte : /chat <argument>");
            return  true;
        }

        if (!args[0].equalsIgnoreCase("global") && !args[0].equalsIgnoreCase("ville")) {
            p.sendMessage(ChatColor.RED + "Le chat recherché est introuvable.");
            return  true;
        }

        if (args[0].equalsIgnoreCase("global")) {
            if (_plugin.playerChat.get(p.getUniqueId()).equalsIgnoreCase("global")) {
                p.sendMessage(ChatColor.RED + "Vous parlez déjà dans le chat global.");
            } else {
                _plugin.playerChat.replace(p.getUniqueId(), "ville", "global");
                p.sendMessage(ChatColor.GREEN + "Vous parlez désormais dans le chat global.");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("ville")) {
            if (_plugin.playerChat.get(p.getUniqueId()).equalsIgnoreCase("ville")) {
                p.sendMessage(ChatColor.RED + "Vous parlez déjà dans le chat de ville.");
            } else {
                _plugin.playerChat.replace(p.getUniqueId(), "global", "ville");
                p.sendMessage(ChatColor.GREEN + "Vous parlez désormais dans le chat de ville.");
            }
            return true;
        }




        return true;
    }
}
