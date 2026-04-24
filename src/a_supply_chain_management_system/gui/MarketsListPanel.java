package a_supply_chain_management_system.gui;

import a_supply_chain_management_system.Customer;
import a_supply_chain_management_system.Market;
import a_supply_chain_management_system.controllers.MarketController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MarketsListPanel extends JPanel {
    private MainFrame mainFrame;
    private JList<Market> marketJList;
    private DefaultListModel<Market> marketListModel;
    private MarketController controller;

    public MarketsListPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.controller = new MarketController();
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
        JLabel title = new JLabel("Markets", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(title, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        marketListModel = new DefaultListModel<>();
        refreshMarketList();

        marketJList = new JList<>(marketListModel);
        marketJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(marketJList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdd = new JButton("Add New Market");
        JButton btnEdit = new JButton("Edit Market");
        JButton btnDelete = new JButton("Delete Market");
        JButton btnView = new JButton("View Details");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnView);
        add(buttonPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addMarket());
        btnEdit.addActionListener(e -> editMarket());
        btnDelete.addActionListener(e -> deleteMarket());
        btnView.addActionListener(e -> viewMarket());
    }

    private void refreshMarketList() {
        marketListModel.clear();
        List<Market> markets = controller.getAllMarkets();
        for (Market m : markets) {
            marketListModel.addElement(m);
        }
    }

    private void addMarket() {
        MarketFormDialog dialog = new MarketFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                controller.registerMarket(dialog.getNameField(), dialog.getMaxCapacity(), dialog.getFunds());
                refreshMarketList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editMarket() {
        Market selected = marketJList.getSelectedValue();
        if (selected == null) return;
        MarketFormDialog dialog = new MarketFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), selected);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            try {
                selected.setName(dialog.getNameField());
                selected.setFunds(dialog.getFunds());
                selected.setMaxCapacity(dialog.getMaxCapacity());
                refreshMarketList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteMarket() {
        Market selected = marketJList.getSelectedValue();
        if (selected == null) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this market?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.removeMarket(selected);
                refreshMarketList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewMarket() {
        Market selected = marketJList.getSelectedValue();
        if (selected == null) return;
        // Use CardLayout navigation for working Back button:
        mainFrame.showDetailPanel(new MarketDetailPanel(mainFrame, selected));
    }
}