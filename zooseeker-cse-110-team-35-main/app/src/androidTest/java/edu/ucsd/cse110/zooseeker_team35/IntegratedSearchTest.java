package edu.ucsd.cse110.zooseeker_team35;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.activities.SearchResultsActivity;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatus;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDao;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDatabase;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;

@RunWith(AndroidJUnit4.class)
public class IntegratedSearchTest {

    private ExhibitStatusDatabase testDb;

    @Before
    public void setZooInfo() {
        Map<String, ZooData.VertexInfo> vertexInfo = ZooData.loadVertexInfoJSON(ApplicationProvider.getApplicationContext(), ZooInfoProvider.nodeInfoJSON);
        Map<String, ZooData.EdgeInfo> edgeInfo = ZooData.loadEdgeInfoJSON(ApplicationProvider.getApplicationContext(), ZooInfoProvider.edgeInfoJSON);
        ZooInfoProvider.setIdVertexMap(vertexInfo);
        ZooInfoProvider.setIdEdgeMap(edgeInfo);

        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, ExhibitStatusDatabase.class)
                .allowMainThreadQueries()
                .build();
        ExhibitStatusDatabase.injectTestDatabase(testDb);

        Map<String, ZooData.VertexInfo> vertices = ZooData.loadVertexInfoJSON(context,"sample_node_info.json");
        List<ExhibitStatus> exhibitStatuses = new ArrayList<>();
        for(String id : vertices.keySet()) {
            if(vertices.get(id).kind == ZooData.VertexInfo.Kind.EXHIBIT) {
                exhibitStatuses.add(new ExhibitStatus(id, false, false));
            }
        }
        ExhibitStatusDao exhibitStatusDao = testDb.exhibitStatusDao();
        exhibitStatusDao.insertAll(exhibitStatuses);
    }

    @After
    public void closeDb() throws IOException {
        testDb.close();
    }

    @Test
    public void testValidNameSearch() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(),
                SearchResultsActivity.class);
        intent.putExtra("searchTerm", "Gorillas");
        ActivityScenario<SearchResultsActivity> scenario = ActivityScenario.launch(intent);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.recyclerView;
            RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(0);
            assertNotNull(firstVH);

            TextView resultView = firstVH.itemView.findViewById(R.id.search_item_text);
            String resultText = resultView.getText().toString();
            assertEquals(resultText, "Gorillas");

            TextView noResultsView = activity.findViewById(R.id.no_results_msg);
            assertEquals(noResultsView.getVisibility(), View.INVISIBLE);
        });
    }

    @Test
    public void testInvalidNameSearch() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(),
                SearchResultsActivity.class);
        intent.putExtra("searchTerm", "Dinosaurs");
        ActivityScenario<SearchResultsActivity> scenario = ActivityScenario.launch(intent);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.recyclerView;
            TextView noResultsView = activity.findViewById(R.id.no_results_msg);
            RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(0);
            assertEquals(noResultsView.getVisibility(), View.VISIBLE);

            assertNull(firstVH);
        });
    }
}
