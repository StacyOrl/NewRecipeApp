package com.stasyorl.recipeapp.Fragments;

import android.app.Activity;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.stasyorl.recipeapp.Listeners.OnBackButtonListener;
import com.stasyorl.recipeapp.MainActivity;
import com.stasyorl.recipeapp.R;

public class UserRegistrationFragment extends Fragment{
    EditText register_email, register_password, register_confirm_password;
    TextView txt_sign_in;
    ImageView close_btn;
    UserLoginFragment loginFragment;
    boolean isPressed = false;

    public boolean isPressed() {
        return isPressed;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_registration, container, false);
        register_email = view.findViewById(R.id.register_email);
        register_password = view.findViewById(R.id.register_password);
        register_confirm_password = view.findViewById(R.id.register_confirm_password);
        txt_sign_in = view.findViewById(R.id.txt_sign_in);
        close_btn = view.findViewById(R.id.imageView_close);



        txt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "YAAYY", Toast.LENGTH_SHORT).show();
                isPressed = true;
            }
        });


        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "yaayy", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().beginTransaction().remove(UserRegistrationFragment.this).commit();
                ((MainActivity) getActivity()).getMainScreen().setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).getFragmentContainer().setVisibility(View.GONE);


            }
        });

        return view;
    }

//    @Override
//    public boolean onBackPressed() {
//        return false;
//    }
}


