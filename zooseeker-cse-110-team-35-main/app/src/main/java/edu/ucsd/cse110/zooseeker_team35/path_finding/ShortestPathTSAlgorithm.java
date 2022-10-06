package edu.ucsd.cse110.zooseeker_team35.path_finding;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.traverse.ClosestFirstIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
Solves the Traveling Salesman problem by repeatedly finding the closeset next unvisited node
*/
public class ShortestPathTSAlgorithm implements PathAlgorithmStrategy {
    @Override
    public List<GraphPath<String, IdentifiedWeightedEdge>> findShortestWeightedPath(Graph<String, IdentifiedWeightedEdge> zooGraph, String start, String end, List<String> selectedExhibits) {
        List<GraphPath<String, IdentifiedWeightedEdge>> pathList = new ArrayList<GraphPath<String, IdentifiedWeightedEdge>>();

        Map<String, List<String>> parentChildMap = new HashMap<>();

        Set<String> targetExhibits = new HashSet<String>();
        for (String exhibitId : selectedExhibits){
            ZooData.VertexInfo vertex = ZooInfoProvider.getVertexWithId(exhibitId);
            if (vertex.group_id != null){
                if (parentChildMap.containsKey(vertex.group_id)){
                    parentChildMap.get(vertex.group_id).add(exhibitId);
                } else {
                    List<String> newList = new ArrayList<>();
                    newList.add(exhibitId);
                    parentChildMap.put(vertex.group_id, newList);
                }
                if (!vertex.group_id.equals(start)) {
                    targetExhibits.add(vertex.group_id);
                }
            } else {
                targetExhibits.add(exhibitId);
            }
        }

        String pathStart = start;
        if (parentChildMap.containsKey(start)){
            for (String childId : parentChildMap.get(start)){
                pathList.add(new SelfPathAdapter<>(childId));
            }
        }
        while (!targetExhibits.isEmpty()) {
            ClosestFirstIterator graphIterator = new ClosestFirstIterator(zooGraph, pathStart);
            graphIterator.next();
            Object closestExhibit = graphIterator.next();
            while (!targetExhibits.contains(closestExhibit.toString())) {
                closestExhibit = graphIterator.next();
            }
            String closestTargetExhibit = closestExhibit.toString();
            targetExhibits.remove(closestTargetExhibit);
            GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(zooGraph, pathStart, closestTargetExhibit);
            pathList.add(path);
            if (parentChildMap.containsKey(closestTargetExhibit)){
                for (String childId : parentChildMap.get(closestTargetExhibit)){
                    pathList.add(new SelfPathAdapter<>(childId));
                }
            }

            pathStart = closestTargetExhibit;
        }
        GraphPath<String, IdentifiedWeightedEdge> exitPath = DijkstraShortestPath.findPathBetween(zooGraph, pathStart, end);
        pathList.add(exitPath);
        return pathList;
    }
}

//Adapter that allows us to create a custom GraphPath to add to our Directions
class SelfPathAdapter<String, IdentifiedWeightedEdge> implements GraphPath<String, IdentifiedWeightedEdge> {
    private String nodeId;

    public SelfPathAdapter(String nodeId){
        this.nodeId = nodeId;

    }

    @Override
    public Graph<String, IdentifiedWeightedEdge> getGraph() {
        return null;
    }

    @Override
    public String getStartVertex() {
        return nodeId;
    }

    @Override
    public String getEndVertex() {
        return nodeId;
    }

    @Override
    public List<IdentifiedWeightedEdge> getEdgeList() {
        return null;
    }

    @Override
    public List<String> getVertexList() {
        return null;
    }

    @Override
    public double getWeight() {
        return 0;
    }

    @Override
    public int getLength() {
        return 0;
    }
}

