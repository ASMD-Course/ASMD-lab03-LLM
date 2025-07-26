package io.github.asmd23.task2.chatgpt;

import io.github.asmd23.task2.Timetable;
import io.github.asmd23.task2.TimetableFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TimetableFactoryTest {

    private TimetableFactory factory;

    @BeforeEach
    void setUp() {
        //factory = new TimetableFactoryImpl(); // Replace with your implementation
    }

    @Test
    void testEmptyTimetableIsEmpty() {
        Timetable table = factory.empty();
        assertTrue(table.activities().isEmpty());
        assertTrue(table.days().isEmpty());
    }

    @Test
    void testSingleTimetable() {
        Timetable table = factory.single("Run", "Friday");
        assertEquals(1, table.getSingleData("Run", "Friday"));
        assertEquals(Set.of("Run"), table.activities());
        assertEquals(Set.of("Friday"), table.days());
    }
}
