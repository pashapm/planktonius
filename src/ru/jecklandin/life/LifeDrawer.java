package ru.jecklandin.life;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

public class LifeDrawer {
	
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
	
	public void setGame(LifeGame game) {
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
				int status = mGame.get(i, j).status;
				if (status == Cell.ALIVE) {
					Bitmap bm = mAlive;
					if (mGame.get(i, j).additStatus == Cell.DYING) {
						bm = mDying;
					} else if (mGame.get(i, j).additStatus == Cell.NEWBORN) {
						bm = mNewBorn;
					} 
					canvas.drawBitmap(bm, CELL_SIZE*i, CELL_SIZE*j, mPaint);
					mPaint.setColor(Color.WHITE);
				} else if (status == Cell.DEAD &&  mGame.get(i, j).additStatus == Cell.NEXTBORN)  {
					mPaint.setColor(Color.parseColor("#00ff00"));
					canvas.drawCircle(CELL_SIZE*(i+0.5f), CELL_SIZE*(j+0.5f), 1, mPaint);
				}
				
				if (mSelX == i && mSelY == j && mShowCursor) {
					canvas.drawBitmap(mSel, CELL_SIZE*i, CELL_SIZE*j, mPaint);
				}
			}
	}
	
	public void drawScreenshot(Canvas canvas) {
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