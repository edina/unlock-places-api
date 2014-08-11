package edina.geocrosswalk.service;

import java.util.List;

import edina.geocrosswalk.domain.BoundingBox;
import edina.geocrosswalk.domain.dao.ISpatialDao;

/**
 * Default implementation of <code>ISpatialService</code>.
 * 
 * @author Joe Vernon
 * @author Brian O'Hare
 * 
 */
public class DefaultSpatialService implements ISpatialService { 

	private ISpatialDao spatialDao;

	
	/* (non-Javadoc)
	 * @see edina.geocrosswalk.service.ISpatialService#clearCache()
	 */
	public void clearGetFeaturesCache() {
		getSpatialDao().clearGetFeaturesCache();
	}
	
	/* (non-Javadoc)
	 * @see edina.geocrosswalk.service.ISpatialService#clearGetFeaturesForClosestMatchSearchCache()
	 */
	public void clearGetFeaturesForClosestMatchSearchCache() {
		getSpatialDao().clearGetFeaturesForClosestMatchSearchCache();
	}

	public void clearGetFootprintsForIdentifierCache() {
		getSpatialDao().clearGetFootprintsForIdentifierCache(); 
	}
	
	/* (non-Javadoc)
	 * @see edina.geocrosswalk.service.ISpatialService#getFeatures(java.lang.String, java.lang.String, edina.geocrosswalk.domain.BoundingBox, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public IResult getFeatures(String name, String featureType,BoundingBox bbox, String operator, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, Integer startYear, Integer endYear, Integer spatialMask, String realSpatial, Integer buffer, String duplicates, String childTypes, String searchVariants, String deepSrc) {
		return getSpatialDao().getFeatures(name, featureType, bbox, operator, startRow, maxRows, gazetteer, count, country, srs, startYear, endYear, spatialMask, realSpatial, buffer, duplicates, childTypes, searchVariants, deepSrc); 
	}
	
	
	/* (non-Javadoc)
	 * @see edina.geocrosswalk.service.ISpatialService#getDeepAttestation(java.lang.String)
	 */
	public IResult getDeepAttestation(String varientId) {
		return getSpatialDao().getDeepAttestation(varientId);
	}

	/**
	 * Returns an <code>IResult</code> for a simple name search.
	 * 
	 * @param name 		the name of the feature(s)
	 * @param startRow 	the first result to display
	 * @param maxRows 	a maximum number of returned results
	 * @param gazetteer which gazetteer(s) to search
	 * @param count 	whether to provide a total results count
	 * @param country 	restrict results to a country
	 * @param srs 		which spatial reference system to return
	 * @return 			the results
	 */
	public IResult getFeaturesForName(String name, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String searchVariants, String deepSrc) {
		return getSpatialDao().getFeaturesForName(name, startRow, maxRows, gazetteer, count, country, srs, searchVariants, deepSrc);
	}

	/**
	 * Returns an <code>IResult</code> for a partial name search
	 * 
	 * @param name 		the name of the feature(s)
	 * @param startRow 	the first result to display
	 * @param maxRows 	a maximum number of returned results
	 * @param gazetteer which gazetteer(s) to search
	 * @param count 	whether to provide a total results count
	 * @param country 	restrict results to a country
	 * @param srs 		which spatial reference system to return
	 * 
	 * @return 			the results
	 */	
	public IResult getFeaturesForNameStartsWith(String name, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String searchVariants, String deepSrc ) {
		return getSpatialDao().getFeaturesForNameStartsWith(name, startRow, maxRows, gazetteer, count, country, srs, searchVariants, deepSrc);
	}

	/**
	 * Returns an <code>IResult</code> containing footprint(s).
	 * 
	 * @param identifier	the identifier(s) for the feature(s)
	 * @param srs 			which spatial reference system to return
	 * 
	 * @return 				the results
	 */
	public IResult getFootprintsForIdentifier(String identifier, String srs) {
		return getSpatialDao().getFootprintsForIdentifier(identifier, srs);
	}
	
	/**
	 * Returns an <code>IResult</code> for a name and feature search.
	 * 
	 * @param name 			the name of the feature(s)
	 * @param featureType 	a feature type to restrict search
	 * @param startRow 		the first result to display
	 * @param maxRows 		a maximum number of returned results
	 * @param gazetteer 	which gazetteer(s) to search
	 * @param count 		whether to provide a total results count
	 * @param country 		restrict results to a country
	 * @param srs 			which spatial reference system to return
	 * 
	 * @return 				the results
	 */
	public IResult getFeaturesForNameAndFeature(String name, String featureType, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String searchVariants, String deepSrc) {
		return getSpatialDao().getFeaturesForNameAndFeature(name, featureType, startRow, maxRows, gazetteer, count, country, srs, searchVariants, deepSrc);
	}
	
	/**
	 * Returns an <code>IResult</code> for a name and bounding box search.
	 * 
	 * @param name 			the name of the feature(s)
	 * @param bbox 			the bounding box
	 * @param startRow 		the first result to display
	 * @param maxRows 		a maximum number of returned results
	 * @param gazetteer 	which gazetteer(s) to search
	 * @param count 		whether to provide a total results count
	 * @param country 		restrict results to a country
	 * @param srs 			which spatial reference system to return
	 *  
	 * @return				the results
	 */
	public IResult getFeaturesForFeatureTypeAndBBox(String featureType, BoundingBox bbox, String operator, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String srs_in, String searchVariants, String deepSrc) {
		return getSpatialDao().getFeaturesForFeatureTypeAndBBox(featureType, bbox, operator, startRow, maxRows, gazetteer, count, country, srs, srs_in, searchVariants, deepSrc);
	}

