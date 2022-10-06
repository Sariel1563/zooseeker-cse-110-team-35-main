package edu.ucsd.cse110.zooseeker_team35.path_finding;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.List;

import edu.ucsd.cse110.zooseeker_team35.path_finding.IdentifiedWeightedEdge;

// interface for algorithms that will solves the modifed Traveling Salesman Problem
public interface PathAlgorithmStrategy {
    List<GraphPath<String, IdentifiedWeightedEdge>> findShortestWeightedPath(Graph<String, IdentifiedWeightedEdge> zooGraph, String start, String end, List<String> selectedExhibits);
}
