package ru.jecklandin.life.widget;

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
import android.widget.RemoteViews;

public class LifeProvider extends AppWidgetProvider {

	public boolean load = false;
	
	private void next() {
		try {
			LifeGame game = LifeGame.createFromFile(LifeApp.mMatrixFile, 15);
			game.next(true);
			game.save();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		ScrProps.initialize(context);

//		if (!load) {
//			return;
//		}
		
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			// Create an Intent to launch ExampleActivity
			Intent intent = new Intent(context, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, 0);

			State state = makeScreenshotFromFile(context);
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wid);
			views.setOnClickPendingIntent(R.id.ImageView01, pendingIntent);

			if (state != null && state.bitmap != null) {
				views.setImageViewBitmap(R.id.ImageView01, state.bitmap);
				views.setTextViewText(R.id.cache, state.cache + "$");
				appWidgetManager.updateAppWidget(appWidgetId, views);
			}
		}
	}

	public static State makeScreenshotFromFile(Context ctx) {
		Bitmap bm = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bm);

		try {
			LifeGame game = LifeGame.createFromFile(LifeApp.mMatrixFile, 15);
			LifeDrawer drawer = new LifeDrawer(ctx);
			drawer.setGame(game);
			drawer.drawScreenshot(c);
			State state = new State();
			state.bitmap = bm;
			state.cache = game.getCache();
			return state;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}