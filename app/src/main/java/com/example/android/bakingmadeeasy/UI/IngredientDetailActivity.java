package com.example.android.bakingmadeeasy.UI;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ProgressBar;
import com.example.android.bakingmadeeasy.Adapters.IngredientAdapter;
import com.example.android.bakingmadeeasy.Fragments.IngredientDetailFragment;
import com.example.android.bakingmadeeasy.Networks.Ingredient;
import com.example.android.bakingmadeeasy.Networks.Recipe;
import com.example.android.bakingmadeeasy.R;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rebecca on 5/25/2017.
 */

public class IngredientDetailActivity extends AppCompatActivity {

    @BindView(R.id.loading_spinner)
    ProgressBar spinnerProgress;

    private IngredientAdapter mIngredientAdapter;
    public LinearLayoutManager mLayoutManager;
    private String ING_LIST_KEY = "ingredient_list_state";
    public static final String INGREDIENTS_IMPORT = "ingredient_list";
    private Parcelable mIngListState;
    private List<Ingredient> ingredientList = new ArrayList<>();
    public Boolean mDualPane;
    public Recipe recipe = new Recipe();
    public Ingredient ingredients;
    Context mContext;

    private static final String LOG_TAG = IngredientDetailActivity.class.getName();

    public void onActivityCreated() {
        String recipeTitle = recipe.getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_detail);
        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        recipe = data.getParcelable("RECIPE");

        Bundle args = new Bundle();
        args.putParcelable("RECIPE", recipe);

        ingredientList = this.recipe.getIngredients();
        IngredientDetailFragment ingredientDetailFragment = new IngredientDetailFragment();
        ingredientDetailFragment.setArguments(args);
        ingredientDetailFragment.setIngredientList(ingredientList);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.ingredient_detail_container, ingredientDetailFragment)
                .commit();

        getSupportActionBar().setTitle(recipe.getName() + " Ingredients");
        }

    public void setIngredients(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(LOG_TAG, "INGREDIENTDETAILACTIVITY: onSaveInstanceState");
        super.onSaveInstanceState(outState);
        mLayoutManager = new LinearLayoutManager(this);
        mIngListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelableArrayList(INGREDIENTS_IMPORT, (ArrayList<? extends Parcelable>) ingredientList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "INGREDIENTDETAILACTIVITY: onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
        mLayoutManager = new LinearLayoutManager(this);
        if (savedInstanceState != null) {
            mIngredientAdapter = savedInstanceState.getParcelable(ING_LIST_KEY);
        }
    }

    @Override
    protected void onResume() {
        Log.i(LOG_TAG, "INGREDIENTDETAILACTIVITY: onResume");
        super.onResume();
        mLayoutManager = new LinearLayoutManager(this);
        if (mIngListState != null) {
            mLayoutManager.onRestoreInstanceState(mIngListState);
        }
    }
}
