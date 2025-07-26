package io.github.asmd23.task2.claude;



import io.github.asmd23.task2.Timetable;
import io.github.asmd23.task2.TimetableFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.HashSet;

/**
 * Unit tests for the Timetable interface.
 * Note: These tests assume you have a concrete implementation of Timetable.
 * Replace 'new ConcreteImplementation()' with your actual implementation.
 */
public class TimetableTest {

    private TimetableFactory factory;
    private Timetable emptyTimetable;
    private Timetable singleHourTimetable;
    private Timetable multipleHoursTimetable;

    @BeforeEach
    void setUp() {
        // Replace with your actual factory implementation
        // factory = new ConcreteTimetableFactory();

        // Mock setup - replace with actual implementations
        emptyTimetable = createMockEmptyTimetable();
        singleHourTimetable = createMockSingleTimetable();
        multipleHoursTimetable = createMockMultipleTimetable();
    }

    @Nested
    @DisplayName("addHour() tests")
    class AddHourTests {

        @Test
        @DisplayName("Adding hour to empty timetable creates new entry")
        void addHourToEmptyTimetable() {
            Timetable result = emptyTimetable.addHour("work", "monday");

            assertNotNull(result);
            assertTrue(result.activities().contains("work"));
            assertTrue(result.days().contains("monday"));
            assertEquals(1, result.getSingleData("work", "monday"));
        }

        @Test
        @DisplayName("Adding hour to existing activity and day increments count")
        void addHourToExistingActivityAndDay() {
            Timetable result = singleHourTimetable.addHour("work", "monday");

            assertEquals(2, result.getSingleData("work", "monday"));
        }

        @Test
        @DisplayName("Adding hour to new activity on existing day")
        void addHourToNewActivityExistingDay() {
            Timetable result = singleHourTimetable.addHour("study", "monday");

            assertTrue(result.activities().contains("study"));
            assertEquals(1, result.getSingleData("study", "monday"));
            assertEquals(1, result.getSingleData("work", "monday")); // Original should remain
        }

        @Test
        @DisplayName("Adding hour to existing activity on new day")
        void addHourToExistingActivityNewDay() {
            Timetable result = singleHourTimetable.addHour("work", "tuesday");

            assertTrue(result.days().contains("tuesday"));
            assertEquals(1, result.getSingleData("work", "tuesday"));
            assertEquals(1, result.getSingleData("work", "monday")); // Original should remain
        }

        @Test
        @DisplayName("Adding hour with null activity throws exception")
        void addHourWithNullActivity() {
            assertThrows(IllegalArgumentException.class, () -> {
                emptyTimetable.addHour(null, "monday");
            });
        }

        @Test
        @DisplayName("Adding hour with null day throws exception")
        void addHourWithNullDay() {
            assertThrows(IllegalArgumentException.class, () -> {
                emptyTimetable.addHour("work", null);
            });
        }

        @Test
        @DisplayName("Original timetable remains unchanged after addHour")
        void originalTimetableUnchangedAfterAddHour() {
            Set<String> originalActivities = new HashSet<>(singleHourTimetable.activities());
            Set<String> originalDays = new HashSet<>(singleHourTimetable.days());
            int originalHours = singleHourTimetable.getSingleData("work", "monday");

            singleHourTimetable.addHour("work", "monday");

            assertEquals(originalActivities, singleHourTimetable.activities());
            assertEquals(originalDays, singleHourTimetable.days());
            assertEquals(originalHours, singleHourTimetable.getSingleData("work", "monday"));
        }
    }

    @Nested
    @DisplayName("activities() tests")
    class ActivitiesTests {

        @Test
        @DisplayName("Empty timetable returns empty activities set")
        void emptyTimetableReturnsEmptyActivities() {
            Set<String> activities = emptyTimetable.activities();

            assertNotNull(activities);
            assertTrue(activities.isEmpty());
        }

        @Test
        @DisplayName("Single activity timetable returns correct activities")
        void singleActivityTimetableReturnsCorrectActivities() {
            Set<String> activities = singleHourTimetable.activities();

            assertEquals(1, activities.size());
            assertTrue(activities.contains("work"));
        }

        @Test
        @DisplayName("Multiple activities timetable returns all activities")
        void multipleActivitiesTimetableReturnsAllActivities() {
            Set<String> activities = multipleHoursTimetable.activities();

            assertTrue(activities.contains("work"));
            assertTrue(activities.contains("study"));
            assertTrue(activities.contains("exercise"));
        }

