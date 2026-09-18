package fr.Valkyar.jobsPlugin.Utility;

import fr.Valkyar.databaseCommunicationAPI.Job;
import fr.Valkyar.databaseCommunicationAPI.JobProgression;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.jobsPlugin.JobsPlugin;
import fr.Valkyar.jobsPlugin.PlayerProfile;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerProfileUtility {

    public static PlayerProfile CreatePlayerProfile(Player p, Database db) { //TODO : problème probablement dans cette méthode
        PlayerProfile pP = new PlayerProfile(p.getUniqueId());
        List<Job> playerJobs = db.getPlayerJobs(p);
        if (playerJobs != null && !playerJobs.isEmpty()) {
            for (Job job : playerJobs) {
                JobProgression progression = db.getJobProgression(p, job.getId());
                if (job.isPrimary()) {
                    pP.setPrimaryJob(job);
                } else {
                    pP.setSecondaryJob(job);
                }
                progression.setMain(job.isPrimary());
                pP.addProgression(progression);
            }
        } else {
            p.sendMessage(ChatColor.RED + "error");
        }
        return pP;
    }

    public static void ResetPlayerProfile(Player p, JobsPlugin plugin) {
        plugin.playerProfiles.remove(p.getUniqueId());
        PlayerProfile pP = CreatePlayerProfile(p, plugin.databaseApi.databaseLink);

        plugin.playerProfiles.put(p.getUniqueId(), pP);
    }

    public static void UpdatePlayerProfileInDB(JobsPlugin plugin, Player p) {
        Database db = plugin.databaseApi.databaseLink;
        PlayerProfile pProfile = plugin.playerProfiles.get(p.getUniqueId()); //TODO : erreur par ici
        if (pProfile.getPrimaryJob().getId() != null) {
            db.setProgress(p, pProfile.getProgression(pProfile.getPrimaryJob().getId()));
        }
        if (pProfile.getSecondaryJob() != null) {
            db.setProgress(p, pProfile.getProgression(pProfile.getSecondaryJob().getId()));
        }
    }
}
