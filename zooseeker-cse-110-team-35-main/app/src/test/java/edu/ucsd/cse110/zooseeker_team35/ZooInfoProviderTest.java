package edu.ucsd.cse110.zooseeker_team35;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatus;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDao;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDatabase;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;

@RunWith(AndroidJUnit4.class)
public class ZooInfoProviderTest {
    private ExhibitStatusDao dao;
    private ExhibitStatusDatabase db;
    private Context context;

    @Before
    public void setZooInfo(){
        context = ApplicationProvider.getApplicationContext();
        Map<String, ZooData.VertexInfo> vertexInfo = ZooData.loadVertexInfoJSON(context, "sample_node_info2.json");
        Map<String, ZooData.EdgeInfo> edgeInfo = ZooData.loadEdgeInfoJSON(context, "sample_edge_info2.json");
        ZooInfoProvider.setIdVertexMap(vertexInfo);
        ZooInfoProvider.setIdEdgeMap(edgeInfo);
        db = Room.inMemoryDatabaseBuilder(context, ExhibitStatusDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.exhibitStatusDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testGetVertexWithId(){
        ZooData.VertexInfo gorillas = ZooInfoProvider.getVertexWithId("gorillas");

        assertEquals(gorillas.id, "gorillas");
        assertEquals(gorillas.name, "Gorillas");
        assertEquals(gorillas.kind, ZooData.VertexInfo.Kind.EXHIBIT);
    }

    @Test
    public void testGetSelectedExhibits(){
        ExhibitStatus test1 = new ExhibitStatus("gators", true, false);
        ExhibitStatus test2 = new ExhibitStatus("gorillas", false, false);
        ExhibitStatus test3 = new ExhibitStatus("arctic_foxes", true, false);
        dao.insert(test1);
        dao.insert(test2);
        dao.insert(test3);
        List<ZooData.VertexInfo> selectedExhibits = ZooInfoProvider.getSelectedExhibits(context, dao);

        assertEquals(selectedExhibits.size(), 2);
        assertTrue(selectedExhibits.contains(ZooInfoProvider.getVertexWithId(test1.getId())));
        assertTrue(selectedExhibits.contains(ZooInfoProvider.getVertexWithId(test3.getId())));
    }
}
