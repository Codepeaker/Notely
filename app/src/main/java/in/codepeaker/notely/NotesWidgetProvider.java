package in.codepeaker.notely;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.view.View;
import android.widget.RemoteViews;

import in.codepeaker.notely.data.NotesData;

/**
 * Implementation of App Widget functionality.
 */
public class NotesWidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, NotesData notesData, int notesPosition) {

        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), R.layout.notely_widget_view);

        if (notesData != null) {
            remoteViews.setViewVisibility(R.id.notes_widget_title_id, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.notes_widget_desc_id, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.empty_view, View.GONE);
        } else {
            remoteViews.setViewVisibility(R.id.notes_widget_title_id, View.GONE);
            remoteViews.setViewVisibility(R.id.notes_widget_desc_id, View.GONE);
            remoteViews.setViewVisibility(R.id.empty_view, View.VISIBLE);
            return;
        }

        remoteViews.setEmptyView(R.id.widget_layout_id, R.id.empty_view);
        remoteViews.setTextViewText(R.id.notes_widget_desc_id, notesData.getNotesDesc());
        remoteViews.setTextViewText(R.id.notes_widget_title_id, notesPosition + 1 + ". " + notesData.getNotesTitle());
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                        int[] appWidgetIds, NotesData notesData, int notesPosition) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, notesData, notesPosition);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, null, 0);
        }
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

