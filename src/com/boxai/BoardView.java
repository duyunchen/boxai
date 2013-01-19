package com.boxai;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class BoardView extends View {

    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 10;

    public static final int BACKGROUND_TILE = 0;
    public static final int PREY_TILE = 1;
    public static final int PREDATOR_TILE = 2;

    public static int UPDATE_DELAY = 600;

    private final Paint mPaint = new Paint();

    private Bitmap[] TILE_BITMAPS = new Bitmap[10];

    private ArrayList<Entity> entities = new ArrayList<Entity>();

    private int width, height;

    private int TILE_SIZE = 70;

    /**
     * Create a simple handler that we can use to cause animation to happen. We
     * set ourselves as a target and we can use the sleep() function to cause an
     * update/invalidate to occur at a later date.
     */

    private RefreshHandler refreshHandler = new RefreshHandler();

    class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            BoardView.this.update();
            BoardView.this.invalidate();
        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBoardView(context);

        // get size of the window
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        this.width = size.x;
        this.height = size.y;

        TILE_SIZE = this.width / BOARD_WIDTH;

        Entity prey = new BasicPrey(3, 3, PREY_TILE);
        entities.add(prey);

        Entity predator = new BasicPredator(9, 9, PREDATOR_TILE);
        entities.add(predator);

        update();
    }

    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initBoardView(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int ii = 0; ii < BOARD_WIDTH; ii++) {
            for (int jj = 0; jj < BOARD_HEIGHT; jj++) {
                canvas.drawBitmap(TILE_BITMAPS[BACKGROUND_TILE], ii * TILE_SIZE, jj * TILE_SIZE, mPaint);
            }
        }

        for (Entity entity : entities) {
            canvas.drawBitmap(TILE_BITMAPS[entity.getTileIndex()], entity.getX() * TILE_SIZE, entity.getY() * TILE_SIZE, mPaint);
        }
    }

    public void update() {
        Prey prey = (Prey) this.entities.get(0);
        Predator predator = (Predator) this.entities.get(1);
        float distance = (float) Math.sqrt((float) (prey.getX() * prey.getX()) + (float) (predator.getY() * predator.getY()));

        for (Entity entity : entities) {
            entity.update(distance);
        }

        if (checkWin()) {
            endGame();
            return;
        }
        refreshHandler.sleep(UPDATE_DELAY);
    }

    public boolean checkWin() {
        Prey prey = (Prey) this.entities.get(0);
        Predator predator = (Predator) this.entities.get(1);

        if (prey.getX() == predator.getX() && prey.getY() == predator.getY()) {
            return true;
        }
        return false;
    }

    public void endGame() {
        // stop the timer

        AlertDialog.Builder ad = new AlertDialog.Builder((Boxai) getContext());

        ad.setMessage("Game over.");
        ad.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        ad.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        ad.create().show();
    }

    private void initBoardView(Context context) {
        Resources r = this.getResources();

        this.loadTile(BACKGROUND_TILE, r.getDrawable(R.drawable.tile));
        this.loadTile(PREY_TILE, r.getDrawable(R.drawable.prey_tile));
        this.loadTile(PREDATOR_TILE, r.getDrawable(R.drawable.predator_tile));
    }

    public void loadTile(int key, Drawable tile) {
        Bitmap bitmap = Bitmap.createBitmap(TILE_SIZE, TILE_SIZE, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tile.setBounds(0, 0, TILE_SIZE, TILE_SIZE);
        tile.draw(canvas);

        TILE_BITMAPS[key] = bitmap;
    }

}
