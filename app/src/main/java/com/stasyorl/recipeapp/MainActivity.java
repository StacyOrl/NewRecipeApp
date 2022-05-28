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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stasyorl.recipeapp.Adapters.CategoryAdapter;
import com.stasyorl.recipeapp.Adapters.RandomRecipeAdapter;
import com.stasyorl.recipeapp.Fragments.EmptyFavouriteFragment;
import com.stasyorl.recipeapp.Fragments.ExistingUserFragment;
import com.stasyorl.recipeapp.Fragments.FavouritesFragment;
import com.stasyorl.recipeapp.Fragments.UserLoginFragment;
import com.stasyorl.recipeapp.Fragments.UserRegistrationFragment;
import com.stasyorl.recipeapp.Listeners.AddToFavListener;
import com.stasyorl.recipeapp.Listeners.CategoryListener;
import com.stasyorl.recipeapp.Listeners.ChangeUser;
import com.stasyorl.recipeapp.Listeners.LoggedOutUser;
import com.stasyorl.recipeapp.Listeners.RandomRecipeResponseListener;
import com.stasyorl.recipeapp.Listeners.RecipeClickListener;
import com.stasyorl.recipeapp.Models.CategoryModel;
import com.stasyorl.recipeapp.Models.RandomRecipeApiResponse;
import com.stasyorl.recipeapp.Models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryListener, ChangeUser {

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
    ImageView favourite_button;
    FrameLayout fragmentContainer;
    LinearLayout mainScreen;

    InternetConnectorReceiver receiver;

    UserRegistrationFragment registrationFragment;
    UserLoginFragment loginFragment;
    FavouritesFragment favouritesFragment;
    ExistingUserFragment existingUserFragment;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference favDatabaseReference, fvrt_listRef;
    Boolean favChecked = false;
    Recipe recipe;
    EmptyFavouriteFragment emptyFavouriteFragment;


    FirebaseUser user;
    String userId;



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
        registrationFragment = new UserRegistrationFragment();
        loginFragment = new UserLoginFragment();
        existingUserFragment = new ExistingUserFragment();
        fragmentContainer = findViewById(R.id.fragmentContainer);
        mainScreen = findViewById(R.id.main_screen);
        imageView_user_pic = findViewById(R.id.imageView_user_pic);
        favouritesFragment = new FavouritesFragment();
        favourite_button = findViewById(R.id.imageView_favourites);
        emptyFavouriteFragment = new EmptyFavouriteFragment();

        favDatabaseReference = FirebaseDatabase.getInstance().getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user==null){
            userId=null;
        }else{
            userId = user.getUid();
        }


        if(userId!=null){
                imageView_user_pic.setOnClickListener(view -> {
//
                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new ExistingUserFragment(userId)).commit();
                    mainScreen.setVisibility(View.GONE);
                    MainActivity.this.onPause();
                    fragmentContainer.setVisibility(View.VISIBLE);


                });

                favourite_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new FavouritesFragment(userId)).commit();
                        mainScreen.setVisibility(View.GONE);
                        MainActivity.this.onPause();
                        fragmentContainer.setVisibility(View.VISIBLE);
                    }
                });
            }else {
            imageView_user_pic.setOnClickListener(view -> {
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, registrationFragment).commit();
                mainScreen.setVisibility(View.GONE);
                fragmentContainer.setVisibility(View.VISIBLE);


            });

            favourite_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, emptyFavouriteFragment).commit();
                    mainScreen.setVisibility(View.GONE);
                    fragmentContainer.setVisibility(View.VISIBLE);
                }
            });
        }

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

        manager = new RequestManager(this);

        categoryArray = MainActivity.this.getResources().getStringArray(R.array.tags);
        recycle_category = findViewById(R.id.category_recycler);
        recycle_category.setHasFixedSize(true);
        recycle_category.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(MainActivity.this, createData(categoryArray),this);
        recycle_category.setAdapter(categoryAdapter);
        onCategoryClicked(0);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "PAUSE", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(this, "RESUME", Toast.LENGTH_SHORT).show();

    }

    private RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            dialog.dismiss();
            recyclerView = findViewById(R.id.recycler_random);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
            randomRecipeAdapter = new RandomRecipeAdapter(MainActivity.this, response.recipes, recipeClickListener, favListener, userId);
            recyclerView.setAdapter(randomRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            no_wifi_image = findViewById(R.id.no_wifi_image);
            no_wifi_image.setVisibility(View.VISIBLE);

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

    private final AddToFavListener favListener = new AddToFavListener() {
        @Override
        public void onButtonClicked(CharSequence title, CharSequence likes, CharSequence serving, CharSequence time, String image, String id) {
            if(userId==null){
                Toast.makeText(MainActivity.this, "Sign up or login first", Toast.LENGTH_SHORT).show();
            }else{
                favDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(id)){
                            Toast.makeText(MainActivity.this, "This is already selected", Toast.LENGTH_SHORT).show();
                        }else{
                            favDatabaseReference.child(userId).child("SavedRecipes").child(id).child("title").setValue(title);
                            favDatabaseReference.child(userId).child("SavedRecipes").child(id).child("likes").setValue(likes);
                            favDatabaseReference.child(userId).child("SavedRecipes").child(id).child("serving").setValue(serving);
                            favDatabaseReference.child(userId).child("SavedRecipes").child(id).child("time").setValue(time);
                            favDatabaseReference.child(userId).child("SavedRecipes").child(id).child("image").setValue(image);
                            favDatabaseReference.child(userId).child("SavedRecipes").child(id).child("id").setValue(id);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        }
    };

    @Override
    public void onCategoryClicked(int position) {
        tags.clear();
        tags.add(categoryArray[position]);
        manager.getRandomRecipes(randomRecipeResponseListener, tags);
        dialog.show();
    }


    @Override
    public void onUserChanged(FirebaseUser mUser, String newUserId) {
        user = mUser;
        userId = newUserId;

        if(userId!=null){
            imageView_user_pic.setOnClickListener(view -> {
//
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new ExistingUserFragment(userId)).commit();
                mainScreen.setVisibility(View.GONE);
                MainActivity.this.onPause();
                fragmentContainer.setVisibility(View.VISIBLE);


            });

            favourite_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new FavouritesFragment(userId)).commit();
                    mainScreen.setVisibility(View.GONE);
                    MainActivity.this.onPause();
                    fragmentContainer.setVisibility(View.VISIBLE);
                }
            });
        }else {
            imageView_user_pic.setOnClickListener(view -> {
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, registrationFragment).commit();
                mainScreen.setVisibility(View.GONE);
                fragmentContainer.setVisibility(View.VISIBLE);


            });

            favourite_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, emptyFavouriteFragment).commit();
                    mainScreen.setVisibility(View.GONE);
                    fragmentContainer.setVisibility(View.VISIBLE);
                }
            });
        }



        randomRecipeResponseListener = new RandomRecipeResponseListener() {
            @Override
            public void didFetch(RandomRecipeApiResponse response, String message) {
                dialog.dismiss();
                recyclerView = findViewById(R.id.recycler_random);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
                randomRecipeAdapter = new RandomRecipeAdapter(MainActivity.this, response.recipes, recipeClickListener, favListener, userId);
                recyclerView.setAdapter(randomRecipeAdapter);
            }

            @Override
            public void didError(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                no_wifi_image = findViewById(R.id.no_wifi_image);
                no_wifi_image.setVisibility(View.VISIBLE);

            }
        };
    }

//    @Override
//    public void onLoggedOut() {
//        user = null;
//        userId = null;
//    }
}
