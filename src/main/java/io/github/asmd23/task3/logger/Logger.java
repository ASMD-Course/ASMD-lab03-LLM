package io.github.asmd23.task3.logger;

import java.util.function.BiConsumer;

public interface Logger {
    void log(Level level, String message);

    /**
     * Creates a Logger instance that uses the provided BiConsumer to log messages.
     *
     * @param logFunction a BiConsumer that takes a Level and a String message
     * @return a Logger instance that logs messages using the provided function
     */
    static Logger fromFunction(BiConsumer<Level, String> logFunction) {
        return logFunction::accept;
    }

    /**
     * Logs an informational message.
     *
     * @param message the informational message to log
     */
    default void info(String message) {
        this.log(Level.INFO, message);
    }

    /**
     * Logs a debug message.
     *
     * @param message the debug message to log
     */
    default void debug(String message) {
        this.log(Level.DEBUG, message);
    }

    /**
     * Logs a warning message.
     *
     * @param message the warning message to log
     */
    default void warn(String message) {
        this.log(Level.WARN, message);
    }

    /**
     * Logs an error message.
     *
     * @param message the error message to log
     */
    default void error(String message) {
        this.log(Level.ERROR, message);
    }
}
