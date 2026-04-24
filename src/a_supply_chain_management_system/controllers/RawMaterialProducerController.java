package a_supply_chain_management_system.controllers;

import a_supply_chain_management_system.RawMaterial;
import a_supply_chain_management_system.RawMaterialProducer;
import a_supply_chain_management_system.SupplyChainManager;

import java.util.List;

public class RawMaterialProducerController {
    private final SupplyChainManager scm = SupplyChainManager.getInstance();

    public List<RawMaterialProducer> getAllProducers() {
        return scm.getProducers();
    }

    public void registerProducer(String name, RawMaterial material, double generationCost, double sellingPrice, int maxCapacity, double funds) {
        RawMaterialProducer producer = new RawMaterialProducer(name, material, generationCost, sellingPrice, maxCapacity, funds);
        scm.addProducer(producer);
    }

    public void generateMaterial(RawMaterialProducer producer, int amount) {
        producer.produce(amount);
    }

    public RawMaterialProducer findProducerByName(String name) {
        return scm.findProducerByName(name);
    }

    public void removeProducer(RawMaterialProducer producer) {
        scm.removeProducer(producer);
    }
}