package edu.ucsd.cse110.zooseeker_team35;
import static org.junit.Assert.*;

import android.content.Context;

import androidx.activity.ComponentActivity;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Map;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;

import edu.ucsd.cse110.zooseeker_team35.activities.DirectionsActivity;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.FindClosestExhibitHelper;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;

public class ClosestLocationTest {
    private static final double DELTA = 1e-15;
    Location location;

    @Before
    public void createMap() {
        location = new Location(LocationManager.GPS_PROVIDER); // my current location: UCSD
        location.setMock(true);
    }

    @Test
    public void testZooAndAirport() {
        double zooLat = 32.7353;
        double zooLng = -117.1490; // san diego zoo coordinates
        double airportLat = 32.733952;
        double airportLng = -117.193346; // san diego airport coordinates

        double toZoo = FindClosestExhibitHelper.euclideanDistance(location, zooLat, zooLng);
        double toAirport = FindClosestExhibitHelper.euclideanDistance(location, airportLat, airportLng);

        double result;

        if(toZoo < toAirport) { result = toZoo; }
        else { result = toAirport; }

        double truth = Math.sqrt(Math.pow((location.getLatitude() - zooLat), 2)
                + Math.pow((location.getLongitude() - zooLng), 2));

        assertEquals(result, truth, DELTA);
    }
}
