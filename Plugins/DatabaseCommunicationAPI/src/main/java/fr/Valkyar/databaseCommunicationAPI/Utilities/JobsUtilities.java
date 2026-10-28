package fr.Valkyar.databaseCommunicationAPI.Utilities;

import fr.Valkyar.databaseCommunicationAPI.Models.Job;
import fr.Valkyar.databaseCommunicationAPI.db.Database;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobsUtilities {
    private static final Map<String, Job> loadedJobs = new HashMap<>();

    public static Map<String, Job> setupJobsList(Database db) {
        loadedJobs.clear();
        loadJobsFromDatabase(db);
        setJobsSpecialisations(db);
        return  loadedJobs;
    }

    public static void loadJobsFromDatabase(Database db) { // init in JobsPlugin
        loadedJobs.clear();

            for (Job job : db.getAllJobsInfos()) {
                loadedJobs.put(job.getDisplayName(), job);
                System.out.print(job.getDisplayName() + " ");
            }
            System.out.println(loadedJobs.size() + " métiers chargés depuis la DB.");
    }

    public static List<Database.specialisation> loadJobsSpecialisations(Database db) {
        return db.getSpecialisations();
    }

    private static void setJobsSpecialisations(Database db) {
        for (Database.specialisation spe : loadJobsSpecialisations(db)) {
            for (Job job : loadedJobs.values()) {
                if (spe.jobId().equals(job.getId())) {
                    job.addSpecialisation(spe.name());
                }
            }
        }
    }

    public static Map<String, Job> getAllJobs() {
        return loadedJobs;
    }
    public static Job getJob(String jobName) {
        return loadedJobs.get(jobName);
    }
    public boolean exists(String jobName) {
        return loadedJobs.containsKey(jobName);
    }
}
