package fr.Valkyar.valkyarTownPlugin.CommandCompletion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ChatCommandCompletion implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("ville");
            completions.add("global");
        }

        return completions;
    }
}
