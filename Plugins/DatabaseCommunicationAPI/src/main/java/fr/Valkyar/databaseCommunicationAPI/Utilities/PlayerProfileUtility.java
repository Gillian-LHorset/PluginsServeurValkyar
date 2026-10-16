package fr.Valkyar.databaseCommunicationAPI.Utilities;

import fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI;
import fr.Valkyar.databaseCommunicationAPI.Models.Job;
import fr.Valkyar.databaseCommunicationAPI.Models.JobProgression;
import fr.Valkyar.databaseCommunicationAPI.Models.Town;
import fr.Valkyar.databaseCommunicationAPI.db.Database;
import fr.Valkyar.databaseCommunicationAPI.Models.PlayerProfile;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static fr.Valkyar.databaseCommunicationAPI.DatabaseCommunicationAPI.*;

public class PlayerProfileUtility {

    public static PlayerProfile CreatePlayerProfile(Player p, Database db) {
        PlayerProfile pP = db.getBasePlayerProfile(p);
        List<Job> playerJobs = db.getPlayerJobs(p);

        Town town = db.getPlayerTown(p);
        if (town != null) {
            pP.setTown(town);
        }

        // if the player didn't have jobs
        if (playerJobs != null || !playerJobs.isEmpty()) {
            for (Job job : playerJobs) {
                JobProgression progression = db.getJobProgression(p, job);
                if (job.isPrimary()) {
                    pP.setPrimaryJob(job);
                } else {
                    pP.setSecondaryJob(job);
                }
                // defin in the JobProgression if the job is a main job
                progression.setMain(job.isPrimary());
                pP.addProgression(progression);
            }
        }

        if (db.getAllPermissionOfAPlayer(p) == null) {
            db.registerPermissionPlayer(p);
        }

        pP.setPermissions(db.getAllPermissionOfAPlayer(p));

        return pP;
    }

    public static void ResetPlayerProfile(Player p, Database db, HashMap<UUID, PlayerProfile> playerProfiles) {
        playerProfiles.remove(p.getUniqueId());
        PlayerProfile pP = CreatePlayerProfile(p, db);

        playerProfiles.put(p.getUniqueId(), pP);
    }

    // TODO : completer
    public static void UpdatePlayerProfileInDB(Player p, Database db, HashMap<UUID, PlayerProfile> playerProfiles) {
        PlayerProfile pProfile = playerProfiles.get(p.getUniqueId());
        if (pProfile.hasMainJobs()) {
            db.setProgress(p, pProfile.getProgression(pProfile.getPrimaryJob().getId()));
            db.SetPlayerJobLvl(p, pProfile.getPrimaryJob().getId(), pProfile.getProgression(pProfile.getPrimaryJob().getId()).getLevel());
        }
        if (pProfile.hasSecondJobs()) {
            db.setProgress(p, pProfile.getProgression(pProfile.getSecondaryJob().getId()));
            db.SetPlayerJobLvl(p, pProfile.getSecondaryJob().getId(), pProfile.getProgression(pProfile.getSecondaryJob().getId()).getLevel());
        }
        // TODO : sauvegarder les permissions en db
        db.setPlayerBalance(p, pProfile.getCurrentMoney());
    }

    public static void RegisterPlayerProfile(Player p, DatabaseCommunicationAPI plugin) {

        PlayerProfile playerProfile = CreatePlayerProfile(p, plugin.databaseLink);

        if (playerProfiles == null) {
            OutputColoredText(RED_OUTPUT, "Erreur création profil");
        };

        plugin.playerProfiles.put(p.getUniqueId(), playerProfile);
    }
}
