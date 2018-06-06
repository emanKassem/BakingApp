package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.android.bakingapp.View.RecipeActivity;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    public static Recipe latestOpenedRecipe;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);
        if(latestOpenedRecipe != null) {
            // Ingredients
            StringBuilder sIngredients = new StringBuilder();
            for (Ingredient ing : latestOpenedRecipe.getIngredients()) {
                sIngredients.append(ing.getIngredient() + " (" + ing.getQuantity() + " " +
                        ing.getMeasure() + ")");
                sIngredients.append("\n");
            }

            views.setTextViewText(R.id.widgetRecipeName, latestOpenedRecipe.getName());
            views.setTextViewText(R.id.recipe_ingredients, sIngredients);
            
            Intent intent = new Intent(context, RecipeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("name", latestOpenedRecipe.getName());
            bundle.putParcelableArrayList("ingredients", latestOpenedRecipe.getIngredients());
            bundle.putParcelableArrayList("steps", latestOpenedRecipe.getSteps());
            intent.putExtras(bundle);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.root_layout, pendingIntent);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateIngredientsWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

