package io.github.asmd23.task2.junie;

import io.github.asmd23.task2.Timetable;
import io.github.asmd23.task2.TimetableFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Test class for the TimetableFactory interface.
 */
public class TimetableFactoryTest {

    private TimetableFactory factory;
    
    @BeforeEach
    public void setUp() {
        // Initialize a TimetableFactory for testing
        //factory = new SimpleTimetableFactory();
    }
    
    @Test
    public void testEmpty() {
        // Test creating an empty timetable
        Timetable table = factory.empty();
        
        // Test that it has no activities and no days
        assertTrue(table.activities().isEmpty());
        assertTrue(table.days().isEmpty());
        
        // Test that getSingleData returns 0 for any activity and day
        assertEquals(0, table.getSingleData("activity", "day"));
        
        // Test that sums returns 0 for any set of activities and days
        assertEquals(0, table.sums(Set.of("activity"), Set.of("day")));
    }
    
    @Test
    public void testSingle() {
        // Test creating a timetable with a single hour
        Timetable table = factory.single("activity1", "day1");
        
        // Test that it has one activity and one day
        assertEquals(Set.of("activity1"), table.activities());
        assertEquals(Set.of("day1"), table.days());
        
        // Test that the activity and day have one hour
        assertEquals(1, table.getSingleData("activity1", "day1"));
        
        // Test that other activities and days have 0 hours
        assertEquals(0, table.getSingleData("activity2", "day1"));
        assertEquals(0, table.getSingleData("activity1", "day2"));
        
        // Test that the sum is 1
        assertEquals(1, table.sums(Set.of("activity1"), Set.of("day1")));
    }
    
    @Test
    public void testJoin() {
        // Create two timetables to join
        Timetable table1 = factory.empty()
                .addHour("activity1", "day1")
                .addHour("activity1", "day1")
                .addHour("activity2", "day2");
        
        Timetable table2 = factory.empty()
                .addHour("activity2", "day1")
                .addHour("activity2", "day2")
                .addHour("activity1", "day3")
                .addHour("activity3", "day3");
        
        // Join the timetables
        Timetable joined = factory.join(table1, table2);
        
        // Test that the activities and days are the union
        assertEquals(Set.of("activity1", "activity2", "activity3"), joined.activities());
        assertEquals(Set.of("day1", "day2", "day3"), joined.days());
        
        // Test that the hours are summed correctly
        assertEquals(2, joined.getSingleData("activity1", "day1"));
        assertEquals(2, joined.getSingleData("activity2", "day2"));
        assertEquals(1, joined.getSingleData("activity2", "day1"));
        assertEquals(1, joined.getSingleData("activity1", "day3"));
        assertEquals(1, joined.getSingleData("activity3", "day3"));
        
        // Test that non-existent activities and days still have 0 hours
        assertEquals(0, joined.getSingleData("activity3", "day1"));
        assertEquals(0, joined.getSingleData("activity1", "day4"));
        
        // Test the total sum
        assertEquals(7, joined.sums(Set.of("activity1", "activity2", "activity3"), Set.of("day1", "day2", "day3")));
    }
    
    @Test
    public void testCut() {
        // Create a timetable with various hours
        Timetable table = factory.empty()
                .addHour("activity1", "day1")
                .addHour("activity1", "day1")
                .addHour("activity1", "day1")
                .addHour("activity1", "day2")
                .addHour("activity1", "day3")
                .addHour("activity1", "day3")
                .addHour("activity2", "day1")
                .addHour("activity2", "day1")
                .addHour("activity3", "day2")
                .addHour("activity3", "day3");
        
        // Cut the timetable to a maximum of 1 hour per activity per day
        Timetable cut = factory.cut(table, (a, d) -> 1);
        
        // Test that the activities and days are preserved
        assertEquals(Set.of("activity1", "activity2", "activity3"), cut.activities());
        assertEquals(Set.of("day1", "day2", "day3"), cut.days());
        
        // Test that the hours are limited correctly
        assertEquals(1, cut.getSingleData("activity1", "day1"));
        assertEquals(1, cut.getSingleData("activity1", "day2"));
        assertEquals(1, cut.getSingleData("activity1", "day3"));
        assertEquals(1, cut.getSingleData("activity2", "day1"));
        assertEquals(1, cut.getSingleData("activity3", "day2"));
        assertEquals(1, cut.getSingleData("activity3", "day3"));
        
        // Test the total sum
        assertEquals(6, cut.sums(Set.of("activity1", "activity2", "activity3"), Set.of("day1", "day2", "day3")));
        
        // Cut the timetable to 0 hours
        cut = factory.cut(table, (a, d) -> 0);
        
        // Test that the activities and days are preserved
        assertEquals(Set.of("activity1", "activity2", "activity3"), cut.activities());
        assertEquals(Set.of("day1", "day2", "day3"), cut.days());
        
        // Test that all hours are 0
        assertEquals(0, cut.getSingleData("activity1", "day1"));
        assertEquals(0, cut.getSingleData("activity1", "day2"));
        assertEquals(0, cut.getSingleData("activity1", "day3"));
        assertEquals(0, cut.getSingleData("activity2", "day1"));
        assertEquals(0, cut.getSingleData("activity3", "day2"));
        assertEquals(0, cut.getSingleData("activity3", "day3"));
        
        // Test the total sum
        assertEquals(0, cut.sums(Set.of("activity1", "activity2", "activity3"), Set.of("day1", "day2", "day3")));
        
        // Cut the timetable with a variable bound
        cut = factory.cut(table, (a, d) -> {
            if (a.equals("activity1") && d.equals("day1")) {
                return 2; // Allow 2 hours for activity1 on day1
            } else {
                return 1; // Allow 1 hour for everything else
            }
        });
        
        // Test that the hours are limited correctly
        assertEquals(2, cut.getSingleData("activity1", "day1"));
        assertEquals(1, cut.getSingleData("activity1", "day2"));
        assertEquals(1, cut.getSingleData("activity1", "day3"));
        assertEquals(1, cut.getSingleData("activity2", "day1"));
        assertEquals(1, cut.getSingleData("activity3", "day2"));
        assertEquals(1, cut.getSingleData("activity3", "day3"));
        
        // Test the total sum
        assertEquals(7, cut.sums(Set.of("activity1", "activity2", "activity3"), Set.of("day1", "day2", "day3")));
    }
}