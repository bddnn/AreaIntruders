package buttons;

import javax.swing.*;
import java.awt.*;

public class ZasadyButton extends JButton {
    private int buttonWidth = 200;
    private int buttonHeight = 70;
    private int buttonX = (800 - buttonWidth) / 2;
    private int buttonY = (500 - buttonHeight) / 2;

    public ZasadyButton() {
        this.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        this.setText("Zasady gry");
        this.setForeground(Color.red);
        this.setFont(new Font("Serif", Font.BOLD, 25));
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setOpaque(false);
    }
}
