package io.github.asmd23.task2.claude;


import io.github.asmd23.task2.Timetable;
import io.github.asmd23.task2.TimetableFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.function.BiFunction;

/**
 * Unit tests for the TimetableFactory interface.
 * Note: These tests assume you have a concrete implementation of TimetableFactory.
 * Replace 'new ConcreteImplementation()' with your actual implementation.
 */
public class TimetableFactoryTest {

    private TimetableFactory factory;

    @BeforeEach
    void setUp() {
        // Replace with your actual factory implementation
        // factory = new ConcreteTimetableFactory();

        // For testing purposes, using a mock implementation
        //factory = createMockFactory();
    }

    @Nested
    @DisplayName("empty() tests")
    class EmptyTests {

        @Test
        @DisplayName("Empty timetable has no activities")
        void emptyTimetableHasNoActivities() {
            Timetable empty = factory.empty();

            assertNotNull(empty);
            assertTrue(empty.activities().isEmpty());
        }

        @Test
        @DisplayName("Empty timetable has no days")
        void emptyTimetableHasNoDays() {
            Timetable empty = factory.empty();

            assertTrue(empty.days().isEmpty());
        }

        @Test
        @DisplayName("Empty timetable returns 0 for any activity-day combination")
        void emptyTimetableReturnsZeroForAnyData() {
            Timetable empty = factory.empty();

            assertEquals(0, empty.getSingleData("work", "monday"));
            assertEquals(0, empty.getSingleData("study", "friday"));
        }

        @Test
        @DisplayName("Empty timetable sums return 0")
        void emptyTimetableSumsReturnZero() {
            Timetable empty = factory.empty();

            assertEquals(0, empty.sums(Set.of("work"), Set.of("monday")));
            assertEquals(0, empty.sums(Set.of("work", "study"), Set.of("monday", "tuesday")));
        }

        @Test
        @DisplayName("Multiple calls to empty() return equivalent timetables")
        void multipleCallsToEmptyReturnEquivalentTimetables() {
            Timetable empty1 = factory.empty();
            Timetable empty2 = factory.empty();

            assertEquals(empty1.activities(), empty2.activities());
            assertEquals(empty1.days(), empty2.days());
            assertEquals(empty1.getSingleData("work", "monday"),
                    empty2.getSingleData("work", "monday"));
        }
    }

    @Nested
    @DisplayName("single() tests")
    class SingleTests {

        @Test
        @DisplayName("Single timetable contains specified activity")
        void singleTimetableContainsSpecifiedActivity() {
            Timetable single = factory.single("work", "monday");

            assertTrue(single.activities().contains("work"));
            assertEquals(1, single.activities().size());
        }

        @Test
        @DisplayName("Single timetable contains specified day")
        void singleTimetableContainsSpecifiedDay() {
            Timetable single = factory.single("work", "monday");

            assertTrue(single.days().contains("monday"));
            assertEquals(1, single.days().size());
        }

        @Test
        @DisplayName("Single timetable has exactly one hour for specified activity-day")
        void singleTimetableHasOneHourForSpecifiedActivityDay() {
            Timetable single = factory.single("work", "monday");

            assertEquals(1, single.getSingleData("work", "monday"));
        }

        @Test
        @DisplayName("Single timetable returns 0 for other activity-day combinations")
        void singleTimetableReturnsZeroForOtherCombinations() {
            Timetable single = factory.single("work", "monday");

            assertEquals(0, single.getSingleData("study", "monday"));
            assertEquals(0, single.getSingleData("work", "tuesday"));
            assertEquals(0, single.getSingleData("study", "tuesday"));
        }

        @Test
        @DisplayName("Single with null activity throws exception")
        void singleWithNullActivityThrowsException() {
            assertThrows(IllegalArgumentException.class, () -> {
                factory.single(null, "monday");
            });
        }

        @Test
        @DisplayName("Single with null day throws exception")
        void singleWithNullDayThrowsException() {
            assertThrows(IllegalArgumentException.class, () -> {
                factory.single("work", null);
            });
        }

