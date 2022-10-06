package edu.ucsd.cse110.zooseeker_team35.location_tracking;

import androidx.annotation.VisibleForTesting;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.zooseeker_team35.direction_display.DetailedDirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.ProceedDirectionFormat;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionFormatStrategy;
import edu.ucsd.cse110.zooseeker_team35.path_finding.IdentifiedWeightedEdge;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooPathFinder;

//      DirectionTracker has methods nextExhibit, prevExhibit such that nextExhibit moves to the next exhibit,
//      prevExhibit moves to the previous exhibit
//      DirectionTracker will be used to keep track of the current exhibit we are getting directions to and
//      the information about the Graph
public class DirectionTracker{
    private static int currentExhibit;
    private static List<GraphPath<String, IdentifiedWeightedEdge>> pathList;
    private static Map<String, ZooData.VertexInfo> vertexInfo;
    private static Map<String, ZooData.EdgeInfo> edgeInfo;
    private static Graph<String, IdentifiedWeightedEdge> graph;

    @VisibleForTesting
    public static void setCurrentExhibit(int currentExhibit) {
        DirectionTracker.currentExhibit = currentExhibit;
    }

    public static void initialize(Graph<String, IdentifiedWeightedEdge> newGraph, List<GraphPath<String, IdentifiedWeightedEdge>> newPathList) {
        pathList = newPathList;
        graph = newGraph;
        currentExhibit = 0;
        vertexInfo = ZooInfoProvider.getVertexMap();
        edgeInfo = ZooInfoProvider.getEdgeMap();
    }

    public static Graph<String, IdentifiedWeightedEdge> getGraph() {
        return graph;
    }

    public static void nextExhibit(){
        if (currentExhibit < pathList.size() - 1) {
            currentExhibit++;
        }
    }
    public static void prevExhibit(){
        if (currentExhibit > 0) {
            currentExhibit--;
        }
    }

    public static String getCurrentExhibit() {
        return ZooInfoProvider.getVertexWithId(pathList.get(currentExhibit).getEndVertex()).name;
    }

    public static String getCurrentExhibitId() {
        return pathList.get(currentExhibit).getEndVertex();
    }

    public static int getCurrentExhibitIndex() {
        return currentExhibit;
    }

    public static List<String> getDirectionsToCurrentExhibit(){
        return getDirectionsToCurrentExhibit(new DetailedDirectionCreator());
    }

    public static List<String> getDirectionsToCurrentExhibit (DirectionCreator directionCreator){
        GraphPath<String, IdentifiedWeightedEdge> path = pathList.get(currentExhibit);
        return getDirectionsToCurrentExhibit(directionCreator, ZooInfoProvider.getVertexWithId(path.getStartVertex()));
    }

    public static List<String> getDirectionsToCurrentExhibit (DirectionCreator directionCreator, ZooData.VertexInfo closestExhibit){
        List<String> directionList = new ArrayList<String>();
        GraphPath<String, IdentifiedWeightedEdge> path = pathList.get(currentExhibit);
        if (path.getWeight() == 0 && ZooInfoProvider.getVertexWithId(path.getStartVertex()).group_id != null ){
            String id = path.getStartVertex();
            ZooData.VertexInfo vertex = ZooInfoProvider.getVertexWithId(id);
            ZooData.VertexInfo parent = ZooInfoProvider.getVertexWithId(vertex.group_id);
            directionList.add("1. Find " + vertex.name + " inside " + parent.name);
            return directionList;
        }
        path = reCalculatedDirection(closestExhibit);
        return directionCreator.createDirections(path, vertexInfo, edgeInfo, graph);
    }

    //recalculate the directions with the closest ehxibit being the start exhibit
    private static GraphPath<String, IdentifiedWeightedEdge> reCalculatedDirection(ZooData.VertexInfo closestExhibit) {
        String lastVertex = pathList.get(currentExhibit).getEndVertex();
        GraphPath<String, IdentifiedWeightedEdge> newPath = DijkstraShortestPath.findPathBetween(graph, closestExhibit.id, lastVertex);
        return newPath;
    }

    //get all exhibits that haven't been visited yet including the vertex that we are currently on
    public static List<ZooData.VertexInfo> getRemainingVertexes() {
        List<ZooData.VertexInfo> remainingExhibits = new LinkedList<>();
        for (GraphPath<String, IdentifiedWeightedEdge> graphPath : pathList.subList(currentExhibit, pathList.size())){
            String id = graphPath.getEndVertex();
            if (id.equals("entrance_exit_gate")){
                continue;
            }
            ZooData.VertexInfo vertex = ZooInfoProvider.getVertexWithId(id);
            remainingExhibits.add(vertex);
        }
        return remainingExhibits;
    }

    //update the pathList by rerouting starting from the closestVertex and visiting all the target exhibits
    public static void updatePathList(String closestVertex, List<String> targetExhibits) {
        if (ZooInfoProvider.getVertexWithId(closestVertex).group_id != null) {
            closestVertex = ZooInfoProvider.getVertexWithId(closestVertex).group_id;
        }
        List<GraphPath<String, IdentifiedWeightedEdge>> newPathList = recalculatePathList(closestVertex, targetExhibits);
        pathList = newPathList;
        if (currentExhibit >= pathList.size()){
            currentExhibit = pathList.size() - 1;
        }
    }

    //calculate the new pathList as a result of rerouting starting from the closestVertex and visitng all the target Exhibits
    private static List<GraphPath<String, IdentifiedWeightedEdge>> recalculatePathList(String closestVertex, List<String> targetExhibits){
        ZooPathFinder zooPathFinder = new ZooPathFinder(graph);
        List<GraphPath<String, IdentifiedWeightedEdge>> pathListRight = zooPathFinder.calculatePath(closestVertex, "entrance_exit_gate", targetExhibits);
        List<GraphPath<String, IdentifiedWeightedEdge>> pathListLeft = pathList.subList(0, currentExhibit);
        String connectingStart = pathList.get(currentExhibit).getStartVertex();
        if (ZooInfoProvider.getVertexWithId(connectingStart).group_id != null){
            connectingStart = ZooInfoProvider.getVertexWithId(connectingStart).group_id;
        }
        String connectingEnd = pathListRight.get(0).getStartVertex();
        if (ZooInfoProvider.getVertexWithId(connectingEnd).group_id != null){
            connectingEnd = ZooInfoProvider.getVertexWithId(connectingEnd).group_id;
        }
        GraphPath<String, IdentifiedWeightedEdge> connectorPath = DijkstraShortestPath.findPathBetween(graph, connectingStart, connectingEnd);

        List<GraphPath<String, IdentifiedWeightedEdge>> combined = new ArrayList<>();
        combined.addAll(pathListLeft);
        combined.add(connectorPath);
        combined.addAll(pathListRight);

        return combined;
    }

}
