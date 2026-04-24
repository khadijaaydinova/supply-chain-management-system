package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.Customer;
import a_supply_chain_management_system.Item;
import a_supply_chain_management_system.controllers.CustomerController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomerDetailPanel extends JPanel {
    private MainFrame mainFrame;
    private Customer customer;
    private CustomerController controller;

    private JLabel lblName, lblFunds, lblInventory;
    private JButton btnShop, btnInventory;

    public CustomerDetailPanel(MainFrame mainFrame, Customer customer) {
        this.mainFrame = mainFrame;
        this.customer = customer;
        this.controller = new CustomerController();

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
        btnBack.addActionListener(e -> mainFrame.goBack(MainFrame.CUSTOMERS));
        backPanel.add(btnBack);
        topPanel.add(backPanel, BorderLayout.WEST);
        JLabel title = new JLabel("Customer: " + customer.getName(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(title, BorderLayout.CENTER);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        add(topPanel, gbc);
        gbc.gridwidth = 1;

        // Info fields
        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        lblName = new JLabel(customer.getName());
        add(lblName, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Balance:"), gbc);
        gbc.gridx = 1;
        lblFunds = new JLabel(String.valueOf(customer.getFunds()));
        add(lblFunds, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Inventory:"), gbc);
        gbc.gridx = 1;
        int currentInventory = customer.getInventory().stream().mapToInt(Item::getAmount).sum();
        lblInventory = new JLabel(currentInventory + " (Unlimited capacity)");
        add(lblInventory, gbc);

        // Action buttons
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        btnShop = new JButton("Shop");
        btnInventory = new JButton("View Inventory");
        buttonPanel.add(btnShop);
        buttonPanel.add(btnInventory);
        add(buttonPanel, gbc);

        // Actions
        btnShop.addActionListener(e -> shop());
        btnInventory.addActionListener(e -> showInventory());
    }

    private void shop() {
        CustomerShopDialog dialog = new CustomerShopDialog((JFrame) SwingUtilities.getWindowAncestor(this), customer, controller);
        dialog.setVisible(true);
        // Optionally refresh info after shopping
        lblFunds.setText(String.valueOf(customer.getFunds()));
        int currentInventory = customer.getInventory().stream().mapToInt(Item::getAmount).sum();
        lblInventory.setText(currentInventory + " (Unlimited capacity)");
    }

    private void showInventory() {
        List<Item> inventory = customer.getInventory();
        StringBuilder sb = new StringBuilder();
        for (Item item : inventory) {
            sb.append(item.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.length() > 0 ? sb.toString() : "No items in inventory.", "Inventory", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
        btnBack.setPreferredSize(new Dimension(80, 28));
        btnBack.addActionListener(e -> mainFrame.goBack(MainFrame.CUSTOMERS));
        backPanel.add(btnBack);
        topPanel.add(backPanel, BorderLayout.WEST);
        JLabel title = new JLabel("Customers", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(title, BorderLayout.CENTER);
        return topPanel;
    }
}