
// this is so we have access to the colors of the game
import java.awt.*;

public class GameBall {

    // declare all the ball's variables, they'll be private so no other class can
    // mess with them
    private int ballPosX, ballPosY, changeInX, changeInY, ballSize;

    // we need to instantiate all these values, which we'll do with the default
    // constructor
    public GameBall(int ballPosX, int ballPosY, int changeInX, int changeInY, int ballSize,
            Color color) {
        this.ballPosX = ballPosX;
        this.ballPosY = ballPosY;
        this.changeInX = changeInX;
        this.changeInY = changeInY;
        this.ballSize = ballSize;
    }

    // this method will actually render the ball for the user onto the JFrame (this
    // will be called by other methods)
    public void renderBall(Graphics ballGraphics) {

        // set the BALL graphics to be white
        ballGraphics.setColor(Color.WHITE);

        // render the pong ball at the position we specify
        ballGraphics.fillOval(ballPosX, ballPosY, ballSize, ballSize); // note, since it is a circular ball, we'll set
                                                                       // both dimensions to the same size variable

    }

    public void bounceOffEdges(int top, int bottom) {

        // if ball is at bottom of screen, move up
        if (ballPosY > bottom - ballSize) {
            bounceY();
        }
        // if ball is at top of screen, move down
        else if (ballPosY < top) {
            bounceY();
        }

    }

    // if the ball bounces off a wall or paddle, these will be used to change its
    // direction
    public void bounceX() {
        changeInX *= -1;
    }

    public void bounceY() {
        changeInY *= -1;
    }

    // every render itll just move the ball to a new position.
    public void updateBallPos() {
        ballPosX += changeInX;
        ballPosY += changeInY;
    }

    // this will allow us to reset the ball
    public void setBallPosX(int newPosX) {
        ballPosX = newPosX;
    }

    public void setBallPosY(int newPosY) {
        ballPosY = newPosY;
    }

    public void setBallChangeX(int newChangeX) {
        changeInX = newChangeX;
    }

    public void setBallChangeY(int newChangeY) {
        changeInY = newChangeY;
    }

    public void setBallSpeed(int newSpeed) {

        changeInX = newSpeed * (changeInX / Math.abs(changeInX));
        changeInY = newSpeed * (changeInY / Math.abs(changeInY));
    }

    // getter functions to check for collisions and stuff in other classes
    public int getPosY() {
        return ballPosY;
    }

    public int getPosX() {
        return ballPosX;
    }

    // checks for if the collision has occurred with the player, and then returns a
    // boolean of true or false
    public boolean checkCollisionStatus(Player player) {
        int rightX = player.getX() + player.paddleWidth;
        int bottomY = player.getY() + player.getHeight();

        // if the x ball position is not within the bounds, return false (not collided)
        if (!(ballPosX > (player.getX() - ballSize) && ballPosX < rightX))
            return false;

        // if the ball is not in the same y coordinate area as the player, return false
        // (not collided)
        if (!(ballPosY > player.getY() && ballPosY < bottomY))
            return false;

        // if the program hasn't returned by this, it means that it has collided
        return true;
    }
}
