package edu.ucsd.cse110.zooseeker_team35.location_tracking;

import android.location.Location;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.zooseeker_team35.activities.DirectionsActivity;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;

public class SkipHandler {
    DirectionsActivity display;
    public SkipHandler(DirectionsActivity display) {
        this.display = display;
    }

    public boolean skipExhibit(Location currentLocation) {
        String currentExhibitId = DirectionTracker.getCurrentExhibitId();
        //disable skipping on last exhibit and on exhibits within a group
        if (currentExhibitId.equals("entrance_exit_gate")){
            return false;
        }
        if (ZooInfoProvider.getVertexWithId(currentExhibitId).group_id != null) {
            return false;
        }
        //get remaining exhibits and remove the current one
        List<ZooData.VertexInfo> targetExhibits = DirectionTracker.getRemainingVertexes();
        targetExhibits.remove(0);

        //if the removed ehxibit was a group vertex, remove exhibits within that group
        while (!targetExhibits.isEmpty() && targetExhibits.get(0).group_id != null) {
            targetExhibits.remove(0);
        }

        //if we have no more exhibits, the only remianing is the exit gate
        if (targetExhibits.isEmpty()){
            targetExhibits.add(ZooInfoProvider.getVertexWithId("entrance_exit_gate"));
        }

        //update DirectionTracker to reroute with the passed in exhibit list
        ZooData.VertexInfo closestExhibit = FindClosestExhibitHelper.closestExhibit(currentLocation, targetExhibits);
        targetExhibits.remove(closestExhibit);
        List<String> remainingExhibits = targetExhibits.stream().map(vertex -> vertex.id).collect(Collectors.toList());
        DirectionTracker.updatePathList(closestExhibit.id, remainingExhibits);

        display.updateDisplay();

        return true;
    }
}
