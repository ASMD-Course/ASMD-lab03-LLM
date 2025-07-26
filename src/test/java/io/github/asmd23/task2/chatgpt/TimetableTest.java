package io.github.asmd23.task2.chatgpt;

import io.github.asmd23.task2.Timetable;
import io.github.asmd23.task2.TimetableFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TimetableTest {

    private TimetableFactory factory;
    private Timetable timetable;

    @BeforeEach
    void setUp() {
        //factory = new TimetableFactoryImpl(); // Replace with your implementation
        timetable = factory.empty();
    }

    @Test
    void testAddHourAddsActivityAndDay() {
        timetable = timetable.addHour("Yoga", "Monday");

        assertTrue(timetable.activities().contains("Yoga"));
        assertTrue(timetable.days().contains("Monday"));
        assertEquals(1, timetable.getSingleData("Yoga", "Monday"));
    }

    @Test
    void testAddHourMultipleTimesSameSlot() {
        timetable = timetable.addHour("Study", "Tuesday")
                .addHour("Study", "Tuesday")
                .addHour("Study", "Tuesday");

        assertEquals(3, timetable.getSingleData("Study", "Tuesday"));
        assertEquals(Set.of("Study"), timetable.activities());
        assertEquals(Set.of("Tuesday"), timetable.days());
    }

    @Test
    void testAddHourDifferentSlots() {
        timetable = timetable.addHour("Gym", "Wed")
                .addHour("Work", "Wed")
                .addHour("Gym", "Thu");

        assertEquals(1, timetable.getSingleData("Gym", "Wed"));
        assertEquals(1, timetable.getSingleData("Work", "Wed"));
        assertEquals(1, timetable.getSingleData("Gym", "Thu"));

        assertEquals(Set.of("Gym", "Work"), timetable.activities());
        assertEquals(Set.of("Wed", "Thu"), timetable.days());
    }

    @Test
    void testSumsForActivitiesAndDays() {
        timetable = timetable.addHour("A", "D1")
                .addHour("A", "D1")
                .addHour("A", "D2")
                .addHour("B", "D1");

        assertEquals(4, timetable.sums(Set.of("A", "B"), Set.of("D1", "D2")));
        assertEquals(3, timetable.sums(Set.of("A"), Set.of("D1", "D2")));
        assertEquals(2, timetable.sums(Set.of("A", "B"), Set.of("D1")));
        assertEquals(0, timetable.sums(Set.of("C"), Set.of("D3"))); // non-existing
    }
}

