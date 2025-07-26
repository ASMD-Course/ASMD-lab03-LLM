package io.github.asmd23.task1.claude.prompt1;

import io.github.asmd23.task2.Timetable;
import io.github.asmd23.task2.TimetableFactory;

import java.util.*;
import java.util.function.BiFunction;

public class TimetableFactoryImpl implements TimetableFactory {

    @Override
    public Timetable empty() {
        return new TimetableImpl(Map.of(), Set.of(), Set.of());
    }

    @Override
    public Timetable single(String activity, String day) {
        return empty().addHour(activity, day);
    }

    @Override
    public Timetable join(Timetable table1, Timetable table2) {
        var result = new HashMap<ActivityDay, Integer>();
        var allActivities = new HashSet<String>();
        var allDays = new HashSet<String>();

        allActivities.addAll(table1.activities());
        allActivities.addAll(table2.activities());
        allDays.addAll(table1.days());
        allDays.addAll(table2.days());

        addTableToMap(table1, result);
        addTableToMap(table2, result);

        return new TimetableImpl(result, allActivities, allDays);
    }

    @Override
    public Timetable cut(Timetable table, BiFunction<String, String, Integer> bounds) {
        var result = new HashMap<ActivityDay, Integer>();
        var activities = new HashSet<>(table.activities());
        var days = new HashSet<>(table.days());

        for (var activity : activities) {
            for (var day : days) {
                var currentHours = table.getSingleData(activity, day);
                var maxAllowed = bounds.apply(activity, day);
                var hoursToKeep = Math.min(currentHours, Math.max(0, maxAllowed));

                if (hoursToKeep > 0) {
                    result.put(new ActivityDay(activity, day), hoursToKeep);
                }
            }
        }
        return new TimetableImpl(result, activities, days);
    }

    private void addTableToMap(Timetable table, Map<ActivityDay, Integer> targetMap) {
        for (var activity : table.activities()) {
            for (var day : table.days()) {
                var hours = table.getSingleData(activity, day);
                if (hours > 0) {
                    var key = new ActivityDay(activity, day);
                    targetMap.merge(key, hours, Integer::sum);
                }
            }
        }
    }

    private record ActivityDay(String activity, String day) {}

    private record TimetableImpl(
            Map<ActivityDay, Integer> data,
            Set<String> activities,
            Set<String> days) implements Timetable {

        public TimetableImpl(Map<ActivityDay, Integer> data, Set<String> activities, Set<String> days) {
            this.data = Map.copyOf(data);
            this.activities = Set.copyOf(activities);
            this.days = Set.copyOf(days);
        }

        @Override
        public Timetable addHour(String activity, String day) {
            var newData = new HashMap<>(data);
            var key = new ActivityDay(activity, day);
            newData.merge(key, 1, Integer::sum);

            var newActivities = new HashSet<>(activities);
            var newDays = new HashSet<>(days);
            newActivities.add(activity);
            newDays.add(day);

            return new TimetableImpl(newData, newActivities, newDays);
        }

        @Override
        public Set<String> activities() {
            return activities;
        }

        @Override
        public Set<String> days() {
            return days;
        }

        @Override
        public int getSingleData(String activity, String day) {
            return data.getOrDefault(new ActivityDay(activity, day), 0);
        }

        @Override
        public int sums(Set<String> activities, Set<String> days) {
            return activities.stream()
                    .flatMapToInt(activity ->
                            days.stream().mapToInt(day -> getSingleData(activity, day)))
                    .sum();
        }
    }
}