package edu.ucsd.cse110.zooseeker_team35;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
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
import java.util.ArrayList;

import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatus;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDao;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDatabase;

@RunWith(AndroidJUnit4.class)
public class ZooseekerDatabaseTester {
    private ExhibitStatusDao dao;
    private ExhibitStatusDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
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
    public void testInsert() {
        ExhibitStatus test1 = new ExhibitStatus("monkeys", false, false);
        ExhibitStatus test2 = new ExhibitStatus("gorillas", false, false);

        long id1 = dao.insert(test1);
        long id2 = dao.insert(test2);

        assertNotEquals(id1, id2);
    }

    @Test
    public void testGet() {
        ExhibitStatus test1 = new ExhibitStatus("monkeys", false, false);
        assertNotNull(test1);
        dao.insert(test1);
        ExhibitStatus test2 = dao.get("monkeys");

        assertEquals(test2.getId(), test1.getId());
        assertEquals(test2.getIsAdded(), test1.getIsAdded());
        assertEquals(test2.getIsVisited(), test1.getIsVisited());
    }

    @Test
    public void testGetAdded() {
        ExhibitStatus test1 = new ExhibitStatus("monkeys", false, false);
        ExhibitStatus test2 = new ExhibitStatus("gorillas", true, false);
        ExhibitStatus test3 = new ExhibitStatus("humans",  false, false);

        List<ExhibitStatus> exhibits = new ArrayList<>();
        exhibits.add(test1);
        exhibits.add(test2);
        exhibits.add(test3);

        dao.insertAll(exhibits);

        List<ExhibitStatus> exhibits2 = dao.getAdded(false);
        assertNotNull(exhibits2);

        assertEquals(exhibits2.get(0).getId(), test1.getId());
        assertEquals(exhibits2.get(0).getIsAdded(), test1.getIsAdded());
        assertEquals(exhibits2.get(0).getIsVisited(), test1.getIsVisited());

        assertEquals(exhibits2.get(1).getId(), test3.getId());
        assertEquals(exhibits2.get(1).getIsAdded(), test3.getIsAdded());
        assertEquals(exhibits2.get(1).getIsVisited(), test3.getIsVisited());

        List<ExhibitStatus> exhibits3 = dao.getAdded(true);
        assertNotNull(exhibits3);

        assertEquals(exhibits3.get(0).getId(), test2.getId());
        assertEquals(exhibits3.get(0).getIsAdded(), test2.getIsAdded());
        assertEquals(exhibits3.get(0).getIsVisited(), test2.getIsVisited());
    }

    @Test
    public void testUpdate() {
        ExhibitStatus test1 = new ExhibitStatus("monkeys", false, false);
        dao.insert(test1);

        test1 = dao.get("monkeys");
        test1.setIsAdded(true);
        int elemsUpdated = dao.update(test1);
        assertEquals(1, elemsUpdated);

        test1 = dao.get("monkeys");
        assertNotNull(test1);
        assertTrue(test1.getIsAdded());
    }

    @Test
    public void testGetAll() {
        ExhibitStatus test1 = new ExhibitStatus("monkeys", false, false);
        assertNotNull(test1);
        dao.insert(test1);

        ExhibitStatus test2 = new ExhibitStatus("gorillas", true, false);
        assertNotNull(test2);
        dao.insert(test2);

        ExhibitStatus test3 = new ExhibitStatus("sharks", false, false);
        assertNotNull(test3);
        dao.insert(test3);

        List<ExhibitStatus> testList = dao.getAll();
        assertNotNull(testList);

        assertEquals(test1.getId(), testList.get(0).getId());
        assertEquals(test1.getIsAdded(), testList.get(0).getIsAdded());

        assertEquals(test2.getId(), testList.get(1).getId());
        assertEquals(test2.getIsAdded(), testList.get(1).getIsAdded());

        assertEquals(test3.getId(), testList.get(2).getId());
        assertEquals(test3.getIsAdded(), testList.get(2).getIsAdded());
    }

    @Test
    public void testGetVisited() {
        ExhibitStatus test1 = new ExhibitStatus("monkeys", false, true);
        ExhibitStatus test2 = new ExhibitStatus("gorillas", true, false);
        ExhibitStatus test3 = new ExhibitStatus("humans",  false, true);

        dao.insert(test1);
        dao.insert(test2);
        dao.insert(test3);

        List<ExhibitStatus> exhibits1 = dao.getVisited(true);
        assertEquals(exhibits1.size(), 2);

        assertEquals(exhibits1.get(0).getId(), test1.getId());
        assertEquals(exhibits1.get(0).getIsAdded(), test1.getIsAdded());
        assertEquals(exhibits1.get(0).getIsVisited(), test1.getIsVisited());

        assertEquals(exhibits1.get(1).getId(), test3.getId());
        assertEquals(exhibits1.get(1).getIsAdded(), test3.getIsAdded());
        assertEquals(exhibits1.get(1).getIsVisited(), test3.getIsVisited());

        List<ExhibitStatus> exhibits2 = dao.getVisited(false);
        assertEquals(exhibits2.get(0).getId(), test2.getId());
        assertEquals(exhibits2.get(0).getIsAdded(), test2.getIsAdded());
        assertEquals(exhibits2.get(0).getIsVisited(), test2.getIsVisited());
    }
}
