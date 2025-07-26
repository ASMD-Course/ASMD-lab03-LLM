package io.github.asmd23.task1.chatgpt.prompt1;

import io.github.asmd23.task2.Timetable;
import io.github.asmd23.task2.TimetableFactory;

import java.util.function.BiFunction;

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
    public Timetable join(Timetable t1, Timetable t2) {
        Timetable result = t1;
        for (String act : t2.activities()) {
            for (String day : t2.days()) {
                int count = t2.getSingleData(act, day);
                for (int i = 0; i < count; i++) {
                    result = result.addHour(act, day);
                }
            }
        }
        return result;
    }

    @Override
    public Timetable cut(Timetable t, BiFunction<String, String, Integer> bounds) {
        Timetable result = empty();
        for (String act : t.activities()) {
            for (String day : t.days()) {
                int limited = Math.min(t.getSingleData(act, day), bounds.apply(act, day));
                for (int i = 0; i < limited; i++) {
                    result = result.addHour(act, day);
                }
            }
        }
        return result;
    }
}