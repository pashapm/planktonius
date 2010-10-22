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
		mGame.save("/sdcard/baken.xml");
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
	
	public static Bitmap makeScreenshotFromFile(Context ctx, String fn) {
		Bitmap bm = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bm);
		
		try {
			LifeGame game = LifeGame.createFromFile(fn, 15);
			LifeDrawer drawer = new LifeDrawer(ctx);
			drawer.setGame(game);
			drawer.drawScreenshot(c);
			return bm;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}

class LifeDrawer {
	
	private Context mCtx;
	private LifeGame mGame;
	private Paint mPaint = new Paint();
	private Matrix mMatrix = new Matrix();
	private Matrix mScrMatrix = new Matrix();
	private Matrix mAdditMatrix = new Matrix();
	
	private Bitmap mAlive;
	private Bitmap mNewBorn;
	private Bitmap mDying;
	private Bitmap mSel;
	
	private View mView;
	
	int mSelX;
	int mSelY;
	
	boolean mShowCursor = true;
	
	public LifeDrawer(View v) {
		mCtx = v.getContext();
		mView = v;
		init();
	}
	
	public LifeDrawer(Context ctx) {
		mCtx = ctx;
		init();
	}
	
	private void init() {
		mAlive = BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.yellow);
		mNewBorn = BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.green);
		mDying = BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.red);
		mSel = BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.sel);
		
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.WHITE);
//		int dx = (ScrProps.screenWidth-ScrProps.scale(300))/2;
//		mMatrix.setTranslate(dx, ScrProps.scale(50));
	}
	
	public Matrix getMatrix() {
		return mAdditMatrix;
	}
	
	void setGame(LifeGame game) {
		mGame = game;
	}
	
	void drawNormal(Canvas canvas) {
		if (mGame == null) {
			return;
		}
		
		int[] c = { 0, 0 };
		mView.getLocationOnScreen(c);  //WTF?? 
		mMatrix.set(mAdditMatrix);
		mMatrix.postTranslate(c[0], c[1]);
		canvas.setMatrix(mMatrix);
		
//		mMatrix.reset(); 
//		int dx = (ScrProps.screenWidth-ScrProps.scale(300))/2;
//		mMatrix.setTranslate(dx, ScrProps.scale(50));
		
//		int CELL_SIZE = ScrProps.scale(300) / mGame.getDimen();
		int CELL_SIZE = 20;
		
		for (int i = 0; i<mGame.getDimen(); ++i) {
			canvas.drawLine(0, CELL_SIZE*i, CELL_SIZE * mGame.getDimen(), CELL_SIZE*i, mPaint);
			canvas.drawLine(CELL_SIZE*i, 0, CELL_SIZE*i, CELL_SIZE * mGame.getDimen(), mPaint);
		}
		canvas.drawLine(0, CELL_SIZE* mGame.getDimen(), CELL_SIZE * mGame.getDimen(), CELL_SIZE* mGame.getDimen(), mPaint);
		canvas.drawLine(CELL_SIZE* mGame.getDimen(), 0, CELL_SIZE * mGame.getDimen(), CELL_SIZE* mGame.getDimen(), mPaint);
		
		
		Matrix bmatrix = new Matrix(mMatrix);
		
		for (int i = 0; i<mGame.getDimen(); ++i) 
			for (int j = 0; j<mGame.getDimen(); ++j) {
				mPaint.setColor(Color.BLACK);
				canvas.drawRect(CELL_SIZE*i, CELL_SIZE*j, (i+1)*CELL_SIZE, (j+1)*CELL_SIZE, mPaint);
				
				if (mGame.get(i, j).status == Cell.ALIVE) {
					Bitmap bm = mAlive;
					if (mGame.get(i, j).additStatus == Cell.DYING) {
						bm = mDying;
					} else if (mGame.get(i, j).additStatus == Cell.NEWBORN) {
						bm = mNewBorn;
					}
					canvas.drawBitmap(bm, CELL_SIZE*i, CELL_SIZE*j, mPaint);
					mPaint.setColor(Color.WHITE);
				} 		
				if (mSelX == i && mSelY == j && mShowCursor) {
					canvas.drawBitmap(mSel, CELL_SIZE*i, CELL_SIZE*j, mPaint);
				}
			}
	}
	
	void drawScreenshot(Canvas canvas) {
		if (mGame == null) {
			return;
		}
		canvas.setMatrix(mScrMatrix);
		mPaint.setColor(Color.BLACK);
		canvas.drawColor(Color.BLACK);
		
		int CELL_SIZE = ScrProps.scale(100) / mGame.getDimen();
		for (int i = 0; i<mGame.getDimen(); ++i) 
			for (int j = 0; j<mGame.getDimen(); ++j) {
				if (mGame.get(i, j).status == Cell.ALIVE) {
					if (mGame.get(i, j).additStatus == Cell.DYING) {
						mPaint.setColor(Color.parseColor("#ff1f00"));
					} else if (mGame.get(i, j).additStatus == Cell.NEWBORN) {
						mPaint.setColor(Color.parseColor("#00c40a"));
					} else {
						mPaint.setColor(Color.parseColor("#ffa100"));
					}
					canvas.drawRect(CELL_SIZE*i, CELL_SIZE*j, (i+1)*CELL_SIZE, (j+1)*CELL_SIZE, mPaint);
					mPaint.setColor(Color.BLACK);
				} 				
			}
	}
}