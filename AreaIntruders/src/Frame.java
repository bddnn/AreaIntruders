import components.GameEventListener;
import panels.GamePanel;
import panels.MenuPanel;
import panels.ZasadyPanel;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame implements GameEventListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "AREA INTRUDERS";
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);
    MenuPanel menuPanel = new MenuPanel();
    ZasadyPanel zasadyPanel = new ZasadyPanel(this);
    GamePanel gamePanel = new GamePanel(this);

    public Frame() {
        cardPanel.add(menuPanel, "MenuPanel");
        cardPanel.add(zasadyPanel, "ZasadyPanel");
        cardPanel.add(gamePanel, "GamePanel");
        this.add(cardPanel);

        this.setTitle(TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    public void showCard(String cardName) {
        cardLayout.show(cardPanel, cardName);
    }

    @Override
    public void onGameOver() {
        showCard("MenuPanel");
    }
}

