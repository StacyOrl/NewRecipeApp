package com.stasyorl.recipeapp.Models;

public class RecipeFromFirebase {
    public String aggregateLikes;
    public String recipeId;
    public String recipeTitle;
    public String readyInMinutes;
    public String servings;
    public String recipeImage;

    public String getAggregateLikes() {
        return aggregateLikes;
    }

    public void setAggregateLikes(String aggregateLikes) {
        this.aggregateLikes = aggregateLikes;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public String getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(String readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }
}
