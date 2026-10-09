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

    public static PlayerProfile CreatePlayerProfile(Player p, Database db) {
        PlayerProfile pP = new PlayerProfile(p.getUniqueId());
        List<Job> playerJobs = db.getPlayerJobs(p);

        // if the player didn't have jobs
        if (playerJobs == null || playerJobs.isEmpty()) {
            return pP;
        }

        for (Job job : playerJobs) {
            JobProgression progression = db.getJobProgression(p, job.getId());
            if (job.isPrimary()) {
                pP.setPrimaryJob(job);
            } else {
                pP.setSecondaryJob(job);
            }
            // defin in the JobProgression if the job is a main job
            progression.setMain(job.isPrimary());
            pP.addProgression(progression);
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
        PlayerProfile pProfile = plugin.playerProfiles.get(p.getUniqueId());
        if (pProfile.hasMainJobs()) {
            db.setProgress(p, pProfile.getProgression(pProfile.getPrimaryJob().getId()));
        }
        if (pProfile.hasSecondJobs()) {
            db.setProgress(p, pProfile.getProgression(pProfile.getSecondaryJob().getId()));
        }
    }
}
