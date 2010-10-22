package ru.jecklandin.life;

import java.io.FileNotFoundException;

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
	
	public void next() {
		mMatrix.learn();
		mMatrix.change();
		mMatrix.learnAfter();
	}
	
	public void save(String fn) {
		mMatrix.writeXmlToFile(fn);
	}
	
	public static LifeGame createFromFile(String fn, int dim) throws FileNotFoundException {
		LifeGame game = new LifeGame(dim);
		game.mMatrix.readFromFile(fn);
		return game;
	}
	
	
}
