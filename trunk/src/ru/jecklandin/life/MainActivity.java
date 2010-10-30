package ru.jecklandin.life;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity implements OnClickListener {
	
	public static int MAIN_DIMENSION = 15;
	
	private LifeGame mGame;
	private GameField mField;
	private Updater mUpdater;
	
	private ImageButton mRight;
	private ImageButton mLeft;
	private ImageButton mTop;
	private ImageButton mBottom;
	private ImageButton mAction;
	private Button mSave;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrProps.initialize(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.alter);
        
        try {
        	String asset = getIntent().getStringExtra("asset");
        	if (asset != null) {
        		mGame = LifeGame.createFromStream(getAssets().open(asset+".xml"), 15);
        	} else {
        		mGame = LifeGame.createFromFile(LifeApp.mMatrixFile, 15);
        	}
        	mGame.save();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        mField = (GameField) findViewById(R.id.field);
        mField.setGame(mGame);
        mUpdater = new Updater(mField);
        mUpdater.start();
        
        mRight = (ImageButton) findViewById(R.id.right);
        mLeft = (ImageButton) findViewById(R.id.left);
        mTop = (ImageButton) findViewById(R.id.up);
        mBottom = (ImageButton) findViewById(R.id.down);
        mAction = (ImageButton) findViewById(R.id.action);
        mSave = (Button) findViewById(R.id.save);
        
        mRight.setOnClickListener(this);
        mLeft.setOnClickListener(this);
        mTop.setOnClickListener(this);
        mBottom.setOnClickListener(this);
        mAction.setOnClickListener(this);
        mSave.setOnClickListener(this);
    }
    
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.right:
			mField.move(3);
			break;
		case R.id.left:
			mField.move(1);
			break;
		case R.id.up:
			mField.move(0);
			break;
		case R.id.down:
			mField.move(2);
			break;
		case R.id.save:
			mField.save();
			break;
		case R.id.action:
			mField.action();
			mGame.save();
			break;
		default:
			break;
		}
	}
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			mField.move(3);
			return true;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			mField.move(1);
			return true;
		case KeyEvent.KEYCODE_DPAD_UP:
			mField.move(0);
			return true;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			mField.move(2);
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
    		mGame.next(true);
    	}
    	return super.onTouchEvent(event);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	 MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.main_menu, menu);
         return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == R.id.info) {
    		Intent i = new Intent(this, Info.class);
    		startActivity(i);
    	}
    	return super.onOptionsItemSelected(item);
    }

    
}