package io.github.asmd23.task3;


import io.github.asmd23.task3.logger.Logger;
import io.github.asmd23.task3.movement.MovementValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogicsIntegrationTests {

    @Mock
    private Logger mockLogger;

    @Mock
    private MovementValidator mockMovementValidator;

    private LogicsImpl logics;
    private static final int BOARD_SIZE = 8;
    private static final Pair<Integer, Integer> INITIAL_PAWN_POS = new Pair<>(0, 0);
    private static final Pair<Integer, Integer> INITIAL_KNIGHT_POS = new Pair<>(4, 4);
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        logics = new LogicsImpl(BOARD_SIZE, INITIAL_PAWN_POS, INITIAL_KNIGHT_POS, mockLogger, mockMovementValidator);
    }

    @Test
    void testValidKnightMoveWithLogging() {
        int targetRow = 6;
        int targetCol = 5;
        when(mockMovementValidator.isValidMovement(2, 1)).thenReturn(true);

        boolean result = logics.hit(targetRow, targetCol);

        assertFalse(result);

        verify(mockLogger).debug("Inside hit method:");
        verify(mockLogger).debug("Knight position: (4,4)");
        verify(mockLogger).debug("Target position: (6,5)");
        verify(mockLogger).debug("x = 6-4 = 2");
        verify(mockLogger).debug("y = 5-4 = 1");
        verify(mockLogger).debug("|x|+|y| = 2+1 = 3");
        verify(mockLogger).debug("Conditions: x!=0 && y!=0 && |x|+|y|==3");
        verify(mockLogger).debug("Result: true && true && true");
        verify(mockLogger).debug("Valid move! Knight moved to (6,5)");
        verify(mockLogger).debug("Hit pawn? false");

        verify(mockMovementValidator).isValidMovement(2, 1);

        assertEquals(new Pair<>(6, 5), logics.getKnightPosition());
    }

    @Test
    void testInvalidKnightMoveWithLogging() {
        int targetRow = 5;
        int targetCol = 5;
        when(mockMovementValidator.isValidMovement(1, 1)).thenReturn(false);

        boolean result = logics.hit(targetRow, targetCol);

        assertFalse(result);

        verify(mockLogger).debug("Inside hit method:");
        verify(mockLogger).debug("Knight position: (4,4)");
        verify(mockLogger).debug("Target position: (5,5)");
        verify(mockLogger).debug("x = 5-4 = 1");
        verify(mockLogger).debug("y = 5-4 = 1");
        verify(mockLogger).debug("|x|+|y| = 1+1 = 2");
        verify(mockLogger).debug("Conditions: x!=0 && y!=0 && |x|+|y|==3");
        verify(mockLogger).debug("Result: true && true && false");
        verify(mockLogger).debug("Invalid move! Knight stays at (4,4)");

        verify(mockLogger, never()).debug(startsWith("Valid move!"));
        verify(mockLogger, never()).debug(startsWith("Hit pawn?"));

        verify(mockMovementValidator).isValidMovement(1, 1);
        assertEquals(INITIAL_KNIGHT_POS, logics.getKnightPosition());
    }

    @Test
    void testKnightHitsPawnWithLogging() {
        Pair<Integer, Integer> knightStartPos = new Pair<>(2, 1);
        logics = new LogicsImpl(BOARD_SIZE, INITIAL_PAWN_POS, knightStartPos, mockLogger, mockMovementValidator);

        when(mockMovementValidator.isValidMovement(-2, -1)).thenReturn(true);

        boolean result = logics.hit(0, 0);

        assertTrue(result);

        verify(mockLogger).debug("Inside hit method:");
        verify(mockLogger).debug("Knight position: (2,1)");
        verify(mockLogger).debug("Target position: (0,0)");
        verify(mockLogger).debug("x = 0-2 = -2");
        verify(mockLogger).debug("y = 0-1 = -1");
        verify(mockLogger).debug("|x|+|y| = 2+1 = 3");
        verify(mockLogger).debug("Conditions: x!=0 && y!=0 && |x|+|y|==3");
        verify(mockLogger).debug("Result: true && true && true");
        verify(mockLogger).debug("Valid move! Knight moved to (0,0)");
        verify(mockLogger).debug("Hit pawn? true");

        verify(mockMovementValidator).isValidMovement(-2, -1);
    }


    @Test
    void testExceptionHandlingWithLogging() {
        assertThrows(IndexOutOfBoundsException.class, () -> logics.hit(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> logics.hit(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> logics.hit(BOARD_SIZE, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> logics.hit(0, BOARD_SIZE));

        verifyNoInteractions(mockLogger);
        verifyNoInteractions(mockMovementValidator);
    }

    @Test
    void testMovementValidatorReturnValueIntegration() {
        when(mockMovementValidator.isValidMovement(2, 1)).thenReturn(true);
        when(mockMovementValidator.isValidMovement(1, 2)).thenReturn(false);

        logics.hit(6, 5);
        assertEquals(new Pair<>(6, 5), logics.getKnightPosition());

        logics = new LogicsImpl(BOARD_SIZE, INITIAL_PAWN_POS, INITIAL_KNIGHT_POS, mockLogger, mockMovementValidator);
        assertFalse(logics.hit(5, 6));
        assertEquals(INITIAL_KNIGHT_POS, logics.getKnightPosition());
    }

    @Test
    void testComplexMovementSequenceWithMocks() {
        when(mockMovementValidator.isValidMovement(2, 1)).thenReturn(true);
        when(mockMovementValidator.isValidMovement(-2, -1)).thenReturn(true);
        when(mockMovementValidator.isValidMovement(1, 1)).thenReturn(false);

        logics.hit(6, 5);
        assertEquals(new Pair<>(6, 5), logics.getKnightPosition());

        logics.hit(4, 4);
        assertEquals(new Pair<>(4, 4), logics.getKnightPosition());

        logics.hit(5, 5);
        assertEquals(new Pair<>(4, 4), logics.getKnightPosition());

        verify(mockMovementValidator).isValidMovement(2, 1);
        verify(mockMovementValidator).isValidMovement(-2, -1);
        verify(mockMovementValidator).isValidMovement(1, 1);
    }

    @Test
    void testLoggerLevelsAndMessages() {
        when(mockMovementValidator.isValidMovement(anyInt(), anyInt())).thenReturn(true);
        logics.hit(6, 5);
        verify(mockLogger, atLeast(1)).debug(anyString());
        verify(mockLogger, never()).info(anyString());
        verify(mockLogger, never()).warn(anyString());
        verify(mockLogger, never()).error(anyString());
    }

    @Test
    void testNegativeMovementValues() {
        when(mockMovementValidator.isValidMovement(-2, -1)).thenReturn(true);

        logics.hit(2, 3);

        verify(mockLogger).debug("x = 2-4 = -2");
        verify(mockLogger).debug("y = 3-4 = -1");
        verify(mockLogger).debug("|x|+|y| = 2+1 = 3");
        verify(mockMovementValidator).isValidMovement(-2, -1);
    }
}