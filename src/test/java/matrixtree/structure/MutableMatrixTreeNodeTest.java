package matrixtree.structure;

import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @author Agustinus Lawandy
 * @since 2020-08-09
 */
class MutableMatrixTreeNodeTest {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	Type integerType = new TypeToken<OrderedHazelMatrixTreeNode<Integer>>() {}.getType();
	@Test
	void testInsertWithIndex() {
		MutableMatrixTreeNode<Integer> root = new OrderedHazelMatrixTreeNode<Integer>(null, 100, 100);
		
		MutableMatrixTreeNode<Integer> lvl1e4 = root.insert(4,1);
		lvl1e4.insert(2,1);
		lvl1e4.insert(2,2);
		
		MutableMatrixTreeNode<Integer> lvl1e25 = root.insert(25,2);
		lvl1e25.insert(5,1);
		lvl1e25.insert(5,2);

//		System.out.println(gson.toJson(root, integerType));
	}
	
	@Test
	void testInsertWithoutIndex() {
		MutableMatrixTreeNode<Integer> root = new OrderedHazelMatrixTreeNode<Integer>(null, 100, 100);
		
		MutableMatrixTreeNode<Integer> lvl1e4 = root.insert(4);
		lvl1e4.insert(2);
		lvl1e4.insert(2);
		
		MutableMatrixTreeNode<Integer> lvl1e25 = root.insert(25);
		lvl1e25.insert(5);
		lvl1e25.insert(5);
		
		System.out.println(gson.toJson(root, integerType));
		
		
	}

}
