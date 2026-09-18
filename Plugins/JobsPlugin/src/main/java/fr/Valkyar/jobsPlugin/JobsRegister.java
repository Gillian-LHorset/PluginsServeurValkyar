package fr.Valkyar.jobsPlugin;

import fr.Valkyar.databaseCommunicationAPI.Job;
import fr.Valkyar.databaseCommunicationAPI.db.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobsRegister {
    private final Map<String, Job> loadedJobs = new HashMap<>();

    public void loadJobsFromDatabase(Database db) {
        loadedJobs.clear();

            for (Job job : db.getAllJobsInfos()) {
                loadedJobs.put(job.getDisplayName(), job);
                System.out.println(job.getDisplayName());
            }
            System.out.println(loadedJobs.size() + " métiers chargés depuis la DB.");
    }

    public Map<String, Job> getAllJobs() {
        return loadedJobs;
    }

    public Job getJob(String jobName) {
        return loadedJobs.get(jobName);
    }

    public boolean exists(String jobName) {
        return loadedJobs.containsKey(jobName);
    }
}
