package com.example.android.bakingmadeeasy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingmadeeasy.Networks.Recipe;
import com.example.android.bakingmadeeasy.R;
import com.example.android.bakingmadeeasy.UI.RecipeStepsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

/**
 * Created by Rebecca on 5/24/2017. RecipeAdapter for the MainActivity UI screen, which will
 * display the cardViews of each recipe image & title.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private static final String LOG_TAG = RecipeAdapter.class.getSimpleName();
    public static final String KEY_STEP_LIST = "com.example.android.bakingmadeeasy.RecipeSteps";
    public List<Recipe> recipeStepList = new ArrayList<>();
    private Context context;
    final private View mEmptyView;


    public RecipeAdapter(List<Recipe> recipeStepList, Context context, View mEmptyView) {
        this.recipeStepList = recipeStepList;
        this.context = context;
        this.mEmptyView = mEmptyView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_single_recipe_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {

        setRecipeImagesWithSwitch(holder, position);
        holder.recipeTitle.setText(recipeStepList.get(position).getName());
//        String path = recipeStepList.get(position).getImage();
//        if (TextUtils.isEmpty(""))
//
//            Picasso.with(context).load(R.drawable.cheesecake)
//                    .into(holder.thumbnail);
//        else {
//            Picasso.with(context).load(path).into(holder.thumbnail);
//        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_card_title)
        TextView recipeTitle;
        @BindView(R.id.thumbnail)
        ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Optional
        public void onClick(View v) {
            Log.i(LOG_TAG, "onClick in Recipe Adapter Called");
            // Start the RecipeSteps activity
            Recipe recipe = recipeStepList.get(getAdapterPosition());
            Bundle data = new Bundle();
            data.putParcelable("RECIPE", recipe);

            Intent recipeStepsIntent = new Intent(context, RecipeStepsActivity.class);
            recipeStepsIntent.putExtras(data);
            context.startActivity(recipeStepsIntent);
        }
    }

    // Helper method to allocate images
    private void setRecipeImagesWithSwitch(RecipeAdapter.ViewHolder holder, int position) {
        String path = recipeStepList.get(position).getImage();
        if (path.equals("")) {
            switch (position) {
                case 0:
                    Picasso.get()
                            .load(R.drawable.nuttella_pie)
                            .resize(850,650)
                            .centerInside()
                            .into(holder.thumbnail);
                    break;
                case 1:
                    Picasso.get()
                            .load(R.drawable.brownies)
                            .resize(850,650)
                            .centerInside()
                            .into(holder.thumbnail);
                    break;
                case 2:
                    Picasso.get()
                            .load(R.drawable.yellow_cake)
                            .resize(850,650)
                            .centerInside()
                            .into(holder.thumbnail);
                    break;
                case 3:
                    Picasso.get()
                            .load(R.drawable.classic_cheesecake)
                            .resize(850,650)
                            .centerInside()
                            .into(holder.thumbnail);
                default:
                    break;
            }
        }else{
            Picasso.get().load(path).into(holder.thumbnail);
        }
    }

    @Override
        public int getItemCount() {return recipeStepList.size(); }

    public interface RecyclerClickListener {

        @Optional
        void onRecyclerClick(Recipe recipe, List<Recipe> recipeList);
    }

    public void setEmptyView() {
        if (recipeStepList == null) {
            mEmptyView.setVisibility(View.VISIBLE);
        }else {
            mEmptyView.setVisibility(View.GONE);
        }
    }
}






