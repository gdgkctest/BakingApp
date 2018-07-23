package com.chuck.android.bakingapp.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuck.android.bakingapp.R;
import com.chuck.android.bakingapp.RecipeListStepActivity;
import com.chuck.android.bakingapp.models.RecipeList;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<RecipeList> recipes;

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout recipesLayout;
        TextView recipeName;

        RecipeViewHolder(View v) {
            super(v);
            //Define ViewHolder Textview and layout
            recipesLayout = v.findViewById(R.id.recipes_layout);
            recipeName = v.findViewById(R.id.recipe_name);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
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
