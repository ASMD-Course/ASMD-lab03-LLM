package io.github.asmd23.task3.logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.function.BiConsumer;

import static org.mockito.Mockito.*;

class LoggerTest {

    @Mock
    private BiConsumer<Level, String> mockLogFunction;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFromFunctionCreatesLoggerThatUsesProvidedFunction() {
        String message = "Test message";
        Level level = Level.INFO;

        Logger logger = Logger.fromFunction(mockLogFunction);
        logger.log(level, message);

        verify(mockLogFunction, times(1)).accept(level, message);
    }

    @Test
    void testFromFunctionWithMultipleCalls() {
        Logger logger = Logger.fromFunction(mockLogFunction);

        logger.log(Level.INFO, "Info message");
        logger.log(Level.ERROR, "Error message");
        logger.log(Level.DEBUG, "Debug message");

        verify(mockLogFunction).accept(Level.INFO, "Info message");
        verify(mockLogFunction).accept(Level.ERROR, "Error message");
        verify(mockLogFunction).accept(Level.DEBUG, "Debug message");
        verify(mockLogFunction, times(3)).accept(any(Level.class), anyString());
    }

    @Test
    void testInfo_CallsLogWithInfoLevel() {
        Logger mockLogger = mock(Logger.class);
        doCallRealMethod().when(mockLogger).info(anyString());
        String message = "Info message";

        mockLogger.info(message);

        verify(mockLogger).log(Level.INFO, message);
    }

    @Test
    void testDebug_CallsLogWithDebugLevel() {
        Logger mockLogger = mock(Logger.class);
        doCallRealMethod().when(mockLogger).debug(anyString());
        String message = "Debug message";
        mockLogger.debug(message);
        verify(mockLogger).log(Level.DEBUG, message);
    }

    @Test
    void testWarn_CallsLogWithWarnLevel() {
        Logger mockLogger = mock(Logger.class);
        doCallRealMethod().when(mockLogger).warn(anyString());
        String message = "Warning message";
        mockLogger.warn(message);
        verify(mockLogger).log(Level.WARN, message);
    }

    @Test
    void testError_CallsLogWithErrorLevel() {
        Logger mockLogger = mock(Logger.class);
        doCallRealMethod().when(mockLogger).error(anyString());
        String message = "Error message";

        mockLogger.error(message);
        verify(mockLogger).log(Level.ERROR, message);
    }

    @Test
    void testAllDefaultMethods_UseCorrectLevels() {
        Logger mockLogger = mock(Logger.class);
        doCallRealMethod().when(mockLogger).info(anyString());
        doCallRealMethod().when(mockLogger).debug(anyString());
        doCallRealMethod().when(mockLogger).warn(anyString());
        doCallRealMethod().when(mockLogger).error(anyString());

        mockLogger.info("Info");
        mockLogger.debug("Debug");
        mockLogger.warn("Warning");
        mockLogger.error("Error");

        verify(mockLogger).log(Level.INFO, "Info");
        verify(mockLogger).log(Level.DEBUG, "Debug");
        verify(mockLogger).log(Level.WARN, "Warning");
        verify(mockLogger).log(Level.ERROR, "Error");
    }

    @Test
    void testFromFunctionWithNullMessage() {
        Logger logger = Logger.fromFunction(mockLogFunction);
        logger.log(Level.INFO, null);
        verify(mockLogFunction).accept(Level.INFO, null);
    }

    @Test
    void testFromFunctionWithEmptyMessage() {

        Logger logger = Logger.fromFunction(mockLogFunction);
        String emptyMessage = "";

        logger.info(emptyMessage);

        verify(mockLogFunction).accept(Level.INFO, emptyMessage);
    }

    @Test
    void testFromFunctionIntegration() {
        StringBuilder logOutput = new StringBuilder();
        BiConsumer<Level, String> actualLogFunction = (level, message) ->
                logOutput.append(level).append(": ").append(message).append("\n");

        Logger logger = Logger.fromFunction(actualLogFunction);

        logger.info("Info message");
        logger.error("Error message");

        String expectedOutput = "INFO: Info message\nERROR: Error message\n";
        assert logOutput.toString().equals(expectedOutput) :
                "Expected: " + expectedOutput + ", but was: " + logOutput.toString();
    }
}