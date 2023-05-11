package com.patricknicolosi.smartkeys.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.patricknicolosi.smartkeys.R;
import com.patricknicolosi.smartkeys.models.ScanneredSmartKeyArrayAdapter;
import com.patricknicolosi.smartkeys.models.SmartKey;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    TextView pageNameTextView;
    TextView noScanneredSmartKeyTextView;

    FirebaseFirestore firebaseFirestore;

    ListView scanneredSmartKeysListView;
    ArrayList<SmartKey> scanneredSmartKeysList = new ArrayList<>();
    ScanneredSmartKeyArrayAdapter scanneredSmartKeyArrayAdapter;

    String emailFromExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Set activity in fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        //Render XML file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Initialize UI
        scanneredSmartKeysListView = findViewById(R.id.scanneredSmartKeysListView);
        noScanneredSmartKeyTextView = findViewById(R.id.noScanneredSmartKeyTextView);
        pageNameTextView = findViewById(R.id.pageNameTextView);
        pageNameTextView.setText("History");


        //Render smartKeysList in custom listview
        scanneredSmartKeyArrayAdapter = new ScanneredSmartKeyArrayAdapter(this, R.layout.scannered_smartkey_list_item, scanneredSmartKeysList);
        scanneredSmartKeysListView.setAdapter(scanneredSmartKeyArrayAdapter);

        //Get extra from intent
        emailFromExtra =  getIntent().getStringExtra("email");

        //Get data from firestore
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("scanneredSmartKeys")
                .whereEqualTo("email", emailFromExtra)
                .orderBy("scanneredSmartKeyDateTime", Query.Direction.DESCENDING)
                .addSnapshotListener((value, e) -> {
                    if (e == null) {
                        if(!value.isEmpty()){
                            noScanneredSmartKeyTextView.setVisibility(View.INVISIBLE);
                        }
                        for (QueryDocumentSnapshot doc : value) {
                            SmartKey smartKey = new SmartKey(doc.getData().get("smartKeyName").toString(), doc.getData().get("smartKeyStreetAddress").toString(), doc.getData().get("smartKeyCode").toString(),(GeoPoint) doc.getData().get("smartKeyLocation"), doc.getData().get("email").toString(), (Boolean) doc.getData().get("scanneredSmartKeyResponse"), doc.getData().get("scanneredSmartKeyDateTime").toString());
                            scanneredSmartKeysList.add(smartKey);
                            scanneredSmartKeyArrayAdapter.notifyDataSetChanged();
                        }
                    }
                });


    }
}