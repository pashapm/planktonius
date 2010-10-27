package ru.jecklandin.life;

import java.io.File;

import android.app.Application;

public class LifeApp extends Application {
	
	public static String mMatrixFile;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		File root = getDir("saved", MODE_PRIVATE);
        mMatrixFile  = root.getAbsolutePath()+"/matrix.xml";
        
        //
        mMatrixFile  = "/sdcard/matrix.xml";
	}

}
