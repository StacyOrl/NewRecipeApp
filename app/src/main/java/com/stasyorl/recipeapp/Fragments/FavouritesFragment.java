package com.stasyorl.recipeapp.Fragments;

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
import com.stasyorl.recipeapp.MainActivity;
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
    ImageView closeButton;
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
        closeButton = view.findViewById(R.id.imageView_close);
        registrationFragment = new UserRegistrationFragment();
        loginFragment = new UserLoginFragment();

        noFavourites = view.findViewById(R.id.no_favourites);
        signOrLogin = view.findViewById(R.id.sign_or_login);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        favouriteRecycler = view.findViewById(R.id.favourites_list);
        favDatabaseReference = database.getReference().child("favourites").child(mUser.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference("SavedRecipes");

        query = FirebaseDatabase.getInstance().getReference().child("favourites").child(mUser.getUid()).child("SavedRecipes");


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeWindow(FavouritesFragment.this);
            }
        });



        favDatabaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild("SavedRecipes")){
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
        favouriteRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<RecipeFromFirebase> options
                = new FirebaseRecyclerOptions.Builder<RecipeFromFirebase>()
                .setQuery(query, RecipeFromFirebase.class)
                .build();
        favouritesAdapter = new FavouritesAdapter(options);
        favouriteRecycler.setAdapter(favouritesAdapter);

        return view;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        favouritesAdapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        favouritesAdapter.stopListening();
    }
    public void closeWindow(Fragment fragment) {
        getParentFragmentManager().beginTransaction().remove(fragment).commit();
        ((MainActivity) getActivity()).getMainScreen().setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).getFragmentContainer().setVisibility(View.GONE);

//        imageView_user_pic.setImageResource(R.drawable.user_profile_pic);


    }



}
