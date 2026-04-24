package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.ProductDesign;
import a_supply_chain_management_system.RawMaterial;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDesignFormDialog extends JDialog {
    private JTextField nameField, priceField, byproductNameField, byproductAmountField, byproductCostField;
    private boolean saved = false;
    private ProductDesign productDesign;

    // For materials selection
    private List<RawMaterial> availableMaterials;
    private DefaultListModel<MaterialAmount> selectedMaterialsModel;
    private JList<MaterialAmount> selectedMaterialsList;
    private JComboBox<RawMaterial> materialComboBox;
    private JTextField materialAmountField;

    public ProductDesignFormDialog(JFrame parent, List<RawMaterial> availableMaterials) {
        super(parent, "Add Product Design", true);
        this.availableMaterials = availableMaterials;
        if (availableMaterials == null || availableMaterials.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No available materials provided!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        setLayout(new BorderLayout());
        setSize(500, 500);
        setLocationRelativeTo(parent);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.add(new JLabel("Product Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Product Price:"));
        priceField = new JTextField();
        formPanel.add(priceField);

        formPanel.add(new JLabel("Byproduct Name:"));
        byproductNameField = new JTextField();
        formPanel.add(byproductNameField);

        formPanel.add(new JLabel("Byproduct Amount:"));
        byproductAmountField = new JTextField();
        formPanel.add(byproductAmountField);

        formPanel.add(new JLabel("Byproduct Cost:"));
        byproductCostField = new JTextField();
        formPanel.add(byproductCostField);

        add(formPanel, BorderLayout.NORTH);

        // --- Materials selection section ---
        JPanel materialsPanel = new JPanel(new BorderLayout(5, 5));
        materialsPanel.setBorder(BorderFactory.createTitledBorder("Required Materials"));

        JPanel addMaterialPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        materialComboBox = new JComboBox<>(availableMaterials.toArray(new RawMaterial[0]));
        materialAmountField = new JTextField(5);
        JButton btnAddMaterial = new JButton("Add Material");
        addMaterialPanel.add(new JLabel("Material:"));
        addMaterialPanel.add(materialComboBox);
        addMaterialPanel.add(new JLabel("Amount:"));
        addMaterialPanel.add(materialAmountField);
        addMaterialPanel.add(btnAddMaterial);

        materialsPanel.add(addMaterialPanel, BorderLayout.NORTH);

        selectedMaterialsModel = new DefaultListModel<>();
        selectedMaterialsList = new JList<>(selectedMaterialsModel);
        selectedMaterialsList.setVisibleRowCount(5);
        materialsPanel.add(new JScrollPane(selectedMaterialsList), BorderLayout.CENTER);

        JButton btnRemoveMaterial = new JButton("Remove Selected");
        materialsPanel.add(btnRemoveMaterial, BorderLayout.SOUTH);

        add(materialsPanel, BorderLayout.CENTER);

        // --- Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Actions ---
        btnAddMaterial.addActionListener(e -> {
            RawMaterial selected = (RawMaterial) materialComboBox.getSelectedItem();
            String amountStr = materialAmountField.getText().trim();
            try {
                int amount = Integer.parseInt(amountStr);
                if (amount <= 0) throw new Exception("Amount must be positive.");
                MaterialAmount ma = new MaterialAmount(selected, amount);
                selectedMaterialsModel.addElement(ma);
                materialAmountField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRemoveMaterial.addActionListener(e -> {
            int idx = selectedMaterialsList.getSelectedIndex();
            if (idx >= 0) selectedMaterialsModel.remove(idx);
        });

        btnSave.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                String byproductName = byproductNameField.getText().trim();
                int byproductAmount = Integer.parseInt(byproductAmountField.getText().trim());
                double byproductCost = Double.parseDouble(byproductCostField.getText().trim());
                if (name.isEmpty() || byproductName.isEmpty()) {
                    throw new Exception("Product and byproduct names cannot be empty.");
                }
                if (price < 0 || byproductAmount < 0 || byproductCost < 0) {
                    throw new Exception("Numeric fields must be positive.");
                }
                productDesign = new ProductDesign(name, price, byproductName, byproductAmount, byproductCost);
                // Add selected materials
                for (int i = 0; i < selectedMaterialsModel.size(); i++) {
                    MaterialAmount ma = selectedMaterialsModel.get(i);
                    RawMaterial materialCopy = new RawMaterial(ma.material.getName(), ma.material.getPrice(), ma.amount);
                    productDesign.addRequiredMaterial(materialCopy);
                }
                saved = true;
                setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> setVisible(false));
    }

    public boolean isSaved() {
        return saved;
    }

    public ProductDesign getProductDesign() {
        return productDesign;
    }

    // Helper class for material + amount
    private static class MaterialAmount {
        RawMaterial material;
        int amount;

        MaterialAmount(RawMaterial material, int amount) {
            this.material = material;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return material.getName() + " (amount: " + amount + ")";
        }
    }
}
