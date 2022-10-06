package edu.ucsd.cse110.zooseeker_team35;

import static org.junit.Assert.assertEquals;
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
import edu.ucsd.cse110.zooseeker_team35.location_tracking.SkipHandler;
import edu.ucsd.cse110.zooseeker_team35.path_finding.IdentifiedWeightedEdge;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooPathFinder;

@RunWith(AndroidJUnit4.class)
public class SkipExhibitTest {
    Graph<String, IdentifiedWeightedEdge> g;
    Map<String, ZooData.VertexInfo> vertexInfo;
    Map<String, ZooData.EdgeInfo> edgeInfo;

    @Before
    public void initialize(){
        //setup exhibits that we will visit
        String exhibit = "hippo";
        String exhibit2 = "flamingo";
        String exhibit3 = "fern_canyon";
        List<String> targetExhibits = new LinkedList<>();
        targetExhibits.add(exhibit);
        targetExhibits.add(exhibit2);
        targetExhibits.add(exhibit3);

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
    public void testSkipOnce(){
        //create a mock directions activitiy that will tell us if anyone tries to update it
        MockDirectionsActivity mockDirections = new MockDirectionsActivity();
        DirectionsActivity testActivity =(DirectionsActivity) mockDirections;
        SkipHandler skipHandler = new SkipHandler(testActivity);

        //mocks location to hippos
        LocationAdapter locationAdapter = new LocationAdapter(LocationManager.GPS_PROVIDER, 32.74531131120979,-117.16626781198586);
        Location mockedLocation = (Location) locationAdapter;

        //skips current exhibit which is flamingo
        skipHandler.skipExhibit(mockedLocation);

        //updateDisplay should have been called
        assertTrue(mockDirections.updateDisplayCalled);

        //the current exhibit should now be hippos
        assertEquals(DirectionTracker.getCurrentExhibitId(), "hippo");
        DirectionTracker.nextExhibit();
        //teh next exhibit should be fern_canyon
        assertEquals(DirectionTracker.getCurrentExhibitId(), "fern_canyon");
    }

    @Test
    public void testSkipAll(){
        //create a mock directions activitiy that will tell us if anyone tries to update it
        MockDirectionsActivity mockDirections = new MockDirectionsActivity();
        DirectionsActivity testActivity =(DirectionsActivity) mockDirections;
        SkipHandler skipHandler = new SkipHandler(testActivity);

        //mocks location to hippos
        LocationAdapter locationAdapter = new LocationAdapter(LocationManager.GPS_PROVIDER, 32.74531131120979,-117.16626781198586);
        Location mockedLocation = (Location) locationAdapter;

        //skip all the exhibits
        skipHandler.skipExhibit(mockedLocation);
        skipHandler.skipExhibit(mockedLocation);
        skipHandler.skipExhibit(mockedLocation);

        //the only exhibit left is the entrance_exit_gate
        assertTrue(mockDirections.updateDisplayCalled);
        assertEquals(DirectionTracker.getCurrentExhibitId(), "entrance_exit_gate");
    }
}
