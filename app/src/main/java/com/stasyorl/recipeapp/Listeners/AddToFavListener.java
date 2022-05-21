package com.stasyorl.recipeapp.Listeners;

public interface AddToFavListener {
    void onButtonClicked(CharSequence title, CharSequence likes, CharSequence serving, CharSequence time, String image, String id);
}
