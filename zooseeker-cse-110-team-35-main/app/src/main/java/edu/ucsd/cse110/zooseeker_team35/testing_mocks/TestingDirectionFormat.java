package edu.ucsd.cse110.zooseeker_team35.testing_mocks;

import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionFormatStrategy;

//Displays directions in a format that is easier to test
public class TestingDirectionFormat implements DirectionFormatStrategy {
    @Override
    public String buildDirection(int directionNumber, String startNode, String endNode, String streetName, double edgeWeight) {
        String pathInfo = startNode + "->" + endNode + ":" + streetName;
        return pathInfo;
    }
}
