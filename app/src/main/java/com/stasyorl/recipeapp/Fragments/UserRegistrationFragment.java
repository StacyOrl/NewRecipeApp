package com.stasyorl.recipeapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.stasyorl.recipeapp.R;

public class UserRegistrationFragment extends Fragment {
    EditText register_email, register_password, register_confirm_password;
    TextView txt_sign_in;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_registration, container, false);
        register_email = view.findViewById(R.id.register_email);
        register_password = view.findViewById(R.id.register_password);
        register_confirm_password = view.findViewById(R.id. register_confirm_password);
        txt_sign_in = view.findViewById(R.id.txt_sign_in);

        txt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return view;
    }


}
