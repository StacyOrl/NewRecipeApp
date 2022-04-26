package com.stasyorl.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stasyorl.recipeapp.Adapters.IngredientsAdapter;
import com.stasyorl.recipeapp.Adapters.InstructionsAdapter;
import com.stasyorl.recipeapp.Adapters.SimilarRecipeAdapter;
import com.stasyorl.recipeapp.Listeners.InstructionsListener;
import com.stasyorl.recipeapp.Listeners.RecipeClickListener;
import com.stasyorl.recipeapp.Listeners.RecipeDetailsListener;
import com.stasyorl.recipeapp.Listeners.SimilarRecipesListener;
import com.stasyorl.recipeapp.Models.InstructionsResponse;
import com.stasyorl.recipeapp.Models.RecipeDetailsResponse;
import com.stasyorl.recipeapp.Models.SimilarRecipeResponse;

import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {

    int id;
    TextView textView_meal_name, textView_meal_source, textView_meal_summory;
    ImageView imageView_meal_image;
    RecyclerView recycler_meal_ingredients, recycler_meal_similar, recycler_meal_instructions;
    InstructionsAdapter instructionsAdapter;

    RequestManager manager;
    ProgressDialog dialog;

    IngredientsAdapter ingredientsAdapter;

    SimilarRecipeAdapter similarRecipeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        findViews();

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        manager = new RequestManager(this);
        manager.getRecipeDetails(recipeDetailsListener, id);
        manager.getSimilarRecipes(similarRecipesListener, id);
        manager.getInstructions(instructionsListener, id);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Details...");
        dialog.show();

    }

    private void findViews() {
        textView_meal_name = findViewById(R.id.textView_meal_name);
        textView_meal_source = findViewById(R.id.textView_meal_source);
        textView_meal_summory = findViewById(R.id.textView_meal_summory);
        imageView_meal_image = findViewById(R.id.imageView_meal_image);
        recycler_meal_ingredients = findViewById(R.id.recycler_meal_ingredients);
        recycler_meal_similar = findViewById(R.id.recycler_meal_similar);
        recycler_meal_instructions = findViewById(R.id.recycler_meal_instructions);
    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            dialog.dismiss();
            textView_meal_name.setText(response.title);
            textView_meal_source.setText(response.sourceName);
            textView_meal_summory.setText(response.summary);
            Picasso.get().load(response.image).into(imageView_meal_image);

            //layout manager for the ingredients recyclerView
            recycler_meal_ingredients.setHasFixedSize(true);
            recycler_meal_ingredients.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            //setting adapter
            ingredientsAdapter = new IngredientsAdapter(RecipeDetailsActivity.this, response.extendedIngredients);
            recycler_meal_ingredients.setAdapter(ingredientsAdapter);
        }


        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();

        }
    };

    private final SimilarRecipesListener similarRecipesListener = new SimilarRecipesListener() {
        @Override
        public void didFetch(List<SimilarRecipeResponse> response, String message) {
            recycler_meal_similar.setHasFixedSize(true);
            recycler_meal_similar.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            similarRecipeAdapter = new SimilarRecipeAdapter(RecipeDetailsActivity.this, response, recipeClickListener);
            recycler_meal_similar.setAdapter(similarRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();

        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(RecipeDetailsActivity.this, RecipeDetailsActivity.class)
            .putExtra("id", id));
        }
    };

    private final InstructionsListener instructionsListener = new InstructionsListener() {

        @Override
        public void didFetch(List<InstructionsResponse> response, String message) {
            recycler_meal_instructions.setHasFixedSize(true);
            recycler_meal_instructions.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
            instructionsAdapter = new InstructionsAdapter(RecipeDetailsActivity.this, response);
            recycler_meal_instructions.setAdapter(instructionsAdapter);
        }

        @Override
        public void didError(String message) {

        }
    };
}