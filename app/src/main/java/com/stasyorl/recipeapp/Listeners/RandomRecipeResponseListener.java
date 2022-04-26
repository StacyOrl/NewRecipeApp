package com.stasyorl.recipeapp.Listeners;

import com.stasyorl.recipeapp.Models.RandomRecipeApiResponse;

public interface RandomRecipeResponseListener {
    void didFetch(RandomRecipeApiResponse response, String message);
    void didError(String message);
}
