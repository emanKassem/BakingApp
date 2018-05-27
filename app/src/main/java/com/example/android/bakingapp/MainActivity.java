package com.example.android.bakingapp;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.android.bakingapp.View.MainActivityView;
import com.example.android.bakingapp.View.RecipeActivity;
import com.example.android.bakingapp.View.RecipeAdapter;
import com.example.android.bakingapp.View.RecipeFragmet;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.presenter.RecipesPresenter;
import com.example.android.bakingapp.repositories.RecipesRepoImp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityView{


    RecipesPresenter recipesPresenter;
    MainActivityView mainActivityView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appBar;
    @BindView(R.id.backdrop)
    ImageView backdropImageView;
    @BindView(R.id.main_content)
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        try{
            Glide.with(this).load(R.drawable.maxresdefault).into(backdropImageView);
        }catch (Exception e){
            e.printStackTrace();
        }

        mainActivityView = this;
        recipesPresenter = new RecipesPresenter(mainActivityView, getApplication());
        recipesPresenter.fetchRecipes();

        if(getResources().getBoolean(R.bool.twoPaneMode)){
            GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(layoutManager);
        }else {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    @Override
    public void DisplayRecipes(List<Recipe> recipes) {
        RecipeAdapter recipteAdapter = new RecipeAdapter(MainActivity.this, recipes, new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Recipe recipe) {
                Intent i = new Intent(MainActivity.this, RecipeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", recipe.getName());
                bundle.putParcelableArrayList("ingredients", recipe.getIngredients());
                bundle.putParcelableArrayList("steps", recipe.getSteps());
                i.putExtras(bundle);
                startActivity(i);

            }
        });
        recyclerView.setAdapter(recipteAdapter);
    }

    @Override
    public void NoRecipes() {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "No internet connection", Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recipesPresenter.fetchRecipes();
                    }
                });

        snackbar.show();
    }
}
