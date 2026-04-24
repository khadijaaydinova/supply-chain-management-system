package a_supply_chain_management_system;

import a_supply_chain_management_system.exceptions.*;

import javax.naming.InvalidNameException;
import java.util.ArrayList;
import java.util.List;

public abstract class BuisnessEntity{
    protected String name;
    protected double funds;
    protected int maxCapacity;
    protected List<Item> inventory = new ArrayList<>();

    public BuisnessEntity(String name, double funds, int maxCapacity) {
        if(name.isEmpty())
            throw new InvalidInputException("Please enter the name of the market.");

        if (funds < 0)
            throw new InvalidAmountException("Funds cannot be negative.");

        if (maxCapacity < 0)
            throw new InvalidAmountException("Storage cannot be negative.");


        this.name = name;
        this.maxCapacity = maxCapacity;
        this.funds = funds;
    }

    public BuisnessEntity(String name, double funds){
        this.name = name;
        this.funds = funds;
    }
    public BuisnessEntity(String name){
        this.name = name;
    }

    //SETTERS
    public void setName(String name){
        if(name.isEmpty()){
            throw new InvalidInputException("Please enter the name of the market.");
        }
        this.name = name; }
    public void setFunds(double funds){
        if (funds < 0){
        throw new InvalidAmountException("Funds cannot be negative.");
        }
        this.funds = funds; }
    public void setMaxCapacity(int maxCapacity){
        if (maxCapacity < 0) {
            throw new InvalidAmountException("Storage cannot be negative.");
        }
        this.maxCapacity = maxCapacity; }
    public void setInventory(List inventory){ this.inventory = inventory; }

    //GETTERS
    public String getName(){ return this.name; }
    public double getFunds(){ return this.funds; }
    public int getMaxCapacity(){ return this.maxCapacity; }
    public List<Item> getInventory(){ return this.inventory; }

    /**
     * The process of buying item from the seller.
     * @param item Material or Product, depending on buyer.
     * @param amount How much buyer wants to buy.
     * @param seller Raw Material Producer, Factory or Market, depending on buyer's rights.
     */
    abstract public void buyItem(Item item, int amount, BuisnessEntity seller);


    //SAME FOR ALL SUBCLASSES

    /**
     * This method manages the buying process, changes the funds and inventory of buyer and seller in a proper way.
     * @param item Material/Product to buy.
     * @param amount The amount of chosen item to buy.
     * @param seller The seller to buy from.
     */
    public void buyingProcess(Item item, int amount, BuisnessEntity seller){

        if (item instanceof Byproduct)
            throw new UnauthorizedActionException("Can't buy Byproduct.");

        if (amount <= 0)
            throw new InvalidAmountException("The amount should be a positive integer.");

        double totalCost = item.getPrice() * amount;

        if (this.funds < totalCost)
            throw new InsufficientFundsExceptions("No enough funds to buy.");

        if (this.currentInventoryAmount() + amount > this.maxCapacity)
            throw new InsufficientStorageException("No enough place left in the storage.");

        //SELLER:
        boolean sellerInventoryCheck = false;

        for (Item i : seller.getInventory()) {
            if (i.getName().equals(item.getName()) && i.getAmount() >= amount) {
                i.setAmount(i.getAmount() - amount);

                sellerInventoryCheck = true;

                if(i.getAmount() == 0)
                    seller.removeFromInventory(i);
                break;
            }
        }

        if (!sellerInventoryCheck)
            throw new InvalidAmountException("Seller does not have enough of this product/material.");

        this.funds -= totalCost;
        this.addToInventory(item, amount);

        seller.funds += totalCost;
    }

    /**
     * Takes an amount of each item (raw product, product, byproduct) and returns its the sum
     * @return Amount of all items in inventory.
     */
    protected int currentInventoryAmount() {
        return inventory.stream().mapToInt(Item::getAmount).sum();
    }

    /**
     * Adds certain item with certain amount to the inventory.
     * @param item Raw material, Product or Byproduct that you want to add to inventory.
     * @param amount How much of this item you want to add.
     */
    protected void addToInventory(Item item, int amount) {
        for (Item i : inventory) {
            if (i.getName().equals(item.getName()) && i.getClass().equals(item.getClass())) {
                i.setAmount(i.getAmount() + amount);
                return;
            }
        }
        // Preserve the type of the item being added
        try {
            Item newItem = item.getClass().getConstructor(String.class, double.class, int.class)
                .newInstance(item.getName(), item.getPrice(), amount);
            inventory.add(newItem);
        } catch (Exception e) {
            // Fallback to generic Item if reflection fails
            inventory.add(new Item(item.getName(), item.getPrice(), amount));
        }
    }

    protected void removeFromInventory(Item itemToRemove) {
        inventory.removeIf(item -> item.getName().equals(itemToRemove.getName()));
    }

    @Override
    public String toString(){
        return "Name: " + name + ", Funds: " + funds + "$, Inventory: " + inventory.toArray().length + "/" + maxCapacity;
    }
}
