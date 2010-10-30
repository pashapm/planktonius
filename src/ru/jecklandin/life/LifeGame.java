package ru.jecklandin.life;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.os.Handler;

public class LifeGame {

	private int mDimen;
	private LifeMatrix mMatrix;
	
	public LifeGame(int dim) {
		super();
		mDimen = dim;
		mMatrix = new LifeMatrix(dim, 0.3);
		mMatrix.learnAfter();
	}
	
	public long getCache() {
		return mMatrix.mCache;
	}
	
	public int getDimen() {
		return mDimen;
	}
	
	public Cell get(int x, int y) {
		return mMatrix.get(x, y);
	}
	
//	public void set(int x, int y, int val) {
//		mMatrix.setValue(x, y, val);
//	}
	
	public void computeNextState() {
		mMatrix.computeNextState();
	}
	
	public void next(boolean save) {
		mMatrix.learn();
		mMatrix.change();
		mMatrix.learnAfter();
		
		if (save) {
			mMatrix.mCache += mMatrix.countMoney();
			mMatrix.writeXmlToFile(LifeApp.mMatrixFile);
		}
	}
	
	public void save() {
		mMatrix.writeXmlToFile(LifeApp.mMatrixFile);
	}
	
	public static LifeGame createFromFile(String fn, int dim) throws FileNotFoundException {
		LifeGame game = new LifeGame(dim);
		game.mMatrix.readFromFile(fn);
		return game;
	} 
	
	public static LifeGame createFromStream(InputStream is, int dim) throws FileNotFoundException {
		LifeGame game = new LifeGame(dim);
		game.mMatrix.readFromXml(is);
		return game;
	}
	
	
}