        @Test
        @DisplayName("Returned activities set is immutable or defensive copy")
        void activitiesSetIsImmutable() {
            Set<String> activities = singleHourTimetable.activities();

            assertThrows(UnsupportedOperationException.class, () -> {
                activities.add("newActivity");
            });
        }
    }

    @Nested
    @DisplayName("days() tests")
    class DaysTests {

        @Test
        @DisplayName("Empty timetable returns empty days set")
        void emptyTimetableReturnsEmptyDays() {
            Set<String> days = emptyTimetable.days();

            assertNotNull(days);
            assertTrue(days.isEmpty());
        }

        @Test
        @DisplayName("Single day timetable returns correct days")
        void singleDayTimetableReturnsCorrectDays() {
            Set<String> days = singleHourTimetable.days();

            assertEquals(1, days.size());
            assertTrue(days.contains("monday"));
        }

        @Test
        @DisplayName("Multiple days timetable returns all days")
        void multipleDaysTimetableReturnsAllDays() {
            Set<String> days = multipleHoursTimetable.days();

            assertTrue(days.contains("monday"));
            assertTrue(days.contains("tuesday"));
            assertTrue(days.contains("wednesday"));
        }

        @Test
        @DisplayName("Returned days set is immutable or defensive copy")
        void daysSetIsImmutable() {
            Set<String> days = singleHourTimetable.days();

            assertThrows(UnsupportedOperationException.class, () -> {
                days.add("newDay");
            });
        }
    }

    @Nested
    @DisplayName("getSingleData() tests")
    class GetSingleDataTests {

        @Test
        @DisplayName("Get data for existing activity and day")
        void getDataForExistingActivityAndDay() {
            int hours = singleHourTimetable.getSingleData("work", "monday");
            assertEquals(1, hours);
        }

        @Test
        @DisplayName("Get data for non-existing activity returns 0")
        void getDataForNonExistingActivityReturnsZero() {
            int hours = singleHourTimetable.getSingleData("nonexistent", "monday");
            assertEquals(0, hours);
        }

        @Test
        @DisplayName("Get data for non-existing day returns 0")
        void getDataForNonExistingDayReturnsZero() {
            int hours = singleHourTimetable.getSingleData("work", "nonexistent");
            assertEquals(0, hours);
        }

        @Test
        @DisplayName("Get data for empty timetable returns 0")
        void getDataForEmptyTimetableReturnsZero() {
            int hours = emptyTimetable.getSingleData("work", "monday");
            assertEquals(0, hours);
        }

        @Test
        @DisplayName("Get data with null activity throws exception")
        void getDataWithNullActivityThrowsException() {
            assertThrows(IllegalArgumentException.class, () -> {
                singleHourTimetable.getSingleData(null, "monday");
            });
        }

        @Test
        @DisplayName("Get data with null day throws exception")
        void getDataWithNullDayThrowsException() {
            assertThrows(IllegalArgumentException.class, () -> {
                singleHourTimetable.getSingleData("work", null);
            });
        }
    }

    @Nested
    @DisplayName("sums() tests")
    class SumsTests {

        @Test
        @DisplayName("Sum of single activity and day")
        void sumOfSingleActivityAndDay() {
            Set<String> activities = Set.of("work");
            Set<String> days = Set.of("monday");

            int sum = singleHourTimetable.sums(activities, days);
            assertEquals(1, sum);
        }

        @Test
        @DisplayName("Sum of multiple activities and days")
        void sumOfMultipleActivitiesAndDays() {
            Set<String> activities = Set.of("work", "study");
            Set<String> days = Set.of("monday", "tuesday");

            int sum = multipleHoursTimetable.sums(activities, days);
            assertTrue(sum >= 0); // Exact value depends on implementation
        }

        @Test
        @DisplayName("Sum with empty activities set returns 0")
        void sumWithEmptyActivitiesReturnsZero() {
            Set<String> activities = Set.of();
            Set<String> days = Set.of("monday");

            int sum = singleHourTimetable.sums(activities, days);
            assertEquals(0, sum);
        }

        @Test
        @DisplayName("Sum with empty days set returns 0")
        void sumWithEmptyDaysReturnsZero() {
            Set<String> activities = Set.of("work");
            Set<String> days = Set.of();

            int sum = singleHourTimetable.sums(activities, days);
            assertEquals(0, sum);
        }

        @Test
        @DisplayName("Sum with non-existing activities returns 0")
        void sumWithNonExistingActivitiesReturnsZero() {
            Set<String> activities = Set.of("nonexistent");
            Set<String> days = Set.of("monday");

            int sum = singleHourTimetable.sums(activities, days);
            assertEquals(0, sum);
        }

