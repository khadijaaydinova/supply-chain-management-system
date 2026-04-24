package a_supply_chain_management_system.controllers;

import a_supply_chain_management_system.BuisnessEntity;
import a_supply_chain_management_system.Customer;
import a_supply_chain_management_system.Item;
import a_supply_chain_management_system.SupplyChainManager;

import java.util.List;

public class CustomerController {
    private final SupplyChainManager scm = SupplyChainManager.getInstance();

    public List<Customer> getAllCustomers() {
        return scm.getCustomers();
    }

    public void registerCustomer(String name, double initialFunds) {
        Customer customer = new Customer(name, initialFunds);
        scm.addCustomer(customer);
    }

    public void buyItem(Customer customer, Item item, int amount, BuisnessEntity seller) {
        customer.buyItem(item, amount, seller);
    }

    public Customer findCustomerByName(String name) {
        return scm.findCustomerByName(name);
    }

    public void removeCustomer(Customer customer) {
        scm.removeCustomer(customer);
    }
}