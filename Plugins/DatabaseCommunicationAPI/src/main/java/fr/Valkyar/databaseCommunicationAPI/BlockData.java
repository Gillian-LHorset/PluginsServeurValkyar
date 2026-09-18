package fr.Valkyar.databaseCommunicationAPI;

public class BlockData {
    private final Integer id;
    private final String displayName;
    private final Double moneyBringing;
    private final Double expBringing;
    private final Integer jobFk;

    public BlockData(Integer id, String displayName, Double moneyBringing, Double expBringing, Integer jobFk) {
        this.id = id;
        this.displayName = displayName;
        this.moneyBringing = moneyBringing;
        this.expBringing = expBringing;
        this.jobFk = jobFk;
    }

    public Integer getId() { return id; }
    public String getDisplayName() { return displayName; }
    public Double moneyBringing() { return moneyBringing; }
    public Double expBringing() { return expBringing; }
    public Integer jobFk() { return jobFk; }

    public BlockData getBlockDataByName(String blockName) {
        if (displayName.equals(blockName)) {
            return this;
        }
        return null;
    }
}
