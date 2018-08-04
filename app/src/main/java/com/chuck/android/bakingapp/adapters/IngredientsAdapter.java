package com.chuck.android.bakingapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuck.android.bakingapp.R;
import com.chuck.android.bakingapp.models.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {
    private List<Ingredient> ingredients;


    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredientRVLinearLayout)
        LinearLayout ingredientLayout;
        @BindView(R.id.ingredientName)
        TextView ingredientName;
        @BindView(R.id.ingredientMeasure)
        TextView ingredientMeasure;
        @BindView(R.id.ingredientQuantity)
        TextView ingredientQuantity;


        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.ingredients_list_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        if (ingredients != null) {
            String ingredientName = ingredients.get(position).getIngredient();
            String ingredientMeasure = ingredients.get(position).getMeasure();
            Float ingredientQuantity = ingredients.get(position).getQuantity();

            holder.ingredientName.setText(ingredientName);
            holder.ingredientMeasure.setText(ingredientMeasure);
            holder.ingredientQuantity.setText(Float.toString(ingredientQuantity));
        }

    }

    public void setIngredients(Context context) {
        ingredients = getIngredients(context);
        notifyDataSetChanged();
    }

    private List<Ingredient> getIngredients(Context context) {
        //ingredients = null;
        SharedPreferences sharedPreferences;
        if ((sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)) != null) {
            String listJson = sharedPreferences.getString("json1", "No Data");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Ingredient>>() {
            }.getType();
            ingredients = gson.fromJson(listJson, type);
        }
        return ingredients;
    }

    @Override
    public int getItemCount() {
        if (ingredients != null)
            return ingredients.size();
        else
            return 0;
    }
}
