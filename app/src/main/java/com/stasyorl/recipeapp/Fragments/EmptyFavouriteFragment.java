package com.stasyorl.recipeapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.stasyorl.recipeapp.MainActivity;
import com.stasyorl.recipeapp.R;

public class EmptyFavouriteFragment extends Fragment {
    UserLoginFragment loginFragment;
    UserRegistrationFragment registrationFragment;
    TextView signUp, logIn;
    ImageView closeBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.no_favourites_fragment, container, false);
        signUp = view.findViewById(R.id.sign_upText);
        logIn = view.findViewById(R.id.login_text);
        closeBtn = view.findViewById(R.id.imageView_close);
        registrationFragment = new UserRegistrationFragment();
        loginFragment = new UserLoginFragment();

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
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeWindow(EmptyFavouriteFragment.this);
            }
        });

        return view;
    }
    public void closeWindow(Fragment fragment) {
        getParentFragmentManager().beginTransaction().remove(fragment).commit();
        ((MainActivity) getActivity()).getMainScreen().setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).getFragmentContainer().setVisibility(View.GONE);

//        imageView_user_pic.setImageResource(R.drawable.user_profile_pic);


    }
}
