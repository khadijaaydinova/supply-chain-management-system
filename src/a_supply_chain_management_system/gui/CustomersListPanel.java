package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.Customer;
import a_supply_chain_management_system.Factory;
import a_supply_chain_management_system.controllers.CustomerController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomersListPanel extends JPanel {
    private MainFrame mainFrame;
    private JList<Customer> customerJList;
    private DefaultListModel<Customer> customerListModel;
    private CustomerController controller;

    public CustomersListPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.controller = new CustomerController();
        setLayout(new BorderLayout(10, 10));

        /*
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
        btnBack.setPreferredSize(new Dimension(80, 28));
        btnBack.addActionListener(e -> mainFrame.goBack(MainFrame.MAIN_MENU));
        backPanel.add(btnBack);
        topPanel.add(backPanel, BorderLayout.WEST);
        JLabel title = new JLabel("Customers", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(title, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

         */
        add(createTopPanel(), BorderLayout.NORTH);


        customerListModel = new DefaultListModel<>();
        refreshCustomerList();

        customerJList = new JList<>(customerListModel);
        customerJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(customerJList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdd = new JButton("Add New Customer");
        JButton btnEdit = new JButton("Edit Customer");
        JButton btnDelete = new JButton("Delete Customer");
        JButton btnView = new JButton("View Details");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnView);
        add(buttonPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addCustomer());
        btnEdit.addActionListener(e -> editCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnView.addActionListener(e -> viewCustomer());
    }

    private void refreshCustomerList() {
        customerListModel.clear();
        List<Customer> customers = controller.getAllCustomers();
        for (Customer c : customers) {
            customerListModel.addElement(c);
        }
    }

    private void addCustomer() {
        CustomerFormDialog dialog = new CustomerFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                controller.registerCustomer(dialog.getNameField(), dialog.getFundsField());
                refreshCustomerList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editCustomer() {
        Customer selected = customerJList.getSelectedValue();
        if (selected == null) return;
        CustomerFormDialog dialog = new CustomerFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), selected);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                selected.setName(dialog.getNameField());
                selected.setFunds(dialog.getFundsField());
                refreshCustomerList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCustomer() {
        Customer selected = customerJList.getSelectedValue();
        if (selected == null) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.removeCustomer(selected);
                refreshCustomerList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewCustomer() {
        Customer selected = customerJList.getSelectedValue();
        if (selected == null) return;
        // Use CardLayout navigation for working Back button:
        mainFrame.showDetailPanel(new CustomerDetailPanel(mainFrame, selected));
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
        btnBack.setPreferredSize(new Dimension(80, 28));
        btnBack.addActionListener(e -> mainFrame.goBack(MainFrame.MAIN_MENU));
        backPanel.add(btnBack);
        topPanel.add(backPanel, BorderLayout.WEST);
        JLabel title = new JLabel("Customers", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(title, BorderLayout.CENTER);
        return topPanel;
    }
}