package io.github.asmd23.task1.junie;

import io.github.asmd23.task2.Timetable;

import java.util.*;

/**
 * Implementation of the Timetable interface.
 * This implementation is immutable - all operations return a new Timetable instance.
 */
public class TimetableImpl implements Timetable {
    
    // Using a nested Map structure to store hours by activity and day
    private final Map<String, Map<String, Integer>> data;
    
    /**
     * Creates a new empty timetable.
     */
    public TimetableImpl() {
        this.data = new HashMap<>();
    }
    
    /**
     * Creates a timetable with the specified data.
     * 
     * @param data Map of activities to days to hours
     */
    private TimetableImpl(Map<String, Map<String, Integer>> data) {
        // Create a deep copy of the data to ensure immutability
        this.data = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : data.entrySet()) {
            this.data.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
    }
    
    /**
     * Creates a timetable with the specified activities and days, but zero hours.
     * 
     * @param activities Set of activities
     * @param days Set of days
     */
    public TimetableImpl(Set<String> activities, Set<String> days) {
        this.data = new HashMap<>();
        for (String activity : activities) {
            Map<String, Integer> dayMap = new HashMap<>();
            for (String day : days) {
                dayMap.put(day, 0);
            }
            this.data.put(activity, dayMap);
        }
    }
    
    @Override
    public Timetable addHour(String activity, String day) {
        // Create a copy of the current data
        Map<String, Map<String, Integer>> newData = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : data.entrySet()) {
            newData.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
        
        // Add the hour to the specified activity and day
        newData.computeIfAbsent(activity, k -> new HashMap<>())
               .compute(day, (k, v) -> v == null ? 1 : v + 1);
        
        return new TimetableImpl(newData);
    }
    
    @Override
    public Set<String> activities() {
        return Collections.unmodifiableSet(data.keySet());
    }
    
    @Override
    public Set<String> days() {
        Set<String> allDays = new HashSet<>();
        for (Map<String, Integer> dayMap : data.values()) {
            allDays.addAll(dayMap.keySet());
        }
        return Collections.unmodifiableSet(allDays);
    }
    
    @Override
    public int getSingleData(String activity, String day) {
        Map<String, Integer> dayMap = data.get(activity);
        if (dayMap == null) {
            return 0;
        }
        return dayMap.getOrDefault(day, 0);
    }
    
    @Override
    public int sums(Set<String> activities, Set<String> days) {
        int total = 0;
        for (String activity : activities) {
            Map<String, Integer> dayMap = data.get(activity);
            if (dayMap != null) {
                for (String day : days) {
                    total += dayMap.getOrDefault(day, 0);
                }
            }
        }
        return total;
    }
}