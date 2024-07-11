package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Gracz extends Components {
    private int width;
    private JButton leftButton;
    private JButton rightButton;
    private Image image;

    public Gracz() {
        initGracz();
    }

    private void initGracz() {

        int START_X = 400;
        setX(START_X);

        int START_Y = 465;
        setY(START_Y);

        leftButton = new JButton("Lewo");
        rightButton = new JButton("Prawo");

        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dx = -2;
            }
        });
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dx = 2;
            }
        });
    }

    public void setImage(Image image) {
        ImageIcon icon = new ImageIcon(image);
        this.width = icon.getIconWidth();
        this.image = image;
    }

    @Override
    public Image getImage() {
        return image;
    }

    public void act() {
        x += dx;

        if(x <= 2)
            x = 2;

        if (x >= Params.OKNO_WIDTH - 2 * width)
            x = Params.OKNO_WIDTH - 2 * width;

    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT)
            dx = -2;

        if (key == KeyEvent.VK_RIGHT)
            dx = 2;

    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT)
            dx = 0;

        if (key == KeyEvent.VK_RIGHT)
            dx = 0;
    }
}
