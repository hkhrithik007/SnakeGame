import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements KeyListener {

  private int[][] snake = new int[100][2]; // max snake length
  private int foodX, foodY;
  private int score = 0;
  private int snakeLength = 3;
  private int direction = 1; // 1: right, 2: down, 3: left, 4: up
  private boolean gameOver = false;
  private Random random = new Random();
  private Timer timer;

  public SnakeGame() {
    setPreferredSize(new Dimension(400, 400));
    setBackground(Color.BLACK);
    setFocusable(true);
    requestFocus();
    addKeyListener(this);

    // initialize snake
    for (int i = 0; i < snakeLength; i++) {
      snake[i][0] = 100 - i * 10; // x-coordinate
      snake[i][1] = 100; // y-coordinate
    }

    // generate food
    generateFood();

    // start game loop
    timer =
        new Timer(
            100,
            new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                moveSnake();
                checkCollision();
                repaint();
              }
            });
    timer.start();
  }

  private void generateFood() {
    foodX = random.nextInt(39) * 10; // 0-390
    foodY = random.nextInt(39) * 10; // 0-390
  }

  private void moveSnake() {
    for (int i = snakeLength - 1; i > 0; i--) {
      snake[i][0] = snake[i - 1][0];
      snake[i][1] = snake[i - 1][1];
    }

    switch (direction) {
      case 1: // right
        snake[0][0] += 10;
        break;
      case 2: // down
        snake[0][1] += 10;
        break;
      case 3: // left
        snake[0][0] -= 10;
        break;
      case 4: // up
        snake[0][1] -= 10;
        break;
    }
  }

  private void checkCollision() {
    // check wall collision
    if (snake[0][0] < 0 || snake[0][0] >= 400 || snake[0][1] < 0 || snake[0][1] >= 400) {
      gameOver = true;
    }

    // check self-collision
    for (int i = 1; i < snakeLength; i++) {
      if (snake[0][0] == snake[i][0] && snake[0][1] == snake[i][1]) {
        gameOver = true;
        break;
      }
    }

    // check food collision
    if (snake[0][0] == foodX && snake[0][1] == foodY) {
      score++;
      snakeLength++;
      generateFood();
    }

    if (gameOver) {
      timer.stop();
      JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score);
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.WHITE);

    // draw snake
    for (int i = 0; i < snakeLength; i++) {
      g.fillRect(snake[i][0], snake[i][1], 10, 10);
    }

    // draw food
    g.fillRect(foodX, foodY, 10, 10);

    // draw score
    g.setFont(new Font("Arial", Font.BOLD, 24));
    g.drawString("Score: " + score, 10, 30);
  }

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_UP:
        if (direction != 2) direction = 4;
        break;
      case KeyEvent.VK_DOWN:
        if (direction != 4) direction = 2;
        break;
      case KeyEvent.VK_LEFT:
        if (direction != 1) direction = 3;
        break;
      case KeyEvent.VK_RIGHT:
        if (direction != 3) direction = 1;
        break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {}

  public static void main(String[] args) {
    JFrame frame = new JFrame("Snake Game");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new SnakeGame());
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
