package edu.ucsd.cse110.zooseeker_team35.direction_display;

import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionFormatStrategy;

// Formats a String direction using the Proceed on `streetname` `distance` feet form `start` towards `end`
public class ProceedDirectionFormat implements DirectionFormatStrategy {

    @Override
    public String buildDirection(int directionNumber, String startNode, String endNode, String streetName, double edgeWeight) {
        String pathInfo = String.format("  %d. Proceed from '%s' on '%s' %.0f ft towards '%s'.\n",
                directionNumber,
                startNode,
                streetName,
                edgeWeight,
                endNode);
        return pathInfo;
    }
}
