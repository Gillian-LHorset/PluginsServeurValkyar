package fr.Valkyar.valkyarTownPlugin.CommandCompletion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TownCommandCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {



        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            list.add("create");
            list.add("leave");
            list.add("disband");
            list.add("claim");
            return list;
        }

        return null;
    }
}
