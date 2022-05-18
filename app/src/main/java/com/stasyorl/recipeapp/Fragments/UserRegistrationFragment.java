package com.stasyorl.recipeapp.Fragments;

import android.app.Activity;
import android.icu.text.IDNA;
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
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stasyorl.recipeapp.Listeners.OnBackButtonListener;
import com.stasyorl.recipeapp.MainActivity;
import com.stasyorl.recipeapp.R;

public class UserRegistrationFragment extends Fragment{
    EditText register_email, register_password, register_confirm_password;
    TextView txt_sign_in;
    ImageView close_btn;
    UserLoginFragment loginFragment;
    Button signUpBtn;

    DatabaseReference databaseReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_registration, container, false);
        register_email = view.findViewById(R.id.register_email);
        register_password = view.findViewById(R.id.register_password);
        register_confirm_password = view.findViewById(R.id.register_confirm_password);
        txt_sign_in = view.findViewById(R.id.txt_sign_in);
        close_btn = view.findViewById(R.id.imageView_close);
        loginFragment = new UserLoginFragment();
        signUpBtn = view.findViewById(R.id.sign_up_button);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://find-your-recipe-a8ee1-default-rtdb.europe-west1.firebasedatabase.app");


        signUpBtn.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "fuck yeah", Toast.LENGTH_SHORT).show();

            final String registerEmail = register_email.getText().toString();
            final String registerPassword = register_password.getText().toString();
            final String confirmPassword = register_confirm_password.getText().toString();

            if(registerEmail.isEmpty() || registerPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
            else if(!registerPassword.equals(confirmPassword)){
                Toast.makeText(getContext(), "Passwords are not matching", Toast.LENGTH_SHORT).show();
            }

            else{
                databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(registerEmail)){
                            Toast.makeText(getContext(), "This email is already registered", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            databaseReference.child("users").child("registerEmail").child(registerEmail).setValue(registerEmail);
                            databaseReference.child("users").child("registerEmail").child(registerPassword).setValue(registerPassword);

                            Toast.makeText(getContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().beginTransaction().remove(UserRegistrationFragment.this);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        txt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainer, loginFragment).commit();
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

        final String emailText = register_email.getText().toString();
        final String passwordText = register_password.getText().toString();

        if(emailText.isEmpty() || passwordText.isEmpty()){
            Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }else{

        }

        return view;
    }

//    @Override
//    public boolean onBackPressed() {
//        return false;
//    }
}


