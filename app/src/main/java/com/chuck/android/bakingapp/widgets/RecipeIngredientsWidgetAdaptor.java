package com.chuck.android.bakingapp.widgets;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.chuck.android.bakingapp.R;
import com.chuck.android.bakingapp.models.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientsWidgetAdaptor implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<Ingredient> ingredients;
    private SharedPreferences sharedPreferences;

    public RecipeIngredientsWidgetAdaptor(Context context) {
        this.context = context;
        this.ingredients = getIngredients();
    }
    private List<Ingredient> getIngredients(){
        ingredients = null;
        if ((sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)) != null)
        {
            String listJson = sharedPreferences.getString("json1", "No Data");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Ingredient>>() {
            }.getType();
            ingredients = gson.fromJson(listJson, type);
        }
        return ingredients;
    }
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget_list_item);

        String holder;
        float quantity;
        if ((holder = ingredients.get(position).getIngredient()) != null)
            remoteViews.setTextViewText(R.id.widgetIngredient_name, holder);
        if ((quantity = ingredients.get(position).getQuantity()) != 0)
            remoteViews.setTextViewText(R.id.widgetQuantity, Float.toString(quantity));
        if ((holder = ingredients.get(position).getMeasure()) != null)
            remoteViews.setTextViewText(R.id.widgetMeasure, holder + " ");
        return remoteViews;
    }
    @Override
    public int getCount() {
        return ingredients.size();
    }
    @Override
    public void onDataSetChanged() {
        ingredients = getIngredients();
    }
    @Override
    public int getViewTypeCount() {
        return 1;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public void onCreate() {
    }
    @Override
    public void onDestroy() {
    }
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
}
