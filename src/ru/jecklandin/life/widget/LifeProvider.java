package ru.jecklandin.life.widget;

import java.io.File;
import java.io.FileNotFoundException;

import ru.jecklandin.life.GameField;
import ru.jecklandin.life.LifeApp;
import ru.jecklandin.life.LifeDrawer;
import ru.jecklandin.life.LifeGame;
import ru.jecklandin.life.MainActivity;
import ru.jecklandin.life.R;
import ru.jecklandin.life.ScrProps;
import ru.jecklandin.life.State;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.widget.RemoteViews;

public class LifeProvider extends AppWidgetProvider {

	public static boolean paused = false;
	
	
	
//	@Override
//	public void onReceive(Context context, Intent intent) {
//		if (intent.getAction().equals("ru.jecklandin.life.NEXT")) {
//			Log.d("", "UPDATING");
//			if (next()) {
//				intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
//				intent.setClass(context, LifeProvider.class);
//				context.sendBroadcast(intent);
//			}
//		}
//		
//		super.onReceive(context, intent);
//	}
	
	private boolean next() {
		try {
			LifeGame game = LifeGame.createFromFile(LifeApp.mMatrixFile, 15);
			game.next(true);
			game.save();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		ScrProps.initialize(context);

		if (paused) {
			return;
		}
		
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			Log.d("!!!!!!!" , "NEXT");
			boolean next_ok = next();
			
			// Create an Intent to launch ExampleActivity
			Intent intent = new Intent(context, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, 0);

			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wid);
			views.setOnClickPendingIntent(R.id.wlay, pendingIntent);
			
			Bitmap emp = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
			emp.setPixel(0, 0, Color.TRANSPARENT);
			
			State state = makeScreenshotFromFile(context);
			if (state != null && state.bitmap != null) {
				if (state.isVoid) {
					views.setImageViewBitmap(R.id.ImageView01, emp);
					views.setTextViewText(R.id.cache, "Lo-o-oser..\n\nPress to\nrestart");
				} else {
					views.setImageViewBitmap(R.id.ImageView01, state.bitmap);
					views.setTextViewText(R.id.cache, state.cache + "$");
				}
			} else {
				views.setImageViewBitmap(R.id.ImageView01, emp);
				views.setTextViewText(R.id.cache, "Press to\nstart");
			}
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}  
	}
	
	public static State makeScreenshotFromFile(Context ctx) {
		Bitmap bm = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bm);

		File f = new File(LifeApp.mMatrixFile); 
		if (!f.exists()) {
			return null;
		}
		
		try {
			LifeGame game = LifeGame.createFromFile(LifeApp.mMatrixFile, 15);
			LifeDrawer drawer = new LifeDrawer(ctx);
			drawer.setGame(game);
			drawer.drawScreenshot(c);
			State state = new State();
			state.bitmap = bm;
			state.cache = game.getCache();
			state.isVoid = game.isFired();
			state.lastTs = game.mMatrix.lastTimestamp;
			return state;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}