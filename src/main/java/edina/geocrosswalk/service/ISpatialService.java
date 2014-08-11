package edina.geocrosswalk.service;

import java.util.List;

import edina.geocrosswalk.domain.BoundingBox;

/**
 * Interface defining Unlock spatial services.
 * 
 * @author Brian O'Hare
 * @author Joe Vernon
 * 
 */
public interface ISpatialService {

	
	/**
	 * Clears the getFeaturesCache
	 */
	void clearGetFeaturesCache();
	
	/**
	 * Clears the getFeaturesForClosestMatchSearchCache
	 */
	void clearGetFeaturesForClosestMatchSearchCache();
	
	/**
	 * Clears the cleargetFootprintsForIdentifierCache
	 */
	void clearGetFootprintsForIdentifierCache();
	
	/**
	 * Returns an <code>IResult</code> for a generic search.
	 * 
	 * @param name 			the name of the feature(s)
	 * @param featureType 	a feature type to restrict search
	 * @param bbox 			the bounding box
	 * @param operator		'within' (default) or 'intersects'
	 * @param startRow 		the first result to display
	 * @param maxRows 		a maximum number of returned results
	 * @param gazetteer 	which gazetteer(s) to search
	 * @param count 		whether to provide a total results count
	 * @param country 		a country name to filter results by
	 * @param srs 			which spatial reference system to return
	 * @param startYear		deep startYear
	 * @param endYear		deep endYear
	 * @param spatialMask	Unlockid of a feature which should be used to spatially filter results
	 * @param realSpatial	Should "real" spatial operation (e.g. "st_within", "st_intersects") be performed involving actual footprints or compare feature bounding boxes only?
	 * @param buffer		Should bounding box or geometry of mask feature be buffered before result extraction? E.g., expand bounding box of Edinburgh by 10 000 meters before extract all villages.
	 * @param duplicates	Should placename duplicates be allowed
	 * @param childTypes	Include child feature types
	 * @param searchVariants	Search for deep variant placenames 
	 * @param deepSrc		Search against a particular Deep source
	 * @return 				the results
	 */
	IResult getFeatures(String name, String featureType, BoundingBox bbox,
			String operator, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, Integer startYear, Integer endYear, Integer spatialMask, String realSpatial, Integer buffer, String duplicates, String childTypes, String searchVariants, String deepSrc);

	
	
	/**
	 * New: Returns an <code>IResult</code> for a search on a Deep variantId
	 * @param variantId
	 * @return
	 */
	IResult getDeepAttestation(String variantId);
	
	/**
	 * Returns an <code>IResult</code> for a simple name search.
	 * 
	 * @param name 		the name of the feature(s)
	 * @param startRow 	the first result to display
	 * @param maxRows 	a maximum number of returned results
	 * @param gazetteer which gazetteer(s) to search
	 * @param count 	whether to provide a total results count
	 * @param country 	a country name to filter results by
	 * @param srs 		which spatial reference system to return
	 * @param searchVariants TODO
	 * @param deepSrc TODO
	 * @return 			the results
	 */
	IResult getFeaturesForName(String name, Integer startRow, Integer maxRows,
			String gazetteer, String count, String country, String srs, String searchVariants, String deepSrc);
	
	/**
	 * Returns an <code>IResult</code> for a partial name search.
	 * 
	 * @param name 		the name of the feature(s)
	 * @param startRow 	the first result to display
	 * @param maxRows 	a maximum number of returned results
	 * @param gazetteer which gazetteer(s) to search
	 * @param count 	whether to provide a total results count
	 * @param country 	a country name to filter results by
 	 * @param srs 		which spatial reference system to return
	 * @param deepSrc 
	 * @param searchVariants 
 	 * 
	 * @return 			the results
	 */	
	IResult getFeaturesForNameStartsWith(String name, Integer startRow,
			Integer maxRows, String gazetteer, String count, String country, String srs, String searchVariants, String deepSrc);

	/**
	 * Returns an <code>IResult</code> containing footprint(s).
	 * 
	 * @param identifier	the identifier(s) for the feature(s)
	 * @param srs 			which spatial reference system to return
	 * 
	 * @return 				the results
	 */
	IResult getFootprintsForIdentifier(String identifier, String srs);

	/**
	 * Returns an <code>IResult</code> for a name and feature search.
	 * 
	 * @param name 			the name of the feature(s)
	 * @param featureType 	a feature type to restrict search
	 * @param startRow 		the first result to display
	 * @param maxRows 		a maximum number of returned results
	 * @param gazetteer 	which gazetteer(s) to search
	 * @param count 		whether to provide a total results count
	 * @param country 		a country name to filter results by
 	 * @param srs 			which spatial reference system to return
	 * @param deepSrc 
	 * @param searchVariants 
 	 * 
	 * @return 				the results
	 */
	IResult getFeaturesForNameAndFeature(String name, String featureType,
			Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String searchVariants, String deepSrc);

