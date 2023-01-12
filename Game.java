
// we'll use JPanel from this to actually set up the windowing, but the AWT class for graphics processing
import javax.swing.*;

// this is the graphics processing library we'll be using
import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// extending JPanel because it allows us to work with the GUI directly in this file
public class Game extends JPanel implements MouseMotionListener {

    // just so we dont have to keep re-entering this later
    static final int GAME_WIDTH = 1440;
    static final int GAME_HEIGHT = 775;

    // Game Defaults
    static final int DEFAULT_SPEED_X = 6;
    static final int DEFAULT_SPEED_Y = 6;

    // this changes betewen mac and windows, so its just here so you can quickly
    // change it all
    static final int MOVE_UP_KEY = KeyEvent.VK_UP;
    static final int MOVE_DOWN_KEY = KeyEvent.VK_DOWN;

    // just the score needed to win the game
    static final int MAX_SCORE = 5;

    private boolean GAME_OVER = false;
    private String WINNER_STRING;

    // how much we want the player's block to move every time a key is pressed
    static final int DESIRED_CHANGE_STEP = 30;

    // The position the players want to move to
    private int playerOneDesiredPosY;
    private int playerTwoDesiredPosY;
    private int ballSpeed;

    // so we can increase difficulty
    int rallyCount;

    // set up the game ball so we can actually render it (the game ball by itself
    // can't do anything)
    private GameBall gameBall;
    private Player playerOne, playerTwo;

    private int playerOneScore, playerTwoScore;

    // set up the constructor for the game
    public Game() {
        // actually initalize the ball in this constructor
        gameBall = new GameBall(GAME_WIDTH / 2, GAME_HEIGHT / 2, DEFAULT_SPEED_X, DEFAULT_SPEED_Y, 12, Color.WHITE);
        resetBall();

        // set up both players
        playerOne = new Player(10, GAME_HEIGHT / 2, 75, 12, Color.WHITE);
        playerTwo = new Player(1420, GAME_HEIGHT / 2, 75, 12, Color.WHITE);

        // basically make sure they're both at the center of the screen
        playerOneDesiredPosY = GAME_HEIGHT / 2;
        playerTwoDesiredPosY = GAME_HEIGHT / 2;

        // set both scores to 0 (initalizes them)
        playerOneScore = 0;
        playerTwoScore = 0;

        // initialize the difficulty mechanisms
        rallyCount = 0;
        ballSpeed = DEFAULT_SPEED_X;

        // this just makes sure keyboard and mouse input work
        KeyListener listener = new CustomKeyListener();
        addKeyListener(listener);
        addMouseMotionListener(this);
        setFocusable(true);
    }

    // this is a function that we need to implement of JPanel, basically we'll run
    // this with the new game graphics every loop to re-render the game and display
    // the updated data

    public void paintComponent(Graphics gameGraphics) {

        /*
         * if the game has ended and a user won, display the WINNER STRING, which is
         * adjusted in the announceWinners method
         */
        if (GAME_OVER) {
            gameGraphics.setFont(new Font("Calibri", Font.PLAIN, 32));
            gameGraphics.drawString(WINNER_STRING,
                    (int) (100),
                    (int) (GAME_HEIGHT / 2));
            return;
        }

        // this will set up the background of the game to be black and the size of the
        // game
        gameGraphics.setColor(Color.BLACK);
        gameGraphics.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        // add the game ball to the graphics of the game
        gameBall.renderBall(gameGraphics);
        playerOne.render(gameGraphics);
        playerTwo.render(gameGraphics);

        // update score
        gameGraphics.setColor(Color.WHITE);

        // we print the string of the score there
        gameGraphics.drawString(playerOneScore + " Player One - Player Two " + playerTwoScore,
                (int) (GAME_WIDTH / 2 - 100),
                (int) (GAME_HEIGHT - GAME_HEIGHT * 0.8));

    }

    // holds the logic to see if anyone won the game
    public void checkWinner() {
        // if the first player got more than 5 points, they win, otherwise the other
        // player wins
        if (playerOneScore > MAX_SCORE) {
            announceWinner("Player One", "Player Two", playerOneScore, playerTwoScore);
        } else if (playerTwoScore > MAX_SCORE) {
            announceWinner("Player Two", "Player One", playerTwoScore, playerOneScore);
        }
    }

