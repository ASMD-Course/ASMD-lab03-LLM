package io.github.asmd23.task3;

import io.github.asmd23.task3.logger.Logger;
import io.github.asmd23.task3.movement.MovementValidator;

import java.util.*;

public class LogicsImpl implements Logics {

    private final Pair<Integer,Integer> pawn;
    private Pair<Integer,Integer> knight;
    private final Random random = new Random();
    private final int size;
    private final Logger logger;
    private final MovementValidator knightMoveValidator;

    private static final MovementValidator DEFAULT_VALIDATOR = (dx, dy) ->
            dx != 0 && dy != 0 && Math.abs(dx) + Math.abs(dy) == 3;

    private static final Logger DEFAULT_LOGGER = Logger.fromFunction((level, message) ->
            System.out.println("[" + level + "] " + message)
    );

    public LogicsImpl(int size) {
        this(size, randomPositionSupplier(size), randomPositionSupplier(size), DEFAULT_LOGGER, DEFAULT_VALIDATOR);
    }

    public LogicsImpl(int size, Logger logger, MovementValidator validator) {
        this(size, randomPositionSupplier(size), randomPositionSupplier(size), logger, validator);
    }

    public LogicsImpl(int size, Pair<Integer, Integer> pawnPos, Pair<Integer, Integer> knightPos) {
        this(size, pawnPos, knightPos, DEFAULT_LOGGER, DEFAULT_VALIDATOR);
    }

    public LogicsImpl(int size, Pair<Integer, Integer> pawnPos, Pair<Integer, Integer> knightPos,
                      Logger logger, MovementValidator validator) {

        if (isWithinBounds(pawnPos, size) || isWithinBounds(knightPos, size)) {
            throw new IndexOutOfBoundsException("Positions must be within board bounds");
        }
        if (pawnPos.equals(knightPos)) {
            throw new IllegalArgumentException("Pawn and knight cannot be at the same position");
        }

        this.size = size;
        this.pawn = pawnPos;
        this.knight = knightPos;
        this.logger = logger;
        this.knightMoveValidator = validator;
    }

    private static boolean isWithinBounds(Pair<Integer, Integer> pos, int size) {
        return pos.getX() < 0 || pos.getY() < 0 || pos.getX() >= size || pos.getY() >= size;
    }

    private static Pair<Integer, Integer> randomPositionSupplier(int size) {
        Random rand = new Random();
        return new Pair<>(rand.nextInt(size), rand.nextInt(size));
    }


    @Override
    public boolean hit(int row, int col) {
        if (row<0 || col<0 || row >= this.size || col >= this.size) {
            throw new IndexOutOfBoundsException();
        }

        int x = row - this.knight.getX();
        int y = col - this.knight.getY();

        // Debug output
        logger.debug("Inside hit method:");
        logger.debug("Knight position: (" + this.knight.getX() + "," + this.knight.getY() + ")");
        logger.debug("Target position: (" + row + "," + col + ")");
        logger.debug("x = " + row + "-" + this.knight.getX() + " = " + x);
        logger.debug("y = " + col + "-" + this.knight.getY() + " = " + y);

        int totMovementSpace = Math.abs(x) + Math.abs(y);
        logger.debug("|x|+|y| = " + Math.abs(x) + "+" + Math.abs(y) + " = " + totMovementSpace);
        logger.debug("Conditions: x!=0 && y!=0 && |x|+|y|==3");
        logger.debug("Result: " + (x!=0) + " && " + (y!=0) + " && " + (totMovementSpace ==3));

        // Using the functional interface
        if (knightMoveValidator.isValidMovement(x, y)) {
            this.knight = new Pair<>(row, col);
            boolean hitPawn = this.pawn.equals(this.knight);
            logger.debug("Valid move! Knight moved to (" + row + "," + col + ")");
            logger.debug("Hit pawn? " + hitPawn);
            return hitPawn;
        }

        logger.debug("Invalid move! Knight stays at (" + this.knight.getX() + "," + this.knight.getY() + ")");
        return false;
    }

    @Override
    public boolean hasKnight(int row, int col) {
        return this.knight.equals(new Pair<>(row,col));
    }

    @Override
    public boolean hasPawn(int row, int col) {
        return this.pawn.equals(new Pair<>(row,col));
    }

    @Override
    public Pair<Integer, Integer> getKnightPosition() {
        return new Pair<>(this.knight.getX(), this.knight.getY());
    }

    @Override
    public Pair<Integer, Integer> getPawnPosition() {
        return new Pair<>(this.pawn.getX(), this.pawn.getY());
    }
}