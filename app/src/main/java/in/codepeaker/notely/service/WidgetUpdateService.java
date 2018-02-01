package in.codepeaker.notely.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import in.codepeaker.notely.NotesWidgetProvider;
import in.codepeaker.notely.data.NotesData;
import in.codepeaker.notely.utils.Constant;

/**
 * Created by github.com/codepeaker on 1/2/18.
 */

public class WidgetUpdateService extends IntentService {
    public static final String UpdateWidget = "updateWidget";


    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }

    public static void startActionNotesWidget(Context context,
                                              NotesData notesData, int notesPosition) {

        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.putExtra(Constant.notesData, notesData);
        intent.putExtra(Constant.notesPosition, notesPosition);

        context.startService(intent);


    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, NotesWidgetProvider.class));

        Bundle data = intent.getExtras();

        NotesData notesData = (NotesData) data.get(Constant.notesData);
        int notesPosition = data.getInt(Constant.notesPosition);

        NotesWidgetProvider.updateAppWidgets(this, appWidgetManager,
                appWidgetIds, notesData, notesPosition);


    }


}
