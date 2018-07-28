package com.chuck.android.bakingapp.widgets;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class RecipeIngredientsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeIngredientsWidgetAdaptor(this.getApplicationContext());
    }
}
