package com.example.android.bakingapp.presenter;

import android.content.Context;

import com.example.android.bakingapp.View.MainActivityView;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.repositories.RecipesRepoImp;

import java.util.List;

public class RecipesPresenter {

    MainActivityView mainActivityView;
    RecipesRepoImp recipesRepoImp;
    private RecipesRepoImp.OnRequestCompletedListener Listener = new RecipesRepoImp.OnRequestCompletedListener() {
        @Override
        public void onRequestCompleted(List<Recipe> recipes) {
            mainActivityView.DisplayRecipes(recipes);
        }

        @Override
        public void onRequestFailed() {
            mainActivityView.NoRecipes();
        }
    };

    public RecipesPresenter(MainActivityView mainActivityView, Context context){
        this.mainActivityView = mainActivityView;
        recipesRepoImp = new RecipesRepoImp(context, Listener);
    }

    public void fetchRecipes(){
        recipesRepoImp.getRecipes();
    }
}
