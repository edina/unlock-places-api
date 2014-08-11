package edina.geocrosswalk.web.ws;

import java.util.ArrayList;
import java.util.List;

import edina.geocrosswalk.domain.FeatureType;
import edina.geocrosswalk.domain.IFeatureType;

/**
 * @author lkryger Created on: 26 Aug 2009
 */
public class SingleNode {
	private List<SingleNode> children;
	private FeatureType entry;

	/**
	 * @param subEntry
	 */
	public SingleNode(IFeatureType subEntry) {
		this.entry = (FeatureType) subEntry;
		children = new ArrayList<SingleNode>();
	}

	public List<SingleNode> getChildren() {
		return children;
	}

	public void setChildren(List<SingleNode> children) {
		this.children = children;
	}

	public String getName() {
		return entry.getName();
	}

	public int getLevel() {
		return entry.getHierarchyLevel();
	}

	public String getCode() {
		return entry.getFeatureCode();
	}

	/**
	 * @param child
	 */
	public void addChildren(SingleNode child) {
		this.children.add(child);

	}

	public FeatureType getEntry() {
		return entry;
	}

	public void setEntry(FeatureType entry) {
		this.entry = entry;
	}
}