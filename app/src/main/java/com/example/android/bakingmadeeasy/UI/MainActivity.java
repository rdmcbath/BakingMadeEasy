package com.example.android.bakingmadeeasy.UI;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.bakingmadeeasy.Adapters.RecipeAdapter;
import com.example.android.bakingmadeeasy.Networks.Recipe;
import com.example.android.bakingmadeeasy.Networks.RecipeClient;
import com.example.android.bakingmadeeasy.Networks.RecipeInterface;
import com.example.android.bakingmadeeasy.R;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    @BindView(R.id.main_recipe_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    TextView mEmptyView;
    @BindView(R.id.loading_spinner)
    ProgressBar spinnerProgress;
    public static List <Recipe> recipeArrayList = new ArrayList<>();
    private RecipeAdapter mRecipeAdapter;
    public LinearLayoutManager mLayoutManager;
    private Parcelable mListState;
    private String STATE_KEY = "list_state";
    private boolean mDualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        spinnerProgress.setVisibility(View.VISIBLE);

        mDualPane = getResources().getBoolean(R.bool.is_tablet);

        if (mDualPane) {
            mLayoutManager = new GridLayoutManager(this, 3);
        } else {
            mLayoutManager = new GridLayoutManager(this, 1);
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecipeAdapter = new RecipeAdapter(recipeArrayList, MainActivity.this, mEmptyView);
        mRecyclerView.setAdapter(mRecipeAdapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, load recipe data
        if (networkInfo != null && networkInfo.isConnected()) {

            loadRecipeList();
            spinnerProgress.setVisibility(View.INVISIBLE);

        }else {

            Toast.makeText(this, R.string.no_network, Toast.LENGTH_SHORT).show();
            spinnerProgress.setVisibility(View.GONE);
        }
    }

    private void loadRecipeList() {
        Log.i(LOG_TAG, "loadRecipeList Called");

        RecipeInterface recipeInterface = RecipeClient.getClient().create(RecipeInterface.class);
        Call<List<Recipe>> call = recipeInterface.getRecipe();
        Log.i(LOG_TAG, "RecipeInterface - GetRecipe Called");

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    List<Recipe> recipeList = response.body();
                    Log.i(LOG_TAG, "ResponseBodyRetrofit Called");
                    recipeArrayList = recipeList;
                    mRecipeAdapter = new RecipeAdapter(recipeList, MainActivity.this, mEmptyView);
                    mRecyclerView.setAdapter(mRecipeAdapter);
                    mLayoutManager.onRestoreInstanceState(mListState);
                } else {
                    System.out.println(response.errorBody());
                }
            }
                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {

                    t.printStackTrace();
                    mEmptyView.setText(R.string.no_network);
                }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOG_TAG, "MAIN ACTIVITY :onSaveInstanceState");
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(STATE_KEY, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(LOG_TAG, "MAIN ACTIVITY onRestoreInstanceState");
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(STATE_KEY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "MAIN ACTIVITY onResume");

        if (mListState != null) {
           mLayoutManager.onRestoreInstanceState(mListState);
        }

        mRecipeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
        mRecipeAdapter.notifyDataSetChanged();
    }
}





