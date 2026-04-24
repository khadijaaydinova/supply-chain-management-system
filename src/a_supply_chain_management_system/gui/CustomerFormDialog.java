package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.Customer;

import javax.swing.*;
import java.awt.*;

public class CustomerFormDialog extends JDialog {
    private JTextField nameField, fundsField;
    private boolean saved = false;

    public CustomerFormDialog(JFrame parent, Customer customer) {
        super(parent, customer == null ? "Add Customer" : "Edit Customer", true);
        setLayout(new BorderLayout());
        setSize(300, 150);
        setLocationRelativeTo(parent);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnBack = new JButton("Back");
        topPanel.add(btnBack);
        add(topPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField(customer != null ? customer.getName() : "");
        formPanel.add(nameField);

        formPanel.add(new JLabel("Funds:"));
        fundsField = new JTextField(customer != null ? String.valueOf(customer.getFunds()) : "");
        formPanel.add(fundsField);
        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save");
        buttonPanel.add(btnSave);
        add(buttonPanel, BorderLayout.SOUTH);

        btnBack.addActionListener(e -> setVisible(false));
        btnSave.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String fundsStr = fundsField.getText().trim();
                if (name.isEmpty())
                    throw new Exception("Name cannot be empty.");
                double funds = Double.parseDouble(fundsStr);
                if (funds < 0)
                    throw new Exception("Funds must be a positive number.");
                saved = true;
                setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public boolean isSaved() { return saved; }
    public String getNameField() { return nameField.getText().trim(); }
    public double getFundsField() { return Double.parseDouble(fundsField.getText().trim()); }
}