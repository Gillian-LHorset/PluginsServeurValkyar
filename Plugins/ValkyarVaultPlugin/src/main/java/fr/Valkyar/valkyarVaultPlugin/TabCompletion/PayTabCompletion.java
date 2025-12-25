package fr.Valkyar.valkyarVaultPlugin.TabCompletion;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PayTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        // add the argument "list" to the tab completer in game
        if (args.length == 1) {

            List<String> players = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                players.add(player.getDisplayName());
            }
            return players;
        } else if (args.length == 2) {
            List<String> arguments = new ArrayList<>();
            arguments.add("<montant>");

            return arguments;
        }
        return null;
    }
}
