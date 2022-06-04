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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stasyorl.recipeapp.MainActivity;
import com.stasyorl.recipeapp.R;

import java.util.ArrayList;

public class UserLoginFragment extends Fragment {

    DatabaseReference databaseReference;

    EditText login_email, login_password;

    TextView txt_sign_up;
    ImageView close_btn, imageView_user_pic;
    UserRegistrationFragment registrationFragment;
    Button loginBtn;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    FirebaseAuth mAuth;
    FirebaseUser mUser;




    public ArrayList<String> users = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_user_login, container, false);

        login_email = view.findViewById(R.id.login_email);
        login_password = view.findViewById(R.id.login_password);
        txt_sign_up = view.findViewById(R.id.txt_sign_up);
        close_btn = view.findViewById(R.id.imageView_close);
        registrationFragment = new UserRegistrationFragment();
        loginBtn = view.findViewById(R.id.login_button);
        imageView_user_pic = view.findViewById(R.id.imageView_user_pic);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://recipeapp-3a3e9-default-rtdb.firebaseio.com/");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();




        txt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainer, registrationFragment).commit();
            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeWindow(UserLoginFragment.this);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLogin();

            }
        });

        return view;
    }

    private void startLogin() {
        final String registerEmail = login_email.getText().toString();
        final String loginPassword = login_password.getText().toString();

        if (registerEmail.isEmpty() || loginPassword.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            login_password.setError("Enter proper password");
        } else {

            if (!registerEmail.matches(emailPattern)) {
                login_email.setError("Enter correct email");
                Toast.makeText(getContext(), "Invalid email (check for spaces)", Toast.LENGTH_SHORT).show();
            }else {
                mAuth.signInWithEmailAndPassword(registerEmail, loginPassword).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        mUser = mAuth.getCurrentUser();
                        Toast.makeText(getContext(), "SIGNED IN SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                        ((MainActivity)getActivity()).onUserChanged(mUser, mUser.getUid());
                        closeWindow(UserLoginFragment.this);

                    }else{
                        Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public void closeWindow(Fragment fragment) {
        getParentFragmentManager().beginTransaction().remove(fragment).commit();
        ((MainActivity) getActivity()).getMainScreen().setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).getFragmentContainer().setVisibility(View.GONE);


    }
}
