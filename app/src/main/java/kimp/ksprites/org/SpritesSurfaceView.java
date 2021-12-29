package kimp.ksprites.org;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

/**
 * Special view for sprites render
 */
public class SpritesSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private SpritesSurfaceThread drawThread;
    private int iteration = 0;

    private Sprite sprite;

    /**
     * Creates new view instance
     */
    public SpritesSurfaceView(@NonNull Context context) {
        super(context); getHolder().addCallback(this);
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        Bitmap texture = BitmapFactory.decodeResource(getResources(), R.drawable.cat_sprites);
        sprite = new Sprite(this, texture);
    }

    /**
     * Sets new sprite texture
     * @param resourceId Texture id for loading
     */
    public void changeTexture(int resourceId) {
        Bitmap texture = BitmapFactory.decodeResource(getResources(), resourceId);

        sprite = new Sprite(this, texture);
    }

    /**
     * Returns currently used sprite
     */
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        drawThread = new SpritesSurfaceThread(this, getHolder());
        drawThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) { }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        drawThread.finish(); boolean retry = true;

        while (retry) {
            try {
                drawThread.join(); retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorOnPrimary, typedValue, true);
        canvas.drawColor(typedValue.data);

        sprite.draw(canvas, iteration);
        iteration++;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            sprite.setTargetPosition(event.getX(), event.getY());
        }

        return true;
    }
}
