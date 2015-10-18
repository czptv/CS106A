/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Copy_2_of_Breakout extends GraphicsProgram {

/** Width and height of application window in pixels.  On some platforms 
  * these may NOT actually be the dimensions of the graphics canvas. */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board.  On some platforms these may NOT actually
  * be the dimensions of the graphics canvas. */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;

/* Method: run() */
/** Runs the Breakout program. */
	/*
	 * Create an instance variable of a brick, a paddle and a ball.
	 */
	
	private GRect brick, paddle;
	private GOval ball;
	private double vx, vy; // the x component and y component of velocity of ball
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private int brickCount;
	
	public void run() {
		setup();
		play();
			
	}
 
	private void setup() {
		drawBricks();
		drawPaddle();
		drawBall();
	}
	
	/*
	 * Draw all the bricks.
	 */
	
	private void drawBricks() {
		for (int i = 0; i<NBRICK_ROWS; i++) {
			drawRow(i);
		}
	}

	//draw a row of the brick pile
	
	private void drawRow(int row) {
		for(int i = 0; i<NBRICKS_PER_ROW; i++) {
			drawOneBrick(row, i);
		}
	}
	
	//create one brick
	
	private void drawOneBrick(int row, int col) {
		double x0 = (WIDTH - NBRICKS_PER_ROW * BRICK_WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP)/2; //the distance of leftmost brick to left border of the world
		double x = x0 + (BRICK_WIDTH + BRICK_SEP) * col;
		double y = BRICK_Y_OFFSET + (BRICK_SEP + BRICK_HEIGHT) * row;
		GRect brick = new GRect (x,y,BRICK_WIDTH, BRICK_HEIGHT);
		brick.setFilled(true);
		colorBrick(row, brick);
		add(brick);
	}
	
	//set color for the bricks according to which rows they are in.	
	
	private void colorBrick(int row, GRect brick) {
		if (row<2) {
			brick.setColor(Color.RED);
		}
		else if (row<4) {
			brick.setColor(Color.ORANGE);
		}
		else if (row<6) {
			brick.setColor(Color.YELLOW);
		}
		else if (row<8) {
			brick.setColor(Color.GREEN);
		}
		else if(row<10) {
			brick.setColor(Color.CYAN);
		}
	}
	
	/*
	 * Draw a paddle.
	 */
	
	private void drawPaddle() {
		double x = (WIDTH - PADDLE_WIDTH)/2;
		double y = HEIGHT - BRICK_Y_OFFSET - PADDLE_HEIGHT;
		paddle = new GRect(x,y,PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
		addMouseListeners();
	}
	
	/*
	 * Make the paddle track the movement of user's mouse.
	 */
	
	public void mouseMoved (MouseEvent e) {
		if ((e.getX() > PADDLE_WIDTH/2) && (e.getX() < WIDTH - PADDLE_WIDTH/2)) {
		double x = e.getX() - PADDLE_WIDTH/2;
		double y = HEIGHT - BRICK_Y_OFFSET - PADDLE_HEIGHT;
		paddle.setLocation(x, y);
		}
	}
	
	/*
	 * Draw a ball.
	 */
	
	private void drawBall() {
		double x = WIDTH/2 - BALL_RADIUS;
		double y = HEIGHT/2 - BALL_RADIUS;
		double d = 2 * BALL_RADIUS;
		ball = new GOval(x, y, d, d);
		ball.setFilled(true);
		add(ball);
	}

	/*
	 * Users start the game with a click. Ball begins to fall after the click. The game does not end
	 * until the ball falls off the bottom of the screen or there is no brick left. If you break all
	 * the bricks before using up the lives, you win. If the ball falls off the bottom of the screen,
	 * you turn to other round until you use up all your lives.
	 */
	
	
	private void play() {
		waitForClick();
		getVelocity();
		gaming();
		showPrompt();
	}
	
	/*
	 * After the user clicks, the balls gains an initial speed and starts to move.
	 */
	
	private void getVelocity() {
		vy = 3.0;
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) {
			vx = -vx;
		}
	}
	
	private void gaming() {
		boolean loseOneTurn=(ball.getY() >= HEIGHT);
		boolean win=(brickCount == 0);
		boolean stillAlive= !(loseOneTurn || win);		
		for (int i=0; i<NTURNS; i++) {
			while (stillAlive) {
				moveBall();
			}
			if (win) break;
		}
	}

	private void moveBall() {
		startFall();
		checkWall();
		checkObject();
	}
	
	private void startFall() {
		ball.move(vx, vy);
	}
	
	private void checkWall() {
		if ((ball.getX() - vx <= 0) || (ball.getX() + vx >= WIDTH)) {
			vx = -vx;
		}
		if (ball.getY())
	}
	
	private void checkObject() {
		if ()
	}
	
	private void showPrompt() {
		if (brickCount == 0) {
			add(prompt("YOU WIN!")); 
		}
		else {
			add(prompt("GAME OVER!"));
		}
	}
	/*
	 * prompt at the end of the game to indicate whether the user wins or loses
	 */
	
	private GLabel prompt(String endGame) {
		GLabel prompt=new GLabel(endGame);
		prompt.setFont("Times-Bold-50");
		double x=(WIDTH-prompt.getWidth())/2;
		double y=HEIGHT*4.0/5.0;
		prompt.setLocation(x, y);
		return prompt;
	}
}