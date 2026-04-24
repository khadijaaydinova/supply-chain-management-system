package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.ProductDesign;
import a_supply_chain_management_system.RawMaterial;
import a_supply_chain_management_system.SupplyChainManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FactoryDesignProductDialog extends JDialog {
    private JTextField productNameField, productCostField, byproductNameField, byproductAmountField, byproductCostField;
    private DefaultListModel<RawMaterial> materialsModel;
    private JList<RawMaterial> materialsList;
    private JComboBox<String> materialComboBox;
    private JTextField matAmountField;
    private boolean saved = false;
    private ProductDesign design;

    public FactoryDesignProductDialog(JFrame parent) {
        super(parent, "Design Product", true);
        setSize(400, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.add(new JLabel("Product Name:"));
        productNameField = new JTextField();
        formPanel.add(productNameField);

        formPanel.add(new JLabel("Product Cost:"));
        productCostField = new JTextField();
        formPanel.add(productCostField);

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

        // Materials section
        JPanel materialsPanel = new JPanel(new BorderLayout(5, 5));
        materialsPanel.setBorder(BorderFactory.createTitledBorder("Required Raw Materials"));
        materialsModel = new DefaultListModel<>();
        materialsList = new JList<>(materialsModel);
        materialsPanel.add(new JScrollPane(materialsList), BorderLayout.CENTER);

        JPanel addMaterialPanel = new JPanel(new FlowLayout());
        // ComboBox for material selection
        List<RawMaterial> allMaterials = new ArrayList<>();
        for (RawMaterial rm : SupplyChainManager.getInstance().getProducers().stream().map(p -> p.getMaterial()).toList()) {
            allMaterials.add(rm);
        }
        // Only unique material names
        java.util.Set<String> uniqueMaterialNames = new java.util.LinkedHashSet<>();
        for (RawMaterial rm : allMaterials) {
            uniqueMaterialNames.add(rm.getName());
        }
        materialComboBox = new JComboBox<>(uniqueMaterialNames.toArray(new String[0]));
        matAmountField = new JTextField(4);
        JButton btnAddMaterial = new JButton("Add");
        addMaterialPanel.add(new JLabel("Material:"));
        addMaterialPanel.add(materialComboBox);
        addMaterialPanel.add(new JLabel("Amount:"));
        addMaterialPanel.add(matAmountField);
        addMaterialPanel.add(btnAddMaterial);
        materialsPanel.add(addMaterialPanel, BorderLayout.SOUTH);

        btnAddMaterial.addActionListener(e -> {
            String name = (String) materialComboBox.getSelectedItem();
            String amountStr = matAmountField.getText().trim();
            try {
                if (name == null || name.isEmpty()) throw new Exception("Select a material.");
                int amount = Integer.parseInt(amountStr);
                if (amount <= 0) throw new Exception("Amount must be positive.");
                materialsModel.addElement(new RawMaterial(name, 0, amount));
                matAmountField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(materialsPanel, BorderLayout.CENTER);

        // Save/Cancel
        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Save Design");
        JButton btnCancel = new JButton("Cancel");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            try {
                String prodName = productNameField.getText().trim();
                double prodCost = Double.parseDouble(productCostField.getText().trim());
                String bypName = byproductNameField.getText().trim();
                int bypAmount = Integer.parseInt(byproductAmountField.getText().trim());
                double bypCost = Double.parseDouble(byproductCostField.getText().trim());
                if (prodName.isEmpty() || bypName.isEmpty())
                    throw new Exception("Product and byproduct names required.");
                if (prodCost < 0 || bypAmount < 0 || bypCost < 0)
                    throw new Exception("Numeric fields must be positive.");
                if (materialsModel.isEmpty())
                    throw new Exception("At least one raw material required.");
                design = new ProductDesign(prodName, prodCost, bypName, bypAmount, bypCost);
                for (int i = 0; i < materialsModel.size(); i++) {
                    design.addRequiredMaterial(materialsModel.get(i));
                }
                saved = true;
                setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancel.addActionListener(e -> setVisible(false));
    }

    public boolean isSaved() { return saved; }
    public ProductDesign getDesign() { return design; }
}