        @Test
        @DisplayName("Single timetable sums correctly")
        void singleTimetableSumsCorrectly() {
            Timetable single = factory.single("work", "monday");

            assertEquals(1, single.sums(Set.of("work"), Set.of("monday")));
            assertEquals(0, single.sums(Set.of("study"), Set.of("monday")));
            assertEquals(0, single.sums(Set.of("work"), Set.of("tuesday")));
        }
    }

    @Nested
    @DisplayName("join() tests")
    class JoinTests {

        @Test
        @DisplayName("Join two empty timetables results in empty timetable")
        void joinTwoEmptyTimetablesResultsInEmpty() {
            Timetable empty1 = factory.empty();
            Timetable empty2 = factory.empty();
            Timetable joined = factory.join(empty1, empty2);

            assertTrue(joined.activities().isEmpty());
            assertTrue(joined.days().isEmpty());
        }

        @Test
        @DisplayName("Join empty with single timetable results in equivalent to single")
        void joinEmptyWithSingleResultsInEquivalentToSingle() {
            Timetable empty = factory.empty();
            Timetable single = factory.single("work", "monday");
            Timetable joined = factory.join(empty, single);

            assertEquals(single.activities(), joined.activities());
            assertEquals(single.days(), joined.days());
            assertEquals(1, joined.getSingleData("work", "monday"));
        }

        @Test
        @DisplayName("Join single with empty timetable results in equivalent to single")
        void joinSingleWithEmptyResultsInEquivalentToSingle() {
            Timetable single = factory.single("work", "monday");
            Timetable empty = factory.empty();
            Timetable joined = factory.join(single, empty);

            assertEquals(single.activities(), joined.activities());
            assertEquals(single.days(), joined.days());
            assertEquals(1, joined.getSingleData("work", "monday"));
        }

        @Test
        @DisplayName("Join two different single timetables combines activities and days")
        void joinTwoDifferentSingleTimetablesCombinesActivitiesAndDays() {
            Timetable single1 = factory.single("work", "monday");
            Timetable single2 = factory.single("study", "tuesday");
            Timetable joined = factory.join(single1, single2);

            assertTrue(joined.activities().contains("work"));
            assertTrue(joined.activities().contains("study"));
            assertTrue(joined.days().contains("monday"));
            assertTrue(joined.days().contains("tuesday"));
            assertEquals(1, joined.getSingleData("work", "monday"));
            assertEquals(1, joined.getSingleData("study", "tuesday"));
        }

        @Test
        @DisplayName("Join same activity-day combinations sums hours")
        void joinSameActivityDayCombinationsSumsHours() {
            Timetable single1 = factory.single("work", "monday");
            Timetable single2 = factory.single("work", "monday");
            Timetable joined = factory.join(single1, single2);

            assertEquals(2, joined.getSingleData("work", "monday"));
            assertEquals(1, joined.activities().size());
            assertEquals(1, joined.days().size());
        }

        @Test
        @DisplayName("Join with overlapping activities and days")
        void joinWithOverlappingActivitiesAndDays() {
            Timetable table1 = factory.single("work", "monday").addHour("study", "monday");
            Timetable table2 = factory.single("work", "tuesday").addHour("work", "monday");
            Timetable joined = factory.join(table1, table2);

            assertTrue(joined.activities().contains("work"));
            assertTrue(joined.activities().contains("study"));
            assertTrue(joined.days().contains("monday"));
            assertTrue(joined.days().contains("tuesday"));
            assertEquals(2, joined.getSingleData("work", "monday")); // 1 + 1
            assertEquals(1, joined.getSingleData("study", "monday"));
            assertEquals(1, joined.getSingleData("work", "tuesday"));
        }

        @Test
        @DisplayName("Join with null first table throws exception")
        void joinWithNullFirstTableThrowsException() {
            Timetable single = factory.single("work", "monday");

            assertThrows(IllegalArgumentException.class, () -> {
                factory.join(null, single);
            });
        }

        @Test
        @DisplayName("Join with null second table throws exception")
        void joinWithNullSecondTableThrowsException() {
            Timetable single = factory.single("work", "monday");

            assertThrows(IllegalArgumentException.class, () -> {
                factory.join(single, null);
            });
        }

