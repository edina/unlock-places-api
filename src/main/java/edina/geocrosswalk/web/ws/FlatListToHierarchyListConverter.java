package edina.geocrosswalk.web.ws;

import java.util.ArrayList;
import java.util.List;

import edina.geocrosswalk.domain.FeatureType;
import edina.geocrosswalk.domain.IFeatureType;

/**
 * @author lkryger Created on: 26 Aug 2009
 */
public class FlatListToHierarchyListConverter {

	public List<SingleNode> hierarchify(List<IFeatureType> inputList) {
		/*
		 * please note level value has to be 1 less than the smallest 'real
		 * node' level; adding a dummy root node for this purpose only (it won't
		 * appear in the output list)
		 */
		IFeatureType rootElement = new FeatureType("n/a", "n/a", inputList.get(
				0).getHierarchyLevel() - 1);
		inputList.add(0, rootElement);
		List<SingleNode> listOfNodes = getImmediateChildren(inputList,
				inputList.get(0));

		return listOfNodes;
	}

	private List<SingleNode> getImmediateChildren(List<IFeatureType> inputList,
			IFeatureType element) {
		List<SingleNode> immediateChildrenList = new ArrayList<SingleNode>();
		int inputElementLevel = element.getHierarchyLevel();
		int indexOfInputElement = inputList.indexOf(element);
		for (int i = indexOfInputElement + 1; i < inputList.size(); i++) {
			IFeatureType subEntry = inputList.get(i);
			if (subEntry.getHierarchyLevel() <= inputElementLevel) {
				break;
			} else {
				SingleNode node = new SingleNode(subEntry);
				List<SingleNode> listOfImmediateChildrenOfSubEntry = getImmediateChildren(
						inputList, subEntry);
				node.setChildren(listOfImmediateChildrenOfSubEntry);
				/*
				 * only add if analyzed node is one level below the current one
				 */
				if (element.getHierarchyLevel() == subEntry.getHierarchyLevel() - 1)
					immediateChildrenList.add(node);
			}
		}
		return immediateChildrenList;
	}
}
