package a_supply_chain_management_system;
import java.util.ArrayList;
import java.util.List;

import a_supply_chain_management_system.exceptions.InsufficientFundsExceptions;
import a_supply_chain_management_system.exceptions.InsufficientStorageException;
import a_supply_chain_management_system.exceptions.InvalidAmountException;
import a_supply_chain_management_system.exceptions.UnauthorizedActionException;

public class Market extends BuisnessEntity {

    public Market(String name, int maxCapacity, double funds) {
        super(name, funds, maxCapacity);
        this.inventory = new ArrayList<>();
    }

    @Override
    public void buyItem(Item item, int amount, BuisnessEntity seller){
        if (seller instanceof RawMaterialProducer)
            throw new UnauthorizedActionException("Markets can't buy from Raw Material Producers.");
        if (seller instanceof Customer)
            throw new UnauthorizedActionException("Markets can't buy from Customers.");
        this.buyingProcess(item, amount, seller);
    }

    /**
     * Sets new price for a product.
     * @param itemName Product's name.
     * @param newPrice New price for the product.
     */
    public void setPrice(String itemName, double newPrice) {
        if (newPrice <= 0) {
            throw new InvalidAmountException("Price should be positive.");
        }

        for (Item i : inventory) {
            if (i.getName().equals(itemName)) {
                i.price = newPrice;
                return;
            }
        }

        throw new InvalidAmountException("There is no such product in the inventory.");
    }
}
