package fr.Valkyar.valkyarTownPlugin.CommandCompletion;

import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.valkyarTownPlugin.ValkyarTownPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

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
                list.add("join");
            } else {
                list.addFirst("spawn");
                list.add("leave");
                list.add("deposit");
                if (sender.hasPermission("town.claim")
                        || sender.hasPermission("town.mayor")
                        || sender.hasPermission("town.second")) {
                    list.add("claim");
                }
                if (sender.hasPermission("town.unclaim")
                        || sender.hasPermission("town.mayor")
                        || sender.hasPermission("town.second")) {
                    list.add("unclaim");
                }
                if (sender.hasPermission("town.withdraw")
                        || sender.hasPermission("town.mayor")
                        || sender.hasPermission("town.second")) {
                    list.add("withdraw");
                }
                if (sender.hasPermission("town.mayor")
                        || sender.hasPermission("town.mayor")) {
                    list.add("disband");
                    list.add("permission");
                }
                if (sender.hasPermission("town.invite")
                        || sender.hasPermission("town.mayor")
                        || sender.hasPermission("town.second")) {
                    list.add("invite");
                }
                if (sender.hasPermission("town.kick")
                        || sender.hasPermission("town.mayor")
                        || sender.hasPermission("town.second")) {
                    list.add("kick");
                }
            }
            list.add("balance");
            return list;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("permission") && sender.hasPermission("town.mayor")) {
                list.add("add");
                list.add("remove");
            } else if (args[0].equalsIgnoreCase("balance")) {
                return db.getAllTownsNames();
            } else if (args[0].equalsIgnoreCase("invite") && sender.hasPermission("town.invite")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!p.getName().equalsIgnoreCase(sender.getName())) {
                        list.add(p.getName());
                    }
                }
            } else if (args[0].equalsIgnoreCase("kick") &&
                    (sender.hasPermission("town.kick")
                    || sender.hasPermission("town.mayor")
                    || sender.hasPermission("town.second"))){
                return db.getAllPlayerOfTownByName(db.getPlayerTown((Player) sender));
            } else if (args[0].equalsIgnoreCase("spawn")
                    && (sender.hasPermission("town.mayor") || sender.hasPermission("town.second"))) {
                list.add("set");
            }
            return list;
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("permission") && sender.hasPermission("town.mayor")) {
                for (Permission perm : _plugin.getDescription().getPermissions()) {
                    if (!perm.getName().equalsIgnoreCase("town.mayor")) {
                        list.add(perm.getName().substring(5));
                    }
                }
            }
            return list;
        }
        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("permission") && sender.hasPermission("town.mayor")) {
               for (String pName : db.getAllPlayerOfTownByName(db.getPlayerTown((Player)  sender))) {
                   if (!pName.equalsIgnoreCase(sender.getName())) {
                       list.add(pName);
                   }
               }
            }
            return list;
        }

        return null;
    }
}
