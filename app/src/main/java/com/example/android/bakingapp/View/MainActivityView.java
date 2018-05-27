package com.example.android.bakingapp.View;

import com.example.android.bakingapp.model.Recipe;

import java.util.List;

public interface MainActivityView {
     void DisplayRecipes(List<Recipe> recipes);
     void NoRecipes();
}
