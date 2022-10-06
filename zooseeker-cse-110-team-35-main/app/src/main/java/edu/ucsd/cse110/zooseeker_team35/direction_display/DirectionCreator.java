package edu.ucsd.cse110.zooseeker_team35.direction_display;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.path_finding.IdentifiedWeightedEdge;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;

public interface DirectionCreator {
    List<String> createDirections(GraphPath<String, IdentifiedWeightedEdge> path, Map<String, ZooData.VertexInfo> vertexInfo, Map<String, ZooData.EdgeInfo> edgeInfo, Graph<String, IdentifiedWeightedEdge> graph);

    List<String> createDirections(DirectionFormatStrategy directionFormatStrategy, GraphPath<String, IdentifiedWeightedEdge> graph, Map<String, ZooData.VertexInfo> vertexInfo, Map<String, ZooData.EdgeInfo> edgeInfo, Graph<String, IdentifiedWeightedEdge> g);
}
