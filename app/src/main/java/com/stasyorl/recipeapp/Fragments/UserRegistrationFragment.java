package com.stasyorl.recipeapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stasyorl.recipeapp.Listeners.ChangeUser;
import com.stasyorl.recipeapp.Listeners.OnBackButtonListener;
import com.stasyorl.recipeapp.MainActivity;
import com.stasyorl.recipeapp.R;
import com.stasyorl.recipeapp.UsersList;

public class UserRegistrationFragment extends Fragment{
    EditText register_name, register_email, register_password, register_confirm_password;
    TextView txt_sign_in;
    ImageView close_btn, imageView_user_pic;
    UserLoginFragment loginFragment;
    Button signUpBtn;

    DatabaseReference databaseReference;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    ChangeUser changeUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_registration, container, false);
        register_email = view.findViewById(R.id.register_email);
        register_password = view.findViewById(R.id.register_password);
        register_confirm_password = view.findViewById(R.id.register_confirm_password);
        register_name = view.findViewById(R.id.register_name);
        txt_sign_in = view.findViewById(R.id.txt_sign_in);
        close_btn = view.findViewById(R.id.imageView_close);
        loginFragment = new UserLoginFragment();
        signUpBtn = view.findViewById(R.id.sign_up_button);
        imageView_user_pic = view.findViewById(R.id.imageView_user_pic);



        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipeapp-3a3e9-default-rtdb.firebaseio.com/");


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        signUpBtn.setOnClickListener(view1 -> {
            PerforAuth();


//            boolean userRegistered = true;
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            intent.putExtra("userRegistered", userRegistered);
//            startActivity(intent);

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
                closeWindow(UserRegistrationFragment.this);
            }
        });

        return view;
    }
    public void closeWindow(Fragment fragment){
        getParentFragmentManager().beginTransaction().remove(fragment).commit();
        ((MainActivity) getActivity()).getMainScreen().setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).getFragmentContainer().setVisibility(View.GONE);

    }

    private void PerforAuth(){
        final String userName = register_name.getText().toString();
        final String registerEmail = register_email.getText().toString();
        final String registerPassword = register_password.getText().toString();
        final String confirmPassword = register_confirm_password.getText().toString();

        if(!registerEmail.matches(emailPattern)){
            register_email.setError("Enter correct email");
        } else if(registerEmail.isEmpty() || registerPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            register_password.setError("Enter proper password");
        }else if(!registerPassword.equals(confirmPassword)){
            Toast.makeText(getContext(), "Passwords are not matching", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.createUserWithEmailAndPassword(registerEmail, registerPassword).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    mUser = mAuth.getCurrentUser();
                    databaseReference.child(mUser.getUid()).child("username").setValue(userName);

                    Toast.makeText(getContext(), "SIGNED UP SUCCESSFULLY"+mUser.getUid(), Toast.LENGTH_SHORT).show();
                    ((MainActivity)getActivity()).onUserChanged(mUser, mUser.getUid());
                    closeWindow(UserRegistrationFragment.this);
                }else{
                    Toast.makeText(getContext(), "This email is already registered", Toast.LENGTH_SHORT).show();
                }
            });
        }



    }

}


