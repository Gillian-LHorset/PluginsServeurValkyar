package fr.Valkyar.databaseCommunicationAPI.Models;

import java.util.List;

public class Job {
    private final Integer id;
    private final String displayName;
    private final boolean isPrimary;
    private List<String> specialisations;

    public Job(Integer id, String displayName, boolean isPrimary) {
        this.id = id;
        this.displayName = displayName;
        this.isPrimary = isPrimary;
    }

    public Integer getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public List<String> getSpecialisations() {
        return specialisations;
    }

    public void addSpecialisation(String specialisation) {
        specialisations.add(specialisation);
    }
}

