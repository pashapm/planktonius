package ru.jecklandin.life;


import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ScrProps {

	public static int screenHeight;
	public static int screenWidth;

	public static boolean isHD;
	
	public static DisplayMetrics mMetrics = new DisplayMetrics();
	
	public static void initialize(Context ctx) {
		Display disp = ((WindowManager) ctx.getSystemService(
				android.content.Context.WINDOW_SERVICE)).getDefaultDisplay();
		ScrProps.screenHeight = disp.getHeight();
		ScrProps.screenWidth = disp.getWidth();
		((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(mMetrics);
		isHD = mMetrics.density != 1;
	}
	 
	public static int scale(int p) {
		return (int) (p*mMetrics.density);
	}
}
