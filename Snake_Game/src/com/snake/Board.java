package com.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


@SuppressWarnings("unused")
public class Board extends JPanel implements ActionListener {

    private final int B_Width = 300;
    private final int B_Height = 300;
    private final int Dot_Size = 10;
    private final int All_Dots = 900;
    private final int Rand_Pos = 29;
    private final int Delay = 140;

    private final int x[] = new int[All_Dots];
    private final int y[] = new int[All_Dots];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;
    private Image backgroundImage;


    public Board() {
        initBoard();
        loadBackgroundImage();
    }

    private void loadBackgroundImage() {
        // Load the image using ImageIcon
        ImageIcon bgImageIcon = new ImageIcon("src/Resource/background.jpeg");
        backgroundImage = bgImageIcon.getImage();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_Width, B_Height));
        loadImages();
        initGame();
    }

    private void loadImages() {
        ImageIcon iid = new ImageIcon("src/Resource/dot.png");
        ball = iid.getImage();  // Image for the snake's body
    
        ImageIcon iia = new ImageIcon("src/Resource/apple.png");
        apple = iia.getImage();  // Image for the apple
    
        ImageIcon iih = new ImageIcon("src/Resource/head.png");
        head = iih.getImage();  // Image for the snake's head
    }
    

    private void initGame() {
        dots = 3;  // The initial size of the snake
    
        // Initialize snake's position in the center of the game board
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * Dot_Size;  // Adjust the x-position to avoid going out of bounds
            y[z] = 50;  // Center the snake vertically
        }
    
        locateApple();  // Correctly position the apple
    
        timer = new Timer(Delay, this);
        timer.start();
    }
    

    private void locateApple() {
        int r = (int) (Math.random() * Rand_Pos);
        apple_x = ((r * Dot_Size));

        r = (int) (Math.random() * Rand_Pos);
        apple_y = ((r * Dot_Size));
    }

    // public void paintComponent(Graphics g) {
    //     super.paintComponent(g);
    //     doDrawing(g);
    // }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Resizing the image to fit the panel

        // Continue drawing game objects
        if (inGame) {
            drawObjects(g); // Your game objects like snake, apple, etc.
        } else {
            gameOver(g); // Draw game over message or components
        }
    }

    private void drawObjects(Graphics g) {
        // Draw the apple
        g.drawImage(apple, apple_x, apple_y, this);
    
        // Draw the snake
        for (int i = 0; i < dots; i++) {
            if (i == 0) {
                g.drawImage(head, x[i], y[i], this);  // Snake's head
            } else {
                g.drawImage(ball, x[i], y[i], this);  // Snake's body
            }
        }
    
        // You can also draw other elements, like a score or boundaries, if needed
    }
    
    private void doDrawing(Graphics g) {
        if (inGame) {
            // Draw the apple in its correct position
            g.drawImage(apple, apple_x, apple_y, this);  

            // for (int z = 0; z < dots; z++) {
            //     System.out.println("x[" + z + "] = " + x[z] + ", y[" + z + "] = " + y[z]);
            //     if (z == 0) {
            //         g.drawImage(head, x[z], y[z], this);  // Draw head
            //     } else {
            //         g.drawImage(ball, x[z], y[z], this);  // Draw body
            //     }
            // }
            
    
            // Draw each segment of the snake
            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);  // Draw the head of the snake
                } else {
                    g.drawImage(ball, x[z], y[z], this);  // Draw the body of the snake
                }
            }
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }
    

    // private void gameOver(Graphics g) {
    //     String msg = "Game Over";
    //     Font small = new Font("Helvetica", Font.BOLD, 14);
    //     FontMetrics metr = getFontMetrics(small);

    //     g.setColor(Color.white);
    //     g.setFont(small);
    //     g.drawString(msg, (B_Width - metr.stringWidth(msg)) / 2, B_Height / 2);
    // }
    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metr = getFontMetrics(small);
    
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_Width - metr.stringWidth(msg)) / 2, B_Height / 2 - 50);
    
        // Draw the Restart Button
        JButton restartButton = new JButton("Restart");
        restartButton.setBounds((B_Width / 2) - 40, (B_Height / 2) - 10, 80, 30);
        restartButton.setFocusable(false);  // Prevent the button from getting focus
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        
        // Add the restart button to the panel
        this.setLayout(null);
        this.add(restartButton);
    }

    private void restartGame() {
        // Remove all buttons (especially the restart button) from the panel
        this.removeAll();
    
        // Reinitialize the game state
        inGame = true;
        dots = 3;  // Reset the snake length
    
        // Reset the snake's position
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * Dot_Size;  // Horizontal reset
            y[z] = 50;  // Vertical reset
        }
    
        // Locate a new apple
        locateApple();
    
        // Restart the timer
        timer.restart();
    
        // Redraw the game panel to show the snake and apple
        repaint();
    }
    
    

    private void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            locateApple();
        }
    }

    private void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= Dot_Size;
        }

        if (rightDirection) {
            x[0] += Dot_Size;
        }

        if (upDirection) {
            y[0] -= Dot_Size;
        }

        if (downDirection) {
            y[0] += Dot_Size;
        }
    }

    // private void checkCollision() {
    //     for (int z = dots; z > 0; z--) {
    //         if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
    //             inGame = false;
    //         }
    //     }

    //     if (y[0] >= B_Height || y[0] < 0 || x[0] >= B_Width || x[0] < 0) {
    //         inGame = false;
    //     }

    //     if (!inGame) {
    //         timer.stop();
    //     }
    // }

    private void checkCollision() {
        for (int z = dots; z > 0; z--) {
            if ((z > 3) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }
    
        if (y[0] >= B_Height || y[0] < 0 || x[0] >= B_Width || x[0] < 0) {
            inGame = false;
        }
    
        // If the game is over, stop the timer
        if (!inGame) {
            timer.stop();
        }
    }
    

    // public void actionPerformed(ActionEvent e) {
    //     if (inGame) {
    //         checkApple();
    //         checkCollision();
    //         move();
    //     }
    //     repaint();
    // }
    @Override
public void actionPerformed(ActionEvent e) {
    if (inGame) {
        checkApple();  // Check if the apple is eaten
        checkCollision();  // Check for collisions
        move();  // Move the snake
    }

    repaint();  // Redraw the game panel
}


    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}