    // generates a string to display who won the game
    public void announceWinner(String playerWinner, String playerLoser, int scoreWinner, int scoreLoser) {
        WINNER_STRING = playerWinner + " WINS!! They beat " + playerLoser + " with a score of " + scoreWinner + "-"
                + scoreLoser;
        GAME_OVER = true;
    }

    // this method will run every updated frame, it will hold the order of the game
    // logic
    public void frameUpdate() {

        if (GAME_OVER)
            return;

        // move the ball every frame
        gameBall.updateBallPos();

        // self explanatory
        checkScored();

        // check if anyone won, also self explanatory
        checkWinner();

        // self explanatory
        checkCollision();

        // check if the game needs to bounce off the ball, and then do the game logic
        // for that
        gameBall.bounceOffEdges(0, GAME_HEIGHT);

        // move the players
        playerOne.movePlayer(playerOneDesiredPosY);
        playerTwo.movePlayer(playerTwoDesiredPosY);

    }

    // bascially check to make sure the user is touching a ball, if they are, then
    // bounce the ball and switch the direction
    public void checkCollision() {
        // check to see if either player is touching the ball
        if (gameBall.checkCollisionStatus(playerOne) || gameBall.checkCollisionStatus(playerTwo)) {
            // if they are, switch the X direction of the ball, then put up the rally count
            // so we can count that
            gameBall.bounceX();
            rallyCount++;
            // every time the rally is above 3, the speed will consistenly increase to make
            // the game harder
            if (rallyCount > 3) {
                ballSpeed++;

                // this is randomized to make it more like pong, basically, sometimes the ball
                // will go back the direction it came from instead of moving how it would
                // naturally
                double randomChance = Math.random();
                if (randomChance > 0.5)
                    gameBall.bounceY();

                // finally, update the ball's speed
                gameBall.setBallSpeed(ballSpeed);
            }
        }
    }

    // this method will check to see if anyone scored
    public void checkScored() {
        // this code will check to see if any of the player's scored
        if (gameBall.getPosX() > GAME_WIDTH) {
            // basically if the second player scored
            playerOneScore++;
            resetBall();
        } else if (gameBall.getPosX() < 0) {
            // if the first player scored
            playerTwoScore++;
            resetBall();
        }
    }

    // Resets the position of the ball, note this is not a game reset. that is not
    // available in this code
    public void resetBall() {

        // these random speed decreases are for the game to be more unpredictable
        double random = Math.random();
        int randomizeDirection;
        if (random > 0.5) {
            randomizeDirection = -1 * (int) (Math.random() * 2 + 1);
        } else {
            randomizeDirection = 1 * (int) (Math.random() * 2 + 1);
        }

        // to randomly decrease speed of game
        int randomSpeedDecrease = (int) (Math.random() * 2);

        int randomLocationChange = (int) (Math.random() * 200 + 100);
        gameBall.setBallPosX(GAME_WIDTH / 2);
        gameBall.setBallPosY(GAME_HEIGHT / 2 + randomLocationChange);
        gameBall.setBallChangeX(DEFAULT_SPEED_X * randomizeDirection - randomSpeedDecrease);
        gameBall.setBallChangeY(DEFAULT_SPEED_Y * randomizeDirection - randomSpeedDecrease);

        ballSpeed = DEFAULT_SPEED_X;

        rallyCount = 0;
        // this is to slow down the game
        try {
            // the game won't run for 1 second to give players time to reset
            Thread.sleep(1000);
        } catch (Exception error) {

        }

    }

    // we won't actually use these, but they're required as an implementation of the
    // MouseMotionListener interface
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent event) {
        playerTwoDesiredPosY = event.getY();
    }

    private class CustomKeyListener implements KeyListener {

        @Override
        public void keyPressed(KeyEvent event) {
            if (event.getKeyCode() == MOVE_UP_KEY) {
                if (playerOne.getY() > DESIRED_CHANGE_STEP)
                    playerOneDesiredPosY -= DESIRED_CHANGE_STEP;

            } else if (event.getKeyCode() == MOVE_DOWN_KEY) {
                if (playerOne.getY() <= GAME_HEIGHT - DESIRED_CHANGE_STEP)
                    playerOneDesiredPosY += DESIRED_CHANGE_STEP;

            }
        }

        // we won't actually use these, but they're required as an implementation of the
        // KeyListener interface
        @Override
        public void keyTyped(KeyEvent event) {
        }

        @Override
        public void keyReleased(KeyEvent event) {
        }
    }

}