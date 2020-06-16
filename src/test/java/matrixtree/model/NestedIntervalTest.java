package matrixtree.model;

import matrixtree.matrices.PathMatrix;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NestedIntervalTest {
    @Test
    void parent() {
        NestedInterval control = new HazelTreePath(2L, 7L, 5L).computePathMatrix().asNestedInterval();
        NestedInterval node = new HazelTreePath(2L, 7L, 5L, 3L).computePathMatrix().asNestedInterval();

        assertTrue(control.supersetOf(node));
        assertTrue(control.contains(node));
        assertFalse(control.subsetOf(node));
        assertFalse(control.mutuallyExclusiveWith(node));
    }

    @Test
    void self() {
        NestedInterval control = new HazelTreePath(2L, 7L, 5L).computePathMatrix().asNestedInterval();
        NestedInterval node = new HazelTreePath(2L, 7L, 5L).computePathMatrix().asNestedInterval();

        assertFalse(control.supersetOf(node));
        assertTrue(control.contains(node));
        assertFalse(control.subsetOf(node));
        assertFalse(control.mutuallyExclusiveWith(node));
    }

    @Test
    void child() {
        NestedInterval control = new HazelTreePath(2L, 7L, 5L, 1L).computePathMatrix().asNestedInterval();
        NestedInterval node = new HazelTreePath(2L, 7L, 5L).computePathMatrix().asNestedInterval();

        assertFalse(control.supersetOf(node));
        assertFalse(control.contains(node));
        assertTrue(control.subsetOf(node));
        assertFalse(control.mutuallyExclusiveWith(node));
    }

    @Test
    void exclusive() {
        NestedInterval control = new HazelTreePath(2L, 8L, 5L).computePathMatrix().asNestedInterval();
        NestedInterval node = new HazelTreePath(2L, 7L, 5L).computePathMatrix().asNestedInterval();

        assertFalse(control.supersetOf(node));
        assertFalse(control.contains(node));
        assertFalse(control.subsetOf(node));
        assertTrue(control.mutuallyExclusiveWith(node));
    }
}
