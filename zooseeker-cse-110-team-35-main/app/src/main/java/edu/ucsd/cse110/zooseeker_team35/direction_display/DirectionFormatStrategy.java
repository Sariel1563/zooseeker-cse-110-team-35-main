package edu.ucsd.cse110.zooseeker_team35.direction_display;

/*
DirectionFormatStrategy will construct a direction in the form of a String using the start location,
end location, street name and distance
 */
public interface DirectionFormatStrategy {
    String buildDirection (int directionNumber, String startNode, String endNode, String streetName, double edgeWeight);
}
