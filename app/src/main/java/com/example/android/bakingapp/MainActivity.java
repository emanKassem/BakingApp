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
import com.example.android.bakingapp.View.RecipeActivity;
import com.example.android.bakingapp.View.RecipeAdapter;
import com.example.android.bakingapp.View.RecipeFragmet;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String ENDPOINT = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private RequestQueue requestQueue;
    private Gson gson;
    List<Recipe> recipes;

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
        requestQueue = Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        fetchRecipes();
        if(getResources().getBoolean(R.bool.twoPaneMode)){
            GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(layoutManager);
        }else {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    /*private void initCollapsingToolbar() {
        collapsingToolbar.setTitle(" ");
        appBar.setExpanded(true);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }*/

    private void fetchRecipes() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ENDPOINT, onRecipesLoaded, onPostsError);
        requestQueue.add(stringRequest);
    }
    private final Response.Listener<String> onRecipesLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(final String response) {
            recipes = Arrays.asList(gson.fromJson(response, Recipe[].class));
            for(Recipe recipe : recipes){
                Log.i("result", recipe.getName());
            }
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
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            fetchRecipes();
                        }
                    });

            snackbar.show();
        }
    };
}
