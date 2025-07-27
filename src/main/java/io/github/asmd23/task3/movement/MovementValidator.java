package io.github.asmd23.task3.movement;

@FunctionalInterface
public interface MovementValidator {
    /**
     * Validates if a movement is valid based on the given deltas.
     * @param deltaX
     * @param deltaY
     * @return true if the movement is valid, false otherwise
     */
    boolean isValidMovement(int deltaX, int deltaY);
}
