package ru.jecklandin.life;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;

public class Info extends Activity {
	
	private int mPrevScreen = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.intro);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN)  {
			return true;
		}
		
		switch (mPrevScreen) {
		case 0:
			setContentView(R.layout.info_evo);
			mPrevScreen = 1;
			break;
		case 1:
			setContentView(R.layout.info_units);
			mPrevScreen = 2;
			break;
		case 2:
			finish();
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}
}
