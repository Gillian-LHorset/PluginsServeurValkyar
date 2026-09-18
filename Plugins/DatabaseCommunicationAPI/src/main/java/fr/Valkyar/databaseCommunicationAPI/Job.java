package fr.Valkyar.databaseCommunicationAPI;

public class Job {
    private final Integer id;
    private final String displayName;
    private final boolean isPrimary;

    public Job(Integer id, String displayName, boolean isPrimary) {
        this.id = id;
        this.displayName = displayName;
        this.isPrimary = isPrimary;
    }

    public Integer getId() { return id; }
    public String getDisplayName() { return displayName; }
    public boolean isPrimary() { return isPrimary; }
}

