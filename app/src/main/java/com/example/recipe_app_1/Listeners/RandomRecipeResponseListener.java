package com.example.recipe_app_1.Listeners;

import com.example.recipe_app_1.Models.RandomRecipeApiResponse;

public interface RandomRecipeResponseListener {
    void didFetch(RandomRecipeApiResponse response, String message);
    void didError(String message);

}
