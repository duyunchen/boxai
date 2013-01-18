package com.boxai;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class BoardView extends View {

	private static final int BOARD_WIDTH = 10;
	private static final int BOARD_HEIGHT = 10;

	private final Paint mPaint = new Paint();

	private Bitmap[] TILE_BITMAPS = new Bitmap[2];

	private ArrayList<Entity> entities = new ArrayList<Entity>();

	private final int TILE_SIZE = 70;

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initBoardView(context);

		invalidate();
	}

	public BoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initBoardView(context);
	}

	@Override
	public void onDraw(Canvas canvas) {
		for (int ii = 0; ii < BOARD_WIDTH; ii++) {
			for (int jj = 0; jj < BOARD_HEIGHT; jj++) {
				canvas.drawBitmap(TILE_BITMAPS[1], ii * TILE_SIZE, jj
						* TILE_SIZE, mPaint);
			}
		}

	}

	private void initBoardView(Context context) {
		Resources r = this.getResources();

		this.loadTile(1, r.getDrawable(R.drawable.tile));
	}

	public void loadTile(int key, Drawable tile) {
		Bitmap bitmap = Bitmap.createBitmap(TILE_SIZE, TILE_SIZE,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		tile.setBounds(0, 0, TILE_SIZE, TILE_SIZE);
		tile.draw(canvas);

		TILE_BITMAPS[key] = bitmap;
	}

}
