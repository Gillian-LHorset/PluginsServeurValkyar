package fr.Valkyar.databaseCommunicationAPI.Models;

public class Town {
    private String name;
    private Double balance;

    public Town(String name, Double balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {return name;}
    public Double getBalance() {return balance;}
}
