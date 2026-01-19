package fr.Valkyar.valkyarTownPlugin.CommandCompletion;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TownCommandCompletion implements TabCompleter {

    private Database db;
    private ValkyarTownPlugin _plugin;
    public TownCommandCompletion(ValkyarTownPlugin  plugin) {
        _plugin = plugin;
        db = plugin.databaseApi.databaseLink;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {



        List<String> list = new ArrayList<String>();

        if (args.length == 1) {
            if (db.getPlayerTown((Player) sender) == null) {
                list.add("create");
            } else {
                list.add("leave");
                list.add("balance");
                list.add("deposit");
                if (sender.hasPermission("town.claim")) {
                    list.add("claim");
                }
                if (sender.hasPermission("town.unclaim")) {
                    list.add("unclaim");
                }
                if (sender.hasPermission("town.withdraw")) {
                    list.add("withdraw");
                }
                if (sender.hasPermission("town.mayor")) {
                    list.add("disband");
                }
            }
            return list;
        }

        return null;
    }
}
