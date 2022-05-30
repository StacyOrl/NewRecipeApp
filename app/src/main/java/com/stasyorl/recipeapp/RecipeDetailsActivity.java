package com.stasyorl.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
    ImageView imageView_meal_image, imageView_plus_description, imageView_plus_instructions, imageView_minus_description, imageView_minus_instruction;
    RecyclerView recycler_meal_ingredients, recycler_meal_similar, recycler_meal_instructions;
    InstructionsAdapter instructionsAdapter;
    CardView cardView_text_animation, cardView_instructions_animation;

    RequestManager manager;
    ProgressDialog dialog;

    IngredientsAdapter ingredientsAdapter;

    SimilarRecipeAdapter similarRecipeAdapter;
    Animation intro_animation, outro_animation;


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

        imageView_plus_description = findViewById(R.id.imageView_plus_description);
        imageView_plus_instructions = findViewById(R.id.imageView_plus_instructions);
        imageView_minus_description = findViewById(R.id.imageView_minus_description);
        imageView_minus_instruction = findViewById(R.id.imageView_minus_instruction);

        intro_animation = AnimationUtils.loadAnimation(this, R.anim.intro_transition);
        outro_animation = AnimationUtils.loadAnimation(this, R.anim.outro_transition);
        cardView_text_animation = findViewById(R.id.cardView_text_animation);
        cardView_instructions_animation = findViewById(R.id.cardView_instructions_animation);

    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            dialog.dismiss();
            textView_meal_name.setText(response.title);
            textView_meal_source.setText(response.sourceName);

            if (textView_meal_source.getText() == ""){
                textView_meal_source.setText(R.string.unknown_source);
            }
            String summary = response.summary;
            summary = summary.split("Try")[0];

            textView_meal_summory.setText(summary.replace("<b>", "").
                    replace("</b>", "").
                    replace("<a", "").
                    replace(">", "").
                    replace("href=", "").
                    replace("</a", ""));
            Picasso.get().load(response.image).into(imageView_meal_image);

            if (imageView_meal_image.getDrawable() == null){
                imageView_meal_image.setImageResource(R.mipmap.ic_no_photo_foreground);
            }

            recycler_meal_ingredients.setHasFixedSize(true);
            recycler_meal_ingredients.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            ingredientsAdapter = new IngredientsAdapter(RecipeDetailsActivity.this, response.extendedIngredients);
            recycler_meal_ingredients.setAdapter(ingredientsAdapter);

            imageView_plus_description.setOnClickListener(view -> {
                textView_meal_summory.setVisibility(View.VISIBLE);
                cardView_text_animation.startAnimation(intro_animation);
                imageView_minus_description.setVisibility(View.VISIBLE);
                imageView_plus_description.setVisibility(View.GONE);
            });

            imageView_minus_description.setOnClickListener(view -> {
                cardView_text_animation.startAnimation(outro_animation);
                textView_meal_summory.setVisibility(View.GONE);

                imageView_plus_description.setVisibility(View.VISIBLE);
                imageView_minus_description.setVisibility(View.GONE);
            });

            imageView_plus_instructions.setOnClickListener(view -> {
                recycler_meal_instructions.setVisibility(View.VISIBLE);
                cardView_instructions_animation.startAnimation(intro_animation);
                imageView_minus_instruction.setVisibility(View.VISIBLE);
                imageView_plus_instructions.setVisibility(View.GONE);
            });

            imageView_minus_instruction.setOnClickListener(view -> {
                recycler_meal_instructions.setVisibility(View.GONE);
                imageView_plus_instructions.setVisibility(View.VISIBLE);
                imageView_minus_instruction.setVisibility(View.GONE);
            });


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

    private final RecipeClickListener recipeClickListener = id -> startActivity(new Intent(RecipeDetailsActivity.this, RecipeDetailsActivity.class)
    .putExtra("id", id));

    private final InstructionsListener instructionsListener = new InstructionsListener() {

        @Override
        public void didFetch(List<InstructionsResponse> response, String message) {
            recycler_meal_instructions.setHasFixedSize(true);
            recycler_meal_instructions.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
            instructionsAdapter = new InstructionsAdapter(RecipeDetailsActivity.this, response);
            recycler_meal_instructions.setAdapter(instructionsAdapter);
            recycler_meal_instructions.setVisibility(View.GONE);
        }

        @Override
        public void didError(String message) {

        }
    };
}