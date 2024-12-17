package labels;

import javax.swing.*;
import java.awt.*;

public class MenuLabel extends JLabel {
    public MenuLabel(){
        this.setText("AREA INTRUDERS");
        this.setForeground(Color.RED);
        this.setFont(new Font("Serif", Font.BOLD, 40));
        this.setBounds(225, -25, 2500, 200);
    }
}
