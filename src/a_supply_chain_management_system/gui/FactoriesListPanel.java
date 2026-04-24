
package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.Factory;
import a_supply_chain_management_system.ProductDesign;
import a_supply_chain_management_system.controllers.FactoryController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FactoriesListPanel extends JPanel {
    private MainFrame mainFrame;
    private JList<Factory> factoryJList;
    private DefaultListModel<Factory> factoryListModel;
    private FactoryController controller;

    public FactoriesListPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.controller = new FactoryController();
        setLayout(new BorderLayout(10, 10));

        // Top panel with Back button and title
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
        btnBack.setPreferredSize(new Dimension(80, 28));
        btnBack.addActionListener(e -> mainFrame.goBack(MainFrame.MAIN_MENU));
        backPanel.add(btnBack);
        topPanel.add(backPanel, BorderLayout.WEST);
        JLabel title = new JLabel("Factories", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(title, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        factoryListModel = new DefaultListModel<>();
        refreshFactoryList();

        factoryJList = new JList<>(factoryListModel);
        factoryJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(factoryJList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdd = new JButton("Add New Factory");
        JButton btnEdit = new JButton("Edit Factory");
        JButton btnDelete = new JButton("Delete Factory");
        JButton btnView = new JButton("View Details");
        JButton btnAddDesign = new JButton("Add Product Design"); // NEW BUTTON
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnView);
        buttonPanel.add(btnAddDesign); // ADD TO PANEL
        add(buttonPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addFactory());
        btnEdit.addActionListener(e -> editFactory());
        btnDelete.addActionListener(e -> deleteFactory());
        btnView.addActionListener(e -> viewFactory());
        btnAddDesign.addActionListener(e -> addProductDesign()); // NEW LISTENER
    }

    private void refreshFactoryList() {
        factoryListModel.clear();
        List<Factory> factories = controller.getAllFactories();
        for (Factory f : factories) {
            factoryListModel.addElement(f);
        }
    }

    private void addFactory() {
        FactoryFormDialog dialog = new FactoryFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                controller.registerFactory(dialog.getNameField(), dialog.getMaxCapacity(), dialog.getFunds());
                refreshFactoryList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editFactory() {
        Factory selected = factoryJList.getSelectedValue();
        if (selected == null) return;
        FactoryFormDialog dialog = new FactoryFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), selected);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                selected.setName(dialog.getNameField());
                selected.setFunds(dialog.getFunds());
                selected.setMaxCapacity(dialog.getMaxCapacity());
                refreshFactoryList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteFactory() {
        Factory selected = factoryJList.getSelectedValue();
        if (selected == null) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this factory?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.removeFactory(selected);
                refreshFactoryList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewFactory() {
        Factory selected = factoryJList.getSelectedValue();
        if (selected == null) return;
        // Use CardLayout navigation for working Back button:
        mainFrame.showDetailPanel(new FactoryDetailPanel(mainFrame, selected));
    }

    // NEW: Add Product Design logic
    private void addProductDesign() {
        Factory selected = factoryJList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a factory first.", "No Factory Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ProductDesignFormDialog dialog = new ProductDesignFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                selected.addDesign(dialog.getProductDesign());
                JOptionPane.showMessageDialog(this, "Product design added!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

