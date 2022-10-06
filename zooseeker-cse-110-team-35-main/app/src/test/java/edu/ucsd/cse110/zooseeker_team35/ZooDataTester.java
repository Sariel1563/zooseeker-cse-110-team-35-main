package edu.ucsd.cse110.zooseeker_team35;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;

@RunWith(AndroidJUnit4.class)
public class ZooDataTester {
    private Map<String, ZooData.VertexInfo> testMap;

    @Before
    public void createMap() {
        Context context = ApplicationProvider.getApplicationContext();
        testMap = ZooData.loadVertexInfoJSON(context, "sample_node_info2.json");
    }

    @Test
    public void testGet(){
        ZooData.VertexInfo vertexTest = testMap.get("gorillas");
        assertNotNull(vertexTest);

        assertEquals(vertexTest.id, "gorillas");
        assertEquals(vertexTest.name, "Gorillas");
        assertEquals(vertexTest.kind, ZooData.VertexInfo.Kind.EXHIBIT);

        ZooData.VertexInfo vertexTest2 = testMap.get("Dinosaurs");
        assertNull(vertexTest2);
    }
}