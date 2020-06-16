package matrixtree.model;

import matrixtree.matrices.PathMatrix;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HazelAncestors implements Ancestors {

    private final HazelTreePath treePath;
    private final LinkedList<PathMatrix> ancestorMatrices;

    public HazelAncestors(HazelTreePath treePath, List<PathMatrix> ancestorMatrices) {
        this.treePath = treePath;
        this.ancestorMatrices = new LinkedList<>(ancestorMatrices);
    }

    @Override
    public TreePath getTreePath() {
        return treePath;
    }

    @Override
    public List<PathMatrix> getAncestorMatrices() {
        return Collections.unmodifiableList(ancestorMatrices);
    }

    @Override
    public PathMatrix getPathMatrix() {
        return ancestorMatrices.getLast();
    }

}
