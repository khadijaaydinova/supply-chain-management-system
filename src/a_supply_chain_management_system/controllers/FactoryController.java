package a_supply_chain_management_system.controllers;

import a_supply_chain_management_system.*;

import java.util.List;

public class FactoryController {
    private final SupplyChainManager scm = SupplyChainManager.getInstance();

    public List<Factory> getAllFactories() {
        return scm.getFactories();
    }

    public void registerFactory(String name, int maxCapacity, double funds) {
        Factory factory = new Factory(name, maxCapacity, funds);
        scm.addFactory(factory);
    }

    public void buyItem(Factory factory, Item item, int amount, BuisnessEntity seller) {
        factory.buyItem(item, amount, seller);
    }

    public void produce(Factory factory, ProductDesign design, int amount) {
        factory.produce(design, amount);
    }

    public void addDesign(Factory factory, ProductDesign design) {
        factory.addDesign(design);
    }

    public void disposeByproduct(Factory factory, Byproduct byproduct, int amount) {
        factory.disposeByproduct(byproduct, amount);
    }

    public Factory findFactoryByName(String name) {
        return scm.findFactoryByName(name);
    }

    public void removeFactory(Factory factory) {
        scm.removeFactory(factory);
    }
}