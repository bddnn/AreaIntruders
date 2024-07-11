package panels;

import buttons.StartButton;
import buttons.WyjscieButton;
import buttons.ZasadyButton;
import components.*;
import labels.MenuLabel;
import labels.ScoreLabel;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel {
    private GameEventListener gameEventListener;
    private JLabel pauseLabel;
    private ImageIcon ship1Icon = new ImageIcon("src/images/statek1.png");
    private ImageIcon ship2Icon = new ImageIcon("src/images/statek2.png");
    private ImageIcon ship3Icon = new ImageIcon("src/images/statek3.png");

    private int score = 0;
    private List<String> topPlayers = new ArrayList<>();
    private List<String> topScores = new ArrayList<>();

    private Dimension d;
    private List<Wrogowie> wrogowie;

    private Gracz gracz = new Gracz();
    private Strzelanie strzelanie = new Strzelanie();

    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String message = "Przegrana";

    private Timer timer;

    private boolean enemiesShootingEnabled = true;

    private JButton leftButton;
    private JButton rightButton;
    private JButton shootButton;
    private JButton restartButton;
    private JButton pauseButton;
    private JButton backButton;

    private int moveDirection = 0;
    private Timer moveTimer;

    private ScoreLabel scoreLabel = new ScoreLabel();


    private String playerNick;
    private boolean showStartScreen = true;
    private int enemyCount;
    private int enemySpeed;

    private boolean isPaused = false;

    public GamePanel(GameEventListener listener){
        initGamePanel();
        gameInit();
        initButtons();
        this.add(scoreLabel);
        this.gameEventListener = listener;
    }

    private void initGamePanel() {
        addKeyListener(new TAdapter());

        d = new Dimension(Params.OKNO_WIDTH, Params.OKNO_HEIGHT);
        setBackground(Color.BLACK);
        setBounds(0,0,800,600);
        setLayout(null);

        timer = new Timer(Params.DELAY, new GameCycle());

        initUIComponents();
    }

    public void startGame() {
        gameInit();
        timer.start();
    }

    private void gameInit() {
        wrogowie = new ArrayList<>();

        for(int i = 0; i < enemyCount; i++){
            for (int j = 0; j < enemyCount; j++){
                var wrog = new Wrogowie(Params.WROGOWIE_INIT_X + 50 * j, Params.WROGOWIE_INIT_Y + 40 * i);
                wrogowie.add(wrog);
            }
        }
    }

    private void updateScore() {
        score++;
        scoreLabel.setText("Wynik: " + score);
        scoreLabel.setSize(scoreLabel.getPreferredSize());
        if(score == 10) {
            JOptionPane.showMessageDialog(this, "Gratulacje! Zdobyłeś 10 punktów!");
            addToTopPlayers(playerNick);
        }
    }

    private void saveScoreToFile(String nick, int score) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("topPlayers.txt", true)))) {
            out.println(nick + " - " + score);
        } catch (IOException e) {
            System.out.println("Błąd zapisu do pliku: " + e.getMessage());
        }
    }

    private void addToTopPlayers(String playerNick) {
        topPlayers.add(playerNick + " - " + score);
        if(topPlayers.size() > 10) {
            topPlayers = new ArrayList<>(topPlayers.subList(0, 10));
        }
    }
    private void drawWrogowie(Graphics g){
        for(Wrogowie wrogowie : wrogowie) {
            if(wrogowie.isVisible()){
                g.drawImage(wrogowie.getImage(), wrogowie.getX(), wrogowie.getY(), this);
            }

            if(wrogowie.isDying()) {
                wrogowie.die();
            }
        }
    }

    private void drawGracz(Graphics g){
        if(gracz.isVisible()){
            g.drawImage(gracz.getImage(), gracz.getX(), gracz.getY(), this);
        }

        if(gracz.isDying()){
            gracz.die();
            inGame = false;
        }
    }

    private void drawStrzelanie(Graphics g){
        if(strzelanie.isVisible()){
            g.drawImage(strzelanie.getImage(), strzelanie.getX(), strzelanie.getY(), this);
        }
    }

    private void drawBombs(Graphics g){
        for(Wrogowie wrogowie : wrogowie){
            Wrogowie.Bomb b = wrogowie.getBomb();

            if(!b.isDestroyed()){
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    public void initStartScreen() {
        JDialog settingsDialog = createSettingsDialog();
        addComponentsToDialog(settingsDialog);
        settingsDialog.setLocationRelativeTo(null);
        settingsDialog.setVisible(true);
        startGame();
    }

    private void addShipSelectionCheckboxes(JDialog settingsDialog) {
        JCheckBox ship1Checkbox = new JCheckBox("Statek 1", new ImageIcon("src/images/statek1.png"));
        ship1Checkbox.setActionCommand("ship1");
        JCheckBox ship2Checkbox = new JCheckBox("Statek 2", new ImageIcon("src/images/statek2.png"));
        ship2Checkbox.setActionCommand("ship2");
        JCheckBox ship3Checkbox = new JCheckBox("Statek 3", new ImageIcon("src/images/statek3.png"));
        ship3Checkbox.setActionCommand("ship3");

        ButtonGroup shipsGroup = new ButtonGroup();
        shipsGroup.add(ship1Checkbox);
        shipsGroup.add(ship2Checkbox);
        shipsGroup.add(ship3Checkbox);

        settingsDialog.add(new JLabel("Wybierz swój statek:"));
        settingsDialog.add(ship1Checkbox);
        settingsDialog.add(ship2Checkbox);
        settingsDialog.add(ship3Checkbox);

        ItemListener shipSelectionListener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                AbstractButton aButton = (AbstractButton)e.getSource();
                boolean selected = e.getStateChange() == ItemEvent.SELECTED;
                if(selected) {
                    Image shipImage = getShipImage(aButton.getActionCommand());
                    gracz.setImage(shipImage);
                    repaint();
                }
            }
        };

        ship1Checkbox.addItemListener(shipSelectionListener);
        ship2Checkbox.addItemListener(shipSelectionListener);
        ship3Checkbox.addItemListener(shipSelectionListener);
    }

    private JDialog createSettingsDialog() {
        JDialog settingsDialog = new JDialog();
        settingsDialog.setSize(500, 510);
        settingsDialog.setModal(true);
        settingsDialog.setLayout(new BoxLayout(settingsDialog.getContentPane(), BoxLayout.PAGE_AXIS));
        return settingsDialog;
    }

    private void addComponentsToDialog(JDialog settingsDialog) {
        JCheckBox enemiesShootingCheckbox = new JCheckBox("Strzelanie wrogów", enemiesShootingEnabled);
        enemiesShootingCheckbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                enemiesShootingEnabled = (e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        JTextField playerNickField = createTextField("");
        JSlider enemyCountSlider = createSlider(1, 10, "Ilość wrogów w linii:");
        JSlider enemySpeedSlider = createSlider(1, 10, "Prędkość opadania:");
        JButton easyButton = createButton("Łatwy", createDifficultyListener(enemyCountSlider, enemySpeedSlider, enemiesShootingCheckbox));
        JButton mediumButton = createButton("Średni", createDifficultyListener(enemyCountSlider, enemySpeedSlider, enemiesShootingCheckbox));
        JButton hardButton = createButton("Trudny", createDifficultyListener(enemyCountSlider, enemySpeedSlider, enemiesShootingCheckbox));
        JButton extremeButton = createButton("Ekstremalny", createDifficultyListener(enemyCountSlider, enemySpeedSlider, enemiesShootingCheckbox));
        JButton startButton = createStartButton(playerNickField, enemyCountSlider, enemySpeedSlider, settingsDialog);

        JRadioButton ship1Button = new JRadioButton(ship1Icon);
        ship1Button.setActionCommand("ship1");
        JRadioButton ship2Button = new JRadioButton(ship2Icon);
        ship2Button.setActionCommand("ship2");
        JRadioButton ship3Button = new JRadioButton(ship3Icon);
        ship3Button.setActionCommand("ship3");

        ButtonGroup shipsGroup = new ButtonGroup();
        shipsGroup.add(ship1Button);
        shipsGroup.add(ship2Button);
        shipsGroup.add(ship3Button);

        settingsDialog.add(new JLabel("Podaj swoją nazwę: "));
        settingsDialog.add(playerNickField);
        addShipSelectionCheckboxes(settingsDialog);
        settingsDialog.add(new JLabel("Wybierz poziom trudności, bądź utwórz swój własny."));
        settingsDialog.add(easyButton);
        settingsDialog.add(mediumButton);
        settingsDialog.add(hardButton);
        settingsDialog.add(extremeButton);
        settingsDialog.add(new JLabel("Ilość wrogów:"));
        settingsDialog.add(enemyCountSlider);
        settingsDialog.add(new JLabel("Prędkość opadania:"));
        settingsDialog.add(enemySpeedSlider);
        settingsDialog.add(enemiesShootingCheckbox);
        settingsDialog.add(startButton);

        ActionListener shipSelectionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedShip = e.getActionCommand();
                Image shipImage = getShipImage(selectedShip);
                gracz.setImage(shipImage);
                repaint();
            }
        };

        ship1Button.addActionListener(shipSelectionListener);
        ship2Button.addActionListener(shipSelectionListener);
        ship3Button.addActionListener(shipSelectionListener);

    }

    private JTextField createTextField(String text) {
        JTextField textField = new JTextField(text);
        return textField;
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setActionCommand(text);
        button.addActionListener(listener);

        return button;
    }

    private JButton createStartButton(JTextField playerNickField, JSlider enemyCountSlider, JSlider enemySpeedSlider, JDialog settingsDialog) {
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerNick = playerNickField.getText();
                enemyCount = enemyCountSlider.getValue();
                enemySpeed = enemySpeedSlider.getValue();
                settingsDialog.dispose();
            }
        });
        return startButton;
    }

    private JSlider createSlider(int min, int max, String label) {
        JSlider slider = new JSlider(min, max);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        return slider;
    }

    private ActionListener createDifficultyListener(JSlider enemyCountSlider, JSlider enemySpeedSlider, JCheckBox enemiesShootingCheckbox) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String difficulty = e.getActionCommand();
                int enemySpeed;
                int enemyCount;
                boolean shootingEnabled;

                if("Łatwy".equals(difficulty)) {
                    enemySpeed = 1;
                    enemyCount = 5;
                    shootingEnabled = false;
                } else if("Średni".equals(difficulty)) {
                    enemySpeed = 4;
                    enemyCount = 6;
                    shootingEnabled = false;
                } else if("Trudny".equals(difficulty)) {
                    enemySpeed = 7;
                    enemyCount = 8;
                    shootingEnabled = false;
                } else if("Ekstremalny".equals(difficulty)) {
                    enemySpeed = 10;
                    enemyCount = 7;
                    shootingEnabled = true;
                } else {
                    return;
                }

                enemyCountSlider.setValue(enemyCount);
                enemySpeedSlider.setValue(enemySpeed);
                enemiesShootingCheckbox.setSelected(shootingEnabled);
            }
        };
    }

    private Image getShipImage(String shipName) {
        ImageIcon shipIcon = null;
        switch (shipName) {
            case "ship1":
                shipIcon = new ImageIcon("src/images/statek1.png");
                break;
            case "ship2":
                shipIcon = new ImageIcon("src/images/statek2.png");
                break;
            case "ship3":
                shipIcon = new ImageIcon("src/images/statek3.png");
                break;
        }
        return shipIcon != null ? shipIcon.getImage() : null;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(showStartScreen)
            ;
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.GREEN);

        if(inGame) {
            g.drawLine(0,  Params.GROUND, Params.OKNO_WIDTH, Params.GROUND);

            drawWrogowie(g);
            drawGracz(g);
            drawStrzelanie(g);
            drawBombs(g);

        } else {
            if(timer.isRunning()){
                timer.stop();
            }
            gameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
            pauseLabel.setVisible(true);
            backButton.setVisible(true);
        } else {
            timer.start();
            pauseLabel.setVisible(false);
            backButton.setVisible(false);
        }
    }

    private void restartGame() {
        this.revalidate();
        this.repaint();

        this.add(restartButton);
        this.add(backButton);

        timer.stop();
        inGame = true;
        deaths = 0;
        scoreLabel.setText("Wynik: 0");

        gracz = new Gracz();
        wrogowie.clear();
        gracz.setImage(getShipImage("ship1"));
        startGame();
        gameInit();

        if (!timer.isRunning()) {
            timer.start();
        }

        if(backButton != null) {
            backButton.setVisible(false);
            this.remove(backButton);
        }

        if(restartButton != null) {
            restartButton.setVisible(false);
            this.remove(restartButton);
        }
    }

    private void initUIComponents() {
        pauseLabel = new JLabel("Gra jest zatrzymana");
        pauseLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        pauseLabel.setForeground(Color.WHITE);
        pauseLabel.setBounds(250, 200, 300, 30);
        pauseLabel.setVisible(false);
        this.add(pauseLabel);

        backButton = new JButton("Powrót do menu");
        backButton.setBounds(Params.OKNO_WIDTH / 2 - 75, Params.OKNO_HEIGHT / 2 + 100, 150, 30);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameEventListener.onGameOver();
            }
        });
        backButton.setVisible(false);
        this.add(backButton);
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Params.OKNO_WIDTH, Params.OKNO_WIDTH);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Params.OKNO_WIDTH / 2 - 30, Params.OKNO_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Params.OKNO_WIDTH / 2 - 30, Params.OKNO_WIDTH - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (Params.OKNO_WIDTH - fontMetrics.stringWidth(message)) / 2, Params.OKNO_HEIGHT / 2);

        if(restartButton == null) {
            restartButton = new JButton("Restart");
            restartButton.setBounds(Params.OKNO_WIDTH / 2 - 50, Params.OKNO_HEIGHT / 2 + 50, 100, 30);
            restartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    restartGame();
                    requestFocusInWindow();
                }
            });
            this.add(restartButton);
        }
        restartButton.setVisible(true);

        if(backButton == null) {
            backButton = new JButton("Powrót do menu");
            backButton.setBounds(Params.OKNO_WIDTH / 2 - 75, Params.OKNO_HEIGHT / 2 + 50, 150, 30);
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gameEventListener.onGameOver();
                }
            });
            this.add(backButton);
        }
        backButton.setVisible(true);

        if(inGame == false) {
            saveScoreToFile(playerNick, deaths);
        }
    }

    private void initButtons() {
        int buttonWidth = 80;
        int buttonHeight = 30;
        int spaceBetweenButtons = 20;
        int bottomPadding = 10;

        int firstButtonX = (Params.OKNO_WIDTH - (3 * buttonWidth + 2 * spaceBetweenButtons)) / 2;
        int buttonsY = Params.OKNO_HEIGHT - buttonHeight - bottomPadding;

        leftButton = new JButton("Lewo");
        leftButton.setBounds(firstButtonX, buttonsY, buttonWidth, buttonHeight);
        leftButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                moveDirection = -1;
                startMoving();
                requestFocusInWindow();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                moveDirection = 0;
            }
        });

        shootButton = new JButton("Strzał");
        shootButton.setBounds(firstButtonX + buttonWidth + spaceBetweenButtons, buttonsY, buttonWidth, buttonHeight);
        shootButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performShooting();
                requestFocusInWindow();
            }
        });

        rightButton = new JButton("Prawo");
        rightButton.setBounds(firstButtonX + 2 * (buttonWidth + spaceBetweenButtons), buttonsY, buttonWidth, buttonHeight);
        rightButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                moveDirection = 1;
                startMoving();
                requestFocusInWindow();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                moveDirection = 0;
            }
        });


        pauseButton = new JButton("Pauza");
        pauseButton.setBounds(10, Params.OKNO_HEIGHT - buttonHeight - bottomPadding, buttonWidth, buttonHeight);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePause();
                requestFocusInWindow();
            }
        });

        scoreLabel.setBounds(Params.OKNO_WIDTH - scoreLabel.getPreferredSize().width - 100, Params.OKNO_HEIGHT - scoreLabel.getPreferredSize().height - bottomPadding + 20, scoreLabel.getPreferredSize().width, scoreLabel.getPreferredSize().height);

        this.add(leftButton);
        this.add(shootButton);
        this.add(rightButton);
        this.add(pauseButton);
        this.add(scoreLabel);
        this.setFocusable(true);
        this.requestFocusInWindow();

        moveTimer = new Timer(Params.DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (moveDirection != 0) {
                    gracz.setX(gracz.getX() + moveDirection * 2);
                    repaint();
                }
            }
        });
    }

    private void performShooting() {
        int x = gracz.getX();
        int y = gracz.getY();

        if (inGame) {
            if (!strzelanie.isVisible()) {
                strzelanie = new Strzelanie(x, y);
            }
        }
    }

    private void startMoving() {
        if (!moveTimer.isRunning()) {
            moveTimer.start();
        }
    }

    private void update() {
        if(deaths == enemyCount * enemyCount){
            inGame = false;
            timer.stop();
            message = "Wygrana!";
        }

        gracz.act();


        if(strzelanie.isVisible()) {
            int strzelanieX = strzelanie.getX();
            int strzelanieY = strzelanie.getY();

            for (Wrogowie wrog : wrogowie) {
                int wrogX = wrog.getX();
                int wrogY = wrog.getY();

                if (wrog.isVisible() && strzelanie.isVisible()) {
                    if (strzelanieX >= wrogX && strzelanieX <= wrogX + Params.WROGOWIE_WIDTH && strzelanieY >= wrogY && strzelanieY <= wrogY + Params.WROGOWIE_HEIGHT) {
                        wrog.setDying(true);
                        deaths++;
                        strzelanie.die();
                        scoreLabel.setText("Wynik: " + deaths);
                        updateScore();
                    }
                }
            }

            int y = strzelanie.getY();
            y -= 7;

            if (y < 0) {
                strzelanie.die();
            } else {
                strzelanie.setY(y);
            }
        }
        for (Wrogowie wrog : wrogowie) {
            int x = wrog.getX();
            if (x >= Params.OKNO_WIDTH - Params.OKNO_RIGHT && direction != -1) {
                direction = -1;

                Iterator<Wrogowie> i1 = wrogowie.iterator();
                while (i1.hasNext()) {
                    Wrogowie w = i1.next();
                    w.setY(w.getY() + enemySpeed * 5);
                }
            }

            if (x <= Params.OKNO_LEFT && direction != 1) {
                direction = 1;

                Iterator<Wrogowie> i2 = wrogowie.iterator();
                while (i2.hasNext()) {
                    Wrogowie w2 = i2.next();
                    w2.setY(w2.getY() + enemySpeed * 5);
                }
            }
        }

            Iterator<Wrogowie> it = wrogowie.iterator();
            while(it.hasNext()) {
                Wrogowie wrog = it.next();
                if(wrog.isVisible()) {
                    int y = wrog.getY();
                    if(y > Params.GROUND - Params.WROGOWIE_HEIGHT){
                        inGame = false;
                        message = "Przegrana.";
                    }
                    wrog.act(direction);
                }
            }

            var generator = new Random();

            for (Wrogowie wrog : wrogowie) {
                if (enemiesShootingEnabled) {
                    int strzal = generator.nextInt(15);
                    Wrogowie.Bomb bomb = wrog.getBomb();

                    if (strzal == Params.CHANCE && wrog.isVisible() && bomb.isDestroyed()) {
                        bomb.setDestroyed(false);
                        bomb.setX(wrog.getX());
                        bomb.setY(wrog.getY());
                    }

                    int bombX = bomb.getX();
                    int bombY = bomb.getY();
                    int graczX = gracz.getX();
                    int graczY = gracz.getY();

                    if (gracz.isVisible() && !bomb.isDestroyed()) {
                        if (bombX >= graczX && bombX <= graczX + Params.GRACZ_WIDTH && bombY >= graczY && bombY <= graczY + Params.GRACZ_HEIGHT) {
                            gracz.setDying(true);
                            bomb.setDestroyed(true);
                        }
                    }

                    if (!bomb.isDestroyed()) {
                        bomb.setY(bomb.getY() + 1);
                        if (bomb.getY() >= Params.GROUND - Params.BOMB_HEIGHT) {
                            bomb.setDestroyed(true);
                        }
                    }
                }
            }
        }


    private void doGameCycle() {
        if(!isPaused) {
            update();
        }

        repaint();
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            gracz.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            gracz.keyPressed(e);

            int key = e.getKeyCode();

            if(key == KeyEvent.VK_SPACE){
                performShooting();
            }

            if (key == KeyEvent.VK_ESCAPE) {
                togglePause();
            }
        }

    }
}
