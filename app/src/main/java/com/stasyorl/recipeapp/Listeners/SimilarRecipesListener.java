package com.stasyorl.recipeapp.Listeners;

import com.stasyorl.recipeapp.Models.SimilarRecipeResponse;

import java.util.List;

public interface SimilarRecipesListener {
    void didFetch(List<SimilarRecipeResponse> response, String message);
    void didError(String message);
}
