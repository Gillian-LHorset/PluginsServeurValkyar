package fr.Valkyar.valkyarVaultPlugin.TabCompletion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class PayTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        // add the argument "list" to the tab completer in game
        if (args.length == 2) {
            List<String> arguments = new ArrayList<>();
            arguments.add("<montant>");

            return arguments;
        }
        return null;
    }
}
