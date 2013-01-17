package com.boxai;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

public class BoardView extends TileView {

	private static final int TILE = 1;

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initBoardView(context);
	}

	private RefreshHandler mRedrawHandler = new RefreshHandler();

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

	private void initBoardView(Context context) {

		setFocusable(true);

		Resources r = this.getContext().getResources();

		resetTiles(4);

		loadTile(TILE, r.getDrawable(R.drawable.tile));

		drawBoard();
		invalidate();
		/*
		 * loadTile(RED_STAR, r.getDrawable(R.drawable.redstar));
		 * loadTile(YELLOW_STAR, r.getDrawable(R.drawable.yellowstar));
		 * loadTile(GREEN_STAR, r.getDrawable(R.drawable.greenstar));
		 */
	}

	public void update() {
		drawBoard();
	}

	private void drawBoard() {
		for (int x = 0; x < mXTileCount; x++) {
			setTile(TILE, x, 0);
			setTile(TILE, x, mYTileCount - 1);
		}
		for (int y = 1; y < mYTileCount - 1; y++) {
			setTile(TILE, 0, y);
			setTile(TILE, mXTileCount - 1, y);
		}
	}

}
