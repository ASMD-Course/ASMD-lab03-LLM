package io.github.asmd23.task1.chatgpt.prompt1;

import io.github.asmd23.task2.Timetable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class TimetableImpl implements Timetable {
    private final Map<String, Map<String, Integer>> data;

    public TimetableImpl() {
        this.data = new HashMap<>();
    }

    public TimetableImpl(Map<String, Map<String, Integer>> data) {
        // Deep copy to preserve immutability
//        this.data = data.entrySet().stream().collect(Collectors.toMap(
//                Map.Entry::getKey,
//                e -> new HashMap<>(e.getValue())
//        ));
        this.data = Map.copyOf(data);
    }

    @Override
    public Timetable addHour(String activity, String day) {
        Map<String, Map<String, Integer>> newData = new HashMap<>(this.data);
        newData.putIfAbsent(activity, new HashMap<>());
        Map<String, Integer> inner = new HashMap<>(newData.get(activity));
        inner.put(day, inner.getOrDefault(day, 0) + 1);
        newData.put(activity, inner);
        return new TimetableImpl(newData);
    }

    @Override
    public Set<String> activities() {
        return Collections.unmodifiableSet(data.keySet());
    }

    @Override
    public Set<String> days() {
        return Collections.unmodifiableSet(
                data.values().stream()
                        .flatMap(m -> m.keySet().stream())
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public int getSingleData(String activity, String day) {
        return data.getOrDefault(activity, Map.of()).getOrDefault(day, 0);
    }

    @Override
    public int sums(Set<String> activities, Set<String> days) {
        return activities.stream()
                .mapToInt(act -> days.stream()
                        .mapToInt(day -> getSingleData(act, day))
                        .sum())
                .sum();
    }
}
