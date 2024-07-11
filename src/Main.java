import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import panels.GamePanel;

public class Main {
    public static void main(String[] args) {
        Frame frame = new Frame();
        frame.menuPanel.getStartButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.showCard("GamePanel");
                frame.gamePanel.initStartScreen();
                frame.gamePanel.requestFocusInWindow();
                frame.gamePanel.setFocusable(true);
            }
        });
        frame.menuPanel.getWyjscieButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        frame.menuPanel.getZasadyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.showCard("ZasadyPanel");
            }
        });
    }
}
