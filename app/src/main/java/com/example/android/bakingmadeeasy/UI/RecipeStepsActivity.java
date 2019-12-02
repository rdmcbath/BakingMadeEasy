package com.example.android.bakingmadeeasy.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import com.example.android.bakingmadeeasy.Adapters.StepAdapter;
import com.example.android.bakingmadeeasy.Fragments.IngredientDetailFragment;
import com.example.android.bakingmadeeasy.Fragments.RecipeStepDetailFragment;
import com.example.android.bakingmadeeasy.Fragments.RecipeStepsFragment;
import com.example.android.bakingmadeeasy.Fragments.StepIntroFragment;
import com.example.android.bakingmadeeasy.Networks.Ingredient;
import com.example.android.bakingmadeeasy.Networks.Recipe;
import com.example.android.bakingmadeeasy.Networks.RecipeStep;
import com.example.android.bakingmadeeasy.R;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rebecca on 5/28/2017.
 */

public class RecipeStepsActivity extends AppCompatActivity {

    private static final String LOG_TAG = RecipeStepsActivity.class.getName();
    @BindView(R.id.empty_view)
    @Nullable
    TextView mEmptyView;
    public StepAdapter mStepAdapter;
    private List<RecipeStep> recipeStepList;
    private List<Ingredient> ingredientList;
    public boolean mDualPane;
    private String STATE_KEY = "list_state";
    private Parcelable mListState;
    private LinearLayoutManager mLayoutManager;
    private Recipe recipe;
    private Ingredient ingredients;
    private int position;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_steps);
        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            recipe = data.getParcelable("RECIPE");
            recipeStepList = this.recipe.getSteps();

            RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
            recipeStepsFragment.setRecipeStepList(recipeStepList);
            recipeStepsFragment.setRecipe(recipe);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_steps_container, recipeStepsFragment)
                    .commit();

            mDualPane = getResources().getBoolean(R.bool.is_tablet);
            if (mDualPane) {

                StepIntroFragment stepIntroFragment = new StepIntroFragment();
                stepIntroFragment.setRecipeStepList(recipeStepList);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.content, stepIntroFragment)
                        .commit();
            }
        }

        getSupportActionBar().setTitle(recipe.getName() + " Steps & Ingredients");
    }

    public void handleClickIngredientDetail() {
        mDualPane = getResources().getBoolean(R.bool.is_tablet);
        if (mDualPane) {
            Bundle data = getIntent().getExtras();
            recipe = data.getParcelable("RECIPE");

            if (data != null) {

                Bundle args = new Bundle();
                args.putParcelable("RECIPE", recipe);

                ingredientList = this.recipe.getIngredients();
                IngredientDetailFragment ingredientDetailFragment = new IngredientDetailFragment();
                ingredientDetailFragment.setArguments(args);
                ingredientDetailFragment.setIngredientList(ingredientList);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, ingredientDetailFragment)
                        .commit();

                getSupportActionBar().setTitle(recipe.getName() + " Ingredients");
            }

        } else {
            Bundle data = getIntent().getExtras();
            recipe = data.getParcelable("RECIPE");
            Intent ingredientDetailIntent = new Intent(getBaseContext(), IngredientDetailActivity.class);
            ingredientDetailIntent.putExtras(data);
            startActivity(ingredientDetailIntent);
        }
    }

    public void handleClickStepDetail(int position) {
        mDualPane = getResources().getBoolean(R.bool.is_tablet);
        if (mDualPane) {
            Bundle data = getIntent().getExtras();
            recipe = data.getParcelable("RECIPE");

            if (data != null) {
                recipeStepList = this.recipe.getSteps();
                RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
                recipeStepDetailFragment.setPosition(getIntent().getIntExtra("position", position - 2));
                recipeStepDetailFragment.setRecipeStepList(recipeStepList);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, recipeStepDetailFragment)
                        .commit();

                getSupportActionBar().setTitle(recipe.getName() + " Step Details");
            }
        } else {
            Bundle data = getIntent().getExtras();
            recipe = data.getParcelable("RECIPE");
            data.putInt("position", position - 2);
            Intent recipeStepDetailIntent = new Intent(getBaseContext(), RecipeStepDetailActivity.class);
            recipeStepDetailIntent.putExtras(data);
            startActivity(recipeStepDetailIntent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        mLayoutManager = new LinearLayoutManager(this);
        Log.i(LOG_TAG, "RECIPESTEPSACTIVITY :onSaveInstanceState");
        super.onSaveInstanceState(state);
        mListState = mLayoutManager.onSaveInstanceState();
        state.putParcelable(STATE_KEY, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        mLayoutManager = new LinearLayoutManager(this);
        Log.i(LOG_TAG, "RECIPESTEPSACTIVITY :onRestoreInstanceState");
        super.onRestoreInstanceState(state);
        if (state != null) {
            mListState = state.getParcelable(STATE_KEY);
        }
    }

    @Override
    protected void onResume() {
        mLayoutManager = new LinearLayoutManager(this);
        Log.i(LOG_TAG, "RECIPESTEPSACTIVITY :onResumeInstanceState");
        super.onResume();
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }
}





