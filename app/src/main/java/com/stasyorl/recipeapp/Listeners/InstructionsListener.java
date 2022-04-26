package com.stasyorl.recipeapp.Listeners;

import com.stasyorl.recipeapp.Models.InstructionsResponse;

import java.util.List;

public interface InstructionsListener {
    void didFetch(List<InstructionsResponse> response, String message);
    void didError(String message);
}
