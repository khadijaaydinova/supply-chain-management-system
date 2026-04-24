package a_supply_chain_management_system;

import a_supply_chain_management_system.exceptions.InsufficientFundsExceptions;
import a_supply_chain_management_system.exceptions.InsufficientStorageException;
import a_supply_chain_management_system.exceptions.InvalidAmountException;
import a_supply_chain_management_system.exceptions.UnauthorizedActionException;

import java.util.ArrayList;
import java.util.List;


public class Customer extends BuisnessEntity {

    public Customer(String name, double initialFunds) {
        super(name, initialFunds);
        this.inventory = new ArrayList<>();
    }



    @Override
    public void buyItem(Item item, int amount, BuisnessEntity seller){
        if (seller instanceof RawMaterialProducer)
            throw new UnauthorizedActionException("Customers can't buy from Raw Material Producers. Choose a market.");
        if (seller instanceof Customer)
            throw new UnauthorizedActionException("Customers can't buy from other Customers. Choose a market.");
        if (seller instanceof Factory)
            throw new UnauthorizedActionException("Customers can't buy from Factories. Choose a market.");
        this.buyingProcess(item, amount, seller);
    }


    @Override
    public String toString() {
        return name;
    }
}
