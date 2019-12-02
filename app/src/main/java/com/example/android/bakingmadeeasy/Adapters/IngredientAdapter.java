package com.example.android.bakingmadeeasy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingmadeeasy.Networks.Ingredient;
import com.example.android.bakingmadeeasy.Networks.Recipe;
import com.example.android.bakingmadeeasy.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rebecca on 5/24/2017.  IngredientAdapter for the IngredientDetailActivity UI screen, which
 * will show the detailed list of ingredients for a given recipe.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private static final String LOG_TAG = IngredientAdapter.class.getName();
    private List<Ingredient> ingredientList;
    public Ingredient ingredients;
    public Context context;
    private Recipe recipe;
    private boolean mDualPane;

    public IngredientAdapter(Recipe recipe, List<Ingredient> ingredientList, Ingredient ingredients,
                             Context context, boolean mDualPane) {
        this.recipe = recipe;
        this.ingredientList = ingredientList;
        this.ingredients = ingredients;
        this.context = context;
        this.mDualPane = mDualPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_detail_single, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IngredientAdapter.ViewHolder holder, int position) {

        Ingredient ingredient = ingredientList.get(position);
        holder.ingredientName.setText(ingredient.getIngredient());
        holder.ingredientQuantity.setText(""+ingredient.getQuantity());
        holder.ingredientMeasure.setText(ingredient.getMeasure());

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_name) TextView ingredientName;
        @BindView(R.id.ingredient_quantity) TextView ingredientQuantity;
        @BindView(R.id.ingredient_measure) TextView ingredientMeasure;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }
}