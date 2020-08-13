package matrixtree.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class NestedIntervalTest {
    @Test
    void parent() {
        RationalInterval control = new HazelTreePath(2L, 7L, 5L).computePathMatrix().asNestedInterval();
        RationalInterval node = new HazelTreePath(2L, 7L, 5L, 3L).computePathMatrix().asNestedInterval();

        assertTrue(control.supersetOf(node));
        assertTrue(control.contains(node));
        assertFalse(control.subsetOf(node));
        assertFalse(control.mutuallyExclusiveWith(node));
    }

    @Test
    void self() {
        RationalInterval control = new HazelTreePath(2L, 7L, 5L).computePathMatrix().asNestedInterval();
        RationalInterval node = new HazelTreePath(2L, 7L, 5L).computePathMatrix().asNestedInterval();

        assertFalse(control.supersetOf(node));
        assertTrue(control.contains(node));
        assertFalse(control.subsetOf(node));
        assertFalse(control.mutuallyExclusiveWith(node));
    }

    @Test
    void child() {
        RationalInterval control = new HazelTreePath(2L, 7L, 5L, 1L).computePathMatrix().asNestedInterval();
        RationalInterval node = new HazelTreePath(2L, 7L, 5L).computePathMatrix().asNestedInterval();

        assertFalse(control.supersetOf(node));
        assertFalse(control.contains(node));
        assertTrue(control.subsetOf(node));
        assertFalse(control.mutuallyExclusiveWith(node));
    }

    @Test
    void exclusive() {
        RationalInterval control = new HazelTreePath(2L, 8L, 5L).computePathMatrix().asNestedInterval();
        RationalInterval node = new HazelTreePath(2L, 7L, 5L).computePathMatrix().asNestedInterval();

        assertFalse(control.supersetOf(node));
        assertFalse(control.contains(node));
        assertFalse(control.subsetOf(node));
        assertTrue(control.mutuallyExclusiveWith(node));
    }
}
