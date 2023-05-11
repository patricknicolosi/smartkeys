package com.patricknicolosi.smartkeys.models;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.patricknicolosi.smartkeys.R;

import java.util.ArrayList;

public class SmartKeyArrayAdapter extends ArrayAdapter {
    public ArrayList<SmartKey> values;

    public SmartKeyArrayAdapter(Context context, int textViewResourceId, ArrayList<SmartKey> values){
        super(context, textViewResourceId, values);
        this.values = values;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.smartkey_list_item, null);

        SmartKey smartKey = (SmartKey) getItem(position);

        TextView smartKeyNameTextView = convertView.findViewById (R.id.smartKeyNameTextView);
        smartKeyNameTextView.setText(smartKey.getSmartKeyName());

        TextView smartKeyStreetAddressTextView = convertView.findViewById(R.id.smartKeyStreetAddressTextView);
        smartKeyStreetAddressTextView.setText(smartKey.getSmartKeyStreetAddress());

        convertView.findViewById(R.id.viewSmartKeyInMapsButton);
        AppCompatImageButton viewSmartKeyInMapsButton = convertView.findViewById(R.id.viewSmartKeyInMapsButton);

        viewSmartKeyInMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri googleMapsUri = Uri.parse("google.streetview:cbll=" + smartKey.getSmartKeyLocation().getLatitude() + "," + smartKey.getSmartKeyLocation().getLongitude());
                Intent intent = new Intent(Intent.ACTION_VIEW, googleMapsUri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(getContext() ,intent, null);
            }
        });

        return convertView;
    }
}
