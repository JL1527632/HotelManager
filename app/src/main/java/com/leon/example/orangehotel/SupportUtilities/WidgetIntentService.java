package com.leon.example.orangehotel.SupportUtilities;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class WidgetIntentService extends IntentService {

    private final String TAG = "WgtISvc";

    public static final String ACTION_UPDATE_LIST = "com.leon.example.orange.update.list";

    public WidgetIntentService() {
        super("WidgetIntentService");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            switch (intent.getAction()) {
                case ACTION_UPDATE_LIST:
                    handleUpdateWidgets();
            }
        }

    }


    public static void startUpdateWidgetInformation(Context context) {
        Intent intent = new Intent(context, WidgetIntentService.class);
        intent.setAction(ACTION_UPDATE_LIST);
        context.startService(intent);
    }

    String[] widgetSummary = new String[]{"",""};

    public void handleUpdateWidgets() {

        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(this, WidgetProvider.class));

        //Update ALL widgets
        WidgetProvider.updateDailySummaryWidget(this, manager, widgetSummary, appWidgetIds);
    }

}
