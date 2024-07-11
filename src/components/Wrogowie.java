package components;

import javax.swing.*;

public class Wrogowie extends Components{
    private Bomb bomb;

    public Wrogowie(int x, int y) {
        initAlien(x,y);
    }

    private void initAlien(int x, int y) {
        this.x = x;
        this.y = y;

        bomb = new Bomb(x,y);

        var wrogowieImg = "src/images/space_invader.png";
        var img = new ImageIcon(wrogowieImg);
        setImage(img.getImage());

    }

    public void act(int direction) {
        this.x += direction;
    }

    public Bomb getBomb() {
        return bomb;
    }

    public class Bomb extends Components {
        private boolean destroyed;

        public Bomb(int x, int y) {
            initBomb(x,y);
        }

        private void initBomb(int x, int y) {
            setDestroyed(true);

            this.x = x;
            this.y = y;

            var bombImg = "src/images/wavy_dash.png";
            var img = new ImageIcon(bombImg);
            setImage(img.getImage());
        }

        public void setDestroyed(boolean destroyed) {
            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {
            return destroyed;
        }

    }
}
