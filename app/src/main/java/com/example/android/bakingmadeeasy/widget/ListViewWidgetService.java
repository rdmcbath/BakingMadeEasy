package com.example.android.bakingmadeeasy.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Rebecca on 6/19/2017.
 */

public class ListViewWidgetService extends RemoteViewsService {

    public static final String PREFS_KEY = "ingredients";
    public static final String PREFS_RECIPE_KEY = "recipe_object";
    public static final String PREFS_RECIPE_TITLE_KEY = "recipe_title";

    public ListViewWidgetService(){
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