	/**
	 * Returns an <code>IResult</code> for a name and bounding box search.
	 * 
	 * @param name 			the name of the feature(s)
	 * @param bbox 			the bounding box
	 * @param startRow 		the first result to display
	 * @param maxRows 		a maximum number of returned results
	 * @param gazetteer 	which gazetteer(s) to search
	 * @param count 		whether to provide a total results count
	 * @param country 		a country name to filter results by
 	 * @param srs 			which spatial reference system to return
	 * @param deepSrc 
	 * @param searchVariants 
	 *  
	 * @return				the results
	 */
	IResult getFeaturesForNameAndBBox(String name, BoundingBox bbox,
			String operator, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String srs_in, String searchVariants, String deepSrc);

	/**
	 * Returns an <code>IResult</code> for a featuretype and bbox search.
	 * 
	 * @param featureType 	a feature type to restrict search
	 * @param bbox 			the bounding box
	 * @param startRow 		the first result to display
	 * @param maxRows 		a maximum number of returned results
	 * @param gazetteer 	which gazetteer(s) to search
	 * @param count 		whether to provide a total results count
	 * @param country 		a country name to filter results by
 	 * @param srs 			which spatial reference system to return
	 * 
	 * @return 				the results
	 */
	IResult getFeaturesForFeatureTypeAndBBox(String featureType, BoundingBox bbox,
			String operator, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String srs_in, String searchVariants, String deepSrc);

	/**
	 * Returns an <code>IResult</code> for a post code search.
	 * 
	 * @param postCode 		a postal unit to search for
	 * @param startRow 		the first result to display
	 * @param maxRows 		a maximum number of returned results
	 * @param gazetteer 	which gazetteer(s) to search
	 * @param count 		whether to provide a total results count
	 * @param country 		a country name to filter results by
 	 * @param srs 			which spatial reference system to return
 	 * 
 	 * @return the results
	 */
	IResult getFeaturesForPostCode(String postCode, Integer startRow,
			Integer maxRows, String gazetteer, String count, String country, String srs);
	
	/**
	 * Returns an <code>IResult</code> containing the feature type list.
	 * 
	 * @param gazetteer 	which gazetteer(s) to search
	 * 
	 * @return 				the results
	 */
	IResult getFeatureTypes(String gazetteer);


	/**
	 * Returns an <code>IResult</code> for the closest match search API.
	 * 
	 * @param name 			the name of the feature(s)
	 * @param bbox 			the bounding box
	 * @param startRow 		the first result to display
	 * @param maxRows 		a maximum number of returned results
	 * @param gazetteer 	which gazetteer(s) to search
	 * @param operator	    spatial operator (within or intersects)
	 * @param count 		whether to provide a total results count
	 * @param country 		a country name to filter results by
 	 * @param srs 			which spatial reference system to return
	 * @param deepSrc 
	 * @param searchVariants 
	 * 
	 * @return the results
	 */
	IResult getFeaturesForClosestMatchSearch(String name, Integer startRow, Integer maxRows, String gazetteer,
			String count, String country, String srs, String searchVariants, String deepSrc);


	/**
	 * Returns an <code>IResult</code> for the closest match search API.
	 * 
	 * @param name 			the name of the feature(s)
	 * @param startRow 		the first result to display
	 * @param maxRows 		a maximum number of returned results
	 * @param gazetteer 	which gazetteer(s) to search
	 * @param count 		whether to provide a total results count
	 * @param country 		a country name to filter results by
 	 * @param srs 			which spatial reference system to return
	 * @param deepSrc 
	 * @param searchVariants 
	 * 
	 * @return the results
	 */
	IResult getFeaturesForUniqueNameSearch(String name, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String searchVariants, String deepSrc);
	
	
	/**
	 * Returns an <code>IResult</code> containing features(s).
	 * 
	 * @param identifier	the identifier(s) for the feature(s)
 	 * @param srs 			which spatial reference system to return
	 * @param deepSrc 
	 * @param searchVariants 
 	 * 
	 * @return 				the results
	 */
	IResult getFeaturesForIdentifier(String identifier, String srs, String searchVariants, String deepSrc);
	

	/**
	 * Returns an <code>IResult</code> for the distance between two features. (meters)
	 * 
	 * @param unlockIdA
	 * @param unlockIdB
	 * @return the distance between two features encapsulated in the IResult (meters)
	 */
	IResult getDistanceBewteenFeatures(Integer unlockIdA,Integer unlockIdB);
	
	/**
	 * Returns an <code>IResult</code> for surrounding features a specified distance away from a given id.
	 * @param identifier the identifier(s) for the feature(s)
	 * @param maxRows    maximum number of returned results
	 * @param featureType type of feature
	 * @param gazetteer 
	 * @param distance the distance in meters
	 * @return the results
	 */

    IResult getBufferedSearchFeatures(String identifier, Integer maxRows, String featureType, String gazetteer, Integer distance);
    
    /**
     * Get list of auto-complete options for a given search term.
     * @param name The search term.
     * @return List of auto-complete options. 
     */
    List<String> getAutoCompleteNames(String name, String country);
}