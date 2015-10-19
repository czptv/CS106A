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

/** Animation delay or pause time between ball moves */
private static final int DELAY = 30;

/* Method: run() */
/** Runs the Breakout program. */
	
	 /*
	  * instance variables
	  */	 
	
	private GRect paddle;    //the paddle
	private GOval ball;     //the ball
	private double vx, vy;    // the x component and y component of velocity of ball
	private RandomGenerator rgen = RandomGenerator.getInstance();    // create a random number generator
	private int brickCount = NBRICK_ROWS * NBRICKS_PER_ROW ;     //total number of bricks
	
	/*
	 * first create the items in the game then play.
	 */
	
	public void run() {
		setup();
		play();
	}
 
	/*
	 * set up the items in the game, including bricks, a paddle and a ball.
	 */
	
	private void setup() {
		drawBricks();
		drawPaddle();
		createBall();
	}
	
	/*
	 * Draw all the bricks.
	 */
	
	private void drawBricks() {
		for (int i = 0; i<NBRICK_ROWS; i++) {
			drawRow(i);
		}
	}

	/*
	 * draw a row of the brick pile
	 */
	
	private void drawRow(int row) {
		for(int i = 0; i<NBRICKS_PER_ROW; i++) {
			drawOneBrick(row, i);
		}
	}
	
	/*
	 * create one brick.
	 */	
	
	private void drawOneBrick(int row, int col) {
		double x0 = (WIDTH - NBRICKS_PER_ROW * BRICK_WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP)/2; //the distance of leftmost brick to left border of the world
		double x = x0 + (BRICK_WIDTH + BRICK_SEP) * col;
		double y = BRICK_Y_OFFSET + (BRICK_SEP + BRICK_HEIGHT) * row;
		GRect brick = new GRect (x,y,BRICK_WIDTH, BRICK_HEIGHT);
		brick.setFilled(true);
		colorBrick(row, brick);
		add(brick);
	}
	
	/*
	 * set color for the bricks according to which rows they are in.	
	 */
	
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
	 * change the x location of the paddle according to the movement of user's mouse.
	 */
	
	public void mouseMoved (MouseEvent e) {
		if ((e.getX() > PADDLE_WIDTH/2) && (e.getX() < WIDTH - PADDLE_WIDTH/2)) {
		double x = e.getX() - PADDLE_WIDTH/2;
		double y = HEIGHT - BRICK_Y_OFFSET - PADDLE_HEIGHT;
		paddle.setLocation(x, y);
		}
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
		showPrompt();
	}

	/*
	 * user plays the game. If the user loses in one turn,the game goes on to the next turn.
	 * If the user loses NTURNS times, he or she loses the game. If the user wins in one turn,
	 * the game does not go on to next turn, but ends.
	 */
	
	private void gaming() {
		boolean loseOneTurn;   //user loses one turn when the ball falls under the paddle.
		boolean win = false;    //user wins when there is no brick left.
		boolean stillAlive= true;    //otherwise user continues playing. 	
		for (int i=0; i<NTURNS; i++) {    //loops for the number of turns.
			while (stillAlive) {    //the ball moves and bounces until the user loses or wins the turn.
				moveBall();
				loseOneTurn=(ball.getY() >= HEIGHT);   //user loses one turn when the ball falls under the paddle.
				win=(brickCount == 0);    //user wins when there is no brick left.
				stillAlive= !(loseOneTurn || win);    //otherwise user continues playing. 	
			}
			createBall();
			stillAlive =true;
			if (win) break;    //breaks from the loop if the user wins in one turn.
			waitForClick();
		}
	}

	/*
	 * the ball starts to fall, and bounces when hitting the wall or objects.
	 */
	private void createBall() {
		drawBall();
		getVelocity();
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
	 * After the user clicks, the balls gains an initial speed and starts to move.
	 */
	
	private void getVelocity() {
		vy = 3.0;
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) {
			vx = -vx;
		}
	}
	
	private void moveBall() {
		startFall();    
		checkWall();    
		checkObject();
	}
	
	/*
	 * the ball falls from its original location at the center of the screen.
	 */
	
	private void startFall() {
		ball.move(vx, vy);
		pause (DELAY);
		ball.sendToBack();
	}
	
	/*
	 * checks if the ball hits the left, the right or the top wall. Then change the movement
	 * of the ball accordingly. 
	 */
	
	private void checkWall() {
		boolean checkLeftWall = ball.getX() <= 0;   
		boolean checkRightWall = ball.getX() + BALL_RADIUS * 2 >= WIDTH;
		boolean checkTop = ball.getY() <= 0;
		if ((checkLeftWall) || (checkRightWall)) {
			vx = -vx;
		}
		if (checkTop) {
			vy = -vy;
		}
	}
	
	/*
	 * Check if the ball hit any object, brick or paddle. Then change its movement accordingly. 
	 */
	
	private void checkObject() {
		double x1 = ball.getX();
		double x2 = x1 + BALL_RADIUS;
		double x3 = x2 + BALL_RADIUS;
		double y1 = ball.getY();
		double y2 = y1 + BALL_RADIUS;
		double y3 = y2 + BALL_RADIUS;
		checkTopBottom (x2, y1);    //check top
		checkTopBottom (x2, y3);    //check bottom
		checkLeftRight (x1, y2);    //check left
		checkLeftRight (x3, y2);    //check right
	}
	
	/*
	 * Check whether the top or bottom of the ball hits any object. 
	 */
	
	private void checkTopBottom (double x, double y) {
		GObject obj = getElementAt(x,y);    //Check middle point of the upper or lower side of the ball
		if (obj == paddle) {    //If the ball hits the paddle, change the direction of its y velocity.
			vy = -vy;    
		}
		else if (obj == ball) {    //The ball does not hit anything at the middle point of its upper or lower side
			GObject leftObject = checkSide(x - BALL_RADIUS, y);    //check leftmost point of its upper or lower side
			GObject rightObject = checkSide(x + BALL_RADIUS, y);   //check rightmost point of its upper or lower side
			boolean leftSide = (leftObject == paddle) && (rightObject != paddle);    //Whether only one of the two corners of 
			boolean rightSide = (rightObject == paddle) && (leftObject != paddle);   //its upper or lower side hit the paddle
			if (leftSide || rightSide) {    //if only one of its corners hit the paddle 
				vx = -vx;    //change the direction of its x velocity
			}
			else if ((leftObject != null) || (rightObject != null)) {    //if the ball hits one or two bricks at the same time
				vy = -vy;    //change the direction of its y velocity
			}
		} 
		else if (obj != null) {     //The ball hits a brick at its middle point of its upper or lower side.
			remove(obj);    
			brickCount--;
			vy = -vy;
		}
	}

	
	private void checkLeftRight (double x, double y) {
		GObject obj = getElementAt(x,y);
		if (obj == paddle) {
			vx = -vx;
		}
		else if (obj == ball) {
			GObject topObject = checkSide(x, y - BALL_RADIUS); //check top vertex
			GObject bottomObject = checkSide(x, y + BALL_RADIUS); //check bottom vertex
			boolean upSide = (topObject == paddle) && (bottomObject != paddle);    //Whether only one of the two corners of 
			boolean lowSide = (bottomObject == paddle) && (topObject != paddle);   //its upper or lower side hit the paddle
			if (upSide || lowSide) {    //if only one of its corners hit the paddle 
				vx = -vx;    //change the direction of its x velocity
			}
			else if ((topObject != null) || (bottomObject != null)) {    //if the ball hits one or two bricks at the same time
				vy = -vy;    //change the direction of its y velocity
			}

		} 
		else if (obj != null) {
			remove(obj);
			brickCount--;
			vx = -vx;
		}
	}
		
	/*
	 * Return the object that the corner of the ball hits.  
	 * If the corner of the ball hit any brick, remove the brick
	 */
	
	private GObject checkSide(double x, double y) {
		GObject obj = getElementAt(x,y);
		if ((obj != null) && (obj !=paddle)) {    //if the object is a brick
			remove (obj);
			brickCount--;
		}
		return obj;
	}
	
	/*
	 * 
	 */
	
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


