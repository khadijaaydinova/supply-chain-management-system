package a_supply_chain_management_system;

public class Byproduct extends Item {
    public Byproduct(String name, double price, int amount) {
        super(name, price, amount);
    }

    @Override
    public String toString() {
        return getName();
    }
}

