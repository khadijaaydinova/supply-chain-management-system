package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.controllers.RawMaterialProducerController;
import a_supply_chain_management_system.RawMaterialProducer;
import a_supply_chain_management_system.RawMaterial;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class RawMaterialProducersPanel extends JPanel {
    private RawMaterialProducerController controller;
    private JTable table;
    private RawMaterialProducerTableModel tableModel;
    private JFrame parentFrame;

    public RawMaterialProducersPanel(JFrame parent) {
        this.parentFrame = parent;
        this.controller = new RawMaterialProducerController();
        setLayout(new BorderLayout());
        tableModel = new RawMaterialProducerTableModel(controller.getAllProducers());
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Add Producer");
        JButton btnEdit = new JButton("Edit Producer");
        JButton btnDelete = new JButton("Delete Producer");
        JButton btnGenerate = new JButton("Generate Material");
        JButton btnBack = new JButton("Back");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnGenerate);
        buttonPanel.add(btnBack);
        JScrollPane buttonScroll = new JScrollPane(buttonPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(buttonScroll, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addProducer());
        btnEdit.addActionListener(e -> editProducer());
        btnDelete.addActionListener(e -> deleteProducer());
        btnGenerate.addActionListener(e -> generateMaterial());
        btnBack.addActionListener(e -> goBack());
    }

    private void addProducer() {
        RawMaterialProducerFormDialog dialog = new RawMaterialProducerFormDialog(parentFrame, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                RawMaterial material = new RawMaterial(dialog.getMaterialName(), dialog.getSellingPrice(), 0);
                controller.registerProducer(
                        dialog.getNameField(),
                        material,
                        dialog.getGenerationCost(),
                        dialog.getSellingPrice(),
                        dialog.getMaxCapacity(),
                        dialog.getFunds()
                );
                tableModel.setProducers(controller.getAllProducers());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editProducer() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        RawMaterialProducer producer = tableModel.getProducerAt(row);
        RawMaterialProducerFormDialog dialog = new RawMaterialProducerFormDialog(parentFrame, producer);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                producer.setName(dialog.getNameField());
                producer.setFunds(dialog.getFunds());
                producer.setMaxCapacity(dialog.getMaxCapacity());
                producer.setMaterial(new RawMaterial(dialog.getMaterialName(), dialog.getSellingPrice(), 0));
                producer.setGenerationCost(dialog.getGenerationCost());
                producer.setSellingPrice(dialog.getSellingPrice());
                tableModel.fireTableRowsUpdated(row, row);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteProducer() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        RawMaterialProducer producer = tableModel.getProducerAt(row);
        try {
            controller.removeProducer(producer);
            tableModel.setProducers(controller.getAllProducers());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateMaterial() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        RawMaterialProducer producer = tableModel.getProducerAt(row);
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to generate:");
        try {
            int amount = Integer.parseInt(amountStr);
            controller.generateMaterial(producer, amount);
            tableModel.fireTableRowsUpdated(row, row);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void goBack() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.getContentPane().removeAll();
        topFrame.getContentPane().add(new MainFrame().getContentPane());
        topFrame.revalidate();
        topFrame.repaint();
    }

    public static void showPanel(JFrame parent) {
        parent.getContentPane().removeAll();
        parent.getContentPane().add(new RawMaterialProducersPanel(parent));
        parent.revalidate();
        parent.repaint();
    }
}

class RawMaterialProducerTableModel extends AbstractTableModel {
    private List<RawMaterialProducer> producers;
    private final String[] columns = {"Name", "Funds", "Material", "Gen. Cost", "Sell Price", "Inventory/MaxCap"};

    public RawMaterialProducerTableModel(List<RawMaterialProducer> producers) {
        this.producers = producers;
    }

    public void setProducers(List<RawMaterialProducer> producers) {
        this.producers = producers;
        fireTableDataChanged();
    }

    public RawMaterialProducer getProducerAt(int row) {
        return producers.get(row);
    }

    @Override
    public int getRowCount() {
        return producers.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        RawMaterialProducer p = producers.get(row);
        switch (col) {
            case 0: return p.getName();
            case 1: return p.getFunds();
            case 2: return p.getMaterial().getName();
            case 3: return p.getGenerationCost();
            case 4: return p.getSellingPrice();
            case 5: return p.getInventory().stream().mapToInt(i -> i.getAmount()).sum() + "/" + p.getMaxCapacity();
            default: return null;
        }
    }
}

