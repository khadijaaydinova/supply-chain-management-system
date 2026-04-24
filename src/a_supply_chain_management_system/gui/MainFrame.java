package a_supply_chain_management_system.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Deque;
import java.util.ArrayDeque;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Deque<String> navigationStack = new ArrayDeque<>();
    private String currentPanel = MAIN_MENU;

    public static final String MAIN_MENU = "mainMenu";
    public static final String PRODUCERS = "producers";
    public static final String FACTORIES = "factories";
    public static final String MARKETS = "markets";
    public static final String CUSTOMERS = "customers";

    public MainFrame() {
        setTitle("Supply Chain Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel mainMenu = createMainMenuPanel();

        // Add all panels to cardPanel
        cardPanel.add(mainMenu, MAIN_MENU);
        cardPanel.add(new RawMaterialProducersListPanel(this), PRODUCERS);
        cardPanel.add(new FactoriesListPanel(this), FACTORIES);
        cardPanel.add(new MarketsListPanel(this), MARKETS);
        cardPanel.add(new CustomersListPanel(this), CUSTOMERS);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private JPanel createMainMenuPanel() {
        JPanel mainMenu = new JPanel(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JButton btnProducers = new JButton("Raw Material Producers");
        centerPanel.add(btnProducers, gbc);
        gbc.gridy++;
        JButton btnFactories = new JButton("Factories");
        centerPanel.add(btnFactories, gbc);
        gbc.gridy++;
        JButton btnMarkets = new JButton("Markets");
        centerPanel.add(btnMarkets, gbc);
        gbc.gridy++;
        JButton btnCustomers = new JButton("Customers");
        centerPanel.add(btnCustomers, gbc);

        mainMenu.add(centerPanel, BorderLayout.CENTER);

        btnProducers.addActionListener(e -> showPanel(PRODUCERS));
        btnFactories.addActionListener(e -> showPanel(FACTORIES));
        btnMarkets.addActionListener(e -> showPanel(MARKETS));
        btnCustomers.addActionListener(e -> showPanel(CUSTOMERS));

        return mainMenu;
    }

    public void showPanel(String name) {
        if (!name.equals(currentPanel)) {
            navigationStack.push(currentPanel);
            currentPanel = name;
        }
        cardLayout.show(cardPanel, name);
        revalidate();
        repaint();
    }

    public void showMainMenu() {
        showPanel(MAIN_MENU);
    }

    public void goBack(String fallbackPanelName) {
        if (!navigationStack.isEmpty()) {
            String previous = navigationStack.pop();
            currentPanel = previous;
            cardLayout.show(cardPanel, previous);
        } else {
            currentPanel = fallbackPanelName;
            cardLayout.show(cardPanel, fallbackPanelName);
        }
        revalidate();
        repaint();
    }

    /**
     * Show a detail panel (e.g., producer details) using CardLayout.
     * This ensures navigationStack and CardLayout are always in sync.
     */
    public void showDetailPanel(JPanel panel) {
        // Generate a unique key for this detail panel
        String detailKey = "detailPanel_" + System.identityHashCode(panel);
        cardPanel.add(panel, detailKey);
        navigationStack.push(currentPanel);
        currentPanel = detailKey;
        cardLayout.show(cardPanel, detailKey);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}