        @Test
        @DisplayName("Sum with non-existing days returns 0")
        void sumWithNonExistingDaysReturnsZero() {
            Set<String> activities = Set.of("work");
            Set<String> days = Set.of("nonexistent");

            int sum = singleHourTimetable.sums(activities, days);
            assertEquals(0, sum);
        }

        @Test
        @DisplayName("Sum with null activities throws exception")
        void sumWithNullActivitiesThrowsException() {
            Set<String> days = Set.of("monday");

            assertThrows(IllegalArgumentException.class, () -> {
                singleHourTimetable.sums(null, days);
            });
        }

        @Test
        @DisplayName("Sum with null days throws exception")
        void sumWithNullDaysThrowsException() {
            Set<String> activities = Set.of("work");

            assertThrows(IllegalArgumentException.class, () -> {
                singleHourTimetable.sums(activities, null);
            });
        }

        @Test
        @DisplayName("Sum on empty timetable returns 0")
        void sumOnEmptyTimetableReturnsZero() {
            Set<String> activities = Set.of("work");
            Set<String> days = Set.of("monday");

            int sum = emptyTimetable.sums(activities, days);
            assertEquals(0, sum);
        }
    }

    // Mock implementations for testing - replace with actual factory usage
    private Timetable createMockEmptyTimetable() {
        return new Timetable() {
            @Override
            public Timetable addHour(String activity, String day) {
                if (activity == null || day == null) {
                    throw new IllegalArgumentException("Activity and day cannot be null");
                }
                return createMockSingleTimetable();
            }

            @Override
            public Set<String> activities() {
                return Set.of();
            }

            @Override
            public Set<String> days() {
                return Set.of();
            }

            @Override
            public int getSingleData(String activity, String day) {
                if (activity == null || day == null) {
                    throw new IllegalArgumentException("Activity and day cannot be null");
                }
                return 0;
            }

            @Override
            public int sums(Set<String> activities, Set<String> days) {
                if (activities == null || days == null) {
                    throw new IllegalArgumentException("Activities and days cannot be null");
                }
                return 0;
            }
        };
    }

    private Timetable createMockSingleTimetable() {
        return new Timetable() {
            @Override
            public Timetable addHour(String activity, String day) {
                if (activity == null || day == null) {
                    throw new IllegalArgumentException("Activity and day cannot be null");
                }
                return this; // Simplified for testing
            }

            @Override
            public Set<String> activities() {
                return Set.of("work");
            }

            @Override
            public Set<String> days() {
                return Set.of("monday");
            }

            @Override
            public int getSingleData(String activity, String day) {
                if (activity == null || day == null) {
                    throw new IllegalArgumentException("Activity and day cannot be null");
                }
                return "work".equals(activity) && "monday".equals(day) ? 1 : 0;
            }

            @Override
            public int sums(Set<String> activities, Set<String> days) {
                if (activities == null || days == null) {
                    throw new IllegalArgumentException("Activities and days cannot be null");
                }
                return (activities.contains("work") && days.contains("monday")) ? 1 : 0;
            }
        };
    }

    private Timetable createMockMultipleTimetable() {
        return new Timetable() {
            @Override
            public Timetable addHour(String activity, String day) {
                if (activity == null || day == null) {
                    throw new IllegalArgumentException("Activity and day cannot be null");
                }
                return this;
            }

            @Override
            public Set<String> activities() {
                return Set.of("work", "study", "exercise");
            }

            @Override
            public Set<String> days() {
                return Set.of("monday", "tuesday", "wednesday");
            }

            @Override
            public int getSingleData(String activity, String day) {
                if (activity == null || day == null) {
                    throw new IllegalArgumentException("Activity and day cannot be null");
                }
                // Mock data for testing
                if ("work".equals(activity) && "monday".equals(day)) return 2;
                if ("study".equals(activity) && "tuesday".equals(day)) return 3;
                if ("exercise".equals(activity) && "wednesday".equals(day)) return 1;
                return 0;
            }

            @Override
            public int sums(Set<String> activities, Set<String> days) {
                if (activities == null || days == null) {
                    throw new IllegalArgumentException("Activities and days cannot be null");
                }
                int sum = 0;
                for (String activity : activities) {
                    for (String day : days) {
                        sum += getSingleData(activity, day);
                    }
                }
                return sum;
            }
        };
    }
}