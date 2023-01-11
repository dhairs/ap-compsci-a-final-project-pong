
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

    static final int gameWidth = 1440;
    static final int gameHeight = 700;

    static final int desiredChangeStep = 30;
    // set up the game ball so we can actually render it (the game ball by itself
    // can't do anything)
    private GameBall gameBall;
    private Player user, computer;

    private int desiredPosY;

    // set up the constructor for the game
    public Game() {
        // actually initalize the ball in this constructor
        gameBall = new GameBall(gameWidth / 2, gameHeight / 2, 4, 4, 8, 12, Color.WHITE);
        user = new Player(10, 200, 75, 6, Color.WHITE);
        computer = new Player(1420, 200, 75, 6, Color.WHITE);

        desiredPosY = gameHeight / 2;

        setFocusable(true);
        KeyListener listener = new CustomKeyListener();
        addKeyListener(listener);
        setFocusable(true);
        // addMouseMotionListener(this);
    }

    public void paintComponent(Graphics gameGraphics) {
        // this will set up the background of the game to be black and the size of the
        // game
        gameGraphics.setColor(Color.BLACK);
        gameGraphics.fillRect(0, 0, gameWidth, gameHeight);

        // add the game ball to the graphics of the game
        gameBall.renderBall(gameGraphics);
        user.render(gameGraphics);
        computer.render(gameGraphics);
    }

    // this method will run every updated frame
    public void frameUpdate() {

        // move the ball every frame
        gameBall.updateBallPos();

        // check if the game needs to bounce off the ball, and then do the game logic
        // for that
        gameBall.bounceOffEdges(0, gameHeight);

        // bascially check to make sure the user is touching a ball, if they are, then
        // bounce the ball and switch the direction
        if (gameBall.checkCollisionStatus(user) || gameBall.checkCollisionStatus(computer))
            gameBall.bounceX();

        user.movePlayer(desiredPosY);

        computer.movePlayer(gameBall.getPosY());

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent event) {
        // desiredPosY = event.getY();
    }

    private class CustomKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent event) {
        }

        @Override
        public void keyPressed(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.VK_DOWN) {
                if (user.getY() < gameHeight - desiredChangeStep - user.getHeight() / 2) {
                    System.out.println(user.getY());
                    desiredPosY += desiredChangeStep;
                }
            } else if (event.getKeyCode() == KeyEvent.VK_UP) {
                System.out.println(user.getY() + " yuh");
                if (user.getY() > desiredChangeStep) {
                    desiredPosY -= desiredChangeStep;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent event) {
            // System.out.println("keyReleased=" + KeyEvent.getKeyText(e.getKeyCode()));
        }
    }

}