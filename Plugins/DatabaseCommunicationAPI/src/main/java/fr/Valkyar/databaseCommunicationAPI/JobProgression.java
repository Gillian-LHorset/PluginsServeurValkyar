package fr.Valkyar.databaseCommunicationAPI;

public class JobProgression {
    private final int jobId;
    private double experience;
    private int level;
    private boolean isMain;

    public JobProgression(int jobId, double experience, int level) {
        this.jobId = jobId;
        this.experience = experience;
        this.level = level;
    }

    public int getJobId() {return jobId;}
    public Double getExperience() { return experience; }
    public Integer getLevel() { return level; }
    public boolean isMain() { return isMain; }

    public void setMain(boolean isMain) { this.isMain = isMain; }
    public void setExperience(double experience) { this.experience = experience; }
    public void setLevel(int level) { this.level = level; }

    public void increaseExperience(double addExp) {this.experience += addExp;}
    public void increaseLevel() {
        this.experience = 0;
        this.level++;
    }
}
