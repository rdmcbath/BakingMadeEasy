package com.example.android.bakingmadeeasy.UI;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import com.example.android.bakingmadeeasy.Adapters.StepAdapter;
import com.example.android.bakingmadeeasy.Fragments.RecipeStepDetailFragment;
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
 * Created by Rebecca on 5/21/2017.
 */

public class RecipeStepDetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = RecipeStepDetailActivity.class.getName();
    private List<RecipeStep> recipeStepList;
    private List<Ingredient> ingredientList;
    private Ingredient ingredients;
    private LinearLayoutManager mLayoutManager;
    private StepAdapter mStepAdapter;
    private Parcelable mListState;
    private String STATE_KEY = "list_state";
    private Recipe recipe;
    private int position;
    private boolean mDualPane;
    private final Context mContext = null;
    @Nullable
    @BindView(R.id.content)
    View contentContainerTabletView;
    @BindView(R.id.step_detail_container)
    View stepDetailContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        recipe = data.getParcelable("RECIPE");
        data.getInt("position", position);

        if (savedInstanceState == null) {

            recipeStepList = this.recipe.getSteps();
            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setPosition(getIntent().getIntExtra("position", position));
            recipeStepDetailFragment.setRecipeStepList(recipeStepList);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, recipeStepDetailFragment)
                    .commit();
        }

        getSupportActionBar().setTitle(recipe.getName() + " Step Details");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(LOG_TAG, "RECIPESTEPDETAILACTIVITY: onSaveInstanceState");
        super.onSaveInstanceState(outState);
        mLayoutManager = new LinearLayoutManager(this);
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(STATE_KEY, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(STATE_KEY);
        }
    }

    @Override
    protected void onResume() {
        mLayoutManager = new LinearLayoutManager(this);
        Log.i(LOG_TAG, "RECIPESTEPDETAILACTIVITY :onResumeInstanceState");
        super.onResume();
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }
}



















