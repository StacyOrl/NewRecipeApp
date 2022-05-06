package com.stasyorl.recipeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stasyorl.recipeapp.Adapters.CategoryAdapter;
import com.stasyorl.recipeapp.Adapters.RandomRecipeAdapter;
import com.stasyorl.recipeapp.Listeners.CategoryListener;
import com.stasyorl.recipeapp.Listeners.RandomRecipeResponseListener;
import com.stasyorl.recipeapp.Listeners.RecipeClickListener;
import com.stasyorl.recipeapp.Models.CategoryModel;
import com.stasyorl.recipeapp.Models.RandomRecipeApiResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryListener {

    SearchView searchView;

    Spinner spinner;
    List<String> tags = new ArrayList<>();

    String[] categoryArray;

    ProgressDialog dialog;
    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    CategoryAdapter categoryAdapter;
    RecyclerView recyclerView, recycle_category;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");

        searchView = findViewById(R.id.searchView_home);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tags.clear();
                tags.add(query);
                manager.getRandomRecipes(randomRecipeResponseListener, tags);
                dialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


//        spinner = findViewById(R.id.spinner_tags);
//        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(
//                this,
//                R.array.tags,
//                R.layout.spinner_text);
//        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);
//        spinner.setAdapter(arrayAdapter);
//        spinner.setOnItemSelectedListener(spinnerSelectedListener);

        manager = new RequestManager(this);
//        manager.getRandomRecipes(randomRecipeResponseListener);
//        dialog.show();

        categoryArray = MainActivity.this.getResources().getStringArray(R.array.tags);
        recycle_category = findViewById(R.id.category_recycler);
        recycle_category.setHasFixedSize(true);
        recycle_category.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(MainActivity.this, createData(categoryArray),this);
        recycle_category.setAdapter(categoryAdapter);
        onCategoryClicked(0);




    }

    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            dialog.dismiss();
            recyclerView = findViewById(R.id.recycler_random);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
            randomRecipeAdapter = new RandomRecipeAdapter(MainActivity.this, response.recipes, recipeClickListener);
            recyclerView.setAdapter(randomRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

        }
    };


    private ArrayList<CategoryModel> createData(String[] array) {
        ArrayList<CategoryModel> models = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            if(i!=0)models.add(new CategoryModel(i, array[i], false));
            else models.add(new CategoryModel(i, array[i], true));
        }

        return models;
    }

    private final AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            tags.clear();
            tags.add(adapterView.getSelectedItem().toString());
            manager.getRandomRecipes(randomRecipeResponseListener, tags);
            dialog.show();


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(MainActivity.this, RecipeDetailsActivity.class)
            .putExtra("id", id));

        }
    };

    @Override
    public void onCategoryClicked(int position) {
        tags.clear();
        tags.add(categoryArray[position]);
        manager.getRandomRecipes(randomRecipeResponseListener, tags);
        dialog.show();
    }

//    private final CategoryListener categoryListener = new CategoryListener() {
//        @Override
//        public void onCategoryClicked(View view, int position) {
//            tags.clear();
//            tags.add(view..toString());
//            manager.getRandomRecipes(randomRecipeResponseListener, tags);
//            dialog.show();
//        }
//    };

}
