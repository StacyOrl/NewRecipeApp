package com.stasyorl.recipeapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stasyorl.recipeapp.Adapters.CategoryAdapter;
import com.stasyorl.recipeapp.Adapters.RandomRecipeAdapter;
import com.stasyorl.recipeapp.Fragments.UserLoginFragment;
import com.stasyorl.recipeapp.Fragments.UserRegistrationFragment;
import com.stasyorl.recipeapp.Listeners.CategoryListener;
import com.stasyorl.recipeapp.Listeners.RandomRecipeResponseListener;
import com.stasyorl.recipeapp.Listeners.RecipeClickListener;
import com.stasyorl.recipeapp.Models.CategoryModel;
import com.stasyorl.recipeapp.Models.RandomRecipeApiResponse;
import com.stasyorl.recipeapp.Models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryListener{

    SearchView searchView;

    List<String> tags = new ArrayList<>();

    String[] categoryArray;

    ProgressDialog dialog;
    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
//    RandomRecipeResponseListener randomRecipeResponseListener;
    CategoryAdapter categoryAdapter;
    RecyclerView recyclerView, recycle_category;
    ImageView no_wifi_image, imageView_user_pic;
    FrameLayout fragmentContainer;
    LinearLayout mainScreen;

    InternetConnectorReceiver receiver;

    UserRegistrationFragment registrationFragment;
    UserLoginFragment loginFragment;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference favDatabaseReference, fvrt_listRef;
    Boolean favChecked = false;
    Recipe recipe;
    FirebaseUser user;

    public LinearLayout getMainScreen() {
        return mainScreen;
    }

    public FrameLayout getFragmentContainer() {
        return fragmentContainer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        favDatabaseReference = database.getReference("favourites");
        fvrt_listRef = database.getReference("favouriteList").child(currentUserId);
//        receiver = new InternetConnectorReceiver(MainActivity.this);
//        registerReceiver(receiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
//        no_wifi_image = findViewById(R.id.no_wifi_image);

        registrationFragment = new UserRegistrationFragment();
        loginFragment = new UserLoginFragment();
        fragmentContainer = findViewById(R.id.fragmentContainer);
        mainScreen = findViewById(R.id.main_screen);
        imageView_user_pic = findViewById(R.id.imageView_user_pic);
        imageView_user_pic.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "KLAPPT", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, registrationFragment).commit();
                mainScreen.setVisibility(View.GONE);
                fragmentContainer.setVisibility(View.VISIBLE);
        });






        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");

        imageView_user_pic = findViewById(R.id.imageView_user_pic);



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


//
        manager = new RequestManager(this);

        categoryArray = MainActivity.this.getResources().getStringArray(R.array.tags);
        recycle_category = findViewById(R.id.category_recycler);
        recycle_category.setHasFixedSize(true);
        recycle_category.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(MainActivity.this, createData(categoryArray),this);
        recycle_category.setAdapter(categoryAdapter);
        onCategoryClicked(0);




    }
//
//    public void addFragment(){
//        fragment = new UserRegistrationFragment();
//        fragmentManager = getFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.fragmentContainer, fragment);
//        fragmentTransaction.commit();
//
//    }
    public void changeUI(boolean isConnected) {
        // Change status according to boolean value
        MainActivity.this.runOnUiThread(() -> {
            if (isConnected) {
                Toast.makeText(MainActivity.this, "yes internet", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            } else {
                Toast.makeText(MainActivity.this, "no internet", Toast.LENGTH_SHORT).show();
                no_wifi_image.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });
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
            dialog.dismiss();
            no_wifi_image = findViewById(R.id.no_wifi_image);

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


    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {

            Intent intent = new Intent(MainActivity.this, RecipeDetailsActivity.class);
            intent.putExtra("id", id);

            startActivity(intent);



        }
    };

    @Override
    public void onCategoryClicked(int position) {
        tags.clear();
        tags.add(categoryArray[position]);
        manager.getRandomRecipes(randomRecipeResponseListener, tags);
        dialog.show();
    }
}
