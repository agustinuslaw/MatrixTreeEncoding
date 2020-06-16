package matrixtree.model;

import jdk.nashorn.api.tree.Tree;
import matrixtree.matrices.PathMatrix;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * Ancestors contain TreePath and precomputed
 *
 * @author Agustinus Lawandy
 * @since 2020.06.15
 */
public interface Ancestors {

	TreePath getTreePath();

	List<PathMatrix> getAncestorMatrices();

	PathMatrix getPathMatrix();
}
