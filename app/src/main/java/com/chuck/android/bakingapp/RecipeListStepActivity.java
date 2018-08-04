package com.chuck.android.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.chuck.android.bakingapp.REST.RecipeAPI;
import com.chuck.android.bakingapp.REST.RecipeInterface;
import com.chuck.android.bakingapp.adapters.RecipeStepsAdapter;
import com.chuck.android.bakingapp.models.RecipeList;
import com.chuck.android.bakingapp.models.Step;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeStepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListStepActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private List<RecipeList> recipes;
    private static final String APP_NAME = MainActivity.class.getSimpleName();
    private View recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.recipe_steps);
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        recyclerView = findViewById(R.id.recipe_list);


        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        getRecipeListFromWeb();


        // assert recyclerView != null;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        List<Step> currentSteps = null;
        if (recipes != null) {
            for (RecipeList recipe : recipes) {
                if (recipe.getId() == 1)
                    currentSteps = recipe.getSteps();
            }
        }
        if (currentSteps != null)
            recyclerView.setAdapter(new RecipeStepsAdapter(this, currentSteps, mTwoPane));

    }

    public void getRecipeListFromWeb() {
        RecipeInterface recipeService = RecipeAPI.getClient().create(RecipeInterface.class);
        Call<List<RecipeList>> call = recipeService.getCurrentRecipes();
        call.enqueue(new Callback<List<RecipeList>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipeList>> call, @NonNull Response<List<RecipeList>> response) {
                recipes = response.body();
                setupRecyclerView((RecyclerView) recyclerView);
            }

            @Override
            public void onFailure(@NonNull Call<List<RecipeList>> call, @NonNull Throwable t) {
                Log.e(APP_NAME, t.toString());
            }
        });

    }


}
