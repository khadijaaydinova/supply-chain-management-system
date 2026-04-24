package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.Market;
import a_supply_chain_management_system.Item;
import a_supply_chain_management_system.BuisnessEntity;
import a_supply_chain_management_system.RawMaterial;
import a_supply_chain_management_system.controllers.MarketController;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class MarketBuyDialog extends JDialog {
    private JTable productTable;
    private MarketBuyTableModel productTableModel;
    private JTextField amountField;
    private JButton btnBuy, btnCancel;

    public MarketBuyDialog(JFrame parent, Market market, MarketController controller) {
        super(parent, "Buy - Select Product", true);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Gather all products from allowed sellers (factories and other markets)
        java.util.List<BuisnessEntity> sellers = new java.util.ArrayList<>();
        sellers.addAll(a_supply_chain_management_system.SupplyChainManager.getInstance().getFactories());
        for (Market m : a_supply_chain_management_system.SupplyChainManager.getInstance().getMarkets()) {
            if (!m.equals(market)) sellers.add(m);
        }
        java.util.List<MarketBuyRow> products = new java.util.ArrayList<>();
        for (BuisnessEntity seller : sellers) {
            for (Item item : seller.getInventory()) {
                if (item instanceof RawMaterial){
                    continue;
                }
                if (item.getAmount() > 0) {
                    products.add(new MarketBuyRow(item, seller));
                }
            }
        }
        productTableModel = new MarketBuyTableModel(products);
        productTable = new JTable(productTableModel);
        add(new JScrollPane(productTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(new JLabel("Amount to buy:"));
        amountField = new JTextField(5);
        bottomPanel.add(amountField);
        btnBuy = new JButton("Buy");
        btnCancel = new JButton("Cancel");
        bottomPanel.add(btnBuy);
        bottomPanel.add(btnCancel);
        add(bottomPanel, BorderLayout.SOUTH);

        btnBuy.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a product.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            MarketBuyRow selected = productTableModel.getProductAt(selectedRow);
            String amountStr = amountField.getText().trim();
            int amount;
            try {
                amount = Integer.parseInt(amountStr);
                if (amount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Amount must be a positive integer.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                controller.buyItem(market, selected.item, amount, selected.seller);
                JOptionPane.showMessageDialog(this, "Purchase successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancel.addActionListener(e -> setVisible(false));
    }
}

class MarketBuyRow {
    Item item;
    BuisnessEntity seller;
    public MarketBuyRow(Item item, BuisnessEntity seller) {
        this.item = item;
        this.seller = seller;
    }
}

class MarketBuyTableModel extends AbstractTableModel {
    private java.util.List<MarketBuyRow> products;
    private final String[] columns = {"Product Name", "Price", "Amount", "Seller"};
    public MarketBuyTableModel(java.util.List<MarketBuyRow> products) {
        this.products = products;
    }
    public MarketBuyRow getProductAt(int row) {
        return products.get(row);
    }
    @Override
    public int getRowCount() { return products.size(); }
    @Override
    public int getColumnCount() { return columns.length; }
    @Override
    public String getColumnName(int col) { return columns[col]; }
    @Override
    public Object getValueAt(int row, int col) {
        MarketBuyRow p = products.get(row);
        switch (col) {
            case 0: return p.item.getName();
            case 1: return p.item.getPrice();
            case 2: return p.item.getAmount();
            case 3: return p.seller.getName();
            default: return null;
        }
    }
}