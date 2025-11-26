package fr.Valkyar.moderatorPlugin.Commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class VanishTabCompletion implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        // add the argument "list" to the tab completer in game
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();
            arguments.add("list");

            return arguments;
        }
        return null;
    }
}
