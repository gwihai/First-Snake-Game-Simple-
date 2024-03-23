import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int screenHeight = 750;
    static final int screenWidth = 750;
    static final int unitSize = 25;
    static final int gameUnits = (screenHeight * screenWidth) / unitSize;
    static final int delay = 100;
    final int[] x = new int[gameUnits];
    final int[] y = new int[gameUnits];

    int bodyParts = 6;
    int eatenApples, appleX, appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    void startGame() {
        newApple();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if(running) {
            //For loop draw gridlines
            for(int i = 0; i < screenHeight / unitSize; i++) {
                g.drawLine(i * unitSize, 0,i * unitSize, screenHeight);
                g.drawLine(0, i * unitSize, screenWidth, i * unitSize);
            }

            //Food
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, unitSize, unitSize);

            //draw snake
            for(int i = 0; i < bodyParts; i++) {
                if(i == 0) {
                    g.setColor(Color.CYAN); //head
                }
                else {
                    g.setColor(Color.BLUE);
                }
                g.fillRect(x[i], y[i], unitSize, unitSize);
                g.setColor(Color.RED);
                g.setFont(new Font("Times", Font.BOLD, 40));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("Score: " + eatenApples, (screenWidth - metrics.stringWidth("Score: " + eatenApples)) / 2, g.getFont().getSize());
            }
        }
        else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt(screenWidth / unitSize) * unitSize;
        appleY = random.nextInt(screenHeight / unitSize) * unitSize;
    }

    public void move() {
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch(direction) {
            case 'U':
                y[0] = y[0] - unitSize;
                break;
            case'D':
                y[0] = y[0] + unitSize;
                break;
            case 'L':
                x[0] = x[0] - unitSize;
                break;
            case 'R':
                x[0] = x[0] + unitSize;
                break;
        }
    }

    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            eatenApples++;
            newApple();
        }
    }

    public void checkCollision() {
        for(int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }
        if(x[0] < 0)
            running = false;
        if(x[0] > screenWidth)
            running = false;
        if(y[0] < 0)
            running = false;
        if(y[0] > screenHeight)
            running = false;
        if(!running)
            timer.stop();
    }

    public void gameOver(Graphics g) {
        //Score text
        g.setColor(Color.RED);
        g.setFont(new Font("Times", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + eatenApples, (screenWidth - metrics1.stringWidth("Score: " + eatenApples)) / 2, g.getFont().getSize());
        //Game over text
        g.setColor(Color.RED);
        g.setFont(new Font("Times", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over!", (screenWidth - metrics.stringWidth("Game Over!")) / 2, screenHeight / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R')
                        direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L')
                        direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D')
                        direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U')
                        direction = 'D';
                    break;
            }
        }
    }
}
