package com.example.android.bakingapp.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity {

    @BindView(R.id.ingredientsRecyclerView)
    RecyclerView ingredientsRecyclerView;
    @BindView(R.id.stepsRecyclerView)
    RecyclerView stepsRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        Bundle bundle = this.getIntent().getExtras();
        ArrayList<Ingredient> ingredients = bundle.getParcelableArrayList("ingredients");
        ArrayList<Step> steps = bundle.getParcelableArrayList("steps");
        RecyclerView.LayoutManager ingredientsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ingredientsRecyclerView.setLayoutManager(ingredientsLayoutManager);
        IngredientAdapter ingredientAdapter = new IngredientAdapter(ingredients);
        ingredientsRecyclerView.setAdapter(ingredientAdapter);
        RecyclerView.LayoutManager StepsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        stepsRecyclerView.setLayoutManager(StepsLayoutManager);
        StepAdapter stepAdapter = new StepAdapter(steps);
        stepsRecyclerView.setAdapter(stepAdapter);

    }
}
