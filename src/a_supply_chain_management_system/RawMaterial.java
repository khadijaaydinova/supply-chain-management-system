package a_supply_chain_management_system;

public class RawMaterial extends Item {
    public RawMaterial(String name, double price, int amount) {
        super(name, price, amount);
    }
    
    @Override
    public String toString() {
        return getName();
    }
}
