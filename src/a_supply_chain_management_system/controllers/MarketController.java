package a_supply_chain_management_system.controllers;

import a_supply_chain_management_system.BuisnessEntity;
import a_supply_chain_management_system.Item;
import a_supply_chain_management_system.Market;
import a_supply_chain_management_system.SupplyChainManager;

import java.util.List;

public class MarketController {
    private final SupplyChainManager scm = SupplyChainManager.getInstance();

    public List<Market> getAllMarkets() {
        return scm.getMarkets();
    }

    public void registerMarket(String name, int maxCapacity, double funds) {
        Market market = new Market(name, maxCapacity, funds);
        scm.addMarket(market);
    }

    public void buyItem(Market market, Item item, int amount, BuisnessEntity seller) {
        market.buyItem(item, amount, seller);
    }

    public void setPrice(Market market, String itemName, double newPrice) {
        market.setPrice(itemName, newPrice);
    }

    public Market findMarketByName(String name) {
        return scm.findMarketByName(name);
    }

    public void removeMarket(Market market) {
        scm.removeMarket(market);
    }
}