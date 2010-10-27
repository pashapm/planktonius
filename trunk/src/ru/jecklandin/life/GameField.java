package ru.jecklandin.life;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class GameField extends TextView {

	public static GameField sInstance;
	
	private LifeGame mGame;
	private boolean mScreenshotMode = false;
	LifeDrawer mDrawer;

	public GameField(Context context, AttributeSet at) {
		super(context, at);
		initialize();
	}
	
	public GameField(Context context) {
		super(context);
		initialize();
	}
	
	private void initialize() {
		sInstance = this;
		mDrawer = new LifeDrawer(this);
	}
	
	public void setGame( LifeGame game) {
		mGame = game;
		mDrawer.setGame(mGame);
		mDrawer.mSelX = game.getDimen() / 2;
		mDrawer.mSelY = game.getDimen() / 2;
	}
	
	public LifeGame getGame() {
		return mGame;
	}

	/**
	 * @param dir 0-top, 1-left, 2-bottom, 3-right
	 */
	public void move(int dir) {
		switch (dir) {
		case 0:
			if (mDrawer.mSelY > 0) {
				mDrawer.mSelY--;
			}
			break;
		case 1:
			if (mDrawer.mSelX > 0) {
				mDrawer.mSelX--;	
			}
			break;
		case 2:
			if (mDrawer.mSelX < mGame.getDimen()) {
				mDrawer.mSelY++;
			}
			break;
		case 3:
			if (mDrawer.mSelX < mGame.getDimen()) {
				mDrawer.mSelX++;
			}
			break;
		default:
			break;
		}
	}
	
	public void save() {
		mGame.save();
	}
	
	public void action() {
		Cell c = mGame.get(mDrawer.mSelX, mDrawer.mSelY);
		if (c.status == Cell.ALIVE) {
			c.status = Cell.DEAD;
		} else {
			c.status = Cell.ALIVE;
			c.additStatus = Cell.NEWBORN;
		}
		mGame.computeNextState();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		PaintFlagsDrawFilter setfil = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);
		canvas.setDrawFilter(setfil);  
		
		if (mScreenshotMode) {
			mDrawer.drawScreenshot(canvas);
		} else {
			mDrawer.drawNormal(canvas);
		}
	}
	
	public Bitmap getScreenShot() {
		mScreenshotMode = true;
		Bitmap bm = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bm);
		onDraw(c);
		mScreenshotMode = false;
		return bm;
	}
	
}

