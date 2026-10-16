package fr.Valkyar.valkyarTownPlugin.CommandCompletion;

import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.Models.Town;
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

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.TownMap;
import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;

public class TownCommandCompletion implements TabCompleter {

    private Database db;
    private ValkyarTownPlugin _plugin;

    public TownCommandCompletion(ValkyarTownPlugin plugin) {
        _plugin = plugin;
        db = plugin.databaseApi.databaseLink;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> list = new ArrayList<String>();

        if (!(sender instanceof Player)) {
            return null;
        }

        Player p = (Player) sender;
        PlayerProfile pp = playerProfiles.get(p.getUniqueId());

        if (args.length == 1) {
            if (!pp.hasTown()) {
                list.add("create");
                list.add("join");
            }
            if (pp.hasTown()) {
                list.addFirst("spawn");
                list.add("leave");
                list.add("deposit");

                if (pp.hasPermission("town_claim")
                        || pp.hasHighPermission()) {
                    list.add("claim");
                }

                if (pp.hasPermission("town_unclaim")
                        || pp.hasHighPermission()) {
                    list.add("unclaim");
                }

                if (pp.hasPermission("town_withdraw")
                        || pp.hasHighPermission()) {
                    list.add("withdraw");
                }

                if (pp.hasPermission("town_mayor")) {
                    list.add("disband");
                    list.add("permission");
                }

                if (pp.hasPermission("town_invite")
                        || pp.hasHighPermission()) {
                    list.add("invite");
                }

                if (pp.hasPermission("town_kick")
                        || pp.hasHighPermission()) {
                    list.add("kick");
                }
            }
            list.add("balance");
            return list;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("permission") && pp.hasPermission("town_mayor")) {
                list.add("add");
                list.add("remove");

            } else if (args[0].equalsIgnoreCase("balance")) {
                for (Town town : TownMap.values()) {
                    list.add(town.getName());
                }
                return list;

            } else if (args[0].equalsIgnoreCase("invite") && pp.hasPermission("town_invite")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString())) {
                        continue;
                    }
                    list.add(player.getName());
                }

            } else if (args[0].equalsIgnoreCase("kick") &&
                    (pp.hasPermission("town_kick")
                            || pp.hasHighPermission())) {
                for (Player player : pp.getTown().getTownPlayers()) {
                    list.add(player.getName());
                }
                return list;

            } else if (args[0].equalsIgnoreCase("spawn") && (pp.hasHighPermission())) {
                list.add("set");
            }
            return list;
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("permission") && pp.hasPermission("town_mayor")) {
                for (String perm : _plugin.databaseApi.databaseLink.getAllPermissionOfAPlayer(p).keySet()) {
                    if (!perm.equalsIgnoreCase("town_mayor")) {
                        list.add(perm.substring(5));
                    }
                }
            }
            return list;
        }

        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("permission") && pp.hasPermission("town_mayor")) {
                for (Player player : pp.getTown().getTownPlayers()) {
                    if (player.getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString())) {
                        continue;
                    }
                    list.add(player.getName());
                }
            }
            return list;
        }

        return null;
    }
}
