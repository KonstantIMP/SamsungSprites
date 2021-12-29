package kimp.ksprites.org;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

/**
 * Execute SpritesSurfaceView drawing
 */
public class SpritesSurfaceThread extends Thread {
    private SpritesSurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private boolean isRunning = false;

    private long timeDelay;

    public static final long DRAW_DELAY_MILLIS = 250;

    public SpritesSurfaceThread(@NonNull SpritesSurfaceView view, @NonNull SurfaceHolder holder) {
        surfaceView = view; surfaceHolder = holder;
        timeDelay = System.currentTimeMillis();
    }

    public synchronized void finish() {
        isRunning = false;
    }

    @Override
    public synchronized void start() {
        isRunning = true; super.start();
    }

    @Override
    public void run() {
        while (isRunning) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - timeDelay >= DRAW_DELAY_MILLIS) {
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas == null) return;
                synchronized (surfaceHolder) {
                    surfaceView.draw(canvas);
                }
                surfaceHolder.unlockCanvasAndPost(canvas);

                timeDelay = currentTime;
            }
        }
    }
}
