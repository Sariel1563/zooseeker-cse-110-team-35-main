package edu.ucsd.cse110.zooseeker_team35.direction_display;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.path_finding.IdentifiedWeightedEdge;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;

public class BriefDirectionCreator implements DirectionCreator{


    public List<String> createDirections(DirectionFormatStrategy directionFormatter, GraphPath<String, IdentifiedWeightedEdge> path, Map<String, ZooData.VertexInfo> vertexInfo, Map<String, ZooData.EdgeInfo> edgeInfo, Graph<String, IdentifiedWeightedEdge> graph) {
        List<String> directionList = new ArrayList<String>();
        List<IdentifiedWeightedEdge> edges = path.getEdgeList();
        List<String> vertexes = path.getVertexList();
        int directionNumber = 1;
        double accumulatedDist = 0;
        String startNode = vertexes.get(0);
        for (int i = 0; i < edges.size(); i++) {
            IdentifiedWeightedEdge e = edges.get(i);
            String curStreetName = edgeInfo.get(e.getId()).street;
            if (accumulatedDist == 0) {
                accumulatedDist = graph.getEdgeWeight(e);
            }
            if (i < edges.size() - 1 && curStreetName.equals(edgeInfo.get(edges.get(i+1).getId()).street) ){
                accumulatedDist += graph.getEdgeWeight(e);
            } else {
                String endNode = vertexes.get(i + 1);
                String pathInfo = directionFormatter.buildDirection(
                        directionNumber++,
                        vertexInfo.get(startNode).name,
                        vertexInfo.get(endNode).name,
                        edgeInfo.get(e.getId()).street,
                        accumulatedDist);
                directionList.add(pathInfo);
                startNode = vertexes.get(i + 1);
                accumulatedDist = 0;
            }
        }
        return directionList;
    }

    public List<String> createDirections(GraphPath<String, IdentifiedWeightedEdge> path, Map<String, ZooData.VertexInfo> vertexInfo, Map<String, ZooData.EdgeInfo> edgeInfo, Graph<String, IdentifiedWeightedEdge> graph) {
        return createDirections(new ProceedDirectionFormat(), path, vertexInfo, edgeInfo, graph);
    }

}
