package io.github.asmd23.task3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LogicsTest {

    private Logics logics;
    private final static int BOARD_SIZE = 8;

    @BeforeEach
    void setUp() {
        this.logics = new LogicsImpl(BOARD_SIZE);
    }

    @Nested
    class Initialization {
        @Test
        public void testConstructorWithRandomPositions() {
            assertNotEquals(logics.getKnightPosition(), logics.getPawnPosition());

            Pair<Integer, Integer> knightPos = logics.getKnightPosition();
            Pair<Integer, Integer> pawnPos = logics.getPawnPosition();

            assertTrue(knightPos.getX() >= 0 && knightPos.getX() < BOARD_SIZE);
            assertTrue(knightPos.getY() >= 0 && knightPos.getY() < BOARD_SIZE);
            assertTrue(pawnPos.getX() >= 0 && pawnPos.getX() < BOARD_SIZE);
            assertTrue(pawnPos.getY() >= 0 && pawnPos.getY() < BOARD_SIZE);
        }

        @Test
        public void testConstructorWithPredefinedPositions() {
            Pair<Integer, Integer> pawnPos = new Pair<>(3, 3);
            Pair<Integer, Integer> knightPos = new Pair<>(1, 1);

            logics = new LogicsImpl(BOARD_SIZE, pawnPos, knightPos);

            assertEquals(pawnPos, logics.getPawnPosition());
            assertEquals(knightPos, logics.getKnightPosition());
            assertTrue(logics.hasPawn(3, 3));
            assertTrue(logics.hasKnight(1, 1));
        }

        @Test
        public void testConstructorWithInvalidPositions() {
            assertThrows(IndexOutOfBoundsException.class, () -> {
                new LogicsImpl(BOARD_SIZE, new Pair<>(-1, 0), new Pair<>(1, 1));
            });

            assertThrows(IndexOutOfBoundsException.class, () -> {
                new LogicsImpl(BOARD_SIZE, new Pair<>(0, 0), new Pair<>(BOARD_SIZE, 1));
            });

            assertThrows(IllegalArgumentException.class, () -> {
                new LogicsImpl(BOARD_SIZE, new Pair<>(2, 2), new Pair<>(2, 2));
            });
        }

        @Test
        void testConstructorThrowsOnInvalidPositions() {
            var pawn = new Pair<>(0, 0);
            var knight = new Pair<>(0, 0);
            assertThrows(IllegalArgumentException.class, () -> new LogicsImpl(4, pawn, knight));

            var outOfBounds = new Pair<>(5, 5);
            assertThrows(IndexOutOfBoundsException.class, () -> new LogicsImpl(4, outOfBounds, new Pair<>(1, 1)));
        }

        @Test
        public void testKnightMovementSequence() {
            Pair<Integer, Integer> pawnPos = new Pair<>(7, 7);
            Pair<Integer, Integer> knightPos = new Pair<>(0, 0);

            logics = new LogicsImpl(BOARD_SIZE, pawnPos, knightPos);

            logics.hit(2, 1);
            assertTrue(logics.hasKnight(2, 1));

            logics.hit(4, 0);
            assertTrue(logics.hasKnight(4, 0));
            assertFalse(logics.hasKnight(2, 1));

            logics.hit(4, 1);
            assertTrue(logics.hasKnight(4, 0));
        }

    }

    @Nested
    class Movement {
        @Test
        void testKnightAndPawnInitialPositions() {
            var pawn = new Pair<>(0, 0);
            var knight = new Pair<>(2, 1);
            Logics logic = new LogicsImpl(4, pawn, knight);

            assertTrue(logic.hasPawn(0, 0));
            assertTrue(logic.hasKnight(2, 1));
            assertFalse(logic.hasPawn(2, 1));
            assertFalse(logic.hasKnight(0, 0));
        }

        @Test
        public void testInvalidKnightMoves() {
            Pair<Integer, Integer> pawnPos = new Pair<>(7, 7);
            Pair<Integer, Integer> knightPos = new Pair<>(3, 3);

            logics = new LogicsImpl(BOARD_SIZE, pawnPos, knightPos);
            Set<Pair<Integer, Integer>> knightInvalidMoves = Stream.of(
                    new Pair<>(3,3),
                    new Pair<>(3,4),
                    new Pair<>(4,3),
                    new Pair<>(4,4),
                    new Pair<>(3,6),
                    new Pair<>(6,3),
                    new Pair<>(0,0),
                    new Pair<>(3,0)
            ).collect(HashSet::new, HashSet::add, HashSet::addAll);


            for (Pair<Integer, Integer> move : knightInvalidMoves) {
                boolean result = logics.hit(move.getX(), move.getY());
                assertFalse(result);
                assertTrue(logics.hasKnight(3, 3));
            }
        }

        @Test
        public void testKnightMovementValidation() {
            Pair<Integer, Integer> pawnPos = new Pair<>(7, 7);
            Pair<Integer, Integer> knightPos = new Pair<>(4, 4);

            logics = new LogicsImpl(BOARD_SIZE, pawnPos, knightPos);
            logics.hit(2, 5);
            assertEquals(logics.getKnightPosition(), new Pair<>(2,5));

            logics = new LogicsImpl(BOARD_SIZE, pawnPos, knightPos);
            logics.hit(6, 3);
            assertEquals(logics.getKnightPosition(), new Pair<>(6,3));


            logics = new LogicsImpl(BOARD_SIZE, pawnPos, knightPos);
            logics.hit(3, 2);
            assertEquals(logics.getKnightPosition(), new Pair<>(3,2));

            logics = new LogicsImpl(BOARD_SIZE, pawnPos, knightPos);
            logics.hit(5, 6);
            assertEquals(logics.getKnightPosition(), new Pair<>(5,6));
        }

        @Test
        public void testBoundaryConditions() {
            Pair<Integer, Integer> pawnPos = new Pair<>(7, 7);
            Pair<Integer, Integer> knightPos = new Pair<>(0, 0);

            logics = new LogicsImpl(BOARD_SIZE, pawnPos, knightPos);

            assertThrows(IndexOutOfBoundsException.class, () -> {
                logics.hit(-1, 2);
            });

            assertThrows(IndexOutOfBoundsException.class, () -> {
                logics.hit(2, -1);
            });

            assertThrows(IndexOutOfBoundsException.class, () -> {
                logics.hit(BOARD_SIZE, 2);
            });

            assertThrows(IndexOutOfBoundsException.class, () -> {
                logics.hit(2, BOARD_SIZE);
            });
        }
    }

    @Nested
    class Finalization {

        @Test
        public void testGameWinCondition() {
            Pair<Integer, Integer> pawnPos = new Pair<>(4, 4);
            Pair<Integer, Integer> knightPos = new Pair<>(2, 3);

            logics = new LogicsImpl(BOARD_SIZE, pawnPos, knightPos);

            boolean won = logics.hit(4, 4);
            assertTrue(won);

            assertTrue(logics.hasKnight(4, 4));
            assertTrue(logics.hasPawn(4, 4));
            assertEquals(logics.getKnightPosition(), logics.getPawnPosition());
        }
    }

}