package buttons;

import javax.swing.*;
import java.awt.*;

public class WyjscieButton extends JButton {
    private int buttonWidth = 250;
    private int buttonHeight = 50;
    private int buttonX = (800 - buttonWidth) / 2;
    private int buttonY = (600 - buttonHeight) / 2;

    public WyjscieButton() {
        this.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        this.setText("Wyjdź z gry");
        this.setForeground(Color.red);
        this.setFont(new Font("Serif", Font.BOLD, 25));
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setOpaque(false);
    }
}
