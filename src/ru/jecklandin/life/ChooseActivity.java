package ru.jecklandin.life;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChooseActivity extends Activity implements OnClickListener {

	private GameField mField1;
	private GameField mField2;
	private GameField mField3;
	
	private LinearLayout mLay1;
	private LinearLayout mLay2;
	private LinearLayout mLay3;
	
	private TextView mTw1;
	private TextView mTw2;
	private TextView mTw3;
	
	private Updater mUpdater;
	
	private Button mNext;
	private String mSelectedAsset = "toad";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		ScrProps.initialize(this);
		
		setContentView(R.layout.choose);
		
		setResult(RESULT_CANCELED);
		
		mNext = (Button) findViewById(R.id.next);
		mNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent i = new Intent(ChooseActivity.this, MainActivity.class);
//				i.putExtra("asset", mSelectedAsset);
//				startActivity(i);
				
				 try {
					LifeGame.createFromStream(getAssets().open(mSelectedAsset+".xml"), 15).save();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				 Bundle extras = getIntent().getExtras();
				 int mAppWidgetId = extras.getInt(
	                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
				 Log.d("!!!!!!",  mAppWidgetId+"");
				 Intent resultValue = new Intent();
		         resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		         setResult(RESULT_OK, resultValue);
		         finish();
			}
		});
		
		mField1 = (GameField) findViewById(R.id.start1);
		mField2 = (GameField) findViewById(R.id.start2);
		mField3 = (GameField) findViewById(R.id.start3);
		try {
			mField1.setGame(LifeGame.createFromStream(getAssets().open("toad_l.xml"), 6));
			mField2.setGame(LifeGame.createFromStream(getAssets().open("baken_l.xml"), 6));
			mField3.setGame(LifeGame.createFromStream(getAssets().open("watch_l.xml"), 6));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mLay1 = ((LinearLayout) findViewById(R.id.lay1));
		mLay2 = ((LinearLayout) findViewById(R.id.lay2));
		mLay3 = ((LinearLayout) findViewById(R.id.lay3));
		
		mLay1.setOnClickListener(this);
		mLay2.setOnClickListener(this);
		mLay3.setOnClickListener(this);
		
		mTw1 = ((TextView) findViewById(R.id.tw1)); 
		mTw2 = ((TextView) findViewById(R.id.tw2)); 
		mTw3 = ((TextView) findViewById(R.id.tw3)); 
		
		mField1.mDrawer.getMatrix().setScale(0.75f, 0.75f);
		mField2.mDrawer.getMatrix().setScale(0.75f, 0.75f);
		mField3.mDrawer.getMatrix().setScale(0.75f, 0.75f);
		
		mField1.mDrawer.mShowCursor = false;
		mField2.mDrawer.mShowCursor = false;
		mField3.mDrawer.mShowCursor = false;
		
		mUpdater = new Updater(mField1, mField2, mField3);
	    mUpdater.start();
	    
	    final Handler h = new Handler();
	    h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if (! mUpdater.isPaused) {
					mField1.getGame().next(false);
					mField2.getGame().next(false);
					mField3.getGame().next(false);
				}
				h.postDelayed(this, 700);
			}
		}, 700);
	    
	    select(2);
	}
	
	@Override
	protected void onStart() {
		mUpdater.isPaused = false;
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		mUpdater.isPaused = true;
		super.onStop();
	}
	
	private void select(int laynum) {
		switch (laynum) {
		case 1:
			mLay1.setBackgroundColor(Color.parseColor("#ffc169"));
			mLay2.setBackgroundColor(Color.parseColor("#00000000"));
			mLay3.setBackgroundColor(Color.parseColor("#00000000"));
			
			mTw1.setTextColor(Color.BLACK);
			mTw2.setTextColor(Color.WHITE);
			mTw3.setTextColor(Color.WHITE);
			
			mSelectedAsset = "toad";
			break;
		case 2:
			mLay1.setBackgroundColor(Color.parseColor("#00000000"));
			mLay2.setBackgroundColor(Color.parseColor("#ffc169"));
			mLay3.setBackgroundColor(Color.parseColor("#00000000"));
			
			mTw1.setTextColor(Color.WHITE);
			mTw2.setTextColor(Color.BLACK);
			mTw3.setTextColor(Color.WHITE);
			
			mSelectedAsset = "baken";
			break;
		case 3:
			mLay1.setBackgroundColor(Color.parseColor("#00000000"));
			mLay2.setBackgroundColor(Color.parseColor("#00000000"));
			mLay3.setBackgroundColor(Color.parseColor("#ffc169"));
			
			mTw1.setTextColor(Color.WHITE);
			mTw2.setTextColor(Color.WHITE);
			mTw3.setTextColor(Color.BLACK);
			
			mSelectedAsset = "watch";
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay1:
			select(1);
			break;
		case R.id.lay2:
			select(2);
			break;
		case R.id.lay3:
			select(3);
			break;
		default:
			break;
		}

	}
}
