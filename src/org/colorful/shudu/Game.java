package org.colorful.shudu;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class Game extends Activity {
	private static final String TAG = "sudoku";
	public static final String KEY_DIFFICALTY = "org.colorful.shudu.difficulty";
	
	private static final String PREF_PUZZLE = "puzzle";
	protected static final int DIFFICULTY_CONTINUE = -1;
	
	
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICALTY_HARD = 2;
	
	private int puzzle[] = new int[9*9];
	private PuzzleView puzzleView;
	
	private final int used[][][] = new int[9][9][];
	
	private final String easyPuzzle = "360000000004230800000004200"+"070460003820000014500013020"
	+"001900000007048300000000045";
	private final String mediumPuzzle = "650000070000506000014000005"+"650000070000506000014000005"
	+"500000630000201000030000097";
	private final String hardPuzzle = "009000000080605020501078000"+"000000700706040102004000000"
	+"000720903090301080000000600";
	
	
	protected int[] getUsedTiles(int x, int y) {
		return used[x][y];
	}
	
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
		//if the activity is restarted , do a continue next time
		getIntent().putExtra(KEY_DIFFICALTY, DIFFICULTY_CONTINUE);
	}

	private void calculateUsedTiles() {
		for( int x = 0; x< 9;x++){
			for(int y=0;y<9; y++){
				used[x][y] = calculateUsedTiles(x,y);
			}
		}
		
	}

	private int[] calculateUsedTiles(int x, int y) {
		int c[] = new int[9];
		//horizontal
		for (int i = 0;i<9 ;i++){
			if(i == y)
				continue;
			int t = getTitle(x,i);
			if(t != 0)
				c[t - 1] = t;
		}
		//vertical
		for (int i = 0; i<9; i++){
			if(i== x){
				continue;
			}
			int t = getTitle(i, y);
			if(t != 0)
				c[t - 1] = t;
		}
		// same cell block
		int startx = (x / 3) * 3;
		int starty = (y/ 3) * 3;
		for (int i = startx; i< startx +3; i++){
			for(int j = starty; j <starty + 3; j++){
				if(i== x && j==y)
					continue;
				int t = getTitle(i, j);
				if(t != 0)
					c[t - 1] = t;
			}
		}
		//compress
		int nused = 0;
		for (int t : c){
			if(t != 0)
				nused++;
		}
		int c1[] = new int[nused];
		nused = 0 ;
		for(int t : c){
			if( t !=0)
				c1[nused++] = t;
		}
		return c1;
	}

	private int getTitle(int x, int y) {
		return puzzle[y * 9 +x];
	}

	private int[] getPuzzle(int diff) {
		String puz;
		switch (diff) {
		case DIFFICULTY_CONTINUE:
			puz = getPreferences(MODE_PRIVATE).getString(PREF_PUZZLE, easyPuzzle);
			break;
		case DIFFICULTY_EASY:
			puz = easyPuzzle;
			break;
		case DIFFICULTY_MEDIUM:
			puz = mediumPuzzle;
			break;
		case DIFFICALTY_HARD:
			puz = hardPuzzle;
		default:
			puz = easyPuzzle;
			break;
		}
		return fromPuzzleString(puz);
	}



	public String getTitleString(int x, int y) {
		int v = getTitle(x, y);
		if(v == 0){
			return "";
		}else{
			return String.valueOf(v);
		}
	}

	public boolean setTileIfValid(int selX, int selY, int value) {
		int tiles[] = getUsedTiles(selX, selY);
		if(value != 0){
			for (int t : tiles){
				if(t == value){
					return false;
				}
			}
		}
		setTile(selX, selY, value);
		calculateUsedTiles();
		return true;
	}

	private void setTile(int x, int y, int value) {
		puzzle[y * 9 +x]  = value;
		
	}

	public void showKeypadOrError(int selX, int selY) {
		int tiles[] = getUsedTiles(selX, selY);
		if(tiles.length == 9){
			Toast toast = Toast.makeText(this,R.string.no_moves_label,  Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}else{
			Log.d(TAG,"showKeypad:used="+toPuzzleString(tiles));
			Dialog vDialog = new Keypad(this, tiles, puzzleView);
			vDialog.show();
		}
	}

	private String toPuzzleString(int[] puz) {
		StringBuilder buf = new StringBuilder();
		for (int element : puz){
			buf.append(element);
		}
		return buf.toString();
	}
	private int[] fromPuzzleString(String str) {
		int[] puz = new int[str.length()];
		for(int i = 0;i<puz.length;i++){
			puz[i] = str.charAt(i)-'0';
		}
		return puz;
	}
	@Override
	protected void onResume() {
		super.onResume();
		Music.play(this,R.raw.game);
	}
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		Music.stop(this);
		getPreferences(MODE_PRIVATE).edit().putString(PREF_PUZZLE, toPuzzleString(puzzle)).commit();
	}

}
