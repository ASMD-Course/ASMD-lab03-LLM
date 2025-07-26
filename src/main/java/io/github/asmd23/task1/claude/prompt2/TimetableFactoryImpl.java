package io.github.asmd23.task1.claude.prompt2;

import io.github.asmd23.task2.Pair;
import io.github.asmd23.task2.Timetable;
import io.github.asmd23.task2.TimetableFactory;

import java.util.*;
import java.util.function.BiFunction;

public class TimetableFactoryImpl implements TimetableFactory {

    @Override
    public Timetable empty() {
        return new TimetableImpl();
    }

    @Override
    public Timetable single(String activity, String day) {
        return new TimetableImpl().addHour(activity, day);
    }

    @Override
    public Timetable join(Timetable table1, Timetable table2) {
        TimetableImpl result = new TimetableImpl();

        // Get all activities and days from both tables
        Set<String> allActivities = new HashSet<>(table1.activities());
        allActivities.addAll(table2.activities());

        Set<String> allDays = new HashSet<>(table1.days());
        allDays.addAll(table2.days());

        // Sum hours from both tables for each activity-day combination
        Map<Pair<String, String>, Integer> data = new HashMap<>();
        for (String activity : allActivities) {
            for (String day : allDays) {
                int hours1 = table1.getSingleData(activity, day);
                int hours2 = table2.getSingleData(activity, day);
                int totalHours = hours1 + hours2;
                if (totalHours > 0) {
                    data.put(new Pair<>(activity, day), totalHours);
                }
            }
        }

        return new TimetableImpl(data);
    }

    @Override
    public Timetable cut(Timetable table, BiFunction<String, String, Integer> bounds) {
        Map<Pair<String, String>, Integer> data = new HashMap<>();

        for (String activity : table.activities()) {
            for (String day : table.days()) {
                int currentHours = table.getSingleData(activity, day);
                int maxHours = bounds.apply(activity, day);
                int limitedHours = Math.min(currentHours, maxHours);
                if (limitedHours > 0) {
                    data.put(new Pair<>(activity, day), limitedHours);
                }
            }
        }

        return new TimetableImpl(data);
    }

    private static class TimetableImpl implements Timetable {
        private final Map<Pair<String, String>, Integer> data;

        public TimetableImpl() {
            this.data = new HashMap<>();
        }

        public TimetableImpl(Map<Pair<String, String>, Integer> data) {
            this.data = new HashMap<>(data);
        }

        @Override
        public Timetable addHour(String activity, String day) {
            Map<Pair<String, String>, Integer> newData = new HashMap<>(this.data);
            Pair<String, String> key = new Pair<>(activity, day);
            newData.put(key, newData.getOrDefault(key, 0) + 1);
            return new TimetableImpl(newData);
        }

        @Override
        public Set<String> activities() {
            Set<String> activities = new HashSet<>();
            for (Pair<String, String> key : data.keySet()) {
                activities.add(key.get1());
            }
            return activities;
        }

        @Override
        public Set<String> days() {
            Set<String> days = new HashSet<>();
            for (Pair<String, String> key : data.keySet()) {
                days.add(key.get2());
            }
            return days;
        }

        @Override
        public int getSingleData(String activity, String day) {
            return data.getOrDefault(new Pair<>(activity, day), 0);
        }

        @Override
        public int sums(Set<String> activities, Set<String> days) {
            int total = 0;
            for (String activity : activities) {
                for (String day : days) {
                    total += getSingleData(activity, day);
                }
            }
            return total;
        }
    }
}
