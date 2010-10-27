package ru.jecklandin.life;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Context;
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
			mGame = LifeGame.createFromFile(LifeApp.mMatrixFile, 15);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        
//        mGame = new LifeGame(MAIN_DIMENSION);
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