package nl.hubble.deap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DeapTest {
    private Deap<Integer> deap;

    @BeforeEach
    public void createDeap() {
        deap = new Deap<>();
    }

    @Test
    public void removeFromEmptyDeap() {
        deap.removeLow();
        deap.removeHigh();
        // Make sure the size didn't go into the negative
        assertEquals(0, deap.getSize());
    }

    @Test
    public void addBadNode() {
        // Make sure you can't add the value that is used as the empty value
        assertThrows(RuntimeException.class, () -> deap.add(null));
    }

    @Test
    public void testAdd() {
        // Array does not contain a two
        assertFalse(arrayContains(deap.toArray(), 2));
        // And low high are both null
        assertNull(deap.getHigh());
        assertNull(deap.getLow());

        // Add the number 2
        deap.add(2);
        // Make sure the deap contains a 2
        assertTrue(arrayContains(deap.toArray(), 2));
        // 2 is both the low and the high
        assertEquals(2, deap.getLow());
        assertEquals(2, deap.getHigh());

        // Add 1
        deap.add(1);
        // deap.Deap contains a 1
        assertTrue(arrayContains(deap.toArray(), 1));
        // Low is 1 and High is 2
        assertEquals(1, deap.getLow());
        assertEquals(2, deap.getHigh());

        // Fill with random
        fillDeapWithRandom();
        deap.prettyPrint();
        assertEquals(getLow(), deap.getLow());
        assertEquals(getHigh(), deap.getHigh());

        // Clear the deap
        deap.clear();
        int space = 15;
        for (int i = 0; i < space; i++) {
            deap.add(i);
            assertEquals(getLow(), deap.getLow());
            assertEquals(getHigh(), deap.getHigh());
        }
    }

    @RepeatedTest(100)
    public void testLowRemove() {
        // Fill deap
        fillDeapWithRandom();

        int items = deap.getSize();
        for (int i = 0; i < items; i++) {
            // Remove all items
            deap.removeLow();
            assertEquals(getLow(), deap.getLow());
            assertEquals(getHigh(), deap.getHigh());
        }

        // assert that the deap is empty
        assertTrue(deap.isEmpty());
    }

    @RepeatedTest(100)
    public void testHighRemove() {
        // Fill deap
        fillDeapWithRandom();

        int items = deap.getSize();
        for (int i = 0; i < items; i++) {
            // Remove all items
            deap.removeHigh();
            assertEquals(getLow(), deap.getLow());
            assertEquals(getHigh(), deap.getHigh());
        }

        // assert that the deap is empty
        assertTrue(deap.isEmpty());
    }

    @RepeatedTest(100)
    public void testHighLowRemove() {
        // Fill deap
        fillDeapWithRandom();

        int items = deap.getSize();
        for (int i = 0; i < items; i++) {
            // Remove all items
            if (i % 2 == 0) {
                // Remove low when even
                deap.removeLow();
            } else {
                // And high when uneven
                deap.removeHigh();
            }
            assertEquals(getLow(), deap.getLow());
            assertEquals(getHigh(), deap.getHigh());
        }

        // assert that the deap is empty
        assertTrue(deap.isEmpty());
    }

    /**
     * Fill the deap with random numbers
     */
    private void fillDeapWithRandom() {
        int space = 15;
        for (int i = 0; i < space; i++) {
            deap.add(random());
        }
    }

    /**
     * Get a random number between 1 and 100
     *
     * @return random number 1~100
     */
    private int random() {
        return (int) Math.floor(Math.random() * 100) + 1;
    }

    @Test
    public void testActualLowHigh() {
        int min = 0; // Should not be null
        int max = 40; // Should not be null
        ArrayList<Integer> array = new ArrayList<>(Arrays.asList(null, min, null, max, null));
        deap = new Deap<>() {
            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public ArrayList<Integer> toArray() {
                return array;
            }
        };

        assertEquals(min, getLow());
        assertEquals(max, getHigh());
    }

    /**
     * Get the actual lowest from the deap array
     * <p>
     * O(N)
     *
     * @return the lowest in the deap
     */
    private Integer getLow() {
        if (deap.isEmpty()) {
            return null;
        }

        ArrayList<Integer> list = deap.toArray();
        int min = Integer.MAX_VALUE;
        for (Integer i : list) {
            if (i != null) {
                min = Math.min(i, min);
            }
        }
        return min;
    }

    /**
     * Get the actual highest from the deap array
     * <p>
     * O(N)
     *
     * @return the highest in de deap
     */
    private Integer getHigh() {
        if (deap.isEmpty()) {
            return null;
        }

        ArrayList<Integer> list = deap.toArray();
        int max = Integer.MIN_VALUE;
        for (Integer i : list) {
            if (i != null) {
                max = Math.max(i, max);
            }
        }
        return max;
    }

    /**
     * O(n)
     *
     * @param arr      int array
     * @param contains value
     * @return true if array contains the value
     */
    private boolean arrayContains(ArrayList<Integer> arr, int contains) {
        for (Integer i : arr) {
            if (i != null && i == contains) {
                return true;
            }
        }
        return false;
    }
}
