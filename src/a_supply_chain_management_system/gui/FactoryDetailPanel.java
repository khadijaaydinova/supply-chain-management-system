package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.Factory;
import a_supply_chain_management_system.Item;
import a_supply_chain_management_system.Byproduct;
import a_supply_chain_management_system.ProductDesign;
import a_supply_chain_management_system.controllers.FactoryController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class FactoryDetailPanel extends JPanel {
    private final MainFrame mainFrame;
    private final Factory factory;
    private final FactoryController controller;

    private JLabel lblName, lblFunds, lblCapacity, lblInventory;
    private JComboBox<ProductDesign> designComboBox;
    private JTextField txtProduceAmount;

    /**
     * Always use FactoryDetailPanel.showInMainFrame(mainFrame, factory)
     * to display this panel, so navigation and the Back button work!
     */
    public FactoryDetailPanel(MainFrame mainFrame, Factory factory) {
        this.mainFrame = mainFrame;
        this.factory = factory;
        this.controller = new FactoryController();

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
        // Use goBack for navigation consistency
        btnBack.addActionListener(e -> mainFrame.goBack(MainFrame.FACTORIES));
        backPanel.add(btnBack);
        topPanel.add(backPanel, BorderLayout.WEST);
        JLabel title = new JLabel("Factory: " + factory.getName(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(title, BorderLayout.CENTER);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        add(topPanel, gbc);
        gbc.gridwidth = 1;

        // Info fields
        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        lblName = new JLabel(factory.getName());
        add(lblName, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Funds:"), gbc);
        gbc.gridx = 1;
        lblFunds = new JLabel(String.valueOf(factory.getFunds()));
        add(lblFunds, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Capacity:"), gbc);
        gbc.gridx = 1;
        lblCapacity = new JLabel(String.valueOf(factory.getMaxCapacity()));
        add(lblCapacity, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Inventory:"), gbc);
        gbc.gridx = 1;
        int currentInventory = factory.getInventory().stream().mapToInt(Item::getAmount).sum();
        lblInventory = new JLabel(currentInventory + "/" + factory.getMaxCapacity());
        add(lblInventory, gbc);

        // Product Design selection and produce section
        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Product Design:"), gbc);
        gbc.gridx = 1;
        List<ProductDesign> designs = factory.getDesigns();
        designComboBox = new JComboBox<>(designs.toArray(new ProductDesign[0]));
        add(designComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = row++;
        add(new JLabel("Amount to Produce:"), gbc);
        gbc.gridx = 1;
        txtProduceAmount = new JTextField(10);
        add(txtProduceAmount, gbc);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnShop = new JButton("Shop");
        JButton btnProduce = new JButton("Produce");
        JButton btnInventory = new JButton("View Inventory");
        JButton btnDisposeByproduct = new JButton("Dispose Byproduct");
        buttonPanel.add(btnShop);
        buttonPanel.add(btnProduce);
        buttonPanel.add(btnInventory);
        buttonPanel.add(btnDisposeByproduct);
        add(buttonPanel, gbc);

        // Actions
        btnShop.addActionListener(e -> shop());
        btnProduce.addActionListener(e -> produce());
        btnInventory.addActionListener(e -> showInventory());
        btnDisposeByproduct.addActionListener(e -> showDisposeByproductDialog());
    }

    private void shop() {
        FactoryBuyDialog dialog = new FactoryBuyDialog((JFrame) SwingUtilities.getWindowAncestor(this), factory, controller);
        dialog.setVisible(true);
        // Optionally refresh info after shopping
        lblFunds.setText(String.valueOf(factory.getFunds()));
        int currentInventory = factory.getInventory().stream().mapToInt(Item::getAmount).sum();
        int maxCapacity = factory.getMaxCapacity();
        lblInventory.setText(currentInventory + "/" + maxCapacity);
    }

    private void produce() {
        ProductDesign design = (ProductDesign) designComboBox.getSelectedItem();
        String amountStr = txtProduceAmount.getText().trim();
        try {
            int amount = Integer.parseInt(amountStr);
            controller.produce(factory, design, amount);
            JOptionPane.showMessageDialog(this, "Production successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Update info
            lblFunds.setText(String.valueOf(factory.getFunds()));
            int currentInventory = factory.getInventory().stream().mapToInt(Item::getAmount).sum();
            lblInventory.setText(currentInventory + "/" + factory.getMaxCapacity());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showInventory() {
        List<Item> inventory = factory.getInventory();
        StringBuilder sb = new StringBuilder();
        for (Item item : inventory) {
            sb.append(item.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.length() > 0 ? sb.toString() : "No items in inventory.", "Inventory", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showDisposeByproductDialog() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        DestroyByproductDialog dialog = new DestroyByproductDialog(parentFrame, factory);
        dialog.setVisible(true);
        // Optionally, refresh inventory label after disposal
        int currentInventory = factory.getInventory().stream().mapToInt(Item::getAmount).sum();
        lblInventory.setText(currentInventory + "/" + factory.getMaxCapacity());
    }

    // --- Inner dialog for destroying byproducts ---
    private static class DestroyByproductDialog extends JDialog {
        public DestroyByproductDialog(JFrame parent, Factory factory) {
            super(parent, "Destroy Byproduct", true);
            setLayout(new BorderLayout());
            setSize(350, 200);
            setLocationRelativeTo(parent);

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton btnBack = new JButton("Back");
            topPanel.add(btnBack);
            add(topPanel, BorderLayout.NORTH);

            // Use type checking for byproducts
            java.util.List<Item> byproducts = factory.getInventory().stream()
                    .filter(i -> i instanceof Byproduct && i.getAmount() > 0)
                    .toList();

            JComboBox<Item> byproductBox = new JComboBox<>(byproducts.toArray(new Item[0]));
            JTextField amountField = new JTextField();

            JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            formPanel.add(new JLabel("Byproduct:"));
            formPanel.add(byproductBox);
            formPanel.add(new JLabel("Amount:"));
            formPanel.add(amountField);
            add(formPanel, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnDispose = new JButton("Dispose");
            buttonPanel.add(btnDispose);
            add(buttonPanel, BorderLayout.SOUTH);

            // Disable controls if no byproducts
            if (byproducts.isEmpty()) {
                byproductBox.setEnabled(false);
                btnDispose.setEnabled(false);
                amountField.setEnabled(false);
                JOptionPane.showMessageDialog(this, "No byproducts available for disposal.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

            btnBack.addActionListener(e -> setVisible(false));
            btnDispose.addActionListener(e -> {
                try {
                    int selected = byproductBox.getSelectedIndex();
                    if (selected < 0) throw new Exception("Select a byproduct.");
                    int amount = Integer.parseInt(amountField.getText().trim());
                    if (amount <= 0) throw new Exception("Amount must be positive.");
                    Item byproduct = byproducts.get(selected);
                    // Use the actual byproduct object from inventory
                    factory.disposeByproduct((Byproduct) byproduct, amount);
                    JOptionPane.showMessageDialog(this, "Byproduct destroyed!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }


}