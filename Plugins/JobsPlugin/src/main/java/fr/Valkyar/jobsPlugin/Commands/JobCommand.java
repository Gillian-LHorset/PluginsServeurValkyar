package fr.Valkyar.jobsPlugin.Commands;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.Valkyar.jobsPlugin.JobsPlugin;

import java.sql.SQLException;
import java.util.List;

public class JobCommand implements CommandExecutor {
    private JobsPlugin _plugin;

    public JobCommand(JobsPlugin plugin) {
        this._plugin = plugin;
    }

    List<String> jobs;
    int index = 1;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                player.sendMessage(ChatColor.RED + "Utilisation de la commande :\n/<command> <argument> <argument>");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("join")) {
                if (_plugin.databaseApi.databaseLink.getPlayerJob(player) == null) {

                    jobs = _plugin.databaseApi.databaseLink.getAllJobs();
                    _plugin.databaseApi.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT, "[JobsPlugin] : Can't get jobs.");

                    for (String job : jobs) {
                        if (args[1].equalsIgnoreCase(job)) {
                            try {
                                _plugin.databaseApi.databaseLink.setPlayerJob(player, String.valueOf(index));
                            } catch (SQLException e) {
                                _plugin.databaseApi.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT, "[JobsPlugin] : Can't register job.");
                                System.err.println(e.getMessage());
                            }
                            player.sendMessage(ChatColor.BLUE + "Le métier " + job + " vous à été attribué.");
                            break;
                        }
                        index++;
                    }
                } else {
                    player.sendMessage( ChatColor.RED + "Vous avez déjà un métier.");
                }

            } else if (args.length == 1 && args[0].equalsIgnoreCase("join")) {
                player.sendMessage( ChatColor.RED + "Veuillez sélectionner un métier.");
            } else if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
                if (_plugin.databaseApi.databaseLink.getPlayerJob(player) != null) {
                    try {
                        _plugin.databaseApi.databaseLink.setPlayerJob(player, null);
                        player.sendMessage(ChatColor.GREEN + "Vous avez quitter votre métier.");
                    } catch (SQLException e) {
                        _plugin.databaseApi.OutputColoredText(DatabaseCommunicationAPI.RED_OUTPUT, "[JobsPlugin] : Can't register job.");
                    }
                } else if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
                    if (_plugin.databaseApi.databaseLink.getPlayerJob(player) == null) {
                        player.sendMessage( ChatColor.RED + "Vous n'avez pas de métier.");
                    }
                }
            }


        }



        return true;
    }
}
