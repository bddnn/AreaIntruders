package labels;

import javax.swing.*;
import java.awt.*;

public class ScoreLabel extends JLabel {
    public ScoreLabel() {
        this.setText("Wynik: " + 0);
        this.setForeground(Color.RED);
        this.setFont(new Font("Serif", Font.BOLD, 20));
        this.setVisible(true);
    }
}
