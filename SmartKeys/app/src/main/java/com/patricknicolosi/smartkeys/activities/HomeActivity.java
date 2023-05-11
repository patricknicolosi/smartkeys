package com.patricknicolosi.smartkeys.activities;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;


import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.patricknicolosi.smartkeys.R;
import com.patricknicolosi.smartkeys.models.SmartKey;
import com.patricknicolosi.smartkeys.models.SmartKeyArrayAdapter;
import com.patricknicolosi.smartkeys.receivers.MyBroadcastReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class HomeActivity extends AppCompatActivity {
    Button scanSmartKeyButton;
    Button logoutAppButton;
    Button historyButton;
    TextView welcomeMessageTextView;
    TextView addASmartKeyTextView;

    FirebaseFirestore firebaseFirestore;

    ListView smartKeysListView;
    ArrayList<SmartKey> smartKeysList = new ArrayList<>();
    SmartKeyArrayAdapter smartKeyArrayAdapter;

    Location deviceLocation;

    String tokenFromSharedPreferences;


    @Override
    protected void onStart() {
        MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter("com.patricknicolosi.smartkeys.NOTIFICATION_RESPONSE");
        registerReceiver(myBroadcastReceiver, filter);
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"WrongViewCast", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Set activity in fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        //Render XML file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialize UI
        scanSmartKeyButton = findViewById(R.id.scanSmartKeyButton);
        logoutAppButton = findViewById(R.id.logoutAppButton);
        historyButton = findViewById(R.id.historyButton);
        welcomeMessageTextView = findViewById(R.id.welcomeMessageTextView);
        smartKeysListView = findViewById(R.id.smartKeysListView);
        addASmartKeyTextView = findViewById(R.id.addASmartKeyTextView);

        //Render smartKeysList in custom listview
        smartKeyArrayAdapter = new SmartKeyArrayAdapter(this, R.layout.smartkey_list_item, smartKeysList);
        smartKeysListView.setAdapter(smartKeyArrayAdapter);

        //Get extra from intent
        String emailFromExtra = getIntent().getStringExtra("email");
        welcomeMessageTextView.setText("Welcome\n" + emailFromExtra.substring(0, emailFromExtra.indexOf('@')));

        //Token from sharedPreferences
        SharedPreferences preferences = getApplication().getSharedPreferences("smartKeysFirebaseToken", Context.MODE_PRIVATE);
        tokenFromSharedPreferences = preferences.getString("token", "");

        //Get data from firestore
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("smartKeys")
                .whereEqualTo("email", emailFromExtra)
                .addSnapshotListener((value, e) -> {
                    if (e == null) {
                        if (!value.isEmpty()) {
                            scanSmartKeyButton.setVisibility(View.VISIBLE);
                            addASmartKeyTextView.setVisibility(View.INVISIBLE);
                        }
                        for (QueryDocumentSnapshot doc : value) {
                            SmartKey smartKey = new SmartKey(doc.getData().get("smartKeyName").toString(), doc.getData().get("smartKeyStreetAddress").toString(), doc.getData().get("smartKeyCode").toString(), (GeoPoint) doc.getData().get("smartKeyLocation"), doc.getData().get("email").toString());
                            smartKeysList.add(smartKey);
                            smartKeyArrayAdapter.notifyDataSetChanged();
                        }
                    }
                });

        //Navigate to history activity
        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
            intent.putExtra("email", emailFromExtra);
            startActivity(intent);
        });

        scanSmartKeyButton.setOnClickListener(v -> {
            //Check GPS poisition and navigate to qr code scanner activity
            getLocationAndScanQrCode();
        });

        //Logout
        logoutAppButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    //Result from zxing qrcode reader
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scanning was stopped by user", Toast.LENGTH_LONG).show();
            } else {
                for (int i = 0; i < smartKeysList.size(); i++) {
                    Map<String, Object> scanneredSmartKey = new HashMap<>();
                    scanneredSmartKey.put("email", smartKeysList.get(i).getEmail());
                    scanneredSmartKey.put("smartKeyCode", smartKeysList.get(i).getSmartKeyCode());
                    scanneredSmartKey.put("smartKeyStreetAddress", smartKeysList.get(i).getSmartKeyStreetAddress());
                    scanneredSmartKey.put("smartKeyName", smartKeysList.get(i).getSmartKeyName());
                    scanneredSmartKey.put("scanneredSmartKeyDateTime", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime()));
                    if (smartKeysList.get(i).getSmartKeyCode().equals(result.getContents())) {
                        //Get SmartKey location
                        Location smartKeyLocation = new Location("");
                        smartKeyLocation.setLatitude(smartKeysList.get(i).getSmartKeyLocation().getLatitude());
                        smartKeyLocation.setLongitude(smartKeysList.get(i).getSmartKeyLocation().getLongitude());
                        //Open if only the device is near SmartKey location
                        if (deviceLocation.distanceTo(smartKeyLocation) < 50.0) {
                            //Load to Firebase
                            scanneredSmartKey.put("scanneredSmartKeyResponse", true);
                            firebaseFirestore.collection("scanneredSmartKeys").add(scanneredSmartKey);
                            //Send notification
                            sendFirebaseNotification("Opening " + smartKeysList.get(i).getSmartKeyName(), tokenFromSharedPreferences);
                            break;
                        } else {
                            //Load to Firebase
                            scanneredSmartKey.put("scanneredSmartKeyResponse", false);
                            firebaseFirestore.collection("scanneredSmartKeys").add(scanneredSmartKey);
                            //Send push notification to device
                            sendFirebaseNotification(smartKeysList.get(i).getSmartKeyName() + " is too far", tokenFromSharedPreferences);
                        }
                    } else if (i == smartKeysList.size() - 1) {
                        Toast.makeText(this, "This isn't a SmartKey", Toast.LENGTH_LONG).show();
                    }

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Get device location function and navigate to qr code scanner activity
    private void getLocationAndScanQrCode() {
        if (isLocationEnabled()) {
            if(deviceLocation == null) Toast.makeText(getApplicationContext(), "Getting location. Wait...", Toast.LENGTH_SHORT).show();
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            LocationRequest locationRequest = new LocationRequest();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, 1);
            }
            LocationCallback locationCallback = new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        deviceLocation = locationResult.getLastLocation();
                        new IntentIntegrator(HomeActivity.this).setPrompt("").initiateScan();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Failed to get location. Retry...", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
        else{
            new AlertDialog.Builder(this)
                    .setMessage("Enable GPS and Internet connection")
                    .setPositiveButton("Go to settings", (paramDialogInterface, paramInt) -> this.startActivity(new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("CLOSE",null)
                    .show();
        }
    }

    //Check if location is enabled
    boolean isLocationEnabled(){
        LocationManager lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean isLocationEnabled;
        try {
            isLocationEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            return isLocationEnabled;
        } catch(Exception ex) {
            return false;
        }
    }

    void sendFirebaseNotification(String notificationBody, String token){
        String url =  "https://chrome-flawless-roadway.glitch.me/?token=" + token + "&notificationBody=" + notificationBody;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                },
                error -> Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show()){
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}