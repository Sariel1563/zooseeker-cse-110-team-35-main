package edu.ucsd.cse110.zooseeker_team35;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

import static edu.ucsd.cse110.zooseeker_team35.UI_TestUtilities.childAtPosition;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.activities.MainActivity;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatus;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDao;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDatabase;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;

@LargeTest
public class ExhibitAddTest {

    private ExhibitStatusDatabase testDb;

    @Before
    public void createDb() {
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
    public void exhibitAddTest() {
        ActivityScenario.launch(MainActivity.class);

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.search_btn), withText("Search"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction materialCheckBox = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_item_recycler),
                                        1),
                                1),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction materialCheckBox2 = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_item_recycler),
                                        3),
                                1),
                        isDisplayed()));
        materialCheckBox2.perform(click());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.back_btn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialTextView.perform(click());

        List<ZooData.VertexInfo> exhibits = ZooInfoProvider.getSelectedExhibits(InstrumentationRegistry.getInstrumentation().getTargetContext());
        assertEquals(exhibits.size(), 2);
        assertEquals(exhibits.stream().filter(vertexInfo -> vertexInfo.id.equals("crocodile")).count(), 1);
        assertEquals(exhibits.stream().filter(vertexInfo -> vertexInfo.id.equals("motmot")).count(), 1);
    }
}
