package com.stasyorl.recipeapp.Fragments;

import android.os.Bundle;
import android.telephony.ims.RegistrationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.internal.zzx;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stasyorl.recipeapp.MainActivity;
import com.stasyorl.recipeapp.R;

public class ExistingUserFragment extends Fragment {
    TextView hiUser;
    Button savedRecipes, logOut;
    ImageView closeButton;
    FirebaseUser mUser;
    DatabaseReference databaseReference;
    ExistingUserFragment existingUserFragment;
    UserRegistrationFragment registrationFragment;
    FavouritesFragment favouritesFragment;
    String currentUserId;

    public ExistingUserFragment() {
    }

    public ExistingUserFragment(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_up_user, container, false);
        hiUser = view.findViewById(R.id.hi_user);
        logOut = view.findViewById(R.id.log_out_btn);
        closeButton = view.findViewById(R.id.imageView_close);
        savedRecipes = view.findViewById(R.id.saved_recipes_btn);
        existingUserFragment = new ExistingUserFragment();
        favouritesFragment = new FavouritesFragment();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        registrationFragment = new UserRegistrationFragment();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeWindow(ExistingUserFragment.this);
            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                ((MainActivity)getActivity()).onUserChanged(mUser, null);
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainer, registrationFragment).commit();
            }
        });

        savedRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FavouritesFragment(currentUserId)).commit();
            }
        });


        return view;
    }
    public void closeWindow(Fragment fragment) {
        getParentFragmentManager().beginTransaction().remove(fragment).commit();
        ((MainActivity) getActivity()).getMainScreen().setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).getFragmentContainer().setVisibility(View.GONE);

    }
}
