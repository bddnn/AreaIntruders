package panels;

import buttons.StartButton;
import buttons.WyjscieButton;
import buttons.ZasadyButton;
import labels.MenuLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MenuPanel extends JPanel {
    StartButton startButton = new StartButton();
    ZasadyButton zasadyButton = new ZasadyButton();
    WyjscieButton wyjscieButton = new WyjscieButton();
    MenuLabel menuLabel = new MenuLabel();

    private JLabel topPlayersLabel = new JLabel("Top 10 Graczy", SwingConstants.CENTER);
    private JDialog topPlayersDialog;
    private JTextArea topPlayersTextArea;

    public MenuPanel() {
        this.setBackground(Color.BLACK);
        this.setBounds(0, 0, 800, 600);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addComponent(menuLabel);
        addComponent(startButton);
        addComponent(zasadyButton);
        addComponent(wyjscieButton);
        addComponent(topPlayersLabel);

        topPlayersLabel.setOpaque(true);

        initTopPlayersComponents();
    }

    private void addComponent(JComponent component) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        component.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        this.add(component);
    }

    private void initTopPlayersComponents() {
        topPlayersLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showTopPlayers();
            }
        });
    }

    private void showTopPlayers() {
        if (topPlayersDialog == null) {
            topPlayersDialog = new JDialog();
            topPlayersDialog.setTitle("Top 10 Graczy");
            topPlayersDialog.setSize(300, 400);
            topPlayersDialog.setLayout(new BorderLayout());
            topPlayersTextArea = new JTextArea();
            topPlayersTextArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(topPlayersTextArea);
            topPlayersDialog.add(scrollPane, BorderLayout.CENTER);
        }
        loadTopPlayers();
        topPlayersDialog.setVisible(true);
    }

    private void loadTopPlayers() {
        File file = new File("topPlayers.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            topPlayersTextArea.setText(sb.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Nie można załadować listy top 10 graczy.", "Błąd", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }


    public StartButton getStartButton() {
        return startButton;
    }

    public WyjscieButton getWyjscieButton() {
        return wyjscieButton;
    }

    public ZasadyButton getZasadyButton() {
        return zasadyButton;
    }
}
