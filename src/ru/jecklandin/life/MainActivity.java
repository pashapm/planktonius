package ru.jecklandin.life;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	
	public static int MAIN_DIMENSION = 6;
	
	private LifeGame mGame;
	private GameField mField;
	private Updater mUpdater;
	
	private Button mRight;
	private Button mLeft;
	private Button mTop;
	private Button mBottom;
	private Button mAction;
	private Button mSave;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrProps.initialize(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.alter);
        
        try {
			mGame = LifeGame.createFromFile("/sdcard/baken_l.xml", 6);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        
//        mGame = new LifeGame(MAIN_DIMENSION);
        mField = (GameField) findViewById(R.id.field);
        mField.setGame(mGame);
        mUpdater = new Updater(mField);
        mUpdater.start();
        
        mRight = (Button) findViewById(R.id.right);
        mLeft = (Button) findViewById(R.id.left);
        mTop = (Button) findViewById(R.id.up);
        mBottom = (Button) findViewById(R.id.down);
        mAction = (Button) findViewById(R.id.action);
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
    		mGame.next();
    	}
    	return super.onTouchEvent(event);
    }
    
    


    
}