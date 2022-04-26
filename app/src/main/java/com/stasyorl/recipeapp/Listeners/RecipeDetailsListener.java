package com.stasyorl.recipeapp.Listeners;

import com.stasyorl.recipeapp.Models.RecipeDetailsResponse;

public interface RecipeDetailsListener {
    void didFetch(RecipeDetailsResponse response, String message);
    void didError(String message);
}
