package edu.ucsd.cse110.zooseeker_team35;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.activities.DirectionsActivity;
import edu.ucsd.cse110.zooseeker_team35.testing_mocks.MockDirectionsActivity;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.DirectionTracker;
import edu.ucsd.cse110.zooseeker_team35.testing_mocks.LocationAdapter;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.RerouteHandler;
import edu.ucsd.cse110.zooseeker_team35.path_finding.IdentifiedWeightedEdge;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooPathFinder;

@RunWith(AndroidJUnit4.class)
public class RerouteTest {
    Graph<String, IdentifiedWeightedEdge> g;
    Map<String, ZooData.VertexInfo> vertexInfo;
    Map<String, ZooData.EdgeInfo> edgeInfo;

    @Before
    public void initialize(){
        //setup target ehxhibits that we want to visit
        String exhibit = "hippo";
        String exhibit2 = "flamingo";
        List<String> targetExhibits = new LinkedList<>();
        targetExhibits.add(exhibit);
        targetExhibits.add(exhibit2);

        //initialzie the state of the app when the app is on the directions screen
        Context context = ApplicationProvider.getApplicationContext();
        g = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");
        vertexInfo = ZooData.loadVertexInfoJSON(context, "sample_node_info.json");
        edgeInfo = ZooData.loadEdgeInfoJSON(context, "sample_edge_info.json");
        ZooInfoProvider.setIdVertexMap(vertexInfo);
        ZooInfoProvider.setIdEdgeMap(edgeInfo);
        ZooPathFinder zooPathFinder = new ZooPathFinder(g);

        List<GraphPath<String, IdentifiedWeightedEdge>> pathList = zooPathFinder.calculatePath("entrance_exit_gate", "entrance_exit_gate", targetExhibits);
        DirectionTracker.initialize(g, pathList);
    }
    @Test
    public void testRerouteNeeded(){
        //create a mock directions activitiy that will tell us if anyone tries to update it
        MockDirectionsActivity mockDirections = new MockDirectionsActivity();
        DirectionsActivity testActivity =(DirectionsActivity) mockDirections;
        RerouteHandler rerouteHandler = new RerouteHandler(testActivity);

        //mocks location to be hippo
        LocationAdapter locationAdapter = new LocationAdapter(LocationManager.GPS_PROVIDER, 32.74531131120979,-117.16626781198586);
        Location mockedLocation = (Location) locationAdapter;

        rerouteHandler.testRerouteNeeded(mockedLocation);

        //updateDisplay should have been called
        assertTrue(mockDirections.updateDisplayCalled);

        //our current exhibit should be hippo
        assertEquals(DirectionTracker.getCurrentExhibitId(), "hippo");
        DirectionTracker.nextExhibit();
        //the next ehxibit shoudl be flamingo
        assertEquals(DirectionTracker.getCurrentExhibitId(), "flamingo");
        DirectionTracker.nextExhibit();
        //the next ehxhibit should be entrance_exit_gate
        assertEquals(DirectionTracker.getCurrentExhibitId(), "entrance_exit_gate");

    }

    @Test
    public void testRerouteNotNeeded(){
        //create a mock directions activitiy that will tell us if anyone tries to update it
        MockDirectionsActivity mockDirections = new MockDirectionsActivity();
        DirectionsActivity testActivity =(DirectionsActivity) mockDirections;
        RerouteHandler rerouteHandler = new RerouteHandler(testActivity);

        //mock location to be entrance
        LocationAdapter locationAdapter = new LocationAdapter(LocationManager.GPS_PROVIDER, 32.73459618734685, -117.14936);
        Location mockedLocation = (Location) locationAdapter;

        rerouteHandler.testRerouteNeeded(mockedLocation);

        assertFalse(mockDirections.updateDisplayCalled);
        //flamingoes shoudl be the starting exhibit
        assertEquals(DirectionTracker.getCurrentExhibitId(), "flamingo");
        DirectionTracker.nextExhibit();
        //the next ehxibit shoudl be hippos
        assertEquals(DirectionTracker.getCurrentExhibitId(), "hippo");
        DirectionTracker.nextExhibit();
        //the next ehxhibit should be entrance_exit_gate
        assertEquals(DirectionTracker.getCurrentExhibitId(), "entrance_exit_gate");

    }
}
