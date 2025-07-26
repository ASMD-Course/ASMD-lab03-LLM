package io.github.asmd23.task1.chatgpt.prompt2;


import io.github.asmd23.task2.Pair;
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
    public Timetable join(Timetable t1, Timetable t2) {
        TimetableImpl table1 = (TimetableImpl) t1;
        TimetableImpl table2 = (TimetableImpl) t2;

        Map<Pair<String, String>, Integer> joined = new HashMap<>(table1.data);
        table2.data.forEach((k, v) -> joined.merge(k, v, Integer::sum));

        Set<String> acts = new HashSet<>(table1.activities);
        acts.addAll(table2.activities);
        Set<String> dys = new HashSet<>(table1.days);
        dys.addAll(table2.days);

        return new TimetableImpl(joined, acts, dys);
    }

    @Override
    public Timetable cut(Timetable t, BiFunction<String, String, Integer> bounds) {
        TimetableImpl table = (TimetableImpl) t;

        Map<Pair<String, String>, Integer> cutData = new HashMap<>();
        for (var entry : table.data.entrySet()) {
            String act = entry.getKey().get1();
            String day = entry.getKey().get2();
            int max = bounds.apply(act, day);
            int current = entry.getValue();
            if (max > 0) {
                cutData.put(entry.getKey(), Math.min(current, max));
            }
        }

        return new TimetableImpl(cutData, table.activities, table.days);
    }

    private static class TimetableImpl implements Timetable {
        private final Map<Pair<String, String>, Integer> data;
        private final Set<String> activities;
        private final Set<String> days;

        private TimetableImpl(Map<Pair<String, String>, Integer> data, Set<String> acts, Set<String> dys) {
            this.data = Map.copyOf(data);
            this.activities = Set.copyOf(acts);
            this.days = Set.copyOf(dys);
        }

        @Override
        public Timetable addHour(String activity, String day) {
            Map<Pair<String, String>, Integer> newData = new HashMap<>(this.data);
            Pair<String, String> key = new Pair<>(activity, day);
            newData.put(key, newData.getOrDefault(key, 0) + 1);

            Set<String> newActs = new HashSet<>(this.activities);
            newActs.add(activity);
            Set<String> newDays = new HashSet<>(this.days);
            newDays.add(day);

            return new TimetableImpl(newData, newActs, newDays);
        }

        @Override
        public Set<String> activities() {
            return this.activities;
        }

        @Override
        public Set<String> days() {
            return this.days;
        }

        @Override
        public int getSingleData(String activity, String day) {
            return this.data.getOrDefault(new Pair<>(activity, day), 0);
        }

        @Override
        public int sums(Set<String> acts, Set<String> dys) {
            return this.data.entrySet().stream()
                    .filter(e -> acts.contains(e.getKey().get1()) && dys.contains(e.getKey().get2()))
                    .mapToInt(Map.Entry::getValue)
                    .sum();
        }
    }
}