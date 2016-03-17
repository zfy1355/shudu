package org.colorful.shudu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

public class PuzzleView extends View {
	private static final String TAG = "shudu";
	private final Game game;

	private float width;
	private float height;
	private int selX;
	private int selY;
	private final Rect selRect = new Rect();

	public PuzzleView(Context context) {
		super(context);
		this.game = (Game) context;
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w / 9f;
		height = h / 9f;
		getRect(selX, selY, selRect);
		Log.d(TAG, "onsizechanged:width " + width + ", height " + height);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	private void getRect(int x, int y, Rect rect) {
		rect.set((int) (x * width), (int) (y * height),
				(int) (x * width + width), (int) (y * height + height));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// draw background
		drawBackground(canvas);

		// draw grid lines
		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.puzzle_dark));
		Paint hilite = new Paint();
		hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
		Paint light = new Paint();
		light.setColor(getResources().getColor(R.color.puzzle_light));
		// draw minor grid lines
		drawMinorLines(canvas, hilite, light);
		// draw the major grid lines
		drawMajorLines(canvas, dark, hilite);
		// define color and style for numbers
		drawNumber(canvas);
		//draw the selection
		drawSelected(canvas);
		//draw the hints
		if(Prefs.getHints(getContext()))
			drawHints(canvas);
		super.onDraw(canvas);
	}

	public void drawHints(Canvas canvas) {
		Paint hint = new Paint();
		int c[] = {getResources().getColor(R.color.puzzle_hint_0),getResources().getColor(R.color.puzzle_hint_1),
				getResources().getColor(R.color.puzzle_hint_2)
		};
		Rect r = new Rect();
		for(int i = 0; i< 9 ; i++){
			for (int j = 0;j < 9;j++){
				int movesleft = 9- game.getUsedTiles(i,j).length;
				if(movesleft < c.length){
					getRect(i, j, r);
					hint.setColor(c[movesleft]);
					canvas.drawRect(r, hint);
				}
			}
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			select(selX,selY-1);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			select(selX,selY+1);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			select(selX-1,selY);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			select(selX+1,selY);
			break;
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_SPACE:
			setSelectedTile(0);break;
		case KeyEvent.KEYCODE_1:
			setSelectedTile(1);break;
		case KeyEvent.KEYCODE_2:
			setSelectedTile(2);break;
		case KeyEvent.KEYCODE_3:
			setSelectedTile(3);break;
		case KeyEvent.KEYCODE_4:
			setSelectedTile(4);break;
		case KeyEvent.KEYCODE_5:
			setSelectedTile(5);break;
		case KeyEvent.KEYCODE_6:
			setSelectedTile(6);break;
		case KeyEvent.KEYCODE_7:
			setSelectedTile(7);break;
		case KeyEvent.KEYCODE_8:
			setSelectedTile(8);break;
		case KeyEvent.KEYCODE_9:
			setSelectedTile(9);break;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			game.showKeypadOrError(selX,selY);
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() != MotionEvent.ACTION_DOWN){
			return super.onTouchEvent(event);
		}
		select((int)(event.getX() / width), (int)(event.getY() / height));
		game.showKeypadOrError(selX, selY);
		Log.d(TAG, "onTouchEvent:x " + selX + ", y "+ selY);
		return true;
	}
	public void setSelectedTile(int tile){
		if(game.setTileIfValid(selX,selY,tile)){
			invalidate();
		}else{
			startAnimation(AnimationUtils.loadAnimation(game, R.anim.shake));
			Log.d(TAG,"setSelectedTile : invalid: " +tile);
		}
	}

	private void select(int x, int y) {
		invalidate(selRect);
		selX = Math.min(Math.max(x, 0), 8);
		selY = Math.min(Math.max(y, 0), 8);
		getRect(selX, selY, selRect);
		invalidate();
	}

	public void drawSelected(Canvas canvas) {
		Paint selected= new Paint();
		selected.setColor(getResources().getColor(R.color.puzzle_selected));
		canvas.drawRect(selRect, selected);
	}

	public void drawNumber(Canvas canvas) {
		Paint foregroudPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		foregroudPaint.setColor(getResources().getColor(
				R.color.puzzle_foreground));
		foregroudPaint.setStyle(Style.FILL);
		foregroudPaint.setTextSize(height / 0.75f);
		foregroudPaint.setTextScaleX(width / height);
		foregroudPaint.setTextAlign(Paint.Align.CENTER);
		// draw number
		FontMetrics fMetrics = foregroudPaint.getFontMetrics();
		float x = width / 2;
		float y = height / 2 - (fMetrics.ascent + fMetrics.descent) / 2;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++)
				canvas.drawText(this.game.getTitleString(i, j), i * width + x,
						j * height + y, foregroudPaint);
		}
	}

	public void drawBackground(Canvas canvas) {
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.puzzle_background));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
	}

	public void drawMajorLines(Canvas canvas, Paint dark, Paint hilite) {
		for (int i = 0; i < 9; i++) {
			if (i % 3 != 0) {
				continue;
			}
			canvas.drawLine(0, i * height, getWidth(), i * height, dark);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1,
					hilite);
			canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(),
					hilite);
		}
	}

	public void drawMinorLines(Canvas canvas, Paint hilite, Paint light) {
		for (int i = 0; i < 9; i++) {
			canvas.drawLine(0, i * height, getWidth(), i * height, light);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1,
					hilite);
			canvas.drawLine(i * width, 0, i * width, getHeight(), light);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(),
					hilite);
		}
	}

}
