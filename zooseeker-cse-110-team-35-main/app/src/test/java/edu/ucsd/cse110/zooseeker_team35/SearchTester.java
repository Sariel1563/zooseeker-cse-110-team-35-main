package edu.ucsd.cse110.zooseeker_team35;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.content.Intent;
import android.widget.TextView;

import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.activities.SearchResultsActivity;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;

@RunWith(AndroidJUnit4.class)
public class SearchTester {

    @Before
    public void setZooInfo() {
        Map<String, ZooData.VertexInfo> vertexInfo = ZooData.loadVertexInfoJSON(ApplicationProvider.getApplicationContext(), ZooInfoProvider.nodeInfoJSON);
        Map<String, ZooData.EdgeInfo> edgeInfo = ZooData.loadEdgeInfoJSON(ApplicationProvider.getApplicationContext(), ZooInfoProvider.edgeInfoJSON);
        ZooInfoProvider.setIdVertexMap(vertexInfo);
        ZooInfoProvider.setIdEdgeMap(edgeInfo);
    }

    //Checks when user searches up an exhibit that doesn't exist
    @Test
    public void invalidSearchTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(),
                SearchResultsActivity.class);
        intent.putExtra("searchTerm", "Dinosaurs");

        try(ActivityScenario<SearchResultsActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                TextView searchText = activity.findViewById(R.id.no_results_msg);
                assertEquals(searchText.getVisibility(), 0);
                scenario.close();
            });
        }
    }

}
