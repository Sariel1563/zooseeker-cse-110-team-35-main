package edu.ucsd.cse110.zooseeker_team35;


import static androidx.core.util.Preconditions.checkNotNull;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import static edu.ucsd.cse110.zooseeker_team35.UI_TestUtilities.*;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.activities.MainActivity;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatus;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDao;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDatabase;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UI_SearchResultSuggestions {

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

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void partialStringSearch() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("mm"), closeSoftKeyboard());

        onView(withId(R.id.search_btn)).perform(click());

        onView(withId(R.id.search_item_recycler))
                .check(matches(atPosition(0, hasDescendant(withText("Capuchin Monkeys")))));

        onView(withId(R.id.search_item_recycler))
                .check(matches(atPosition(1, hasDescendant(withText("Gorillas")))));

        onView(withId(R.id.search_item_recycler))
                .check(matches(atPosition(2, hasDescendant(withText("Hippos")))));

        onView(withId(R.id.search_item_recycler))
                .check(matches(atPosition(3, hasDescendant(withText("Orangutans")))));

        onView(withId(R.id.search_item_recycler))
                .check(matches(atPosition(4, hasDescendant(withText("Siamangs")))));

        onView(withId(R.id.search_item_recycler))
                .check(matches(recyclerViewSize(5)));
    }

    @Test
    public void tagSearch(){
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("ape"), closeSoftKeyboard());

        onView(withId(R.id.search_btn)).perform(click());

        onView(withId(R.id.search_item_recycler))
                .check(matches(atPosition(0, hasDescendant(withText("Gorillas")))));

        onView(withId(R.id.search_item_recycler))
                .check(matches(atPosition(1, hasDescendant(withText("Orangutans")))));

        onView(withId(R.id.search_item_recycler))
                .check(matches(recyclerViewSize(2)));
    }

    @Test
    public void specificSearch(){
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Blue Capped Motmot"), closeSoftKeyboard());

        onView(withId(R.id.search_btn)).perform(click());

        onView(withId(R.id.search_item_recycler))
                .check(matches(atPosition(0, hasDescendant(withText("Blue Capped Motmot")))));

        onView(withId(R.id.search_item_recycler))
                .check(matches(recyclerViewSize(1)));
    }

    @Test
    public void invalidSearch(){
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("godzilla"), closeSoftKeyboard());

        onView(withId(R.id.search_btn)).perform(click());

        onView(withId(R.id.search_item_recycler))
                .check(matches(recyclerViewSize(0)));
    }

    @Test
    public void searchButton2Test() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.search_btn), withText("Search"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.search_bar_2),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("gorilla"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.search_btn_2), withText("Search"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton2.perform(click());

        onView(withId(R.id.search_item_recycler))
                .check(matches(atPosition(0, hasDescendant(withText("Gorillas")))));

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.back_btn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialTextView.perform(click());
    }

}
