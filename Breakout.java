/*
 * File: Breakout.java
 * -------------------
 * Name: Veronica Peng
 * Parter: Yulei Wang
 * Section Leader: Jay Evan
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

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
private static final int DELAY = 20;

/* Method: run() */
/** Runs the Breakout program. */
	
	 /*
	  * instance variables
	  */	 
	
	private GRect paddle;    //the paddle
	private GOval ball;     //the ball
	private double vx, vy;    // the x component and y component of velocity of ball
	private GLabel lifeCount;    //the prompt to show the life user still has
	private RandomGenerator rgen = RandomGenerator.getInstance();    // create a random number generator
	private int brickCount = NBRICK_ROWS * NBRICKS_PER_ROW ;     //total number of bricks
	
	/*
	 * first create the items in the game then play.
	 */
	
	public void run() {
		setup();
		showInitialLifeCount();
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
	 * draw a ball and assign a initial velocity for it
	 */
	
	private void createBall() {
		drawBall();
		getVelocity();
	}
	
	/*
	 * Draw a ball at the center of the world with radius BALL_RADIUS.
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
	 * The balls gains an initial speed and starts to move. 
	 * The x component of the initial velocity is 3.0.
	 * The y component of the initial velocity is a random number of certain range.
	 */
	
	private void getVelocity() {
		vy = 3.0;
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) {
			vx = -vx;
		}
	}
	

	/*
	 * display the turns users have at the beginning of the game
	 */
	
	private void showInitialLifeCount() {
		GLabel lifeCount = prompt("You have " + NTURNS + " lives.");
		add (lifeCount);
		waitForClick ();
		remove (lifeCount);
	}
	
	/*
	 * Users start the game with a click. The game does not end until the ball falls off the bottom 
	 * of the screen or there is no brick left. If you break all the bricks before using up the lives, 
	 * you win. If the ball falls off the bottom of the screen, you turn to other round until you use 
	 * up all your lives.
	 */
	
	private void play() {
		gaming();
		showPrompt();
	}

	/*
	 * user plays the game. If the user loses in one turn,the game goes on to the next turn.
	 * If the user loses NTURNS times, he or she loses the game. If the user wins in one turn,
	 * the game does not go on to next turn, but ends.
	 */
	
	private void gaming() {
		boolean loseOneTurn;
		boolean win = false;
		boolean stillAlive= true;
		for (int i=NTURNS; i>0; i--) {    //loops for the number of turns.
			GLabel life=showLifeCount(i);
			waitForClick();
			add (life);
			while (stillAlive) {    //the ball moves and bounces until the user loses or wins the turn.
				moveBall();
				loseOneTurn=(ball.getY() >= HEIGHT);   //user loses one turn when the ball falls under the paddle.
				win=(brickCount == 0);    //user wins when there is no brick left.
				stillAlive= !(loseOneTurn || win);
			}
			if (win) break;    //breaks from the loop if the user wins in one turn.
			remove (ball);
			createBall();    //a new ball appears on the center of the screen after one turn
			stillAlive =true;
			remove (life);
		}
	}		
	
	/*
	 * Show remaining lives the user has at the upper left corner of the screen.
	 */
	
	private GLabel showLifeCount(int life) {
		GLabel lifeCount=new GLabel("Life Count: " + life);
		lifeCount.setFont("Times-15");
		lifeCount.setLocation(10, lifeCount.getAscent() + 10);
		return lifeCount;
	}
	
	/*
	 * the ball starts to fall, and bounces when hitting the wall or objects.
	 */

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
	 * Check the four corners of the square inscribing the ball in order. 
	 * If one of the corner is contained in an object other than paddle, remove the object and then bounce off.
	 * If one of the corner is contained in paddle, bounce off without removing the paddle.
	 * 
	 */
	
	private void checkObject() {
		// x and y parameters for the different corners
		double leftX = ball.getX();
		double rightX = ball.getX() + (2 * BALL_RADIUS);
		double upperY = ball.getY();
		double lowerY = ball.getY() + (2 * BALL_RADIUS);
		//check the corners for object
		GObject upperLeft = checkCorner(leftX, upperY);    //check upper-left corner
		if (upperLeft == null) {
			GObject upperRight = checkCorner(rightX, upperY);    //check upper-right corner
		}
		GObject lowerLeft = checkCorner(leftX, lowerY);    ////check lower-left corner
		if (lowerLeft == null) {
			GObject lowerRight = checkCorner(rightX, lowerY);    //check lower-right corner		
			if ((lowerLeft == paddle) && (lowerRight == paddle)) {    //When both lower corners hit paddle, change direction.
				vy = -vy;	
			}
		}
	}
	
	/*
	 * check one corner of the square inscribing the ball in order.	 
	 * If one of the corner is contained in an object other than paddle, remove the object and then bounce off.
	 * If one of the corner is contained in paddle, bounce off without removing the paddle.
	 */
	
	private GObject checkCorner(double x, double y) {
		GObject obj = getElementAt(x, y);    //check the corner for GObject
		if (obj == paddle) {   
			vy = -Math.abs(vy);
			PrecisionPaddle();
		} else if (obj != null && obj != lifeCount) {    //check if the ball hits a brick
			remove (obj);
			vy = -vy;
			brickCount--;
			AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
			bounceClip.play();
		}
		return obj;
	}
	
	/*
	 * check if ball gets into the paddle, update location as appropriate
	 */
	
	private void PrecisionPaddle() {
		if (ball.getY() > HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT - BALL_RADIUS * 2 ) {    //check if the ball drops below the paddle
			double diff = ball.getY() - (HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT - BALL_RADIUS * 2 );
			ball.move(0, -2 * diff);    //move ball an amount equal to the amount it drops below the paddle
		}
	}

	/*
	 * Show the prompt to indicate whether user loses or wins.
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




