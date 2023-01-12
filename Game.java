
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
    static final int gameWidth = 1440;
    static final int gameHeight = 775;

    // this changes betewen mac and windows, so its just here so you can quickly
    // change it all
    static final int MOVE_UP_KEY = KeyEvent.VK_UP;
    static final int MOVE_DOWN_KEY = KeyEvent.VK_DOWN;

    // how much we want the player's block to move every time a key is pressed
    static final int desiredChangeStep = 30;
    private int playerOneDesiredPosY;
    private int playerTwoDesiredPosY;
    private int ballSpeed;

    int rallyCount;

    // set up the game ball so we can actually render it (the game ball by itself
    // can't do anything)
    private GameBall gameBall;
    private Player playerOne, playerTwo;

    private int playerOneScore, playerTwoScore;

    // set up the constructor for the game
    public Game() {
        // actually initalize the ball in this constructor
        gameBall = new GameBall(gameWidth / 2, gameHeight / 2, 4, 4, 8, 12, Color.WHITE);
        playerOne = new Player(10, 200, 75, 12, Color.WHITE);
        playerTwo = new Player(1420, 200, 75, 12, Color.WHITE);

        playerOneDesiredPosY = gameHeight / 2;
        playerTwoDesiredPosY = gameHeight / 2;

        playerOneScore = 0;
        playerTwoScore = 0;

        rallyCount = 0;
        ballSpeed = 3;

        setFocusable(true);
        KeyListener listener = new CustomKeyListener();
        addKeyListener(listener);
        addMouseMotionListener(this);
        setFocusable(true);
    }

    public void paintComponent(Graphics gameGraphics) {

        // this will set up the background of the game to be black and the size of the
        // game
        gameGraphics.setColor(Color.BLACK);
        gameGraphics.fillRect(0, 0, gameWidth, gameHeight);

        // add the game ball to the graphics of the game
        gameBall.renderBall(gameGraphics);
        playerOne.render(gameGraphics);
        playerTwo.render(gameGraphics);

        // update score
        gameGraphics.setColor(Color.WHITE);
        // we print the string of the score there
        gameGraphics.drawString(playerOneScore + " Player One - Player Two " + playerTwoScore,
                (int) (gameWidth / 2 - 100),
                (int) (gameHeight - gameHeight * 0.8));

    }

    // this method will check to see if anyone scored
    public void checkScored() {
        // this code will check to see if any of the player's scored
        if (gameBall.getPosX() > gameWidth) {
            // basically if the second player scored
            playerOneScore++;
            resetBall();
        } else if (gameBall.getPosX() < 0) {
            // if the first player scored
            playerTwoScore++;
            resetBall();
        }
    }

    // this method will run every updated frame
    public void frameUpdate() {
        // move the ball every frame
        gameBall.updateBallPos();

        checkScored();

        // check if the game needs to bounce off the ball, and then do the game logic
        // for that
        gameBall.bounceOffEdges(0, gameHeight);

        // bascially check to make sure the user is touching a ball, if they are, then
        // bounce the ball and switch the direction
        if (gameBall.checkCollisionStatus(playerOne) || gameBall.checkCollisionStatus(playerTwo)) {
            gameBall.bounceX();
            rallyCount++;
            if (rallyCount > 3) {
                ballSpeed++;
                double randomChance = Math.random();
                if (randomChance > 0.5)
                    gameBall.bounceY();
                gameBall.setBallSpeed(ballSpeed);
            }
        }

        playerOne.movePlayer(playerOneDesiredPosY);

        playerTwo.movePlayer(playerTwoDesiredPosY);

    }

    // Resets the position of the ball, note this is not a game reset. that is not
    // available in this code
    public void resetBall() {

        gameBall.setBallPosX(gameWidth / 2);
        gameBall.setBallPosY(gameHeight / 2);
        gameBall.setBallChangeX(3);
        gameBall.setBallChangeY(3);
        rallyCount = 0;
        try {
            Thread.sleep(1000);
        } catch (Exception error) {

        }

    }

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
                if (playerOne.getY() > desiredChangeStep)
                    playerOneDesiredPosY -= desiredChangeStep;

            } else if (event.getKeyCode() == MOVE_DOWN_KEY) {
                if (playerOne.getY() <= gameHeight - desiredChangeStep)
                    playerOneDesiredPosY += desiredChangeStep;

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