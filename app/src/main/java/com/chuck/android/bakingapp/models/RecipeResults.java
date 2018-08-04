package com.chuck.android.bakingapp.models;

import java.util.List;

public class RecipeResults {
    private List<RecipeList> results;

    public RecipeResults(List<RecipeList> results) {
        this.results = results;
    }

    public List<RecipeList> getResults() {
        return results;
    }

    public void setResults(List<RecipeList> results) {
        this.results = results;
    }
}
