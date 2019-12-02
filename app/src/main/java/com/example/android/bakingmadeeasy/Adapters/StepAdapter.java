package com.example.android.bakingmadeeasy.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingmadeeasy.Networks.Ingredient;
import com.example.android.bakingmadeeasy.Networks.Recipe;
import com.example.android.bakingmadeeasy.Networks.RecipeStep;
import com.example.android.bakingmadeeasy.R;
import com.example.android.bakingmadeeasy.UI.RecipeStepsActivity;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rebecca on 5/24/2017. StepAdapter for the RecipeStepActivity UI screen.  Lists recipe
 * steps, with a card Ingredients label, recipe steps label, and a recyclerview list of short
 * description steps.  OnClick handling for recipeStepList and ingredientList.
 */
public class StepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String LOG_TAG = StepAdapter.class.getSimpleName();
    @BindView(R.id.recipe_short_desc)
    TextView recipeShortDescription;
    private final int VIEW_TYPE_INGREDIENTS_LABEL = 0;
    private final int VIEW_TYPE_RECIPE_STEPS_LABEL = 1;
    private final int VIEW_TYPE_RECIPE_STEP = 2;
    public boolean mDualPane;
    public List<RecipeStep> recipeStepList;
    public List<Ingredient> ingredientList;
    public Context mContext;
    public View mEmptyView;
    public Recipe recipe;
    public Ingredient ingredients;

    public StepAdapter(Recipe recipe, List<RecipeStep> recipeStepList, Ingredient ingredients,
                       List<Ingredient> ingredientList, Context context, boolean mDualPane) {
        this.recipe = recipe;
        this.recipeStepList = recipeStepList;
        this.ingredientList = ingredientList;
        this.ingredients = ingredients;
        this.mContext = context;
        this.mDualPane = mDualPane;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_INGREDIENTS_LABEL;
        }
        if (position == 1) {
            return VIEW_TYPE_RECIPE_STEPS_LABEL;
        } else {
            return VIEW_TYPE_RECIPE_STEP;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_INGREDIENTS_LABEL) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_steps_ingredients_label, parent, false);
            return new ViewHolderIngredientsLabel(itemView);
        }
        if (viewType == VIEW_TYPE_RECIPE_STEPS_LABEL) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_steps_label, parent, false);
            return new ViewHolderRecipeStepsLabel(itemView);

        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_steps_short_desc, parent, false);
            return new ViewHolderReceipeStepsList(itemView);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_RECIPE_STEP:
                ViewHolderReceipeStepsList viewHolderReceipeStepsList = (ViewHolderReceipeStepsList) holder;
                viewHolderReceipeStepsList.bindViews(position - 2);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 2 + recipeStepList.size();
    }

    public void setEmptyView() {
        if (recipeStepList == null) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    public class ViewHolderRecipeStepsLabel extends RecyclerView.ViewHolder {

        public ViewHolderRecipeStepsLabel(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ViewHolderIngredientsLabel extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ingredients_label)
        TextView ingredientsLabel;

        public void bindViews() {
            ingredientsLabel.setText("Total Ingredients = " + ingredientList.size());
        }

        public ViewHolderIngredientsLabel(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.i(LOG_TAG, "onClick for IngredientDetail Called");

            if (mContext instanceof RecipeStepsActivity) {
                RecipeStepsActivity recipeStepsActivity = ((RecipeStepsActivity) mContext);
                ingredientList = recipe.getIngredients();
                Ingredient ingredients = ingredientList.get(getAdapterPosition());
                Bundle data = new Bundle();
                data.putParcelable("RECIPE", recipe);
                recipeStepsActivity.handleClickIngredientDetail();

                Fragment fragmentGet = new Fragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("RECIPE", recipe);
                fragmentGet.setArguments(bundle);
            }
        }
    }

        public class ViewHolderReceipeStepsList extends RecyclerView.ViewHolder implements View.OnClickListener {

            @BindView(R.id.recipe_short_desc)
            TextView recipeShortDescription;

            public ViewHolderReceipeStepsList(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Log.i(LOG_TAG, "onClick for recipeStep Called");

                if (mContext instanceof RecipeStepsActivity) {
                    RecipeStepsActivity recipeStepsActivity = ((RecipeStepsActivity) mContext);
                    recipeStepList = recipe.getSteps();
                    RecipeStep recipeStep = recipeStepList.get(getAdapterPosition());
                    Bundle data = new Bundle();
                    data.putInt("position", getAdapterPosition());
                    recipeStepsActivity.handleClickStepDetail(getAdapterPosition());
                }
            }

            public void bindViews(int position) {
                recipeShortDescription.setText(recipeStepList.get(position).getShortDescription());
            }
        }
    }



