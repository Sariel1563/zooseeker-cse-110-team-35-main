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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.location_tracking.DirectionTracker;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.FindClosestExhibitHelper;
import edu.ucsd.cse110.zooseeker_team35.testing_mocks.LocationAdapter;
import edu.ucsd.cse110.zooseeker_team35.path_finding.IdentifiedWeightedEdge;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooPathFinder;
import edu.ucsd.cse110.zooseeker_team35.testing_mocks.TestDirectionCreator;


@RunWith(AndroidJUnit4.class)
public class LiveDirectionsTest {
    Graph<String, IdentifiedWeightedEdge> g;
    Map<String, ZooData.VertexInfo> vertexInfo;
    Map<String, ZooData.EdgeInfo> edgeInfo;
    List<String> targetExhibits;
    @Before
    public void initialize(){
        //exhibit plan
        String exhibit = "hippo";
        targetExhibits = new LinkedList<>();
        targetExhibits.add(exhibit);

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
    public void testLiveDirections(){
        //mock our location to be flamingos
        LocationAdapter locationAdapter = new LocationAdapter(LocationManager.GPS_PROVIDER, 32.7440416465169, -117.15952052282296);
        Location mockedFlamingoLocation = (Location) locationAdapter;

        ZooData.VertexInfo closestVertex = FindClosestExhibitHelper.closestExhibit(mockedFlamingoLocation);

        List<String> directions = DirectionTracker.getDirectionsToCurrentExhibit(new TestDirectionCreator(), closestVertex);

        //directions should start from flamingoes to hippos
        List<String> solutions = new ArrayList<>();
        solutions.add("Flamingos->Monkey Trail / Hippo Trail:Monkey Trail");
        solutions.add("Monkey Trail / Hippo Trail->Hippos:Hippo Trail");

        assertEquals(directions, solutions);
    }

}
