package edu.ucsd.cse110.zooseeker_team35.location_tracking;

import android.content.DialogInterface;
import android.location.Location;

import androidx.appcompat.app.AlertDialog;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.zooseeker_team35.activities.DirectionsActivity;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;

public class RerouteHandler {
    boolean rerouteOffered = false;
    DirectionsActivity display;
    public RerouteHandler(DirectionsActivity display) {
        this.display = display;
    }

    public void setRerouteOffered(boolean rerouteOffered) {
        this.rerouteOffered = rerouteOffered;
    }

    public void testRerouteNeeded(Location currentLocation) {
        String currentId = DirectionTracker.getCurrentExhibitId();
        List<ZooData.VertexInfo> unvisitedNodes = DirectionTracker.getRemainingVertexes();
        if (!unvisitedNodes.isEmpty()) {
            ZooData.VertexInfo closestExhibit = FindClosestExhibitHelper.closestExhibitPathwise(DirectionTracker.getGraph(),currentLocation, unvisitedNodes);
            //check if the two exhibits belong to same group
            ZooData.VertexInfo curExhibit = ZooInfoProvider.getVertexWithId(currentId);
            if (!checkNodeEquality(curExhibit, closestExhibit)) {
                reroute(closestExhibit.id);
            }
        }
    }

    public void calculateRerouteNeeded(Location currentLocation) {
        String currentId = DirectionTracker.getCurrentExhibitId();
        List<ZooData.VertexInfo> unvisitedNodes = DirectionTracker.getRemainingVertexes();
        if (!unvisitedNodes.isEmpty()) {
            ZooData.VertexInfo closestExhibit = FindClosestExhibitHelper.closestExhibitPathwise(DirectionTracker.getGraph(),currentLocation, unvisitedNodes);
            //check if the two exhibits belong to same group
            ZooData.VertexInfo curExhibit = ZooInfoProvider.getVertexWithId(currentId);
            if (!checkNodeEquality(curExhibit, closestExhibit)) {
                promptReroute(closestExhibit.id);
            }
        }
    }

    private boolean checkNodeEquality (ZooData.VertexInfo node1, ZooData.VertexInfo node2) {
        if (node1.id.equals(node2.id)){
            return true;
        }
        if (node1.group_id != null && node1.group_id.equals(node2.id)){
            return true;
        }
        if (node2.group_id != null && node2.group_id.equals(node1.id)){
            return true;
        }
        if (node2.group_id != null && node1.group_id != null && node1.group_id.equals(node2.group_id)){
            return true;
        }
        return false;
    }

    //Reroute occurs if there is a new optimal route through the rest of the plan due to a change
    //in the user's current location
    private void promptReroute(String closestVertex) {
        if (rerouteOffered || ZooInfoProvider.getVertexWithId(DirectionTracker.getCurrentExhibitId()).group_id != null) {
            return;
        }
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        reroute(closestVertex);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(display);
        builder.setMessage("You are closer to " + ZooInfoProvider.getVertexWithId(closestVertex).name + " than your current destination. Do You Want To Reroute?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

        rerouteOffered = true;
    }

    //This method is called if user choose to reroute when prompted
    private void reroute(String closestVertex){
        List<String> remainingExhibits = DirectionTracker.getRemainingVertexes().stream().map(vertex -> vertex.id).collect(Collectors.toList());
        remainingExhibits.remove(closestVertex);
        DirectionTracker.updatePathList(closestVertex, remainingExhibits);
        display.updateDisplay();
    }
}
