import javax.swing.*; // the JavaX Swing toolkit gives us access to specific methods necessary to create 
import java.awt.*; // this library will be used for the timer

// these two are for updating the AWT's Timer
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameContainer {
    private static JFrame gameFrame = new JFrame("Pong Game");

    // global variable to track the current state of the game
    Game pong;

    // Constructor to set up all the defaults for the JPanel so it can run properly,
    // and exit properly
    public GameContainer() {
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // make the game nice and large for big screens (1600x900, 16:9 aspect ratio)
        gameFrame.setSize(1600, 900);

        // create a new instance of our GameContainer so we can actually see the game:
        pong = new Game();

        // make the game actually show up on the screen
        gameFrame.add(pong);

        // make sure the player can actually see the game
        gameFrame.setVisible(true);

        gameFrame.requestFocus();
    }

    public void startGame() {
        // make a new Timer with delay 25 so that it can be run at 60 frames per second
        Timer timer = new Timer(25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                // run the game's frame updates so everything looks nice and dandy
                pong.frameUpdate();
                // repaint the screen
                pong.repaint();

            }
        });

        // start the timer after it's been created
        timer.start();
    }
}
