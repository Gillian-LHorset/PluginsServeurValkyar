package fr.Valkyar.databaseCommunicationAPI.Models;

public class JobProgression {
    private final Job job;
    private final int jobId;
    private double experience;
    private int level;
    private boolean isMain;
    private String specialisation;

    public JobProgression(int jobId, double experience, int level, Job job, String specialisation) {
        this.jobId = jobId;
        this.experience = experience;
        this.level = level;
        this.job = job;
        this.specialisation = specialisation;
    }

    public int getJobId() {return jobId;}
    public Double getExperience() { return experience; }
    public Integer getLevel() { return level; }
    public Job getJob() { return job; }
    public boolean isMain() { return isMain; }
    public String getSpecialisation() { return specialisation; }

    public void setMain(boolean isMain) { this.isMain = isMain; }
    public void setExperience(double experience) { this.experience = experience; }
    public void setLevel(int level) { this.level = level; }
    public void setSpecialisation(String specialisation) { this.specialisation = specialisation; }

    public boolean hasSpecialisation() {
        return specialisation != null;
    }

    public void increaseExperience(double addExp) {this.experience += addExp;}
    public void increaseLevel() {
        this.experience = 0;
        this.level++;
    }
}
