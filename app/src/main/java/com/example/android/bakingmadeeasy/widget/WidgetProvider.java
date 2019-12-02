package com.example.android.bakingmadeeasy.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.bakingmadeeasy.Networks.Ingredient;
import com.example.android.bakingmadeeasy.R;
import com.example.android.bakingmadeeasy.UI.IngredientDetailActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.android.bakingmadeeasy.widget.ListViewWidgetService.PREFS_KEY;
import static com.example.android.bakingmadeeasy.widget.ListViewWidgetService.PREFS_RECIPE_TITLE_KEY;

/**
 * Created by Rebecca on 6/19/2017.
 */

public class WidgetProvider extends AppWidgetProvider {

    public WidgetProvider() {
    }

    public WidgetProvider(Context context) {
        mContext = context;
    }

    private Context mContext;
    private List<Ingredient> ingredientList;
    private String ingredient;
    public static final String EXTRA_ITEM = "1";
    private String recipeTitle;

    public void onWidgetUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Log.d("WIDGET", "onWidgetUpdate called");
        // retrieve sharedPref for ingredientList
        SharedPreferences prefsIngList = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefsIngList.contains(PREFS_KEY)) {
            String jsonIngredientList = prefsIngList.getString(PREFS_KEY, null);
            Gson gson = new Gson();
            Ingredient[] ingredients = gson.fromJson(jsonIngredientList, Ingredient[].class);
            ingredientList = Arrays.asList(ingredients);
            ingredientList = new ArrayList(ingredientList);
            if (jsonIngredientList == null) {
                Log.d("WIDGET", "No Ingredient List Found - SharedPrefs");
            }
            // retrieve sharedPref for Recipe title
            SharedPreferences prefsRecipeTitle = PreferenceManager.getDefaultSharedPreferences(context);
            String recipeTitle = prefsRecipeTitle.getString(PREFS_RECIPE_TITLE_KEY, "Recipe Name");

            AppWidgetManager.getInstance(context);
            Intent widgetIntent = new Intent(context, ListViewWidgetService.class);
            widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            widgetIntent.setData(Uri.parse(widgetIntent.toUri(Intent.URI_INTENT_SCHEME)));
            // set remoteAdapter on listView
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);
            rv.setRemoteAdapter(R.id.widget_ingredient_list_view, widgetIntent);
            //set remoteAdapter on textView for recipe title
            rv.setTextViewText(R.id.widget_recipe_title, recipeTitle);
            rv.setRemoteAdapter(R.id.widget_recipe_title, widgetIntent);
            // Update the remoteView at this end
            appWidgetManager.updateAppWidget(appWidgetId, rv);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_ingredient_list_view);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_recipe_title);
            Log.d("WIDGET", "updated WIDGET ListView and TitleView in onWidgetUpdate");

            //add the ability to click on the ListView items and open a new activity in main app
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);
            Intent startActivityIntent = new Intent(context, IngredientDetailActivity.class);
            PendingIntent startActivityPendingIntent = PendingIntent.getActivity
                    (context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setPendingIntentTemplate(R.id.widget_ingredient_list_view, startActivityPendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_ingredient_list_view);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_recipe_title);
        }
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            onWidgetUpdate(context, appWidgetManager, appWidgetId);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredient_list_view);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipe_title);
        }
    }

    public void onDeleted(Context context, int[] appWidgetIds) {
    }

    public void onEnabled(Context context) {
    }

    public void onDisabled(Context context) {
    }
}

