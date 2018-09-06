package com.leon.example.orangehotel.SupportUtilities;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.leon.example.orangehotel.R;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    private static String TAG = "WidgetProvider";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, String[] roomSummary,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.summary_widget_provider);

        if(roomSummary!=null) {
            views.setTextViewText(R.id.date_view, roomSummary[0]);
            views.setTextViewText(R.id.rooms_view, roomSummary[1]);
        }
        else
        {
            views.setTextViewText(R.id.date_view, DateUtilities.getTodayAsString());
            views.setTextViewText(R.id.rooms_view, "");
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    //Custom function.
    public static void updateDailySummaryWidget(Context context, AppWidgetManager manager, String[] summary, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, manager, summary, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetIntentService.startUpdateWidgetInformation(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

