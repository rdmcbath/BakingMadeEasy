package com.example.android.bakingmadeeasy.Fragments;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.bakingmadeeasy.Adapters.StepAdapter;
import com.example.android.bakingmadeeasy.Networks.Ingredient;
import com.example.android.bakingmadeeasy.Networks.Recipe;
import com.example.android.bakingmadeeasy.Networks.RecipeStep;
import com.example.android.bakingmadeeasy.R;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rebecca on 5/24/2017. This fragment is for the list of recipe steps that show up
 * once a recipe is clicked from the MainActivity.
 */

public class RecipeStepsFragment extends Fragment {
    private static final String LOG_TAG = RecipeStepsFragment.class.getSimpleName();

    @BindView(R.id.recipe_steps_list)
    RecyclerView mRecipeStepsRecyclerView;
    @Nullable
    @BindView(R.id.empty_view)
    TextView mEmptyView;
    private List<RecipeStep> recipeStepList;
    private List<Ingredient> ingredientList;
    boolean mDualPane;
    private StepAdapter mStepAdapter;
    private static final String LIST_IMPORT = "step_list";
    public LinearLayoutManager mLayoutManager;
    public Recipe recipe;
    public Ingredient ingredients;

    public RecipeStepsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            recipeStepList = savedInstanceState.getParcelableArrayList(LIST_IMPORT);
        }

        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        ButterKnife.bind(this, rootView);

        if (recipeStepList != null) {
            mLayoutManager = new LinearLayoutManager(getContext());
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecipeStepsRecyclerView.setLayoutManager(mLayoutManager);
            mStepAdapter = new StepAdapter(recipe, recipeStepList, ingredients, ingredientList,
                    getContext(), mDualPane);
            mRecipeStepsRecyclerView.setAdapter(mStepAdapter);
        } else {
            Toast.makeText(getContext(), "Nothing to see here", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setRecipeStepList(List<RecipeStep> recipeStepList) {
        this.recipeStepList = recipeStepList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_IMPORT, (ArrayList<? extends Parcelable>) recipeStepList);
    }

    @Override
    public void onResume() {
        super.onResume();
        mStepAdapter.notifyDataSetChanged();
    }
}

