package com.stasyorl.recipeapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.stasyorl.recipeapp.Adapters.FavouritesAdapter;
import com.stasyorl.recipeapp.Models.Recipe;
import com.stasyorl.recipeapp.Models.RecipeFromFirebase;
import com.stasyorl.recipeapp.R;
import com.stasyorl.recipeapp.UsersList;

import java.util.ArrayList;

public class FavouritesFragment extends Fragment {
    RecyclerView favouriteRecycler;
    FavouritesAdapter favouritesAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference favDatabaseReference;
    LinearLayout noFavourites;
    TextView textExplain, signUp, logIn;
    LinearLayout signOrLogin;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    UserLoginFragment loginFragment;
    UserRegistrationFragment registrationFragment;


    DatabaseReference databaseReference;
    ArrayList<Object> favourites = new ArrayList<Object>();
    int favouriteRecipe;
    Query query;

    int totalSize;


    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourites_fragment, container, false);

        signUp = view.findViewById(R.id.sign_upText);
        logIn = view.findViewById(R.id.login_text);
        registrationFragment = new UserRegistrationFragment();
        loginFragment = new UserLoginFragment();

        noFavourites = view.findViewById(R.id.no_favourites);
        signOrLogin = view.findViewById(R.id.sign_or_login);

//        mAuth = FirebaseAuth.getInstance();
//        mUser = mAuth.getCurrentUser();
//        String userId = mUser.getUid();
        favouriteRecycler = view.findViewById(R.id.favourites_list);
        favDatabaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("SavedRecipes");

        query = FirebaseDatabase.getInstance().getReference().child("SavedRecipes");






        favDatabaseReference.child("SavedRecipes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if((int)snapshot.getChildrenCount()==0){

                    favouriteRecycler.setVisibility(View.GONE);
                    noFavourites.setVisibility(View.VISIBLE);
                    textExplain = view.findViewById(R.id.simpleText);
                    signOrLogin.setVisibility(View.INVISIBLE);
                    textExplain.setText("You haven't added your recipes yet");
                }
//                else{
//                    favDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                            if(snapshot.hasChild("Saved recipes")){
//                            favouriteRecycler.setVisibility(View.VISIBLE);
//                            noFavourites.setVisibility(View.GONE);
//                            favouritesAdapter = new FavouritesAdapter(options);
//                            favouriteRecycler.setAdapter(favouritesAdapter);
//
//                            onStart();
//                    }
//
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//
//                }
//                favouriteRecipe = (int) snapshot.getChildrenCount();
//                Toast.makeText(getContext(), String.valueOf(favouriteRecipe), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        noFavourites.setVisibility(View.GONE);
        favouriteRecycler.setVisibility(View.VISIBLE);
        favouriteRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<RecipeFromFirebase> options
                = new FirebaseRecyclerOptions.Builder<RecipeFromFirebase>()
                .setQuery(query, RecipeFromFirebase.class)
                .build();

        favouritesAdapter = new FavouritesAdapter(options);
        favouriteRecycler.setAdapter(favouritesAdapter);


//        favDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                favouriteRecipe = (long) snapshot.child(mUser.getUid()).child("Saved recipes").getChildrenCount();
//                favouriteRecipe = (int) snapshot.getChildrenCount();
//
//                Toast.makeText(getContext(), String.valueOf(snapshot.child(mUser.getUid()).getValue()), Toast.LENGTH_LONG).show();
//                }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(!snapshot.hasChild(mUser.getUid())){
//                    noFavourites = view.findViewById(R.id.no_favourites);
//                    noFavourites.setVisibility(View.VISIBLE);
//                    favouriteRecycler.setVisibility(View.GONE);
//
//                    signUp.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Toast.makeText(getContext(), "YAYAY", Toast.LENGTH_SHORT).show();
//                            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainer, registrationFragment).commit();
//                        }
//                    });
//                    logIn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainer, loginFragment).commit();
//                        }
//                    });
//
//                }else if(favouritesAdapter.getSnapshots().size() == 0){
//                    noFavourites = view.findViewById(R.id.no_favourites);
//                    noFavourites.setVisibility(View.VISIBLE);
//                    textExplain = view.findViewById(R.id.simpleText);
//                    textExplain.setText("You haven't added your recipes yet");
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        return view;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        favouritesAdapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override
    public void onStop()
    {
        super.onStop();
        favouritesAdapter.stopListening();
    }


}
