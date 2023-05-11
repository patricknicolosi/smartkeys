package com.patricknicolosi.smartkeys.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.patricknicolosi.smartkeys.activities.HomeActivity;
import com.patricknicolosi.smartkeys.activities.MainActivity;

import java.util.Objects;

public class LoginFragment extends Fragment {
    Button loginButton;
    Button dontHaveAnAccountButton;
    EditText emailLoginEditText;
    EditText passwordLoginEditText;

    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Inflate XML layout
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        //Initialize UI
        loginButton =  (Button) rootView.findViewById(R.id.loginButton);
        dontHaveAnAccountButton = (Button) rootView.findViewById(R.id.dontHaveAnAccountButton);
        emailLoginEditText = rootView.findViewById(R.id.emailLoginEditText);
        passwordLoginEditText = rootView.findViewById(R.id.passwordLoginEditText);

        //Initialize firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //Login user and navigate to HomeScreen
        loginButton.setOnClickListener(v -> {
            if(!emailLoginEditText.getText().toString().matches("") && !passwordLoginEditText.getText().toString().matches("")){
                //Show a loading until
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.show(getContext(), "","Logging. Please wait...", true);
                //Sign in
                firebaseAuth.signInWithEmailAndPassword(emailLoginEditText.getText().toString(), passwordLoginEditText.getText().toString())
                        .addOnCompleteListener(requireActivity(), task -> {
                            if (task.isSuccessful()) {
                                //Dismiss progress dialog
                                progressDialog.dismiss();
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent = new Intent(getContext(), HomeActivity.class);
                                intent.putExtra("email", firebaseAuth.getCurrentUser().getEmail());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                progressDialog.dismiss();
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Login error")
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

        //Change fragment from Login to Signup fragment
        dontHaveAnAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new SignupFragment()).commit();
            }
        });

        return rootView;
    }
}