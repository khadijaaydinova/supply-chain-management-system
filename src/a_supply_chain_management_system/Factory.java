package a_supply_chain_management_system;

import a_supply_chain_management_system.exceptions.InsufficientStorageException;
import a_supply_chain_management_system.exceptions.InvalidAmountException;
import a_supply_chain_management_system.exceptions.InsufficientFundsExceptions;
import a_supply_chain_management_system.exceptions.UnauthorizedActionException;

import java.util.ArrayList;
import java.util.List;

public class Factory extends BuisnessEntity implements Produce {
    private List<ProductDesign> designs = new ArrayList<>();

    public Factory(String name, int maxCapacity, double funds) {
        super(name, funds, maxCapacity);
    }

    /**
     * Produces 1 amount of product using the first design.
     */
    @Override
    public void produce() {
        if (designs.isEmpty()) {
            throw new RuntimeException("There are no product designs.");
        }
        produce(designs.get(0), 1);
    }
    /**
     * Produces chosen amount of product using the first design.
     */
    @Override
    public void produce(int amount) {
        if (designs.isEmpty()) {
            throw new RuntimeException("Нет доступных дизайнов для производства.");
        }
        produce(designs.get(0), amount);
    }
    /**
     * Produces the products using the chosen design.
     * @param design Product Design.
     * @param amount Amount to produce.
     */
    public void produce(ProductDesign design, int amount) {
        if (amount <= 0) throw new InvalidAmountException("The amount should be positive integer.");

        double totalCost = design.getByproductCost() * amount;
        if (funds < totalCost) throw new InsufficientFundsExceptions("There are no enough funds to produce.");

        //Searching for required raw materials.
        for (RawMaterial req : design.getMaterials()) {
            boolean found = false;
            for (Item i : inventory) {
                if (i.getName().equals(req.getName()) && i.getAmount() >= req.getAmount() * amount) {
                    found = true;
                    break;
                }
            }
            if (!found) throw new InvalidAmountException("The Lack Materials: " + req.getName());
        }

        if (currentInventoryAmount() + amount > maxCapacity)
            throw new InsufficientStorageException("Not enough place in the storage.");

        // Removing the used materials from storage
        for (RawMaterial req : design.getMaterials()) {
            for (Item i : inventory) {
                if (i.getName().equals(req.getName())) {
                    i.setAmount(i.getAmount() - req.getAmount() * amount);
                    break;
                }
            }
        }


        addToInventory(new Product(design.getOutput().getName(), design.getOutput().getPrice(), amount), amount);
        addToInventory(new Byproduct(design.getByproduct().getName(), design.getByproduct().getPrice(), design.getByproductAmount() * amount), design.getByproductAmount() * amount);

        funds -= totalCost;
    }


    @Override
    public void buyItem(Item item, int amount, BuisnessEntity seller) {
        if (seller instanceof Customer)
            throw new UnauthorizedActionException("Factories can't buy from Customers.");
        this.buyingProcess(item, amount, seller);
    }

    /**
     * Adds new Product Design.
     */
    public void addDesign(ProductDesign design) {
        designs.add(design);
    }

    /**
     * Disposes the chosen Byproduct.
     * @param byproduct The byproduct to be disposed.
     * @param amountToDispose How much you want to dispose.
     */
    public void disposeByproduct(Byproduct byproduct, int amountToDispose) {
        if (amountToDispose <= 0)
            throw new InvalidAmountException("The amount should be a positive integer.");

        for (Item i : inventory) {
            if (i.getName().equals(byproduct.getName())) {
                if (amountToDispose > i.getAmount())
                    throw new InvalidAmountException("There is not enough byproduct for disposal.");

                double disposalCost = byproduct.getPrice() * amountToDispose;

                if (disposalCost > funds)
                    throw new InsufficientFundsExceptions("Not enough funds to dispose.");

                i.setAmount(i.getAmount() - amountToDispose);
                funds -= disposalCost;

                if (i.getAmount() == 0)
                    inventory.remove(i);

                return;
            }
        }

        throw new RuntimeException("Byproduct not found in inventory.");
    }

    /**
     * @return The list of existing Product Designs.
     */
    public List<ProductDesign> getDesigns() {
        return designs;
    }

}


