package fr.Valkyar.databaseCommunicationAPI.Models;

public class Town {
    private int id;
    private String name;
    private Double balance;

    public Town(int id, String name, Double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public Double getBalance() {return balance;}


    public void DepositMoney(Double amount) {
        balance += amount;
    }
    public void WhithdrawMoney(Double amount) {
        balance -= amount;
    }
}
