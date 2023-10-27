package com.example.bulletinboard;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHelper {

    public static LatLng getLatLongFromLocation(Context context, String locationName) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(locationName, 1);
            if (!addressList.isEmpty()) {
                double latitude = addressList.get(0).getLatitude();
                double longitude = addressList.get(0).getLongitude();
                return new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
