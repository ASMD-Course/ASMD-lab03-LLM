package io.github.asmd23.task1.junie;

import io.github.asmd23.task2.Timetable;
import io.github.asmd23.task2.TimetableFactory;

import java.util.function.BiFunction;

/**
 * Implementation of the TimetableFactory interface.
 * Creates and manipulates Timetable instances.
 */
public class TimetableFactoryImpl implements TimetableFactory {

    @Override
    public Timetable empty() {
        return new TimetableImpl();
    }

    @Override
    public Timetable single(String activity, String day) {
        return empty().addHour(activity, day);
    }

    @Override
    public Timetable join(Timetable table1, Timetable table2) {
        // Start with a copy of table1
        Timetable result = table1;
        
        // Add all activities and days from table2
        for (String activity : table2.activities()) {
            for (String day : table2.days()) {
                int hours = table2.getSingleData(activity, day);
                // Add each hour individually
                for (int i = 0; i < hours; i++) {
                    result = result.addHour(activity, day);
                }
            }
        }
        
        return result;
    }

    @Override
    public Timetable cut(Timetable table, BiFunction<String, String, Integer> bounds) {
        // Start with a timetable that has the same activities and days as the original, but zero hours
        TimetableImpl result = new TimetableImpl(table.activities(), table.days());
        
        // For each activity and day, add hours up to the bound
        for (String activity : table.activities()) {
            for (String day : table.days()) {
                int hours = table.getSingleData(activity, day);
                int maxHours = bounds.apply(activity, day);
                
                // Add hours up to the bound
                int hoursToAdd = Math.min(hours, maxHours);
                Timetable tempResult = result;
                for (int i = 0; i < hoursToAdd; i++) {
                    tempResult = tempResult.addHour(activity, day);
                }
                result = (TimetableImpl) tempResult;
            }
        }
        
        return result;
    }
}