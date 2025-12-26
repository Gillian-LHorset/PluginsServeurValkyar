package fr.Valkyar.jobsPlugin.TabCompletion;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
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
    public List <String> jobsList;
    public JobCommandCompletion(JobsPlugin plugin) {
        this._plugin = plugin;
        jobsList = _plugin.databaseApi.databaseLink.getAllJobs();
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        // add the argument "list" to the tab completer in game

        List<String> arguments = new ArrayList<>();
        if (args.length == 1) {
            arguments.add("join");
            arguments.add("leave");

            return arguments;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("join")) {
            return jobsList;
        }
        return null;
    }
}
