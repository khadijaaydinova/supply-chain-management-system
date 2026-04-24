package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.RawMaterialProducer;
import a_supply_chain_management_system.controllers.RawMaterialProducerController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RawMaterialProducersListPanel extends JPanel {
    private MainFrame mainFrame;
    private JList<RawMaterialProducer> producerJList;
    private DefaultListModel<RawMaterialProducer> producerListModel;
    private RawMaterialProducerController controller;

    public RawMaterialProducersListPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.controller = new RawMaterialProducerController();
        setLayout(new BorderLayout(10, 10));

        // Top panel with Back button and title
        add(createTopPanel(), BorderLayout.NORTH);

        producerListModel = new DefaultListModel<>();
        refreshProducerList();

        producerJList = new JList<>(producerListModel);
        producerJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(producerJList);
        add(scrollPane, BorderLayout.CENTER);

        add(createButtonPanel(), BorderLayout.SOUTH);

        // Double-click to view details
        producerJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewProducer();
                }
            }
        });

        // Enter key to view details
        producerJList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    viewProducer();
                }
            }
        });
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
        JLabel title = new JLabel("Raw Material Producers", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(title, BorderLayout.CENTER);
        return topPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdd = new JButton("Add New Producer");
        JButton btnEdit = new JButton("Edit Producer");
        JButton btnDelete = new JButton("Delete Producer");
        JButton btnView = new JButton("View Details");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnView);

        btnAdd.addActionListener(e -> addProducer());
        btnEdit.addActionListener(e -> editProducer());
        btnDelete.addActionListener(e -> deleteProducer());
        btnView.addActionListener(e -> viewProducer());

        return buttonPanel;
    }

    private void refreshProducerList() {
        producerListModel.clear();
        List<RawMaterialProducer> producers = controller.getAllProducers();
        for (RawMaterialProducer p : producers) {
            producerListModel.addElement(p);
        }
    }

    private void addProducer() {
        RawMaterialProducerFormDialog dialog = new RawMaterialProducerFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                controller.registerProducer(
                        dialog.getNameField(),
                        dialog.getMaterial(),
                        dialog.getGenerationCost(),
                        dialog.getSellingPrice(),
                        dialog.getMaxCapacity(),
                        dialog.getFunds()
                );
                refreshProducerList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editProducer() {
        RawMaterialProducer selected = producerJList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a producer to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        RawMaterialProducerFormDialog dialog = new RawMaterialProducerFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), selected);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                selected.setName(dialog.getNameField());
                selected.setFunds(dialog.getFunds());
                selected.setMaxCapacity(dialog.getMaxCapacity());
                selected.setMaterial(dialog.getMaterial());
                selected.setGenerationCost(dialog.getGenerationCost());
                selected.setSellingPrice(dialog.getSellingPrice());
                refreshProducerList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteProducer() {
        RawMaterialProducer selected = producerJList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a producer to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this producer?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.removeProducer(selected);
                refreshProducerList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewProducer() {
        RawMaterialProducer selected = producerJList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a producer to view.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        mainFrame.showDetailPanel(new RawMaterialProducerDetailPanel(mainFrame, selected));
    }
}
