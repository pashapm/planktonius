package ru.jecklandin.life.widget;

import java.util.Formatter.BigDecimalLayoutForm;

import ru.jecklandin.life.GameField;
import ru.jecklandin.life.LifeApp;
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
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.RemoteViews;

public class LifeProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        ScrProps.initialize(context);
        
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            
            if (GameField.sInstance != null) {
//            	Bitmap bm2 = GameField.sInstance.getScreenShot();
            	
            	State state = GameField.makeScreenshotFromFile(context, LifeApp.mMatrixFile);
            	 
            	
                // Get the layout for the App Widget and attach an on-click listener to the button
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wid);
                views.setOnClickPendingIntent(R.id.ImageView01, pendingIntent);
                
                if (state != null && state.bitmap != null) {
                	views.setImageViewBitmap(R.id.ImageView01, state.bitmap);
                	views.setTextViewText(R.id.cache, state.cache+"$");
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }
            }
        }
    }
}