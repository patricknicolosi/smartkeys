package com.patricknicolosi.smartkeys.models;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.patricknicolosi.smartkeys.R;

import java.util.ArrayList;

public class ScanneredSmartKeyArrayAdapter extends ArrayAdapter {
    public ArrayList<SmartKey> values;

    public ScanneredSmartKeyArrayAdapter(Context context, int textViewResourceId, ArrayList<SmartKey> values){
        super(context, textViewResourceId, values);
        this.values = values;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.scannered_smartkey_list_item, null);

        SmartKey smartKey = (SmartKey) getItem(position);

        TextView scanneredSmartKeyNameTextView = convertView.findViewById (R.id.scanneredSmartKeyNameTextView);
        scanneredSmartKeyNameTextView.setText(smartKey.getSmartKeyName());

        TextView scanneredSmartKeyStreetAddressTextView = convertView.findViewById(R.id.scanneredSmartKeyStreetAddressTextView);
        scanneredSmartKeyStreetAddressTextView.setText(smartKey.getSmartKeyStreetAddress());

        ImageView responseIconImageView = convertView.findViewById(R.id.scanneredSmartKeyResponseImageView);
        if(smartKey.getScanneredSmartKeyResponse()){
            responseIconImageView.setImageResource(R.drawable.icon_baseline_check);
        }
        else{
            responseIconImageView.setImageResource(R.drawable.icon_close);
        }

        TextView scanneredSmartKeyDateTime = convertView.findViewById(R.id.scanneredSmartKeyDateTimeTextView);
        scanneredSmartKeyDateTime.setText(smartKey.getScanneredSmartKeyDateTime().toString());

        return convertView;
    }
}
