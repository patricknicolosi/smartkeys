package com.patricknicolosi.smartkeys.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.patricknicolosi.smartkeys.R;

import java.util.Objects;


public class SignupFragment extends Fragment {
    Button doYouHaveAnAccountButton;
    Button signupButton;
    EditText emailSignupEditText;
    EditText passwordSignupEditText;

    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Inflate XML layout
        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);

        //Initialize UI
        signupButton = rootView.findViewById(R.id.signupButton);
        doYouHaveAnAccountButton = rootView.findViewById(R.id.doYouHaveAnAccountButton);
        emailSignupEditText = rootView.findViewById(R.id.emailSignupEditText);
        passwordSignupEditText = rootView.findViewById(R.id.passwordSignupEditText);

        //Initialize firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //Change from Signup to Login fragment
        doYouHaveAnAccountButton.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new LoginFragment()).commit();
        });

        //Create a new user in Firebase
        signupButton.setOnClickListener(v -> {
            if(!emailSignupEditText.getText().toString().matches("") && !passwordSignupEditText.getText().toString().matches("")){
                //Create a user
                firebaseAuth.createUserWithEmailAndPassword(emailSignupEditText.getText().toString(), passwordSignupEditText.getText().toString())
                        .addOnCompleteListener(requireActivity(), task -> {
                            //Successfully registered user
                            if (task.isSuccessful()) {
                                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new LoginFragment()).commit();
                                Toast.makeText(getContext(), "Successfully registered user! Login now", Toast.LENGTH_LONG).show();
                            }
                            //Problems with user creation
                            else {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Signup error")
                                        .setMessage(task.getException().getMessage())
                                        .setPositiveButton("CLOSE", null)
                                        .show();
                            }
                        });
            }
            else{
                Toast.makeText(getContext(), "You must enter your email and password", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}