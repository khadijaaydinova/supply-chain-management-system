package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.Factory;

import javax.swing.*;
import java.awt.*;

public class FactoryFormDialog extends JDialog {
    private JTextField nameField, maxCapField, fundsField;
    private boolean saved = false;

    public FactoryFormDialog(JFrame parent, Factory factory) {
        super(parent, factory == null ? "Add Factory" : "Edit Factory", true);
        setLayout(new BorderLayout());
        setSize(350, 200);
        setLocationRelativeTo(parent);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnBack = new JButton("Back");
        topPanel.add(btnBack);
        add(topPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField(factory != null ? factory.getName() : "");
        formPanel.add(nameField);

        formPanel.add(new JLabel("Max Capacity:"));
        maxCapField = new JTextField(factory != null ? String.valueOf(factory.getMaxCapacity()) : "");
        formPanel.add(maxCapField);

        formPanel.add(new JLabel("Funds:"));
        fundsField = new JTextField(factory != null ? String.valueOf(factory.getFunds()) : "");
        formPanel.add(fundsField);
        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save");
        buttonPanel.add(btnSave);
        add(buttonPanel, BorderLayout.SOUTH);

        btnBack.addActionListener(e -> setVisible(false));
        btnSave.addActionListener(e -> {
            try {
                if (nameField.getText().trim().isEmpty())
                    throw new Exception("Name cannot be empty.");
                int maxCap = Integer.parseInt(maxCapField.getText().trim());
                double funds = Double.parseDouble(fundsField.getText().trim());
                if (maxCap < 0 || funds < 0)
                    throw new Exception("Numeric fields must be positive.");
                saved = true;
                setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public boolean isSaved() { return saved; }
    public String getNameField() { return nameField.getText().trim(); }
    public int getMaxCapacity() { return Integer.parseInt(maxCapField.getText().trim()); }
    public double getFunds() { return Double.parseDouble(fundsField.getText().trim()); }
}