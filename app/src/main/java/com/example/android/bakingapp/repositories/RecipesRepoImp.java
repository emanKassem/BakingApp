package com.example.android.bakingapp.repositories;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.bakingapp.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

public class RecipesRepoImp{
    private static final String ENDPOINT = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private RequestQueue requestQueue;
    Context context;
    private Gson gson;
    List<Recipe>recipes;
    private OnRequestCompletedListener mRequestCompletedListener;

    public RecipesRepoImp(Context context, OnRequestCompletedListener callback){
        this.context = context;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        requestQueue = Volley.newRequestQueue(context);
        mRequestCompletedListener = callback;
    }

    public void getRecipes() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ENDPOINT, onRecipesLoaded, onPostsError);
        requestQueue.add(stringRequest);

    }
    private final Response.Listener<String> onRecipesLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(final String response) {
             recipes = Arrays.asList(gson.fromJson(response, Recipe[].class));
             mRequestCompletedListener.onRequestCompleted(recipes);
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            mRequestCompletedListener.onRequestFailed();

        }
    };

    public interface OnRequestCompletedListener{
        void onRequestCompleted(List<Recipe> recipes);
        void onRequestFailed();
    }
}
