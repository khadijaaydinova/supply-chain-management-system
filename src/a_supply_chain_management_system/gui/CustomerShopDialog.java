package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.Customer;
import a_supply_chain_management_system.Item;
import a_supply_chain_management_system.Market;
import a_supply_chain_management_system.controllers.CustomerController;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class CustomerShopDialog extends JDialog {
    private JTable productTable;
    private ProductTableModel productTableModel;
    private JTextField amountField;
    private JButton btnPurchase, btnCancel;

    public CustomerShopDialog(JFrame parent, Customer customer, CustomerController customerController) {
        super(parent, "Shop - Select Product", true);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Gather all products from all markets (with amount > 0)
        java.util.List<Market> markets = a_supply_chain_management_system.SupplyChainManager.getInstance().getMarkets();
        java.util.List<ProductRow> products = new java.util.ArrayList<>();
        for (Market market : markets) {
            for (Item item : market.getInventory()) {
                if (item.getAmount() > 0) {
                    products.add(new ProductRow(item, market));
                }
            }
        }
        productTableModel = new ProductTableModel(products);
        productTable = new JTable(productTableModel);
        add(new JScrollPane(productTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(new JLabel("Amount to buy:"));
        amountField = new JTextField(5);
        bottomPanel.add(amountField);
        btnPurchase = new JButton("Purchase");
        btnCancel = new JButton("Cancel");
        bottomPanel.add(btnPurchase);
        bottomPanel.add(btnCancel);
        add(bottomPanel, BorderLayout.SOUTH);

        btnPurchase.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a product.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ProductRow selected = productTableModel.getProductAt(selectedRow);
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
                customerController.buyItem(customer, selected.item, amount, selected.seller);
                JOptionPane.showMessageDialog(this, "Purchase successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancel.addActionListener(e -> setVisible(false));
    }
}

class ProductRow {
    Item item;
    Market seller;
    public ProductRow(Item item, Market seller) {
        this.item = item;
        this.seller = seller;
    }
}

class ProductTableModel extends AbstractTableModel {
    private java.util.List<ProductRow> products;
    private final String[] columns = {"Product Name", "Price", "Amount", "Seller"};
    public ProductTableModel(java.util.List<ProductRow> products) {
        this.products = products;
    }
    public ProductRow getProductAt(int row) {
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
        ProductRow p = products.get(row);
        switch (col) {
            case 0: return p.item.getName();
            case 1: return p.item.getPrice();
            case 2: return p.item.getAmount();
            case 3: return p.seller.getName();
            default: return null;
        }
    }
}