	/**
	 * Returns an <code>IResult</code> for a featuretype and bbox search.
	 * 
	 * @param featureType 	a feature type to restrict search
	 * @param bbox 			the bounding box
	 * @param startRow 		the first result to display
	 * @param maxRows 		a maximum number of returned results
	 * @param gazetteer 	which gazetteer(s) to search
	 * @param count 		whether to provide a total results count
	 * @param country 		restrict results to a country
	 * @param srs 			which spatial reference system to return
	 * 
	 * @return 				the results
	 */
	public IResult getFeaturesForNameAndBBox(String name, BoundingBox bbox,	String operator, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String srs_in, String searchVariants, String deepSrc) {
		return getSpatialDao().getFeaturesForNameAndBBox(name, bbox, operator, startRow, maxRows, gazetteer, count, country, srs, srs_in, searchVariants, deepSrc);
	}

	/**
	 * Returns an <code>IResult</code> for a post code search.
	 * 
	 * @param postCode 		a postal unit to search for
	 * @param startRow 		the first result to display
	 * @param maxRows 		a maximum number of returned results
	 * @param gazetteer 	which gazetteer(s) to search
	 * @param count 		whether to provide a total results count
	 * @param country 		restrict results to a country
	 * @param srs 			which spatial reference system to return
	 * 
	 * @return the results
	 */
	public IResult getFeaturesForPostCode(String postCode, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs) {
		return getSpatialDao().getFeaturesForPostCode(postCode, startRow, maxRows, gazetteer, count, country, srs);
	}

	/**
	 * Returns an <code>IResult</code> containing the feature type list.
	 * 
	 * @param gazetteer 	which gazetteer(s) to search
	 * 
	 * @return 				the results
	 */
	public IResult getFeatureTypes(String gazetteer) {
		return getSpatialDao().getFeatureTypes(gazetteer);
	} 

	 
	/**
	 * Returns an <code>IResult</code> for the Closest Match search API.
	 * 
	 * @param name 			the name of the feature(s)
	 * @param bbox 			the bounding box
	 * @param startRow 		the first result to display
	 * @param maxRows 		a maximum number of returned results
	 * @param gazetteer 	which gazetteer(s) to search
	 * @param operator	    spatial operator (within or intersects)
	 * @param count 		whether to provide a total results count
	 * @param country 		restrict results to a country
	 * @param srs 			which spatial reference system to return
	 * 
	 * @return the results
	 */
	public IResult getFeaturesForClosestMatchSearch(String name, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String searchVariants, String deepSrc) {
		return getSpatialDao().getFeaturesForClosestMatchSearch(name, startRow, maxRows, gazetteer, count, country, srs, searchVariants, deepSrc);
	}
	

	/**
	 * Returns an <code>IResult</code> for the Unique Feature name search API.
	 * This function removes cross-gazetteer duplicates, returning one result per real world place. 
	 * 
	 * @param name 			the name of the feature(s)
	 * @param startRow 		the first result to display
	 * @param maxRows 		a maximum number of returned results
	 * @param gazetteer 	which gazetteer(s) to search
	 * @param count 		whether to provide a total results count
	 * @param country 		restrict results to a country
	 * @param srs 			which spatial reference system to return
	 * 
	 * @return the results
	 */
	public IResult getFeaturesForUniqueNameSearch(String name, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String searchVariants, String deepSrc) {
		return getSpatialDao().getFeaturesForUniqueNameSearch(name, startRow, maxRows, gazetteer, count, country, srs, searchVariants, deepSrc);
	}
	
	/**
	 * Returns an <code>IResult</code> containing features(s).
	 * 
	 * @param identifier	the identifier(s) for the feature(s)
	 * @param srs 			which spatial reference system to return
	 * 
	 * @return 				the results
	 */
	public IResult getFeaturesForIdentifier(String identifier, String srs, String searchVariants, String deepSrc) {
		return getSpatialDao().getFeaturesForIdentifier(identifier, srs, searchVariants, deepSrc);
	}
	
	/**
	 * Gets the <code>ISpatialDao</code>.
	 * 
	 * @return the spatialDao
	 */
	public ISpatialDao getSpatialDao() {
		return spatialDao;
	}

	/**
	 * Sets the <code>ISpatialDao</code>.
	 * 
	 * @param spatialDao the spatialDao to set
	 */
	public void setSpatialDao(ISpatialDao spatialDao) {
		this.spatialDao = spatialDao;
	}

    /**
     * Returns an <code>IResult</code> for the distance between two features.
     * 
     * @param unlockIdA
     * @param unlockIdB
     * @return the distance between two features encapsulated in the IResult
     */
    public IResult getDistanceBewteenFeatures(Integer unlockIdA, Integer unlockIdB) {
     
        return getSpatialDao().getDistanceBetweenFeatures(unlockIdA, unlockIdB);
    }

    /**
     * Returns an <code>IResult</code> for surrounding features a specified distance away from a given id.
     * @param identifier the identifier(s) for the feature(s)
     * @param maxRows    maximum number of returned results
     * @param featureType type of feature
     * @param gazetter 
     * @param distance the distance in meters
     * @return the results
     */
    public IResult getBufferedSearchFeatures(String identifier, Integer maxRows, String featureType,
            String gazetteer, Integer distance) {

        return getSpatialDao().getBufferedSearchFeatures(identifier, maxRows, featureType, gazetteer, distance);
    }
    
    /**
     * Get list of auto-complete options for given search term.
     * 
     * @param name The search term.
     * @return List of auto-complete options.
     */
    public List<String> getAutoCompleteNames(String name, String country){
        return getSpatialDao().getAutoCompleteNames(name, country);
    }




	
}