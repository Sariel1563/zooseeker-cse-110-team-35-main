package edu.ucsd.cse110.zooseeker_team35.testing_mocks;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.direction_display.BriefDirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionFormatStrategy;
import edu.ucsd.cse110.zooseeker_team35.path_finding.IdentifiedWeightedEdge;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;

//Creates directions from a GraphPath and displays them in a format that is easier to test
public class TestDirectionCreator implements DirectionCreator {

    @Override
    public List<String> createDirections(GraphPath<String, IdentifiedWeightedEdge> path, Map<String, ZooData.VertexInfo> vertexInfo, Map<String, ZooData.EdgeInfo> edgeInfo, Graph<String, IdentifiedWeightedEdge> graph) {
        return new BriefDirectionCreator().createDirections(new TestingDirectionFormat(), path, vertexInfo, edgeInfo, graph);
    }

    @Override
    public List<String> createDirections(DirectionFormatStrategy testingDirectionFormat, GraphPath<String, IdentifiedWeightedEdge> graph, Map<String, ZooData.VertexInfo> vertexInfo, Map<String, ZooData.EdgeInfo> edgeInfo, Graph<String, IdentifiedWeightedEdge> g) {
        return new BriefDirectionCreator().createDirections(testingDirectionFormat, graph, vertexInfo, edgeInfo, g);
    }
}
