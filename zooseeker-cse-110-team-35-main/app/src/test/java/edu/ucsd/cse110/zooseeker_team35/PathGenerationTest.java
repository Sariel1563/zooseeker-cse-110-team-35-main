package edu.ucsd.cse110.zooseeker_team35;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.path_finding.IdentifiedWeightedEdge;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooPathFinder;

@RunWith(AndroidJUnit4.class)
public class PathGenerationTest {
    @Before
    public void setup() {
        Map<String, ZooData.VertexInfo> vertexInfo = ZooData.loadVertexInfoJSON(ApplicationProvider.getApplicationContext(), ZooInfoProvider.nodeInfoJSON);
        Map<String, ZooData.EdgeInfo> edgeInfo = ZooData.loadEdgeInfoJSON(ApplicationProvider.getApplicationContext(), ZooInfoProvider.edgeInfoJSON);
        ZooInfoProvider.setIdVertexMap(vertexInfo);
        ZooInfoProvider.setIdEdgeMap(edgeInfo);
    }
    @Test
    public void testPathCreation() {
        Context context = ApplicationProvider.getApplicationContext();

        String start = "entrance_exit_gate";
        String solutionExhibitOne = "flamingo";
        String solutionExhibitTwo = "gorilla";
        String solutionExhibitThree = "hippo";
        String end = "entrance_exit_gate";

        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");

        ZooPathFinder zooPathFinder = new ZooPathFinder(g);


        List<String> targetExhibits = new LinkedList<>();
        targetExhibits.add(solutionExhibitTwo);
        targetExhibits.add(solutionExhibitThree);
        targetExhibits.add(solutionExhibitOne);
        List<GraphPath<String, IdentifiedWeightedEdge>> paths = zooPathFinder.calculatePath(start, end, targetExhibits);
        assertEquals( paths.size(), 4);
        assertEquals( paths.get(0), DijkstraShortestPath.findPathBetween(g, start, solutionExhibitOne));
        assertEquals( paths.get(1), DijkstraShortestPath.findPathBetween(g, solutionExhibitOne, solutionExhibitTwo));
        assertEquals( paths.get(2), DijkstraShortestPath.findPathBetween(g, solutionExhibitTwo, solutionExhibitThree));
        assertEquals( paths.get(3), DijkstraShortestPath.findPathBetween(g, solutionExhibitThree, end));
    }

    @Test
    public void testPathCreationSingleExhibit() {
        Context context = ApplicationProvider.getApplicationContext();

        String start = "entrance_exit_gate";
        String solutionExhibitOne = "crocodile";
        String end = "entrance_exit_gate";

        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");

        ZooPathFinder zooPathFinder = new ZooPathFinder(g);
        List<String> targetExhibits = new LinkedList<>();
        targetExhibits.add(solutionExhibitOne);
        List<GraphPath<String, IdentifiedWeightedEdge>> paths = zooPathFinder.calculatePath(start, end, targetExhibits);
        assertEquals( paths.size(), 2);
        assertEquals( paths.get(0), DijkstraShortestPath.findPathBetween(g, start, solutionExhibitOne));
        assertEquals( paths.get(1), DijkstraShortestPath.findPathBetween(g, solutionExhibitOne, end));
    }


}
