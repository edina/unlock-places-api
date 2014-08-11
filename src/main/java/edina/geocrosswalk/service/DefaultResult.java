package edina.geocrosswalk.service;

import java.io.Serializable;
import java.util.List;

import org.jdom.Document;

import edina.geocrosswalk.domain.IFeature;
import edina.geocrosswalk.domain.IFeatureType;
import edina.geocrosswalk.domain.IFootprint;

/**
 * Default result set for geocrosswalk queries.
 * 
 * @author Brian O'Hare
 * @version $Rev$, $Date: $
 * 
 */
public class DefaultResult implements IResult, Serializable {

	private static final long serialVersionUID = -1910227889072152584L;
	private Integer totalResultsCount = 0;
	private Integer totalFootprintsCount = 0;
	private Integer totalFeatureTypesCount = 0;
	private List<IFeature> features;
	private List<IFootprint> footprints;
	private List<IFeatureType> featureTypes;

	private Document deepAttestations;

	private Integer distanceBetweenFeatures;

	/**
	 * @return the totalResultsCount
	 */
	public Integer getTotalResultsCount() {
		return totalResultsCount;
	}

	/**
	 * @param totalResultsCount
	 *            the totalResultsCount to set
	 */
	public void setTotalResultsCount(Integer totalResultsCount) {
		this.totalResultsCount = totalResultsCount;
	}

	/**
	 * @return the totalResultsCount
	 */
	public Integer getTotalFootprintsCount() {
		return totalFootprintsCount;
	}

	/**
	 * @param totalResultsCount
	 *            the totalResultsCount to set
	 */
	public void setTotalFootprintsCount(Integer totalFootprintsCount) {
		this.totalFootprintsCount = totalFootprintsCount;
	}

	/**
	 * @return the features
	 */
	public List<IFeature> getFeatures() {
		return features;
	}

	/**
	 * @param features
	 *            the features to set
	 */
	public void setFeatures(List<IFeature> features) {
		this.features = features;
	}

	/**
	 * @return the footprint
	 */
	public List<IFootprint> getFootprints() {
		return footprints;
	}

	/**
	 * @param footprint
	 *            the footprint to set
	 */
	public void setFootprints(List<IFootprint> footprints) {
		this.footprints = footprints;
	}

	/**
	 * @return the featureTypes
	 */
	public List<IFeatureType> getFeatureTypes() {
		return featureTypes;
	}

	/**
	 * @param featureTypes
	 *            the feature types list to set
	 */
	public void setFeatureTypes(List<IFeatureType> featureTypes) {
		this.featureTypes = featureTypes;
	}

	/**
	 * Gets the totalFeatureTypesCount
	 * 
	 * @return the totalFeatureTypesCount
	 */
	public Integer getTotalFeatureTypesCount() {
		if (featureTypes != null) {
			return getFeatureTypes().size();
		} else {
			return totalFeatureTypesCount;
		}
	}

	public Integer getDistanceBetweenFeatures() {
		return distanceBetweenFeatures;
	}

	public void setDistanceBetweenFeatures(Integer distanceBetweenFeatures) {
		this.distanceBetweenFeatures = distanceBetweenFeatures;
	}

	public Document getDeepAttestations() {
		return deepAttestations;
	}

	public void setDeepAttestations(Document deepAttestations) {
		this.deepAttestations = deepAttestations;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deepAttestations == null) ? 0 : deepAttestations.hashCode());
		result = prime * result + ((distanceBetweenFeatures == null) ? 0 : distanceBetweenFeatures.hashCode());
		result = prime * result + ((featureTypes == null) ? 0 : featureTypes.hashCode());
		result = prime * result + ((features == null) ? 0 : features.hashCode());
		result = prime * result + ((footprints == null) ? 0 : footprints.hashCode());
		result = prime * result + ((totalFeatureTypesCount == null) ? 0 : totalFeatureTypesCount.hashCode());
		result = prime * result + ((totalFootprintsCount == null) ? 0 : totalFootprintsCount.hashCode());
		result = prime * result + ((totalResultsCount == null) ? 0 : totalResultsCount.hashCode());
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
		DefaultResult other = (DefaultResult) obj;
		if (deepAttestations == null) {
			if (other.deepAttestations != null)
				return false;
		} else if (!deepAttestations.equals(other.deepAttestations))
			return false;
		if (distanceBetweenFeatures == null) {
			if (other.distanceBetweenFeatures != null)
				return false;
		} else if (!distanceBetweenFeatures.equals(other.distanceBetweenFeatures))
			return false;
		if (featureTypes == null) {
			if (other.featureTypes != null)
				return false;
		} else if (!featureTypes.equals(other.featureTypes))
			return false;
		if (features == null) {
			if (other.features != null)
				return false;
		} else if (!features.equals(other.features))
			return false;
		if (footprints == null) {
			if (other.footprints != null)
				return false;
		} else if (!footprints.equals(other.footprints))
			return false;
		if (totalFeatureTypesCount == null) {
			if (other.totalFeatureTypesCount != null)
				return false;
		} else if (!totalFeatureTypesCount.equals(other.totalFeatureTypesCount))
			return false;
		if (totalFootprintsCount == null) {
			if (other.totalFootprintsCount != null)
				return false;
		} else if (!totalFootprintsCount.equals(other.totalFootprintsCount))
			return false;
		if (totalResultsCount == null) {
			if (other.totalResultsCount != null)
				return false;
		} else if (!totalResultsCount.equals(other.totalResultsCount))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DefaultResult [totalResultsCount=" + totalResultsCount + ", totalFootprintsCount=" + totalFootprintsCount
				+ ", totalFeatureTypesCount=" + totalFeatureTypesCount + ", features=" + features + ", footprints=" + footprints + ", featureTypes="
				+ featureTypes + ", deepAttestations=" + deepAttestations + ", distanceBetweenFeatures=" + distanceBetweenFeatures + "]";
	}

}
