package edu.ucsd.cse110.zooseeker_team35.path_finding;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.List;

public class ZooPathFinder {
    Graph<String, IdentifiedWeightedEdge> zooGraph;
    PathAlgorithmStrategy pathAlgorithm;

    public Graph<String, IdentifiedWeightedEdge> getZooGraph() {
        return zooGraph;
    }

    public ZooPathFinder(Graph<String, IdentifiedWeightedEdge> zooGraph) {
        pathAlgorithm = new ShortestPathTSAlgorithm();
        this.zooGraph = zooGraph;
    }

    public ZooPathFinder(Graph<String, IdentifiedWeightedEdge> zooGraph, PathAlgorithmStrategy pathAlgorithm){
        this.pathAlgorithm = pathAlgorithm;
        this.zooGraph = zooGraph;
    }

    public List<GraphPath<String, IdentifiedWeightedEdge>> calculatePath(String start, String end, List<String> selectedExhibits) {
        return pathAlgorithm.findShortestWeightedPath(zooGraph, start, end, selectedExhibits);
    }
}
