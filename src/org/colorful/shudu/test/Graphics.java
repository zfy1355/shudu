package org.colorful.shudu.test;

import javax.security.auth.PrivateCredentialPermission;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.os.Bundle;
import android.view.View;

public class Graphics extends Activity{
	private static final String QUOTE = " now is the time for all "+ "good men to come to the aid of the country.";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(new GraphicsView(this));
	}
	static public class GraphicsView extends View{
		public GraphicsView(Context context){
			super(context);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			Path circlePath = new Path();
			circlePath.addCircle(150, 150, 100, Direction.CW);
		
//			canvas.drawPath(circlePath, cPaint);
//			canvas.drawTextOnPath(QUOTE, circlePath, 0, 20, tpaint);
		}
	}
}
