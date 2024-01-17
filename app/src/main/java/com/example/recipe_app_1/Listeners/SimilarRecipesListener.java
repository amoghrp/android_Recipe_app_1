package com.example.recipe_app_1.Listeners;

import com.example.recipe_app_1.Models.SimilarRecipeResponse;

import java.util.List;

public interface SimilarRecipesListener {
    void didFetch(List<SimilarRecipeResponse> response , String message);
    void didError(String message);

}
