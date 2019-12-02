package com.example.android.bakingmadeeasy.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingmadeeasy.Networks.Ingredient;
import com.example.android.bakingmadeeasy.Networks.Recipe;
import com.example.android.bakingmadeeasy.Networks.RecipeClient;
import com.example.android.bakingmadeeasy.Networks.RecipeInterface;
import com.example.android.bakingmadeeasy.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.example.android.bakingmadeeasy.widget.ListViewWidgetService.PREFS_KEY;
import static com.example.android.bakingmadeeasy.widget.ListViewWidgetService.PREFS_RECIPE_KEY;

/**
 * Created by Rebecca on 6/19/2017.
 */

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Ingredient> ingredientList;
    private Ingredient ingredient;
    private int mAppWidgetId;
    private JSONArray jsonIngredientList;
    private Recipe recipe;

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        Log.d("WIDGET", "RemoteViewsFactory Constructor");
        mContext = context;
    }

    public WidgetRemoteViewsFactory(Context context) {
        this.mContext = context;
    }

    public void onCreate() {
        }


    @Override
    public void onDataSetChanged() {
        List ingredientList;
        SharedPreferences prefs = getDefaultSharedPreferences(mContext);
        if (prefs.contains(PREFS_KEY)) {
            String jsonIngredientList = prefs.getString(PREFS_KEY, null);
            RecipeInterface recipeInterface = RecipeClient.getClient().create(RecipeInterface.class);
            Call<List<Recipe>> call = recipeInterface.getRecipe();
            call.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    List<Recipe> recipeList = response.body();
                }
                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                }
            });
            Log.d("WIDGET", "datasetchanged");
        }
    }

    @Override
    public int getCount() {
        SharedPreferences prefs = getDefaultSharedPreferences(mContext);
        String jsonIngredientList = prefs.getString(PREFS_KEY, null);
        Gson gson = new Gson();
        Ingredient[] ingredients = gson.fromJson(jsonIngredientList, Ingredient[].class);
        ingredientList = Arrays.asList(ingredients);
        ingredientList = new ArrayList(ingredientList);
        Log.d("WIDGET", "getCount = " + ingredientList.size());
        return ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d("WIDGET", "getViewAt = " + position);

        RemoteViews titleView = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_ingredients);
        titleView.addView(R.id.widget_recipe_title, titleView);

        RemoteViews singleViews = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_ingredient_item);
        SharedPreferences prefsIngList = getDefaultSharedPreferences(mContext);
        if (prefsIngList.contains(PREFS_KEY)) {
            try {
                jsonIngredientList = new JSONArray(prefsIngList.getString(PREFS_KEY, null));
                singleViews.setTextViewText(R.id.widget_ingredient_label,
                        jsonIngredientList.getJSONObject(position).getString("ingredient"));
                singleViews.setTextViewText(R.id.widget_measure,
                        jsonIngredientList.getJSONObject(position).getString("measure"));
                singleViews.setTextViewText(R.id.widget_quantity,
                        String.format(Locale.getDefault(),
                                "%.1f", jsonIngredientList.getJSONObject(position).getDouble("quantity")));

                SharedPreferences prefsRecipeObject = getDefaultSharedPreferences(mContext);
                if (prefsRecipeObject.contains(PREFS_RECIPE_KEY)) {
                    String jsonRecipe = prefsRecipeObject.getString(PREFS_RECIPE_KEY, null);
                    Gson gson = new Gson();
                    recipe = gson.fromJson(jsonRecipe, Recipe.class);

                    Intent fillInIntent = new Intent();
                    Bundle data = new Bundle();
                    data.putInt(WidgetProvider.EXTRA_ITEM, position);
                    data.putParcelable("RECIPE", recipe);
                    fillInIntent.putExtras(data);
                    singleViews.setOnClickFillInIntent(R.id.widget_ingredient_item_detail, fillInIntent);
                    Log.d("WIDGET", "FillInIntent Called");
                }

            } catch (JSONException j) {
                j.printStackTrace();
            }
            return singleViews;
        }
        return titleView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDestroy() {
    }
}