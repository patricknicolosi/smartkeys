package com.patricknicolosi.smartkeys.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;
import com.google.type.DateTime;

import java.text.DateFormat;

public class SmartKey {
    String smartKeyName;
    String smartKeyStreetAddress;
    String smartKeyCode;
    GeoPoint smartKeyLocation;
    String email;
    Boolean scanneredSmartKeyResponse;
    String scanneredSmartKeyDateTime;

    public SmartKey(String smartKeyName, String smartKeyStreetAddress, String smartKeyCode, GeoPoint smartKeyLocation, String email){
        this.smartKeyName = smartKeyName;
        this.smartKeyStreetAddress = smartKeyStreetAddress;
        this.smartKeyCode = smartKeyCode;
        this.smartKeyLocation = smartKeyLocation;
        this.email = email;
    }

    public SmartKey(String smartKeyName, String smartKeyStreetAddress, String smartKeyCode, GeoPoint smartKeyLocation, String email, Boolean scanneredSmartKeyResponse, String scanneredSmartKeyDateTime){
        this.smartKeyName = smartKeyName;
        this.smartKeyStreetAddress = smartKeyStreetAddress;
        this.smartKeyCode = smartKeyCode;
        this.smartKeyLocation = smartKeyLocation;
        this.email = email;
        this.scanneredSmartKeyResponse = scanneredSmartKeyResponse;
        this.scanneredSmartKeyDateTime = scanneredSmartKeyDateTime;
    }

    public void setSmartKeyName(String smartKeyName){
        this.smartKeyName = smartKeyName;
    }

    public void setSmartKeyStreetAddress(String smartKeyStreetAddress){
        this.smartKeyStreetAddress = smartKeyStreetAddress;
    }

    public void setSmartKeyCode(String smartKeyCode){
        this.smartKeyCode = smartKeyCode;
    }

    public void setSmartKeyLocation(GeoPoint smartKeyLocation){
        this.smartKeyLocation = smartKeyLocation;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setScanneredSmartKeyResponse(Boolean scanneredSmartKeyResponse){
        this.scanneredSmartKeyResponse = scanneredSmartKeyResponse;
    }

    public void setScanneredSmartKeyDateTime(String scanneredSmartKeyDateTime){
        this.scanneredSmartKeyDateTime = scanneredSmartKeyDateTime;
    }

    public String getSmartKeyName(){
        return this.smartKeyName;
    }

    public String getSmartKeyStreetAddress(){
        return this.smartKeyStreetAddress;
    }

    public String getSmartKeyCode(){
        return this.smartKeyCode;
    }

    public GeoPoint getSmartKeyLocation(){
        return this.smartKeyLocation;
    }

    public String getEmail(){
        return this.email;
    }

    public Boolean getScanneredSmartKeyResponse(){
        return this.scanneredSmartKeyResponse;
    }

    public String getScanneredSmartKeyDateTime(){
        return this.scanneredSmartKeyDateTime;
    }

}
