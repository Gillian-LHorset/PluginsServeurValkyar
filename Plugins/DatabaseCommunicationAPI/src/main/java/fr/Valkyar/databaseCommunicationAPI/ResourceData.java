package fr.Valkyar.databaseCommunicationAPI;

public class ResourceData {
    private final Integer id;
    private final String displayName;
    private final Double moneyBringing;
    private final Double expBringing;
    private final String actionType;
    private final Integer jobFk;

    public ResourceData(Integer id, String displayName, Double moneyBringing, Double expBringing, String actionType, Integer jobFk) {
        this.id = id;
        this.displayName = displayName;
        this.moneyBringing = moneyBringing;
        this.expBringing = expBringing;
        this.actionType = actionType;
        this.jobFk = jobFk;
    }

    public Integer getId() { return id; }
    public String getDisplayName() { return displayName; }
    public Double moneyBringing() { return moneyBringing; }
    public Double expBringing() { return expBringing; }
    public String getActionType() { return actionType; }
    public Integer jobFk() { return jobFk; }

    public ResourceData getResourceDataByName(String resourceName) {
        if (displayName.equals(resourceName)) {
            return this;
        }
        return null;
    }
}
