package brickBrcker;

import javax.swing.*;
import java.awt.Graphics2D;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

import javax.swing.JPanel;

// making Gameplay object to a panel
public class Gameplay extends JPanel implements KeyListener, ActionListener{
	private boolean play = false;
	private int score = 0;
	private int totalBricks = 21;
	private Timer timer;
	private int delay = 8;
	private int playerX = 310;
	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	private Color ballColor = Color.getColor("green");
	private boolean isPaused = false;
	private boolean playerWon = false;

	private MapGenerator map;

	public Gameplay() {
		map = new MapGenerator(3,7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this); //we're using a different timer so util.timer will not work
		timer.start();
	}

	public void paint (Graphics g) {
		//background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);

		//drawing map
		map.draw((Graphics2D)g);

		//border
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.clearRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);

		//score
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString(""+score, 590, 30);


		//the paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);

		//the ball
		g.setColor(ballColor);
		g.fillOval(ballposX, ballposY, 20, 20);

		if (isPaused) {
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Paused", 230, 350);
		}

		//THIS BLOCK IS FOR IF WIN THE GAME
		if(totalBricks <= 0) {
			play = false;
			playerWon = true;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("YOU WON: ", 260, 300);

			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 350);
		}
		//THIS BLOCK IS FOR IF FAIL AND WANT TO RESTART THE GAME
		if(ballposY > 570) {
			play = false;
			playerWon = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over, Score: "+score, 190, 300);
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 350);
		}

		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();

		//to change the ball direction
		if (play) {

			// ball direction change when touch with paddle
			if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
				ballYdir = -ballYdir;
			}
			A: for(int i= 0; i<map.map.length; i++) {
					 for(int j = 0; j<map.map[0].length; j++) {
						 if(map.map[i][j] > 0) {
							 int brickX = j * map.brickWidth + 80;
							 int brickY = i * map.brickHight + 50;
							 int brickWidth = map.brickWidth;
							 int brickHeight = map.brickHight;

							 Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
							 Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
							 Rectangle brickRect = rect;

							 if(ballRect.intersects(brickRect) ) {
								 map.setBrickValue(0, i, j);
								 totalBricks--;
								 score +=5;

								 if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
									 ballXdir =- ballXdir;
								 } else {
									 ballYdir =-ballYdir;
								 }

								 break A;

							 }

						 }
					 }

			}

			ballposX += ballXdir; //ball direction
			ballposY += ballYdir; //ball direction
			if (ballposX < 0 ) {
				ballXdir = -ballXdir; //ball direction
			}
			if (ballposY < 0 ) {
				ballYdir = -ballYdir; //ball direction
			}
			if (ballposX > 670 ) {
				ballXdir = -ballXdir; //ball direction
			}
		}

		repaint();  //it will recall the paint method and draw everything again
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}



	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(playerX >=600) {
				playerX = 600;
				} else {
					moveRight();
				}
			}
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				if(playerX < 10) {
					playerX = 10;
				} else {
					moveLeft();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_P) {
				if (play) {
					play = false;
					isPaused = true;
				}
				else {
					play = true;
					isPaused = false;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_B) {
				ballColor = Color.getColor("blue");
				repaint();
			}
			if (e.getKeyCode() == KeyEvent.VK_G) {
				ballColor = Color.getColor("pink");
				repaint();
			}
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {   // THIS KEY EVENT TO RESTART THE GAME USING ENTER KEY
				if(!play) {
					isPaused = false;
					play = true;
					ballposX = 120;
					ballposY = 350;
					ballXdir = -1;
					ballYdir = -2;
					playerX = 310;
					if (playerWon == false) score = 0;
					totalBricks = 21;
					map = new MapGenerator(3, 7);
					repaint();
				}
			}
	}

	private void moveRight() {
		play = true;
		playerX+=40; // incrementing the player x
	    }

	private void moveLeft() {
		play = true;
		playerX-=40; // incrementing player x
	}


	//https://www.youtube.com/watch?v=K9qMm3JbOH0

}
