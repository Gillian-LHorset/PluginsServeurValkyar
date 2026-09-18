package fr.Valkyar.valkyarTownPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ChatCommand implements CommandExecutor {

    private ValkyarTownPlugin _plugin;
    private Database db;

    public ChatCommand(ValkyarTownPlugin plugin) {
        _plugin = plugin;
        db = plugin.databaseApi.databaseLink;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player p) {
            if (db.getPlayerTown(p) != null) {
                if (args.length == 0) {
                    p.sendMessage(ChatColor.RED + "Veuillez spécifier un chat dans lequel vous voulez parler.");
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("globale")) {
                        if (_plugin.playerChat.get(p.getUniqueId()).equalsIgnoreCase("globale")) {
                            p.sendMessage(ChatColor.RED + "Vous parlez déjà dans le chat globale.");
                        } else {
                            _plugin.playerChat.replace(p.getUniqueId(), "ville", "globale");
                            p.sendMessage(ChatColor.GREEN + "Vous parlez désormais dans le chat globale.");
                        }
                    } else if (args[0].equalsIgnoreCase("ville")) {
                        if (_plugin.playerChat.get(p.getUniqueId()).equalsIgnoreCase("ville")) {
                            p.sendMessage(ChatColor.RED + "Vous parlez déjà dans le chat de ville.");
                        } else {
                            _plugin.playerChat.replace(p.getUniqueId(), "globale", "ville");
                            p.sendMessage(ChatColor.GREEN + "Vous parlez désormais dans le chat de ville.");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Le chat recherché est introuvable.");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Erreur de syntaxe.\nUtilisation correcte : /chat <argument>");
                }
            } else {
                p.sendMessage(ChatColor.RED + "Vous n'avez pas de ville.");
            }
        }

        return true;
    }
}
