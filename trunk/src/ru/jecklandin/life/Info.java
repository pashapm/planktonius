package ru.jecklandin.life;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Info extends Activity implements OnClickListener {
	
	private int mPrevScreen = 0;
	private boolean mInfoMode = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.intro);
		setResult(RESULT_CANCELED);
		mInfoMode = getIntent().getBooleanExtra("mode", false);
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
			Button contBtn = (Button) findViewById(R.id.cont_btn);
			if (mInfoMode) {
				contBtn.setVisibility(View.GONE);
			} else {
				findViewById(R.id.further).setVisibility(View.GONE);
				contBtn.setOnClickListener(this);
			}
			mPrevScreen = 2;
			break;
		case 2:
			if (mInfoMode) {
				finish();
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.cont_btn:
			Intent intent = getIntent();
			Bundle extras = intent.getExtras();
			int mAppWidgetId = 0;
			if (extras != null) {
			    mAppWidgetId = extras.getInt(
			            AppWidgetManager.EXTRA_APPWIDGET_ID, 
			            AppWidgetManager.INVALID_APPWIDGET_ID);
			}
			
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
			setResult(RESULT_OK, resultValue);
			finish();
			break;
		default:
			break;
		}
		
	}
}
