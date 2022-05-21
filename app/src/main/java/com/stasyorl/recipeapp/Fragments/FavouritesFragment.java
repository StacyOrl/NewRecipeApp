package com.stasyorl.recipeapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.stasyorl.recipeapp.Adapters.FavouritesAdapter;
import com.stasyorl.recipeapp.Models.Recipe;
import com.stasyorl.recipeapp.R;

public class FavouritesFragment extends Fragment {
    RecyclerView favouriteRecycler;
    FavouritesAdapter favouritesAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference favDatabaseReference;
    LinearLayout noFavourites;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourites_fragment, container, false);

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

        if(favouritesAdapter.getSnapshots().size() == 0){
            noFavourites = view.findViewById(R.id.no_favourites);
            noFavourites.setVisibility(View.VISIBLE);
            favouriteRecycler.setVisibility(View.GONE);

        }
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
