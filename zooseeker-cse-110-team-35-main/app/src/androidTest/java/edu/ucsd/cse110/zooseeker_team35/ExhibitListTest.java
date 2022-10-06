package edu.ucsd.cse110.zooseeker_team35;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

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
public class ExhibitListTest {
    private ExhibitStatusDatabase testDb;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

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
    public void addExhibitsTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("flamingo"), closeSoftKeyboard());

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
                                        0),
                                1),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.search_bar_2), withText("flamingo"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("koi fish"));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.search_bar_2), withText("koi fish"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.search_btn_2), withText("Search"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialCheckBox2 = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_item_recycler),
                                        0),
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

        ViewInteraction textView = onView(
                allOf(withId(R.id.exhibit_item_text), withText("Flamingos"),
                        withParent(withParent(withId(R.id.added_exhibits_recycler))),
                        isDisplayed()));
        textView.check(matches(withText("Flamingos")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.exhibit_item_text), withText("Koi Fish"),
                        withParent(withParent(withId(R.id.added_exhibits_recycler))),
                        isDisplayed()));
        textView2.check(matches(withText("Koi Fish")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.exhibit_count), withText("2"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView3.check(matches(withText("2")));
    }
    @Test
    public void removeExhibitsTest() {
        ActivityScenario.launch(MainActivity.class);

        ViewInteraction textView = onView(
                allOf(withId(R.id.no_exhibit), withText("No Exhibits Added"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("No Exhibits Added")));

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("toucan"), closeSoftKeyboard());

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
                                        0),
                                1),
                        isDisplayed()));
        materialCheckBox.perform(click());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.back_btn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("hippos"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.search_btn), withText("Search"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction materialCheckBox3 = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_item_recycler),
                                        0),
                                1),
                        isDisplayed()));
        materialCheckBox3.perform(click());

        ViewInteraction materialTextView2 = onView(
                allOf(withId(R.id.back_btn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialTextView2.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText5.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("toucan"), closeSoftKeyboard());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.search_btn), withText("Search"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction materialCheckBox4 = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_item_recycler),
                                        0),
                                1),
                        isDisplayed()));
        materialCheckBox4.perform(click());

        ViewInteraction materialTextView3 = onView(
                allOf(withId(R.id.back_btn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialTextView3.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.exhibit_item_text), withText("Hippos"),
                        withParent(withParent(withId(R.id.added_exhibits_recycler))),
                        isDisplayed()));
        textView3.check(matches(withText("Hippos")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.exhibit_count), withText("1"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView4.check(matches(withText("1")));

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText7.perform(click());


        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText9.perform(replaceText("Hippo"), closeSoftKeyboard());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.search_btn), withText("Search"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction materialCheckBox6 = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_item_recycler),
                                        0),
                                1),
                        isDisplayed()));
        materialCheckBox6.perform(click());

        ViewInteraction materialTextView5 = onView(
                allOf(withId(R.id.back_btn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialTextView5.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.no_exhibit), withText("No Exhibits Added"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView5.check(matches(withText("No Exhibits Added")));
    }
}
