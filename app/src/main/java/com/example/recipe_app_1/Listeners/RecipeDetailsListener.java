package com.example.recipe_app_1.Listeners;

import com.example.recipe_app_1.Models.RecipeDetailsResponse;

public interface RecipeDetailsListener {
    void didFetch(RecipeDetailsResponse response, String message);
    void didError(String message);
}
