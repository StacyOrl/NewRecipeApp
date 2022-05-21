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
import com.google.firebase.FirebaseOptions;
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
import com.stasyorl.recipeapp.MainActivity;
import com.stasyorl.recipeapp.Models.Recipe;
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
    ArrayList<Long> favourites = new ArrayList<>();
    long favouriteRecipe;


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

        databaseReference = FirebaseDatabase.getInstance().getReference("favourites");



        favouriteRecycler = view.findViewById(R.id.favourites_list);
        favDatabaseReference = database.getReference().child("favourites");

        favouriteRecycler.setLayoutManager(
                new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Recipe> options
                = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(favDatabaseReference, Recipe.class)
                .build();
        favouritesAdapter = new FavouritesAdapter(options);
        favouriteRecycler.setAdapter(favouritesAdapter);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favouriteRecipe = (long) snapshot.child(mUser.getUid()).child("favourites").getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        if(noUser()){
                    noFavourites.setVisibility(View.VISIBLE);
                    favouriteRecycler.setVisibility(View.GONE);

                    signUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainer, registrationFragment).commit();
                        }
                    });
                    logIn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainer, loginFragment).commit();
                        }
                    });
        }else if(favouriteRecipe==0){
                    favouriteRecycler.setVisibility(View.GONE);
                    noFavourites.setVisibility(View.VISIBLE);
                    textExplain = view.findViewById(R.id.simpleText);
                    signOrLogin.setVisibility(View.INVISIBLE);
                    textExplain.setText("You haven't added your recipes yet");
        }else if(favouriteRecipe>=1){
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild("favourites")){
                        favouriteRecycler.setVisibility(View.VISIBLE);
                        noFavourites.setVisibility(View.GONE);
                        favouritesAdapter = new FavouritesAdapter(options);
                        favouriteRecycler.setAdapter(favouritesAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();

                }
            });

        }
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

    public boolean noUser(){
        if(!UsersList.users.contains(mUser.getUid())){
            return true;
        }
        else{
            return false;
        }
    }
}
