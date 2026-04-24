/*
package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.BuisnessEntity;
import a_supply_chain_management_system.Factory;
import a_supply_chain_management_system.Item;
import a_supply_chain_management_system.controllers.FactoryController;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class FactoryShopDialog extends JDialog{
    private JTable productTable;
    private a_supply_chain_management_system.gui.ProductTableModel productTableModel;
    private JTextField amountField;
    private JButton btnPurchase, btnCancel;

    public FactoryShopDialog(JFrame parent, Factory factory, FactoryController factoryController) {
        super(parent, "Shop - Select Product", true);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

            // Gather all products from all markets (with amount > 0)
        /*
        java.util.List<Market> markets = a_supply_chain_management_system.SupplyChainManager.getInstance().getMarkets();
            java.util.List<a_supply_chain_management_system.gui.ProductRow> products = new java.util.ArrayList<>();
            for (Market market : markets) {
                for (Item item : market.getInventory()) {
                    if (item.getAmount() > 0) {
                        products.add(new a_supply_chain_management_system.gui.ProductRow(item, market));
                    }
                }
            }


        java.util.List<BuisnessEntity> sellers = new java.util.ArrayList<>();
        sellers.addAll(a_supply_chain_management_system.SupplyChainManager.getInstance().getFactories());
        for (Factory m : a_supply_chain_management_system.SupplyChainManager.getInstance().getFactories()) {
            if (!m.equals(factory)) sellers.add(m);
        }
        java.util.List<FactoryShoppingProductRow> products = new java.util.ArrayList<>();
        for (BuisnessEntity seller : sellers) {
            for (Item item : seller.getInventory()) {
                if (item.getAmount() > 0) {
                    products.add(new FactoryShoppingProductRow(item, seller));
                }
            }
        }

        productTableModel = new a_supply_chain_management_system.gui.ProductTableModel(products);
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
                a_supply_chain_management_system.gui.ProductRow selected = productTableModel.getProductAt(selectedRow);
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
                    factoryController.buyItem(factory, selected.item, amount, selected.seller);
                    JOptionPane.showMessageDialog(this, "Purchase successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            btnCancel.addActionListener(e -> setVisible(false));
        }
    }

    class FactoryShoppingProductRow {
        Item item;
        BuisnessEntity seller;
        public FactoryShoppingProductRow(Item item, BuisnessEntity seller) {
            this.item = item;
            this.seller = seller;
        }
    }



    class FactoryShoppingProduct extends AbstractTableModel {
        private java.util.List<a_supply_chain_management_system.gui.FactoryShoppingProductRow> products;
        private final String[] columns = {"Product Name", "Price", "Amount", "Seller"};
        public FactoryShoppingProduct(java.util.List<a_supply_chain_management_system.gui.FactoryShoppingProductRow> products) {
            this.products = products;
        }
        public a_supply_chain_management_system.gui.FactoryShoppingProductRow getProductAt(int row) {
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
            a_supply_chain_management_system.gui.ProductRow p = products.get(row);
            switch (col) {
                case 0: return p.item.getName();
                case 1: return p.item.getPrice();
                case 2: return p.item.getAmount();
                case 3: return p.seller.getName();
                default: return null;
            }
        }
    }
*/