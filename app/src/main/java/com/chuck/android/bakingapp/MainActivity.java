package com.chuck.android.bakingapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.View;

import com.chuck.android.bakingapp.adapters.RecipeAdapter;
import com.chuck.android.bakingapp.REST.RecipeAPI;
import com.chuck.android.bakingapp.REST.RecipeInterface;
import com.chuck.android.bakingapp.models.RecipeList;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recipeList);
        initRecyclerView();
        getRecipeListfromWeb();


    }
    private void initRecyclerView() {
        adapter = new RecipeAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public void getRecipeListfromWeb(){
        RecipeInterface recipeService = RecipeAPI.getClient().create(RecipeInterface.class);
        Call<List<RecipeList>> call = recipeService.getCurrentRecipes();
        call.enqueue(new Callback<List<RecipeList>>() {
            @Override
            public void onResponse(Call<List<RecipeList>> call, Response<List<RecipeList>> response) {
                List<RecipeList> recipes = response.body();
                adapter.setRecipes(recipes);
            }

            @Override
            public void onFailure(Call<List<RecipeList>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

    }


}
