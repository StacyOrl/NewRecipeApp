package com.stasyorl.recipeapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.stasyorl.recipeapp.MainActivity;
import com.stasyorl.recipeapp.R;

public class UserLoginFragment extends Fragment {

    EditText loginEmail, loginPassword;
    TextView txt_sign_up;
    ImageView close_btn;
    UserRegistrationFragment registrationFragment;
    Button loginBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_login, container, false);

        loginEmail = view.findViewById(R.id.login_email);
        loginPassword = view.findViewById(R.id.login_password);
        txt_sign_up = view.findViewById(R.id.txt_sign_up);
        close_btn = view.findViewById(R.id.imageView_close);
        registrationFragment = new UserRegistrationFragment();
        loginBtn = view.findViewById(R.id.login_button);

        txt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainer, registrationFragment).commit();
            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().remove(UserLoginFragment.this).commit();
                ((MainActivity) getActivity()).getMainScreen().setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).getFragmentContainer().setVisibility(View.GONE);


            }
        });



        return view;


    }
}
