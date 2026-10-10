package fr.Valkyar.jobsPlugin;
import fr.Valkyar.databaseCommunicationAPI.Job;
import fr.Valkyar.databaseCommunicationAPI.JobProgression;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerProfile {
    private UUID playerUuid;
    private Job primaryJob;
    private Job secondaryJob;
    private Double currentMoney;

    public PlayerProfile(UUID playerUuid, Double CurrentMoney) {
        this.playerUuid = playerUuid;
        currentMoney = CurrentMoney;
    }

    // Integer is the job id
    private final Map<Integer, JobProgression> progressions = new HashMap<>();

    public boolean hasJob(Job job) {
        return (primaryJob.getId().equals(job.getId()))
                || (secondaryJob.getId().equals(job.getId()));
    }
    public boolean hasJob(int jobId) {
        return this.progressions.containsKey(jobId);
    }

    public Integer hasJobInList(List<Job> job) {
        for (Job j : job) {
            if (primaryJob == j || secondaryJob == j) {
                return j.getId();
            }
        }
        return null;
    }

    public boolean hasAnyJobs() {
        return (primaryJob != null || secondaryJob != null);
    }
    public boolean hasMainJobs() {
        return (primaryJob != null);
    }
    public boolean hasSecondJobs() {
        return (secondaryJob != null);
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }
    public Job getPrimaryJob() {
        return primaryJob;
    }
    public Job getSecondaryJob() {
        return secondaryJob;
    }
    public Double getCurrentMoney() {return currentMoney;}


    public JobProgression getProgression(Integer jobId) {
        if (progressions.containsKey(jobId)) {
            return progressions.get(jobId);
        }
        return null;
    }

    public void setPrimaryJob(Job primaryJob) {
        this.primaryJob = primaryJob;
    }
    public void setSecondaryJob(Job secondaryJob) {
        this.secondaryJob = secondaryJob;
    }

    public void increaseExp(Integer jobId, Double amount) {
        if (progressions.get(jobId) == null) {
            System.out.println("erreur ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            return;
        }
        progressions.get(jobId).increaseExperience(amount);
    }

    public void addProgression(JobProgression progression) {
        this.progressions.put(progression.getJobId() ,progression);
    }

    public void increaseMoney(Double amount) {
        currentMoney += amount;
    }

    public void decreaseMoney(Double amount) {
        currentMoney -= amount;
    }
}
