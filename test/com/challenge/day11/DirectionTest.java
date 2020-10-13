package com.challenge.day11;

import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DirectionTest {

    @Test
    public void testGetNewDirection() {
        test(Direction.UP, Turn.LEFT, Direction.LEFT);
        test(Direction.UP, Turn.RIGHT, Direction.RIGHT);

        test(Direction.RIGHT, Turn.LEFT, Direction.UP);
        test(Direction.RIGHT, Turn.RIGHT, Direction.DOWN);

        test(Direction.DOWN, Turn.LEFT, Direction.RIGHT);
        test(Direction.DOWN, Turn.RIGHT, Direction.LEFT);

        test(Direction.LEFT, Turn.LEFT, Direction.DOWN);
        test(Direction.LEFT, Turn.RIGHT, Direction.UP);
    }

    private void test(Direction direction, Turn turn, Direction expected) {
        Direction newDirection = direction.turn(turn);
        System.out.printf("If we are at direction %s and turn %s we go to %s direction%n", direction, turn, newDirection);
        assertThat(newDirection).isEqualTo(expected);
    }
}