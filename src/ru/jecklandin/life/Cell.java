package ru.jecklandin.life;

import java.util.concurrent.BrokenBarrierException;

public class Cell {
	public final static int ALIVE=1;
	public final static int DEAD=0;
	
	//addit states
	public final static int OK=0;
	public final static int NEWBORN=1;
	public final static int DYING=2;
	
	
	int x; //координаты клетки
	int y;
	public LifeMatrix matrix; //матрица состояния клеток для данного шага
	
	private int nextStatus; //вычисленный статус клетки для следующего шага
	private int nextAdditStatus;
	
	public int status; 
	public int additStatus; 
	
	public Cell(LifeMatrix matrix, int x, int y, int value) {
		this.matrix = matrix;
		this.x = x;
		this.y = y;
		this.status = value;
	}
	
	public void learn() {
		int neighbors = countNeighbors();
		if ((this.status==DEAD)&&(neighbors==3)) {
			nextStatus = ALIVE;
			nextAdditStatus = NEWBORN;
		}
		
		if (this.status==ALIVE) {
			if (neighbors == 2) {
				nextStatus = ALIVE;
				nextAdditStatus = OK;
			} else if (neighbors == 3) {
				nextStatus = ALIVE;
				nextAdditStatus = OK;
			} else {
				nextStatus = DEAD;
				nextAdditStatus = OK;
			}
		}
	}
	
	public void learnAfter() {
		int neighbors = countNeighbors();
		
		if(this.status==ALIVE) {
			if ((neighbors<2)||(neighbors>3)) {
				additStatus = DYING;
			} 
		}
	}
	
	public void computeNextState() {
		int neighbors = countNeighbors();
		
		if (this.status==ALIVE) {
			if (neighbors == 2 || neighbors == 3) {
				if (additStatus == DYING) {
					additStatus = OK;
				}
			} else {
				additStatus = DYING;
			}
		}
	}
	
	public void change() {
		status = nextStatus;
		additStatus = nextAdditStatus;
	}
	
	private int countNeighbors() {
		int result=0;
		result += matrix.get(x+1, y).status == ALIVE ? 1 : 0;
		result += matrix.get(x+1, y+1).status == ALIVE ? 1 : 0;
		result += matrix.get(x, y+1).status == ALIVE ? 1 : 0;
		result += matrix.get(x-1, y+1).status == ALIVE ? 1 : 0;
		result += matrix.get(x-1, y).status == ALIVE ? 1 : 0;
		result += matrix.get(x-1, y-1).status == ALIVE ? 1 : 0;
		result += matrix.get(x, y-1).status == ALIVE ? 1 : 0;
		result += matrix.get(x+1, y-1).status == ALIVE ? 1 : 0;
		return result;
	}
	
	@Override
	public int hashCode() {
		return x + y*1000;
	}
	
	//TODO
	@Override
	public boolean equals(Object o) {
		Cell co = (Cell) o;
		return co.x == co.y && co.x == co.x;
	}

}