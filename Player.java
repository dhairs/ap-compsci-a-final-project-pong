import java.awt.*; // we'll again use this so we can choose colors for Graphics

public class Player {

    // declare instance variables
    private int playerPaddleHeight, playerPaddlePosX, playerPaddlePosY, playerPaddleSpeed;
    private Color playerPaddleColor;

    // constant
    final int paddleWidth = 15;

    // this will just set up the paddle that the
    public Player(int x, int y, int height, int speed, Color color) {
        this.playerPaddlePosX = x;
        this.playerPaddlePosY = y;
        this.playerPaddleHeight = height;
        this.playerPaddleSpeed = speed;
        this.playerPaddleColor = color;
    }

    public void movePlayer(int desiredPositionY) {

        int centerY = playerPaddlePosY + (playerPaddleHeight / 2);

        // basically, this makes sure the paddle won't jitter around a ton
        if (!(Math.abs(centerY - desiredPositionY) > playerPaddleSpeed))
            return;
        // if the paddle is below the position we want to be at
        if (centerY > desiredPositionY) {
            // move the paddle up by the speed
            playerPaddlePosY -= playerPaddleSpeed;
        }
        // if the center of the paddle is above wherver the position we want to move to
        // is
        if (centerY < desiredPositionY) {
            // move the paddle down by speed
            playerPaddlePosY += playerPaddleSpeed;
        }
    }

    public void render(Graphics playerPaddleGraphics) {

        // paint the rectangle for the paddle
        playerPaddleGraphics.setColor(playerPaddleColor);
        playerPaddleGraphics.fillRect(playerPaddlePosX, playerPaddlePosY, paddleWidth, playerPaddleHeight);

    }

    public int getX() {
        return playerPaddlePosX;
    }

    public int getY() {
        return playerPaddlePosY;
    }

    public int getHeight() {
        return playerPaddleHeight;
    }
}