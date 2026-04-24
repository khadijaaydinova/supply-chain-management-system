package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.RawMaterialProducer;
import a_supply_chain_management_system.RawMaterial;

import javax.swing.*;
import java.awt.*;

public class RawMaterialProducerFormDialog extends JDialog {
    private JTextField nameField, materialNameField, genCostField, sellPriceField, maxCapField, fundsField;
    private boolean saved = false;

    public RawMaterialProducerFormDialog(JFrame parent, RawMaterialProducer producer) {
        super(parent, producer == null ? "Add Producer" : "Edit Producer", true);
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(parent);

        // Top panel with Back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
        btnBack.setPreferredSize(new Dimension(80, 28));
        btnBack.addActionListener(e -> setVisible(false));
        topPanel.add(btnBack);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField(producer != null ? producer.getName() : "");
        formPanel.add(nameField);
        formPanel.add(new JLabel("Material Name:"));
        materialNameField = new JTextField(producer != null ? producer.getMaterial().getName() : "");
        formPanel.add(materialNameField);
        formPanel.add(new JLabel("Generation Cost:"));
        genCostField = new JTextField(producer != null ? String.valueOf(producer.getGenerationCost()) : "");
        formPanel.add(genCostField);
        formPanel.add(new JLabel("Selling Price:"));
        sellPriceField = new JTextField(producer != null ? String.valueOf(producer.getSellingPrice()) : "");
        formPanel.add(sellPriceField);
        formPanel.add(new JLabel("Max Capacity:"));
        maxCapField = new JTextField(producer != null ? String.valueOf(producer.getMaxCapacity()) : "");
        formPanel.add(maxCapField);
        formPanel.add(new JLabel("Funds:"));
        fundsField = new JTextField(producer != null ? String.valueOf(producer.getFunds()) : "");
        formPanel.add(fundsField);
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");
        formPanel.add(btnSave);
        formPanel.add(btnCancel);
        getContentPane().add(formPanel, BorderLayout.CENTER);

        btnSave.addActionListener(e -> {
            try {
                if (nameField.getText().trim().isEmpty() || materialNameField.getText().trim().isEmpty())
                    throw new Exception("Name and Material Name cannot be empty.");
                double genCost = Double.parseDouble(genCostField.getText().trim());
                double sellPrice = Double.parseDouble(sellPriceField.getText().trim());
                int maxCap = Integer.parseInt(maxCapField.getText().trim());
                double funds = Double.parseDouble(fundsField.getText().trim());
                if (genCost < 0 || sellPrice < 0 || maxCap < 0 || funds < 0)
                    throw new Exception("Numeric fields must be positive.");
                saved = true;
                setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancel.addActionListener(e -> setVisible(false));
    }

    public boolean isSaved() { return saved; }
    public String getNameField() { return nameField.getText().trim(); }
    public RawMaterial getMaterial() {
        return new RawMaterial(materialNameField.getText().trim(), getSellingPrice(), 0);
    }
    public double getGenerationCost() { return Double.parseDouble(genCostField.getText().trim()); }
    public double getSellingPrice() { return Double.parseDouble(sellPriceField.getText().trim()); }
    public int getMaxCapacity() { return Integer.parseInt(maxCapField.getText().trim()); }
    public double getFunds() { return Double.parseDouble(fundsField.getText().trim()); }
    public String getMaterialName() {
        return materialNameField.getText().trim();
    }
}