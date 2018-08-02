package com.chuck.android.bakingapp.adapters;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.chuck.android.bakingapp.R;
import com.chuck.android.bakingapp.RecipeListStepActivity;
import com.chuck.android.bakingapp.models.RecipeList;
import com.chuck.android.bakingapp.widgets.RecipeIngredientsWidget;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<RecipeList> recipes;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout recipesLayout;
        TextView recipeName;
        ImageView recipeImage;

        RecipeViewHolder(View v) {
            super(v);
            //Define ViewHolder Textview and layout
            recipesLayout = v.findViewById(R.id.recipes_layout);
            recipeName = v.findViewById(R.id.recipe_name);
            recipeImage = v.findViewById(R.id.recipe_image);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
            editor = sharedPreferences.edit();
            Gson gsonIngList = new Gson();
            String json = gsonIngList.toJson(recipes.get(position).getIngredients());
            editor.putString("json1",json);
            editor.putString("Recipe Title",recipes.get(position).getName());
            editor.apply();

            RemoteViews remoteViews = new RemoteViews(view.getContext().getPackageName(), R.layout.recipe_ingredients_widget);
            remoteViews.setTextViewText(R.id.widget_title,recipes.get(position).getName() + " Shopping List");

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(view.getContext());
            ComponentName thisWidget = new ComponentName(view.getContext(), RecipeIngredientsWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetListView);
            appWidgetManager.updateAppWidget(thisWidget,remoteViews);
            Intent myIntent = new Intent(view.getContext(), RecipeListStepActivity.class);
            myIntent.putExtra("EXTRA_RECIPE_ID", recipes.get(position).getId());
            view.getContext().startActivity(myIntent);
        }
    }



    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_list_item,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeViewHolder holder, int position) {
        if (recipes != null) {
            String recipeName = recipes.get(position).getName();
            holder.recipeName.setText(recipeName);
            String recipeImage = recipes.get(position).getImage();
            if (recipeImage != null && !recipeImage.isEmpty())
                Picasso.get().load(recipeImage).into(holder.recipeImage);
            holder.recipeImage.setContentDescription(recipeName);
        }

    }
    public void setRecipes(List<RecipeList> currentRecipes) {
        //After Adapter is initialized add recipe object to adapter, when switching recipe lists
        this.recipes = currentRecipes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        //only return item count if recipes object is initialized
        if (recipes != null)
            return recipes.size();
        else
            return 0;
    }

}
