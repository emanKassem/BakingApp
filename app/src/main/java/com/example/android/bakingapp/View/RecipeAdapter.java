package com.example.android.bakingapp.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LENOVO on 31/03/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipteViewHolder> {

    List<Recipe> recipes;
    Context context;
    public interface OnItemClickListener {

        void onItemClick(Recipe recipe);
    }
    private final OnItemClickListener listener;

    public RecipeAdapter(Context context, List<Recipe> recipes, OnItemClickListener listener){
        this.context = context;
        this.recipes = recipes;
        this.listener = listener;
    }
    @Override
    public RecipteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View RecipteView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item,parent ,false);
        return new RecipteViewHolder(RecipteView);

    }

    @Override
    public void onBindViewHolder(RecipteViewHolder holder, int position) {
        String name = recipes.get(position).getName();
        String image = recipes.get(position).getImage();
        holder.recipeTextView.setText(name);
        if(image.isEmpty()){
            holder.recipeImageView.setVisibility(View.GONE);
        }else{
            Glide.with(context).load(image).into(holder.recipeImageView);
        }
        holder.bind(recipes.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipteViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.recipe_image)
        ImageView recipeImageView;
        @BindView(R.id.recipe_name)
         TextView recipeTextView;
        public RecipteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Recipe recipe, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(recipe);
                }
            });
        }
    }
}
