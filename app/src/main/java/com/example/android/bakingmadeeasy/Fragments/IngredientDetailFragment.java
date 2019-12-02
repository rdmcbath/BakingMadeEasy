package com.example.android.bakingmadeeasy.Fragments;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.example.android.bakingmadeeasy.Adapters.IngredientAdapter;
import com.example.android.bakingmadeeasy.Networks.Ingredient;
import com.example.android.bakingmadeeasy.Networks.Recipe;
import com.example.android.bakingmadeeasy.R;
import com.example.android.bakingmadeeasy.widget.WidgetProvider;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.example.android.bakingmadeeasy.widget.ListViewWidgetService.PREFS_KEY;
import static com.example.android.bakingmadeeasy.widget.ListViewWidgetService.PREFS_RECIPE_KEY;
import static com.example.android.bakingmadeeasy.widget.ListViewWidgetService.PREFS_RECIPE_TITLE_KEY;

/**
 * Created by Rebecca on 6/20/2017.
 */

public class IngredientDetailFragment extends Fragment {

    @BindView(R.id.ingredient_detail_recyclerview)
    RecyclerView ingredientDetailRecyclerView;
    @BindView(R.id.add_to_widget_button)
    Button button;
    private List<Ingredient> ingredientList = new ArrayList<>();
    private IngredientAdapter mIngredientAdapter;
    public static final String INGREDIENT_LIST_IN = "ingredient_list";
    private Recipe recipe = new Recipe();
    private Ingredient ingredients;
    Context context = getActivity();
    private boolean mDualPane;

    public IngredientDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle data = this.getArguments();
        if (data != null) {
            recipe = data.getParcelable("RECIPE");
        }

        if (savedInstanceState != null) {
            ingredientList = savedInstanceState.getParcelableArrayList(INGREDIENT_LIST_IN);
        }

        View rootView = inflater.inflate(R.layout.fragment_ingredient_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (ingredientList != null) {
            ingredientDetailRecyclerView.setLayoutManager
                    (new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mIngredientAdapter = new IngredientAdapter(recipe, ingredientList, ingredients, context, mDualPane);
            ingredientDetailRecyclerView.setAdapter(mIngredientAdapter);
        } else {
            Toast.makeText(getContext(), "Nothing to see here \uD83D\uDE15", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(INGREDIENT_LIST_IN, (ArrayList<? extends Parcelable>) ingredientList);
    }

    public static String toJson(Object jsonObject) {
        return new Gson().toJson(jsonObject);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.add_to_widget_button)
    void addToWidget(View v) {
        //save the selected ingredient list & recipe title to shared preferences for home widget
        SharedPreferences prefsIngList = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences prefsRecipeObject = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences prefsRecipeTitle = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor;
        editor = prefsIngList.edit();
        editor = prefsRecipeObject.edit();
        editor = prefsRecipeTitle.edit();
        Gson gson = new Gson();
        String jsonIngredientList = gson.toJson(ingredientList);
        String recipeObject = gson.toJson(recipe);
        String recipeTitle = recipe.getName();
        editor.putString(PREFS_KEY, jsonIngredientList);
        editor.putString(PREFS_RECIPE_KEY, recipeObject);
        editor.putString(PREFS_RECIPE_TITLE_KEY, recipeTitle);
        editor.commit();

        Intent widgetUpdater = new Intent(getContext(), WidgetProvider.class);
        widgetUpdater.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(getContext()).getAppWidgetIds(new ComponentName(getContext(), WidgetProvider.class));
        widgetUpdater.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getContext().sendBroadcast(widgetUpdater);

        Toast.makeText(getContext(), R.string.widget_ingredients_added, Toast.LENGTH_SHORT).show();
        Log.d("WIDGET", "button clicked to add ingredients to widget");
    }
}



