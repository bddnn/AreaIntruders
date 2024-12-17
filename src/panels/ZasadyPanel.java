package panels;

import components.GameEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ZasadyPanel extends JPanel {
    private JButton backButton;

    public ZasadyPanel(GameEventListener gameEventListener){
        this.setBackground(Color.PINK);
        this.setBounds(0,0,800,600);
        this.setLayout(new BorderLayout());

        JTextArea rulesText = new JTextArea();
        rulesText.setEditable(false);
        rulesText.setText("Rules:\n"
                + "1. Use the arrow keys to move your spaceship.\n"
                + "2. Press space to shoot bullets.\n"
                + "3. Destroy all enemies to win the game.\n"
                + "4. If an enemy reaches the bottom or hits your spaceship, you lose.\n");

        add(new JScrollPane(rulesText), BorderLayout.CENTER);

        rulesText.setVisible(true);
    }
}
