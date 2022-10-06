package edu.ucsd.cse110.zooseeker_team35.path_finding;

import android.content.Context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatus;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDao;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDatabase;

/*
A static class that contains information about the exhibits at the Zoo
with methods to get the vertex list, edge list, exhibit list and the list of
selected vertexes
 */

public class ZooInfoProvider{
    public static String edgeInfoJSON = "sample_edge_info.json";
    public static String nodeInfoJSON = "sample_node_info.json";
    public static String zooGraphJSON = "sample_zoo_graph.json";

    private static Map<String, ZooData.VertexInfo> idVertexMap = new HashMap<>();
    private static Map<String, ZooData.EdgeInfo> idEdgeMap = new HashMap<>();
    private static List<ZooData.VertexInfo> vertexes;
    private static List<ZooData.VertexInfo> exhibits;

    public static void setIdVertexMap(Map<String, ZooData.VertexInfo> idVertexMap) {
        ZooInfoProvider.idVertexMap = idVertexMap;
        List<ZooData.VertexInfo> vertexes = new LinkedList<>();
        List<ZooData.VertexInfo> exhibits = new LinkedList<>();
        for (ZooData.VertexInfo vertex : idVertexMap.values()){
            vertexes.add(vertex);
            if (vertex.kind == ZooData.VertexInfo.Kind.EXHIBIT){
                exhibits.add(vertex);
            }
        }

        exhibits.sort(Comparator.comparing(v -> v.name));

        ZooInfoProvider.setVertexes(vertexes);
        ZooInfoProvider.setExhibits(exhibits);
    }

    public static void setIdEdgeMap(Map<String, ZooData.EdgeInfo> idEdgeMap) {
        ZooInfoProvider.idEdgeMap = idEdgeMap;
    }

    public static Map<String, ZooData.VertexInfo> getIdVertexMap() {
        return idVertexMap;
    }

    public static ZooData.VertexInfo getVertexWithId(String id) {
        ZooData.VertexInfo toReturn = idVertexMap.get(id);
        if (toReturn == null){
            System.out.println("crashing bc id = " + id);
        }
        return toReturn;
    }

    public static List<ZooData.VertexInfo> getExhibits() {
        return exhibits;
    }

    private static void setExhibits(List<ZooData.VertexInfo> exhibits) {
        ZooInfoProvider.exhibits = exhibits;
    }

    public static List<ZooData.VertexInfo> getVertexes() {
        return vertexes;
    }

    private static void setVertexes(List<ZooData.VertexInfo> vertexes) {
        ZooInfoProvider.vertexes = vertexes;
    }

    public static List<ZooData.VertexInfo> getSelectedExhibits(Context context) {
        return getSelectedExhibits(context, ExhibitStatusDatabase.getSingleton(context).exhibitStatusDao());
    }

    public static List<ZooData.VertexInfo> getSelectedExhibits(Context context, ExhibitStatusDao dao) {
        List<ExhibitStatus> exhibitStatuses = dao.getAdded(true);
        List<ZooData.VertexInfo> selectedExhibits = new ArrayList<>();
        for (ExhibitStatus exhibitStatus : exhibitStatuses){
            if (exhibitStatus.getIsAdded()){
                if (getVertexWithId(exhibitStatus.getId()) != null ){
                    selectedExhibits.add(getVertexWithId(exhibitStatus.getId()));
                }
            }
        }
        return selectedExhibits;
    }

    public static List<ZooData.VertexInfo> getVisitableVertexList() {
        List<ZooData.VertexInfo> visitable = new LinkedList<>();
        for (ZooData.VertexInfo vertex : vertexes){
            if (vertex.group_id == null){
                visitable.add(vertex);
            }
        }
        return visitable;
    }


    public static Map<String, ZooData.EdgeInfo> getEdgeMap() {
        return idEdgeMap;
    }

    public static Map<String, ZooData.VertexInfo> getVertexMap() {
        return idVertexMap;
    }

    public static List<ZooData.VertexInfo> getUnvisitedVertex(Context context) {
        ExhibitStatusDao dao = ExhibitStatusDatabase.getSingleton(context).exhibitStatusDao();
        List<ExhibitStatus> exhibitStatuses = dao.getVisited(false);
        List<ZooData.VertexInfo> selectedExhibits = new ArrayList<>();
        for (ExhibitStatus exhibitStatus : exhibitStatuses){
            if (exhibitStatus.getIsAdded()){
                ZooData.VertexInfo vertex = getVertexWithId(exhibitStatus.getId());
                if (vertex != null ){
                    selectedExhibits.add(getVertexWithId(exhibitStatus.getId()));
                    if (vertex.group_id != null){
                        selectedExhibits.add(getVertexWithId(vertex.group_id));
                    }
                }
            }
        }
        System.out.println(selectedExhibits);
        return selectedExhibits;
    }

}
