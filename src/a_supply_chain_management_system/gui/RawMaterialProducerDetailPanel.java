
package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.RawMaterialProducer;
import a_supply_chain_management_system.Item;
import a_supply_chain_management_system.controllers.RawMaterialProducerController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RawMaterialProducerDetailPanel extends JPanel {
    private MainFrame mainFrame;
    private RawMaterialProducer producer;
    private RawMaterialProducerController controller;

    private JLabel lblName, lblFunds, lblMaterial, lblInventory;
    private JTextField txtAmount;
    private JButton btnProduce, btnInventory;

    public RawMaterialProducerDetailPanel(MainFrame mainFrame, RawMaterialProducer producer) {
        this.mainFrame = mainFrame;
        this.producer = producer;
        this.controller = new RawMaterialProducerController();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Top panel with Back button
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
        btnBack.setPreferredSize(new Dimension(80, 28));
        // FIX: Use goBack for navigation consistency
        btnBack.addActionListener(e -> mainFrame.goBack(MainFrame.PRODUCERS));
        backPanel.add(btnBack);
        topPanel.add(backPanel, BorderLayout.WEST);
        JLabel title = new JLabel("Producer: " + producer.getName(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(title, BorderLayout.CENTER);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        add(topPanel, gbc);
        gbc.gridwidth = 1;

        // Info fields
        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        lblName = new JLabel(producer.getName());
        add(lblName, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Fund:"), gbc);
        gbc.gridx = 1;
        lblFunds = new JLabel(String.valueOf(producer.getFunds()));
        add(lblFunds, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Material:"), gbc);
        gbc.gridx = 1;
        lblMaterial = new JLabel(producer.getMaterial().getName());
        add(lblMaterial, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Inventory/Capacity:"), gbc);
        gbc.gridx = 1;
        int currentInventory = producer.getInventory().stream().mapToInt(Item::getAmount).sum();
        lblInventory = new JLabel(currentInventory + "/" + producer.getMaxCapacity());
        add(lblInventory, gbc);

        // Produce section
        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Amount to Produce:"), gbc);
        gbc.gridx = 1;
        txtAmount = new JTextField(10);
        add(txtAmount, gbc);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnProduce = new JButton("Produce");
        btnInventory = new JButton("View Inventory");
        buttonPanel.add(btnProduce);
        buttonPanel.add(btnInventory);
        add(buttonPanel, gbc);

        // Actions
        btnProduce.addActionListener(e -> produce());
        btnInventory.addActionListener(e -> showInventory());
    }

    private void produce() {
        String amountStr = txtAmount.getText().trim();
        try {
            int amount = Integer.parseInt(amountStr);
            controller.generateMaterial(producer, amount);
            JOptionPane.showMessageDialog(this, "Produced successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Update info
            lblFunds.setText(String.valueOf(producer.getFunds()));
            int currentInventory = producer.getInventory().stream().mapToInt(Item::getAmount).sum();
            lblInventory.setText(currentInventory + "/" + producer.getMaxCapacity());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showInventory() {
        List<Item> inventory = producer.getInventory();
        StringBuilder sb = new StringBuilder();
        for (Item item : inventory) {
            sb.append(item.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.length() > 0 ? sb.toString() : "No items in inventory.", "Inventory", JOptionPane.INFORMATION_MESSAGE);
    }
}