        @Test
        @DisplayName("Join is commutative")
        void joinIsCommutative() {
            Timetable table1 = factory.single("work", "monday");
            Timetable table2 = factory.single("study", "tuesday");

            Timetable joined1 = factory.join(table1, table2);
            Timetable joined2 = factory.join(table2, table1);

            assertEquals(joined1.activities(), joined2.activities());
            assertEquals(joined1.days(), joined2.days());
            assertEquals(joined1.getSingleData("work", "monday"),
                    joined2.getSingleData("work", "monday"));
            assertEquals(joined1.getSingleData("study", "tuesday"),
                    joined2.getSingleData("study", "tuesday"));
        }
    }

    @Nested
    @DisplayName("cut() tests")
    class CutTests {

        @Test
        @DisplayName("Cut empty timetable returns empty timetable")
        void cutEmptyTimetableReturnsEmptyTimetable() {
            Timetable empty = factory.empty();
            BiFunction<String, String, Integer> bounds = (a, d) -> 5;
            Timetable cut = factory.cut(empty, bounds);

            assertTrue(cut.activities().isEmpty());
            assertTrue(cut.days().isEmpty());
        }

        @Test
        @DisplayName("Cut with bounds higher than actual hours leaves unchanged")
        void cutWithBoundsHigherThanActualLeavesUnchanged() {
            Timetable single = factory.single("work", "monday");
            BiFunction<String, String, Integer> bounds = (a, d) -> 5;
            Timetable cut = factory.cut(single, bounds);

            assertEquals(1, cut.getSingleData("work", "monday"));
            assertEquals(single.activities(), cut.activities());
            assertEquals(single.days(), cut.days());
        }

        @Test
        @DisplayName("Cut with bounds lower than actual hours reduces hours")
        void cutWithBoundsLowerThanActualReducesHours() {
            // Create a timetable with multiple hours
            Timetable table = factory.single("work", "monday")
                    .addHour("work", "monday")
                    .addHour("work", "monday"); // 3 hours total

            BiFunction<String, String, Integer> bounds = (a, d) -> 2;
            Timetable cut = factory.cut(table, bounds);

            assertEquals(2, cut.getSingleData("work", "monday"));
        }

        @Test
        @DisplayName("Cut with zero bounds removes all hours")
        void cutWithZeroBoundsRemovesAllHours() {
            Timetable single = factory.single("work", "monday");
            BiFunction<String, String, Integer> bounds = (a, d) -> 0;
            Timetable cut = factory.cut(single, bounds);

            assertEquals(0, cut.getSingleData("work", "monday"));
            // Activities and days might still be present depending on implementation
        }

        @Test
        @DisplayName("Cut with different bounds for different activities")
        void cutWithDifferentBoundsForDifferentActivities() {
            Timetable table = factory.single("work", "monday")
                    .addHour("work", "monday")
                    .addHour("study", "monday")
                    .addHour("study", "monday")
                    .addHour("study", "monday"); // work: 2, study: 3

            BiFunction<String, String, Integer> bounds = (activity, day) ->
                    "work".equals(activity) ? 1 : 2;

            Timetable cut = factory.cut(table, bounds);

            assertEquals(1, cut.getSingleData("work", "monday"));
            assertEquals(2, cut.getSingleData("study", "monday"));
        }

        @Test
        @DisplayName("Cut with different bounds for different days")
        void cutWithDifferentBoundsForDifferentDays() {
            Timetable table = factory.single("work", "monday")
                    .addHour("work", "monday")
                    .addHour("work", "tuesday")
                    .addHour("work", "tuesday")
                    .addHour("work", "tuesday"); // monday: 2, tuesday: 3

            BiFunction<String, String, Integer> bounds = (activity, day) ->
                    "monday".equals(day) ? 1 : 2;

            Timetable cut = factory.cut(table, bounds);

            assertEquals(1, cut.getSingleData("work", "monday"));
            assertEquals(2, cut.getSingleData("work", "tuesday"));
        }
    }
}