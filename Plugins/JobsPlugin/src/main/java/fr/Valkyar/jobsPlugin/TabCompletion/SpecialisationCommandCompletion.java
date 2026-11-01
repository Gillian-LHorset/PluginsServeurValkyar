package fr.Valkyar.jobsPlugin.TabCompletion;

import fr.Valkyar.databaseCommunicationAPI.Models.Job;
import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.playerProfiles;
import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.specialisationsList;

public class SpecialisationCommandCompletion implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> tabList = new ArrayList<>();
        if (sender instanceof Player p) {
            PlayerProfile playerProfile = playerProfiles.get(p.getUniqueId());
            Job pJob = playerProfile.getPrimaryJob();

            if (args.length == 1) {
                tabList.add("choisir");
            }

            if (args.length == 2) {
                specialisationsList.stream()
                        .filter(spe -> spe.jobId().equals(pJob.getId()))
                        .map(spe -> spe.name().toLowerCase())
                        .forEach(spe -> tabList.add(spe));
            }

            return tabList;
        }
        return null;
    }
}
