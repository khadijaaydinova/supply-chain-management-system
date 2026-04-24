package a_supply_chain_management_system;

import a_supply_chain_management_system.exceptions.InsufficientFundsExceptions;
import a_supply_chain_management_system.exceptions.InsufficientStorageException;
import a_supply_chain_management_system.exceptions.InvalidAmountException;

public class RawMaterialProducer extends BuisnessEntity implements Produce{

    private RawMaterial material;
    private double generationCost;
    private double sellingPrice;

    public RawMaterialProducer(String name, RawMaterial material, double generationCost, double sellingPrice, int maxCapacity, double funds){
        super(name, funds, maxCapacity);
        this.material = material;
        this.generationCost = generationCost;
        this.sellingPrice = sellingPrice;
    }

    //SETTERS
    public void setMaterial(RawMaterial material){ this.material = material; }
    public void setGenerationCost(double generationCost){ this.generationCost = generationCost; }
    public void setSellingPrice(double sellingPrice){ this.sellingPrice = sellingPrice; }

    //GETTERS
    public RawMaterial getMaterial(){ return this.material; }
    public double getGenerationCost(){ return this.generationCost; }
    public double getSellingPrice(){ return this.sellingPrice; }


    @Override
    public void produce(){
        produce(1);
    }

    @Override
    public void produce(int amount){
        if(amount <= 0) throw new InvalidAmountException("The amount should be positive integer.");
        double totalCost = generationCost * amount;
        if(funds < totalCost) throw new InsufficientFundsExceptions("There is no enough funds to produce.");
        if(currentInventoryAmount() + amount > maxCapacity) throw new InsufficientStorageException("There is no enough space in storage to produce.");

        funds -= totalCost;
        addToInventory(new RawMaterial(material.getName(), sellingPrice, amount), amount);
    }

    @Override
    public void buyItem(Item item, int amount, BuisnessEntity seller){
        throw new UnsupportedOperationException("Raw Material Producers can't buy anything.");
    }

    @Override
    public String toString() {
        int total = getInventory().stream().mapToInt(Item::getAmount).sum();
        return "Name: " + getName() + ", Funds: " + getFunds() + ", Inventory: " + total + "/" + getMaxCapacity();
    }

}
