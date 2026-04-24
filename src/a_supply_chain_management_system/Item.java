package a_supply_chain_management_system;


import a_supply_chain_management_system.exceptions.InvalidAmountException;

public class Item {
    protected String name;
    protected double price;
    protected int amount;

    public Item(String name, double price, int amount) {
        if (price < 0) 
            throw new InvalidAmountException("Price cannot be negative.");
        if (amount < 0) 
            throw new InvalidAmountException("Amount cannot be negative.");
        
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    // GETTERS
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getAmount() { return amount; }

    // SETTERS
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) {
        if (price < 0) {
            throw new InvalidAmountException("Price cannot be negative.");
        }
        this.price = price; 
    }
    public void setAmount(int amount) {
        if (amount < 0) {
            throw new InvalidAmountException("Amount cannot be negative.");
        }
        this.amount = amount; 
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Price: $" + price + ", Amount: " + amount;
    }
}