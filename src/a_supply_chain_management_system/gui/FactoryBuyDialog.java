package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.Factory;
import a_supply_chain_management_system.Item;
import a_supply_chain_management_system.BuisnessEntity;
import a_supply_chain_management_system.controllers.FactoryController;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class FactoryBuyDialog extends JDialog {
    private JTable productTable;
    private FactoryBuyTableModel productTableModel;
    private JTextField amountField;
    private JButton btnBuy, btnCancel;

    public FactoryBuyDialog(JFrame parent, Factory factory, FactoryController controller) {
        super(parent, "Buy - Select Product", true);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Gather all products from allowed sellers (factories and markets and raw material producers)
        java.util.List<BuisnessEntity> sellers = new java.util.ArrayList<>();
        sellers.addAll(a_supply_chain_management_system.SupplyChainManager.getInstance().getFactories());
        sellers.addAll(a_supply_chain_management_system.SupplyChainManager.getInstance().getMarkets());
        sellers.addAll(a_supply_chain_management_system.SupplyChainManager.getInstance().getProducers()); // Add raw material producers
        sellers.remove(factory); // can't buy from self
        java.util.List<FactoryBuyRow> products = new java.util.ArrayList<>();
        for (BuisnessEntity seller : sellers) {
            for (Item item : seller.getInventory()) {
                if (item.getAmount() > 0) {
                    products.add(new FactoryBuyRow(item, seller));
                }
            }
        }
        productTableModel = new FactoryBuyTableModel(products);
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
            FactoryBuyRow selected = productTableModel.getProductAt(selectedRow);
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
                controller.buyItem(factory, selected.item, amount, selected.seller);
                JOptionPane.showMessageDialog(this, "Purchase successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancel.addActionListener(e -> setVisible(false));
    }
}

class FactoryBuyRow {
    Item item;
    BuisnessEntity seller;
    public FactoryBuyRow(Item item, BuisnessEntity seller) {
        this.item = item;
        this.seller = seller;
    }
}

class FactoryBuyTableModel extends AbstractTableModel {
    private java.util.List<FactoryBuyRow> products;
    private final String[] columns = {"Product Name", "Price", "Amount", "Seller"};
    public FactoryBuyTableModel(java.util.List<FactoryBuyRow> products) {
        this.products = products;
    }
    public FactoryBuyRow getProductAt(int row) {
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
        FactoryBuyRow p = products.get(row);
        switch (col) {
            case 0: return p.item.getName();
            case 1: return p.item.getPrice();
            case 2: return p.item.getAmount();
            case 3: return p.seller.getName();
            default: return null;
        }
    }
}