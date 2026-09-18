package fr.Valkyar.jobsPlugin.TabCompletion;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JobCommandCompletion implements TabCompleter {

    private JobsPlugin _plugin;
    private Database db;


    public JobCommandCompletion(JobsPlugin plugin) {
        _plugin = plugin;
        db = _plugin.databaseApi.databaseLink;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        // add the argument "list" to the tab completer in game
        List <String> jobsList = new ArrayList<>();

        List<String> arguments = new ArrayList<>();
        if (args.length == 1) {
            arguments.add("join");
            arguments.add("leave");

            return arguments;
        } else if (args[0].equalsIgnoreCase("join")) {
            if (args.length == 2) {
                arguments.add("principal");
                arguments.add("secondaire");
                return arguments;
            } else if (args.length == 3) {
                if (args[1].equalsIgnoreCase("principal")) {
                    _plugin.jobsList.values().forEach(job -> {
                        if (job.isPrimary()) {
                            jobsList.add(job.getDisplayName());
                        }
                    });
                    return jobsList;

                } else if (args[1].equalsIgnoreCase("secondaire")) {
                    _plugin.jobsList.values().forEach(job -> {
                        if (!job.isPrimary()) {
                            jobsList.add(job.getDisplayName());
                        }
                    });
                    return jobsList;

                }
            }
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (args.length == 2) {
                arguments.add("principal");
                arguments.add("secondaire");
                return arguments;
            }
        }



        return null;
    }
}
