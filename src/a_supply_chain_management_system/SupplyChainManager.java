package a_supply_chain_management_system;
import java.util.ArrayList;
import java.util.List;

public class SupplyChainManager {

    private List<RawMaterialProducer> producers;
    private List<Factory> factories;
    private List<Market> markets;
    private List<Customer> customers;
    private static SupplyChainManager instance;

    public SupplyChainManager() {
        producers = new ArrayList<>();
        factories = new ArrayList<>();
        markets = new ArrayList<>();
        customers = new ArrayList<>();
    }

    /**
     * First checks. Then creates a new SupplyChainManager if there is none exists.
     * @return A new or an existing SupplyChainManager.
     */
    public static SupplyChainManager getInstance() {
        if (instance == null) {
            instance = new SupplyChainManager();
        }
        return instance;
    }

//ADD
    public void addProducer(RawMaterialProducer producer) { producers.add(producer); }
    public void addFactory(Factory factory) { factories.add(factory); }
    public void addMarket(Market market) { markets.add(market); }
    public void addCustomer(Customer customer) { customers.add(customer); }

//DELETE
    public void removeProducer(RawMaterialProducer producer) { producers.remove(producer); }
    public void removeFactory(Factory factory) { factories.remove(factory); }
    public void removeMarket(Market market) { markets.remove(market); }
    public void removeCustomer(Customer customer) { customers.remove(customer); }

//SEARCH
    public RawMaterialProducer findProducerByName(String name) {
        for (RawMaterialProducer p : producers) {
            if (p.name.equals(name)) {
                return p;
            }
        }
        return null;
    }

    public Factory findFactoryByName(String name) {
        for (Factory f : factories) {
            if (f.name.equals(name)) {
                return f;
            }
        }
        return null;
    }

    public Market findMarketByName(String name) {
        for (Market m : markets) {
            if (m.name.equals(name)) {
                return m;
            }
        }
        return null;
    }

    public Customer findCustomerByName(String name) {
        for (Customer c : customers) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

//GETTERS
    public List<RawMaterialProducer> getProducers() { return producers; }
    public List<Factory> getFactories() { return factories; }
    public List<Market> getMarkets() { return markets; }
    public List<Customer> getCustomers() { return customers; }
}
