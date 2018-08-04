package com.chuck.android.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.chuck.android.bakingapp.REST.RecipeAPI;
import com.chuck.android.bakingapp.REST.RecipeInterface;
import com.chuck.android.bakingapp.adapters.RecipeAdapter;
import com.chuck.android.bakingapp.models.RecipeList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    @Nullable
    private CountingIdlingResource mIdlingResource = new CountingIdlingResource("RecipeNetworkCall");


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle(getString(R.string.main_title));
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recipeList);
        initRecyclerView();
        getRecipeListfromWeb();


    }

    private void initRecyclerView() {
        adapter = new RecipeAdapter();
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void getRecipeListfromWeb() {
        //Espresso Idling Resource increment
        mIdlingResource.increment();
        RecipeInterface recipeService = RecipeAPI.getClient().create(RecipeInterface.class);
        Call<List<RecipeList>> call = recipeService.getCurrentRecipes();
        call.enqueue(new Callback<List<RecipeList>>() {
            @Override
            public void onResponse(Call<List<RecipeList>> call, Response<List<RecipeList>> response) {
                List<RecipeList> recipes = response.body();
                adapter.setRecipes(recipes);
                mIdlingResource.decrement();
            }

            @Override
            public void onFailure(Call<List<RecipeList>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

    }

    public CountingIdlingResource getIdlingResourceInTest() {
        return mIdlingResource;
    }


}
