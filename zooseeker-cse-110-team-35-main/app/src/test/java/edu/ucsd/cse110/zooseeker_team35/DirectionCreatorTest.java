package edu.ucsd.cse110.zooseeker_team35;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.content.Context;

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

import edu.ucsd.cse110.zooseeker_team35.direction_display.BriefDirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DetailedDirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.testing_mocks.TestingDirectionFormat;
import edu.ucsd.cse110.zooseeker_team35.path_finding.IdentifiedWeightedEdge;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooPathFinder;

@RunWith(AndroidJUnit4.class)
public class DirectionCreatorTest {
    Graph<String, IdentifiedWeightedEdge> g;
    Map<String, ZooData.VertexInfo> vertexInfo;
    Map<String, ZooData.EdgeInfo> edgeInfo;
    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        g = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");
        vertexInfo = ZooData.loadVertexInfoJSON(context, "sample_node_info.json");
        edgeInfo = ZooData.loadEdgeInfoJSON(context, "sample_edge_info.json");
        ZooInfoProvider.setIdVertexMap(vertexInfo);
        ZooInfoProvider.setIdEdgeMap(edgeInfo);
    }

    @Test
    public void checkBriefDirectionCreator() {
        String start = "entrance_exit_gate";
        String exhibit = "hippo";
        String end = "entrance_exit_gate";
        List<String> targetExhibits = new LinkedList<>();
        targetExhibits.add(exhibit);

        ZooPathFinder zooPathFinder = new ZooPathFinder(g);
        List<GraphPath<String, IdentifiedWeightedEdge>> paths = zooPathFinder.calculatePath(start, end, targetExhibits);

        DirectionCreator dc = new BriefDirectionCreator();

        List<String> briefDirections;

        List<String> solutions = new ArrayList<>();
        solutions.add("Entrance and Exit Gate->Front Street / Treetops Way:Gate Path");
        solutions.add("Front Street / Treetops Way->Treetops Way / Hippo Trail:Treetops Way");
        solutions.add("Treetops Way / Hippo Trail->Hippos:Hippo Trail");
        briefDirections = dc.createDirections(new TestingDirectionFormat(),paths.get(0),vertexInfo,edgeInfo,g);
        for(int j = 0; j < briefDirections.size(); j++) {
            assertEquals(briefDirections.get(0), solutions.get(0));
        }

    }

    @Test
    public void checkDetailedDirectionCreator() {
        String start = "entrance_exit_gate";
        String exhibit = "hippo";
        String end = "entrance_exit_gate";
        List<String> targetExhibits = new LinkedList<>();
        targetExhibits.add(exhibit);

        ZooPathFinder zooPathFinder = new ZooPathFinder(g);
        List<GraphPath<String, IdentifiedWeightedEdge>> paths = zooPathFinder.calculatePath(start, end, targetExhibits);

        DirectionCreator dc = new DetailedDirectionCreator();

        List<String> briefDirections;

        List<String> solutions = new ArrayList<>();
        solutions.add("Entrance and Exit Gate->Front Street / Treetops Way:Gate Path");
        solutions.add("Front Street / Treetops Way->Treetops Way / Fern Canyon Trail:Treetops Way");
        solutions.add("Treetops Way / Fern Canyon Trail->Treetops Way / Orangutan Trail:Treetops Way");
        solutions.add("Treetops Way / Orangutan Trail->Treetops Way / Hippo Trail:Treetops Way");
        solutions.add("Treetops Way / Hippo Trail->Hippos:Hippo Trail");
        briefDirections = dc.createDirections(new TestingDirectionFormat(),paths.get(0),vertexInfo,edgeInfo,g);
        for(int j = 0; j < briefDirections.size(); j++) {
            assertEquals(briefDirections.get(0), solutions.get(0));
        }
    }
}
