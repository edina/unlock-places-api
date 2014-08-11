package edina.geocrosswalk.service;

import java.util.List;

import org.jdom.Document;

import edina.geocrosswalk.domain.IFeature;
import edina.geocrosswalk.domain.IFeatureType;
import edina.geocrosswalk.domain.IFootprint;

/**
 * Interface defining feature and footprint result(s),
 * with their total result counts, from a returned search.
 * 
 * @author Brian O'Hare
 * @author Joe Vernon
 */
public interface IResult {

	List<IFeature> getFeatures();

	Integer getTotalResultsCount();

	List<IFootprint> getFootprints();

	Integer getTotalFootprintsCount();
	
	List<IFeatureType> getFeatureTypes();

	Integer getTotalFeatureTypesCount();	
	
	Integer getDistanceBetweenFeatures();
		
	Document getDeepAttestations();
}
