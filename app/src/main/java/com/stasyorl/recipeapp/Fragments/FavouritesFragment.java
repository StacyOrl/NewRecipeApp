package com.stasyorl.recipeapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stasyorl.recipeapp.Adapters.FavouritesAdapter;
import com.stasyorl.recipeapp.FavouritesViewHolder;
import com.stasyorl.recipeapp.Listeners.OnDeleteListener;
import com.stasyorl.recipeapp.Listeners.RecipeClickListener;
import com.stasyorl.recipeapp.MainActivity;
import com.stasyorl.recipeapp.Models.Recipe;
import com.stasyorl.recipeapp.Models.RecipeFromFirebase;
import com.stasyorl.recipeapp.R;
import com.stasyorl.recipeapp.RecipeDetailsActivity;
import com.stasyorl.recipeapp.UsersList;

import java.util.ArrayList;

public class FavouritesFragment extends Fragment {

    
    RecyclerView favouriteRecycler;
    FavouritesAdapter favouritesAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference favDatabaseReference;
    LinearLayout noFavourites;
    TextView textExplain, signUp, logIn;
    ImageView closeButton;
    LinearLayout signOrLogin;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    UserLoginFragment loginFragment;
    UserRegistrationFragment registrationFragment;
    OnDeleteListener deleteListener;


//    DatabaseReference databaseReference;
    Query query;

    boolean favChecker = false;
    RecipeFromFirebase recipeModel;
    RecipeClickListener recipeClickListener;
    boolean isDeleted = false;
    boolean closedWindow = false;

    public FavouritesFragment() {
    }

    public FavouritesFragment(OnDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourites_fragment, container, false);


        recipeClickListener = id -> {
            Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
            intent.putExtra("id", id);

            startActivity(intent);
        };

        signUp = view.findViewById(R.id.sign_upText);
        logIn = view.findViewById(R.id.login_text);
        closeButton = view.findViewById(R.id.imageView_close);
        registrationFragment = new UserRegistrationFragment();
        loginFragment = new UserLoginFragment();

        noFavourites = view.findViewById(R.id.no_favourites);
        signOrLogin = view.findViewById(R.id.sign_or_login);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        favouriteRecycler = view.findViewById(R.id.favourites_list);
        favDatabaseReference = database.getReference();
        recipeModel = new RecipeFromFirebase();
        DatabaseReference favReference = FirebaseDatabase.getInstance().getReference();

        String currentUserId = mUser.getUid();


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeWindow(FavouritesFragment.this);
            }
        });
        favDatabaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child(currentUserId).hasChild("SavedRecipes")){
                    favouriteRecycler.setVisibility(View.GONE);
                    noFavourites.setVisibility(View.VISIBLE);
                    textExplain = view.findViewById(R.id.simpleText);
                    signOrLogin.setVisibility(View.INVISIBLE);
                    textExplain.setText("You haven't added your recipes yet");
                }else{
                    noFavourites.setVisibility(View.GONE);
                    favouriteRecycler.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        query = FirebaseDatabase.getInstance().getReference().child(currentUserId).child("SavedRecipes");
        favouriteRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<RecipeFromFirebase> options
                = new FirebaseRecyclerOptions.Builder<RecipeFromFirebase>()
                .setQuery(query, RecipeFromFirebase.class)
                .build();


        FirebaseRecyclerAdapter<RecipeFromFirebase, FavouritesViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<RecipeFromFirebase, FavouritesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position, @NonNull RecipeFromFirebase model) {


                final String postKey = getRef(position).getKey();
                query = FirebaseDatabase.getInstance().getReference().orderByKey();

                holder.setItem(model.getTitle(), model.getLikes(), model.getServing(), model.getTime(), model.getImage());

                String title = getItem(position).getTitle();
                String likes = getItem(position).getLikes();
                String servings = getItem(position).getServing();
                String time = getItem(position).getTime();
                String image = getItem(position).getImage();
                String id = getItem(position).getId();



                holder.favouriteChecker(id, currentUserId);

                holder.fvrt_button.setOnClickListener(view1 -> {
                    favChecker = true;
                    favReference.child(currentUserId).child("SavedRecipes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(favChecker){
                                if(snapshot.hasChild(id)){
                                    delete(postKey, currentUserId);
                                    deleteListener.onDeleteRecipe(true);
                                    favChecker = false;

                                }else{
                                    favReference.child(currentUserId).child("SavedRecipes").child(id).child("title").setValue(title);
                                    favReference.child(currentUserId).child("SavedRecipes").child(id).child("likes").setValue(likes);
                                    favReference.child(currentUserId).child("SavedRecipes").child(id).child("serving").setValue(servings);
                                    favReference.child(currentUserId).child("SavedRecipes").child(id).child("time").setValue(time);
                                    favReference.child(currentUserId).child("SavedRecipes").child(id).child("image").setValue(image);
                                    favReference.child(currentUserId).child("SavedRecipes").child(id).child("id").setValue(id);
                                    favChecker = false;
                                    deleteListener.onDeleteRecipe(false);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                });

                holder.random_list_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recipeClickListener.onRecipeClicked(id);
                    }
                });
            }

            @NonNull
            @Override
            public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_random_recipe, parent, false);
                return new FavouritesViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        favouriteRecycler.setAdapter(firebaseRecyclerAdapter);

        return view;
    }

    void delete(String id, String currentUserId){
        Query favQuery = favDatabaseReference.child(currentUserId).child("SavedRecipes").orderByChild("id").equalTo(id);
        favQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    dataSnapshot.getRef().removeValue();




                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void closeWindow(Fragment fragment) {

        getParentFragmentManager().beginTransaction().remove(fragment).commit();
        ((MainActivity) getActivity()).getMainScreen().setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).getFragmentContainer().setVisibility(View.GONE);

    }

//
//
//    }
}
