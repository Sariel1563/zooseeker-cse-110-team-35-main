package edu.ucsd.cse110.zooseeker_team35.testing_mocks;

import android.util.Log;

import edu.ucsd.cse110.zooseeker_team35.activities.DirectionsActivity;

//A mocked directions activity who only stores whether updateDisplay has been called and logs it

public class MockDirectionsActivity extends DirectionsActivity {
    public boolean updateDisplayCalled = false;
    @Override
    public void updateDisplay(){
        Log.i("mock_directionsActivity", "display updated called");
        updateDisplayCalled = true;
        return;
    }
}
