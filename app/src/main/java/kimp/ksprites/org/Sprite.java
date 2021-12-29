package kimp.ksprites.org;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Random;

/**
 * Drawable object for hero show
 */
public class Sprite {
    private SpritesSurfaceView surfaceView;
    private Bitmap spritesTexture;

    private final int spriteWidth, spriteHeight;

    private Pair<Double, Double> currentPosition;
    private Pair<Double, Double> targetPosition;
    private Pair<Double, Double> speed;

    private double speedMultiply = 0.5;
    private double scaleFactor = 1.0;

    public static final int SPRITE_SPEED_DPS = 30;

    private HashMap<Integer, Rect[]> animationRows = new HashMap<>();

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * Creates the sprite
     * @param view Parent which display the Sprite
     * @param texture Texture with full sprites map
     */
    public Sprite(@NonNull SpritesSurfaceView view, Bitmap texture) {
        surfaceView = view; spritesTexture = texture;
        spriteWidth = texture.getWidth() / 3;
        spriteHeight = texture.getHeight() / 4;

        for (Direction direction: new Direction[]{Direction.DIRECTION_DOWN, Direction.DIRECTION_LEFT, Direction.DIRECTION_RIGHT, Direction.DIRECTION_UP}) {
            Rect [] currentRow = new Rect[3];

            for (int i = 0; i < 3; i++) {
                currentRow[i] = new Rect(spriteWidth * i, direction.rowNumber * spriteHeight, spriteWidth * i + spriteWidth, direction.rowNumber * spriteHeight + spriteHeight);
            }

            animationRows.put(direction.rowNumber, currentRow);

            if (direction == Direction.DIRECTION_DOWN) {
                animationRows.put(Direction.DIRECTION_STOP.rowNumber, new Rect[]{currentRow[1]});
            }
        }

        currentPosition = new Pair<>(view.getWidth() / 2.0, view.getHeight() / 2.0);
        //currentPosition = new Pair<>(25.0, 25.0);
        speed = new Pair<>(0.0, 0.0);
        targetPosition = currentPosition;
    }

    /**
     * Draws the sprite on the canvas
     * @param canvas Canvas for drawing
     * @param iteration Number of drawing(For animation)
     */
    public void draw(@NonNull Canvas canvas, int iteration) {
        currentPosition = new Pair<>(currentPosition.first + speed.first,
                currentPosition.second + speed.second);
        checkWall();

        Direction currentDirection;
        if (currentPosition.equals(targetPosition) || (speed.first == 0 && speed.second == 0)) currentDirection = Direction.DIRECTION_STOP;
        else if (speed.second > 0 && speed.second > Math.abs(speed.first)) currentDirection = Direction.DIRECTION_DOWN;
        else if (speed.first > 0 && speed.first > Math.abs(speed.second)) currentDirection = Direction.DIRECTION_RIGHT;
        else if (speed.first < 0 && Math.abs(speed.first) > Math.abs(speed.second)) currentDirection = Direction.DIRECTION_LEFT;
        else currentDirection = Direction.DIRECTION_UP;

        if (iteration % 20 == 19) {
            Random random = new Random();
            scaleFactor = Math.abs(random.nextGaussian());
        }

        Rect [] animation = animationRows.get(currentDirection.rowNumber);
        Rect source = new Rect(currentPosition.first.intValue(), currentPosition.second.intValue(), currentPosition.first.intValue() + (int)(spriteWidth * scaleFactor), currentPosition.second.intValue() + (int)(scaleFactor * spriteHeight));

        canvas.drawBitmap(spritesTexture, animation[iteration % animation.length], source, paint);
    }

    /**
     * Set sprite's target position
     */
    public void setTargetPosition(double targetX, double targetY) {
        targetPosition = new Pair<>(Math.min(targetX, surfaceView.getWidth()),
                Math.min(targetY, surfaceView.getHeight()));
        calculateSpeed();
    }

    /**
     * Set speed multiply for calculations
     */
    public void setSpeedMultiply(double multiply) {
        speedMultiply = multiply; calculateSpeed();
    }

    private void calculateSpeed() {
        speed = new Pair<>(speedMultiply * SPRITE_SPEED_DPS * (targetPosition.first - currentPosition.first) / surfaceView.getWidth(),
                speedMultiply * SPRITE_SPEED_DPS * (targetPosition.second - currentPosition.second) / surfaceView.getHeight());
        if (surfaceView.getHeight() == 0 || surfaceView.getWidth() == 0) speed = new Pair<>(0.0, 0.0);
    }

    private void checkWall() {
        if (currentPosition.first < 0 || currentPosition.first + spriteWidth >= surfaceView.getWidth()) {
            speed = new Pair<>(-speed.first, speed.second);
        }
        if (currentPosition.second < 0 || currentPosition.second + spriteHeight >= surfaceView.getHeight()) {
            speed = new Pair<>(speed.first, -speed.second);
        }
    }
}
