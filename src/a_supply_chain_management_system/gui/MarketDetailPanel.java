package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.Market;
import a_supply_chain_management_system.Item;
import a_supply_chain_management_system.controllers.MarketController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MarketDetailPanel extends JPanel {
    private MainFrame mainFrame;
    private Market market;
    private MarketController controller;

    private JLabel lblName, lblFunds, lblInventory;
    private JButton btnBuy, btnSetPrice, btnInventory;

    public MarketDetailPanel(MainFrame mainFrame, Market market) {
        this.mainFrame = mainFrame;
        this.market = market;
        this.controller = new MarketController();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Top panel with Back button and title
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
        btnBack.setPreferredSize(new Dimension(80, 28));
        btnBack.addActionListener(e -> mainFrame.goBack(MainFrame.MARKETS));
        backPanel.add(btnBack);
        topPanel.add(backPanel, BorderLayout.WEST);
        JLabel title = new JLabel("Market: " + market.getName(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(title, BorderLayout.CENTER);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        add(topPanel, gbc);
        gbc.gridwidth = 1;

        // Info fields
        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        lblName = new JLabel(market.getName());
        add(lblName, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Balance:"), gbc);
        gbc.gridx = 1;
        lblFunds = new JLabel(String.valueOf(market.getFunds()));
        add(lblFunds, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Inventory/Capacity:"), gbc);
        gbc.gridx = 1;
        int currentInventory = market.getInventory().stream().mapToInt(Item::getAmount).sum();
        lblInventory = new JLabel(currentInventory + "/" + market.getMaxCapacity());
        add(lblInventory, gbc);

        // Action buttons
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        btnBuy = new JButton("Buy");
        btnSetPrice = new JButton("Set Price");
        btnInventory = new JButton("View Inventory");
        buttonPanel.add(btnBuy);
        buttonPanel.add(btnSetPrice);
        buttonPanel.add(btnInventory);
        add(buttonPanel, gbc);

        // Actions
        btnBuy.addActionListener(e -> buy());
        btnSetPrice.addActionListener(e -> setPrice());
        btnInventory.addActionListener(e -> showInventory());
    }

    private void buy() {
        MarketBuyDialog dialog = new MarketBuyDialog((JFrame) SwingUtilities.getWindowAncestor(this), market, controller);
        dialog.setVisible(true);
        // Optionally refresh info after buying
        lblFunds.setText(String.valueOf(market.getFunds()));
        int currentInventory = market.getInventory().stream().mapToInt(Item::getAmount).sum();
        lblInventory.setText(currentInventory + "/" + market.getMaxCapacity());
    }

    private void setPrice() {
        List<Item> inventory = market.getInventory();
        if (inventory.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No products in inventory.", "Set Price", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] productNames = inventory.stream().map(Item::getName).toArray(String[]::new);
        String selectedProduct = (String) JOptionPane.showInputDialog(this, "Select product:", "Set Price",
                JOptionPane.PLAIN_MESSAGE, null, productNames, productNames[0]);
        if (selectedProduct == null) return;
        String priceStr = JOptionPane.showInputDialog(this, "Enter new price for " + selectedProduct + ":");
        try {
            double newPrice = Double.parseDouble(priceStr);
            controller.setPrice(market, selectedProduct, newPrice);
            JOptionPane.showMessageDialog(this, "Price updated.", "Set Price", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showInventory() {
        List<Item> inventory = market.getInventory();
        StringBuilder sb = new StringBuilder();
        for (Item item : inventory) {
            sb.append(item.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.length() > 0 ? sb.toString() : "No items in inventory.", "Inventory", JOptionPane.INFORMATION_MESSAGE);
    }
}