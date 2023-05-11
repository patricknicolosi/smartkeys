package com.patricknicolosi.smartkeys.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.patricknicolosi.smartkeys.R;
import com.patricknicolosi.smartkeys.fragments.LoginFragment;


public class MainActivity extends AppCompatActivity {


    @Override
    public void finish(){
        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //If user logged in navigate to home activity
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        //Set activity in fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        //Render from XML file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        //Load login fragment
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new LoginFragment()).commit();
    }
}
