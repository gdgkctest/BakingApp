package com.chuck.android.bakingapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuck.android.bakingapp.R;
import com.chuck.android.bakingapp.models.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {
    private List<Ingredient> ingredients;

    public class IngredientViewHolder  extends RecyclerView.ViewHolder{
        @BindView(R.id.ingredientRVLinearLayout)LinearLayout ingredientLayout;
        @BindView(R.id.ingredientName) TextView ingredientName;
        @BindView(R.id.ingredientMeasure) TextView ingredientMeasure;
        @BindView(R.id.ingredientQuantity) TextView ingredientQuantity;


        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);

        }
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.ingredients_list_item,parent,false);
        return new IngredientViewHolder(view);    }

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
    public void setIngredients(List<Ingredient> currentIngredients){
        ingredients = currentIngredients;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (ingredients != null)
            return ingredients.size();
        else
            return 0;    }
}
