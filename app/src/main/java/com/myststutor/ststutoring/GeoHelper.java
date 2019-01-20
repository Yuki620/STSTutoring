package com.myststutor.ststutoring;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class GeoHelper {

    public static double[] getLocationFromAddress(Context context,  String address) {
        double[] loc = new double[2];
        double longitude = 0;
        double latitude = 0;
        Geocoder coder = new Geocoder(context);
        try {
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName("Irvine, CA", 1);

            if (adresses.size() > 0) {
                longitude = adresses.get(0).getLongitude();
                latitude = adresses.get(0).getLatitude();
                Log.i("TEST", "Loc: " + longitude + ", " + latitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        loc[0] = longitude;
        loc[1] = latitude;
        return loc;
    }

    public static float getDistance(double lon1, double lat1, double lon2, double lat2) {
        Location locationA = new Location("point A");

        locationA.setLatitude(lat1);
        locationA.setLongitude(lon1);

        Location locationB = new Location("point B");

        locationB.setLatitude(lat2);
        locationB.setLongitude(lon2);

        float distance = locationA.distanceTo(locationB);
        return distance;
    }

    
}
