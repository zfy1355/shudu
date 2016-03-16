package org.colorful.shudu;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Game extends Activity {
	private static final String TAG = "sudoku";
	public static final String KEY_DIFFICALTY = "org.colorful.shudu.difficulty";
	
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICALTY_HARD = 2;
	
	private int puzzle[] = new int[9*9];
	private PuzzleView puzzleView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"oncreate");
		int diff = getIntent().getIntExtra(KEY_DIFFICALTY, DIFFICULTY_EASY);
		puzzle = getPuzzle(diff);
		calculateUsedTiles();
		puzzleView = new PuzzleView(this);
		setContentView(puzzleView);
		puzzleView.requestFocus();
	}

	private void calculateUsedTiles() {
		// TODO Auto-generated method stub
		
	}

	private int[] getPuzzle(int diff) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitleString(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean setTileIfValid(int selX, int selY, int tile) {
		// TODO Auto-generated method stub
		return false;
	}

	public void showKeypadOrError(int selX, int selY) {
		// TODO Auto-generated method stub
		
	}

	public int[] getUsedTiles(int i, int j) {
		// TODO Auto-generated method stub
		int[] s = {}; 
		return s;
	}
}
