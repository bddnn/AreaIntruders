package buttons;

import javax.swing.*;
import java.awt.*;

public class StartButton extends JButton{
    private int buttonWidth = 200;
    private int buttonHeight = 50;
    public StartButton(){
        this.setText("Start");
        this.setForeground(Color.red);
        this.setFont(new Font("Serif", Font.BOLD, 25));
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setOpaque(false);

    }
}
