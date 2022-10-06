package edu.ucsd.cse110.zooseeker_team35.testing_mocks;

import android.location.Location;

//An adapter for location that allows us to create a location object at the provided lat and lng
public class LocationAdapter extends Location {
    double lat;
    double lang;

    public LocationAdapter(String provider, double lat, double lang){
        this(provider);
        this.lat = lat;
        this.lang = lang;
    }

    public LocationAdapter(String provider) {
        super(provider);
    }

    @Override
    public double getLatitude() {
        return lat;
    }

    @Override
    public double getLongitude() {
        return lang;
    }
}
