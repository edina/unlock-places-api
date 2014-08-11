package edina.geocrosswalk.domain;

import java.io.Serializable;

/**
 * Defines a geocrosswalk feature's footprint.
 * 
 * @author Joe Vernon
 * 
 */

public class FeatureType implements IFeatureType, Serializable {

	
	private static final long serialVersionUID = 5361848048718538296L;
	private String featureCode;
	private String name;
	private Integer hierarchyLevel;


	public FeatureType() {

	}


	public FeatureType(String featureCode, String name, Integer hierarchyLevel) {
		this.featureCode = featureCode;
		this.name = name;
		this.hierarchyLevel = hierarchyLevel;
	}


	/**
	 * @param featureCode the featureCode to set
	 */
	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}


	/**
	 * @return the featureCode
	 */
	public String getFeatureCode() {
		return featureCode;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name.replaceAll("&", "&amp;");
	}


	/**
	 * @param hierarchyLevel the hierarchyLevel to set
	 */
	public void setHierarchyLevel(Integer hierarchyLevel) {
		this.hierarchyLevel = hierarchyLevel;
	}


	/**
	 * @return the hierarchyLevel
	 */
	public Integer getHierarchyLevel() {
		return hierarchyLevel;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((featureCode == null) ? 0 : featureCode.hashCode());
		result = prime * result + ((hierarchyLevel == null) ? 0 : hierarchyLevel.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeatureType other = (FeatureType) obj;
		if (featureCode == null) {
			if (other.featureCode != null)
				return false;
		} else if (!featureCode.equals(other.featureCode))
			return false;
		if (hierarchyLevel == null) {
			if (other.hierarchyLevel != null)
				return false;
		} else if (!hierarchyLevel.equals(other.hierarchyLevel))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
