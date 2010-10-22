package ru.jecklandin.life;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.view.View;

class Updater extends Thread {
	
	public boolean isPaused = false;
	
	private List<View> mViews = new ArrayList<View>();
	
	public Updater(View... v) {
		mViews = Arrays.asList(v);
	}
	
	@Override
	public void run() {
		while (!isInterrupted()) {
			
			try {
				sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			
			if (isPaused) {
				return;
			}
			
			for (View v : mViews) {
				v.postInvalidate();
			}
			

		}  
	}
}
