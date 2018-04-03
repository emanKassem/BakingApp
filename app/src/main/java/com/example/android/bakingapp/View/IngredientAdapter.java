package com.example.android.bakingapp.View;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngreadientViewHolder> {

    ArrayList<Ingredient> ingredients;
    public IngredientAdapter(ArrayList<Ingredient> ingredients){
        this.ingredients = ingredients;
    }
    @Override
    public IngreadientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        return new IngreadientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngreadientViewHolder holder, int position) {

        holder.ingredientNameTv.setText(ingredients.get(position).getIngredient());
        holder.ingredientAmountTv.setText(String.valueOf(ingredients.get(position).getQuantity()));
        holder.ingredientMeasureTv.setText(ingredients.get(position).getMeasure());

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class IngreadientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_name)
        TextView ingredientNameTv;
        @BindView(R.id.ingredient_amount)
        TextView ingredientAmountTv;
        @BindView(R.id.ingredient_measure)
        TextView ingredientMeasureTv;
        public IngreadientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
