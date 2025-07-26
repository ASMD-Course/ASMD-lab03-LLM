package io.github.asmd23.task2.junie;

import io.github.asmd23.task2.Timetable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Test class for the Timetable interface.
 * 
 * Since Timetable is an interface, we need a concrete implementation for testing.
 */
public class TimetableTest {

    private Timetable emptyTimetable;
    
    @BeforeEach
    public void setUp() {
        // Initialize an empty timetable for testing
        //emptyTimetable = new SimpleTimetable();
    }
    
    @Test
    public void testEmptyTimetable() {
        // Test that an empty timetable has no activities and no days
        assertTrue(emptyTimetable.activities().isEmpty());
        assertTrue(emptyTimetable.days().isEmpty());
        
        // Test that getSingleData returns 0 for any activity and day
        assertEquals(0, emptyTimetable.getSingleData("activity", "day"));
        
        // Test that sums returns 0 for any set of activities and days
        assertEquals(0, emptyTimetable.sums(Set.of("activity"), Set.of("day")));
    }
    
    @Test
    public void testAddHour() {
        // Test adding an hour to an empty timetable
        Timetable timetable = emptyTimetable.addHour("activity1", "day1");
        
        // Test that the activity and day were added
        assertEquals(Set.of("activity1"), timetable.activities());
        assertEquals(Set.of("day1"), timetable.days());
        
        // Test that the hour was added
        assertEquals(1, timetable.getSingleData("activity1", "day1"));
        
        // Test that other activities and days still have 0 hours
        assertEquals(0, timetable.getSingleData("activity2", "day1"));
        assertEquals(0, timetable.getSingleData("activity1", "day2"));
        
        // Test adding another hour to the same activity and day
        timetable = timetable.addHour("activity1", "day1");
        
        // Test that the hour was added
        assertEquals(2, timetable.getSingleData("activity1", "day1"));
        
        // Test adding an hour to a different activity and day
        timetable = timetable.addHour("activity2", "day2");
        
        // Test that the new activity and day were added
        assertEquals(Set.of("activity1", "activity2"), timetable.activities());
        assertEquals(Set.of("day1", "day2"), timetable.days());
        
        // Test that the hour was added
        assertEquals(1, timetable.getSingleData("activity2", "day2"));
    }
    
    @Test
    public void testActivities() {
        // Test that an empty timetable has no activities
        assertTrue(emptyTimetable.activities().isEmpty());
        
        // Test adding hours to different activities
        Timetable timetable = emptyTimetable
                .addHour("activity1", "day1")
                .addHour("activity2", "day1")
                .addHour("activity3", "day2");
        
        // Test that all activities were added
        assertEquals(Set.of("activity1", "activity2", "activity3"), timetable.activities());
        
        // Test that adding hours to existing activities doesn't change the set
        timetable = timetable.addHour("activity1", "day2");
        assertEquals(Set.of("activity1", "activity2", "activity3"), timetable.activities());
    }
    
    @Test
    public void testDays() {
        // Test that an empty timetable has no days
        assertTrue(emptyTimetable.days().isEmpty());
        
        // Test adding hours to different days
        Timetable timetable = emptyTimetable
                .addHour("activity1", "day1")
                .addHour("activity1", "day2")
                .addHour("activity2", "day3");
        
        // Test that all days were added
        assertEquals(Set.of("day1", "day2", "day3"), timetable.days());
        
        // Test that adding hours to existing days doesn't change the set
        timetable = timetable.addHour("activity3", "day1");
        assertEquals(Set.of("day1", "day2", "day3"), timetable.days());
    }
    
    @Test
    public void testGetSingleData() {
        // Create a timetable with various hours
        Timetable timetable = emptyTimetable
                .addHour("activity1", "day1")
                .addHour("activity1", "day1")
                .addHour("activity1", "day2")
                .addHour("activity2", "day1");
        
        // Test getting hours for various activities and days
        assertEquals(2, timetable.getSingleData("activity1", "day1"));
        assertEquals(1, timetable.getSingleData("activity1", "day2"));
        assertEquals(1, timetable.getSingleData("activity2", "day1"));
        
        // Test that non-existent activities and days return 0
        assertEquals(0, timetable.getSingleData("activity3", "day1"));
        assertEquals(0, timetable.getSingleData("activity1", "day3"));
        assertEquals(0, timetable.getSingleData("activity2", "day2"));
    }
    
    @Test
    public void testSums() {
        // Create a timetable with various hours
        Timetable timetable = emptyTimetable
                .addHour("activity1", "day1")
                .addHour("activity1", "day1")
                .addHour("activity1", "day2")
                .addHour("activity2", "day1")
                .addHour("activity2", "day3")
                .addHour("activity3", "day2");
        
        // Test sums for various combinations of activities and days
        assertEquals(3, timetable.sums(Set.of("activity1"), Set.of("day1", "day2")));
        assertEquals(2, timetable.sums(Set.of("activity2"), Set.of("day1", "day2", "day3")));
        assertEquals(3, timetable.sums(Set.of("activity1", "activity2"), Set.of("day1")));
        assertEquals(2, timetable.sums(Set.of("activity1", "activity3"), Set.of("day2")));
        assertEquals(6, timetable.sums(Set.of("activity1", "activity2", "activity3"), Set.of("day1", "day2", "day3")));
        
        // Test that empty sets return 0
        assertEquals(0, timetable.sums(Set.of(), Set.of("day1")));
        assertEquals(0, timetable.sums(Set.of("activity1"), Set.of()));
        assertEquals(0, timetable.sums(Set.of(), Set.of()));
        
        // Test that non-existent activities and days return 0
        assertEquals(0, timetable.sums(Set.of("activity4"), Set.of("day1")));
        assertEquals(0, timetable.sums(Set.of("activity1"), Set.of("day4")));
    }
}