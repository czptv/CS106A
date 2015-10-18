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

public class CopyOfBreakout extends GraphicsProgram {

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
	
	private GRect paddle;
	private GOval ball;
	private double vx, vy; // the x component and y component of velocity of ball
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private int brickCount = NBRICK_ROWS * NBRICKS_PER_ROW ;
	
	public void run() {
		drawBall();
		play();
			
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
		gaming();
	}
	
	/*
	 * After the user clicks, the balls gains an initial speed and starts to move.
	 */
	
	
	private void gaming() {
		boolean loseOneTurn=(ball.getY() >= HEIGHT);
		boolean win=(brickCount == 0);
		boolean stillAlive= !(loseOneTurn || win);		
		for (int i=0; i<NTURNS; i++) {
			while (stillAlive) {
				moveBall();
			}
		}
	}

	private void moveBall() {
		ball.move(4, 3);
	}
}	
