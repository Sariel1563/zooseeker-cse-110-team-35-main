package edu.ucsd.cse110.zooseeker_team35;

import static org.junit.Assert.assertEquals;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionFormatStrategy;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.DirectionTracker;
import edu.ucsd.cse110.zooseeker_team35.path_finding.IdentifiedWeightedEdge;
import edu.ucsd.cse110.zooseeker_team35.direction_display.ProceedDirectionFormat;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooPathFinder;

@RunWith(AndroidJUnit4.class)
public class DirectionTrackerTest {
    ZooPathFinder zooPathFinder;

    @Before
    public void setDirectionTrackerInfo() {
        Graph<String, IdentifiedWeightedEdge> zooGraph = ZooData.loadZooGraphJSON(ApplicationProvider.getApplicationContext(), "sample_zoo_graph2.json");
        zooPathFinder = new ZooPathFinder(zooGraph);
        Map<String, ZooData.VertexInfo> vertexInfo = ZooData.loadVertexInfoJSON(ApplicationProvider.getApplicationContext(), "sample_node_info2.json");
        Map<String, ZooData.EdgeInfo> edgeInfo = ZooData.loadEdgeInfoJSON(ApplicationProvider.getApplicationContext(), "sample_edge_info2.json");
        ZooInfoProvider.setIdVertexMap(vertexInfo);
        ZooInfoProvider.setIdEdgeMap(edgeInfo);

    }

    @Test
    public void testGetCurrentExhibit() {
        List<String> targetExhibits = new ArrayList<>();
        targetExhibits.add("gorillas");
        List<GraphPath<String, IdentifiedWeightedEdge>> pathList = zooPathFinder.calculatePath("entrance_exit_gate", "entrance_exit_gate", targetExhibits);
        DirectionTracker.initialize(zooPathFinder.getZooGraph(), pathList);
        assertEquals(DirectionTracker.getCurrentExhibit(), "Gorillas");
    }

    @Test
    public void testGetDirectionsToExhibit() {
        List<String> targetExhibits = new ArrayList<>();
        targetExhibits.add("gorillas");
        List<GraphPath<String, IdentifiedWeightedEdge>> pathList = zooPathFinder.calculatePath("entrance_exit_gate", "entrance_exit_gate", targetExhibits);
        DirectionTracker.initialize(zooPathFinder.getZooGraph(), pathList);
        List<String> expectedDirections = new ArrayList<String>();
        DirectionFormatStrategy formatStrategy = new ProceedDirectionFormat();
        expectedDirections.add(formatStrategy.buildDirection(1, "Entrance and Exit Gate", "Entrance Plaza", "Entrance Way", 10 ));
        expectedDirections.add(formatStrategy.buildDirection(2, "Entrance Plaza", "Gorillas", "Africa Rocks Street", 200 ));
        assertEquals(DirectionTracker.getDirectionsToCurrentExhibit(), expectedDirections);
    }

    @Test
    public void testNextExhibit() {
        List<String> targetExhibits = new ArrayList<>();
        targetExhibits.add("gorillas");
        targetExhibits.add("gators");
        List<GraphPath<String, IdentifiedWeightedEdge>> pathList = zooPathFinder.calculatePath("entrance_exit_gate", "entrance_exit_gate", targetExhibits);
        DirectionTracker.initialize(zooPathFinder.getZooGraph(), pathList);
        DirectionTracker.nextExhibit();
        assertEquals(DirectionTracker.getCurrentExhibit(), "Gorillas");
        DirectionTracker.nextExhibit();
        assertEquals(DirectionTracker.getCurrentExhibit(), "Entrance and Exit Gate");
        DirectionTracker.nextExhibit();
        assertEquals(DirectionTracker.getCurrentExhibit(), "Entrance and Exit Gate");
    }

    @Test
    public void testPrevExhibit() {
        List<String> targetExhibits = new ArrayList<>();
        targetExhibits.add("gorillas");
        targetExhibits.add("gators");
        List<GraphPath<String, IdentifiedWeightedEdge>> pathList = zooPathFinder.calculatePath("entrance_exit_gate", "entrance_exit_gate", targetExhibits);
        DirectionTracker.initialize(zooPathFinder.getZooGraph(), pathList);
        DirectionTracker.setCurrentExhibit(1);
        DirectionTracker.prevExhibit();
        assertEquals(DirectionTracker.getCurrentExhibit(), "Alligators");
        DirectionTracker.prevExhibit();
        assertEquals(DirectionTracker.getCurrentExhibit(), "Alligators");
    }


}
