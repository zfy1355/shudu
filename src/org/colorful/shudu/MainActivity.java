package org.colorful.shudu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);
		View continuButton = findViewById(R.id.continue_button);
		continuButton.setOnClickListener(this);
		View newButton = findViewById(R.id.new_button);
		newButton.setOnClickListener(this);
		View aboutButton = findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);
		View exitButton = findViewById(R.id.exit_button);
		exitButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.setting:
			startActivity(new Intent(this,Prefs.class));
			return true;

		default:
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.continue_button:
			startGame(Game.DIFFICULTY_CONTINUE);
		case R.id.about_button:
			Intent intent = new Intent(this,AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.new_button:
			new AlertDialog.Builder(this).setTitle(R.string.new_game_title)
			.setItems(R.array.difficalty, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startGame(which);
				}
			}).show();
			break;
		case R.id.exit_button:
			finish();
			break;
		default:
			break;
		}
	}
	
	private void startGame(int difficaulty) {
		Log.d("Start","clicked on " + difficaulty);
		Intent intent = new Intent(MainActivity.this,Game.class);
		intent.putExtra(Game.KEY_DIFFICALTY, difficaulty);
		startActivity(intent);
	}
	@Override
	protected void onResume() {
		super.onResume();
		Music.play(this,R.raw.main);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Music.stop(this);
	}
	
}
