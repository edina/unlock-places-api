package edina.geocrosswalk.domain.dao;

import java.util.List;

import edina.geocrosswalk.domain.BoundingBox;
import edina.geocrosswalk.service.IResult;

/**
 * Interface defining requirements for spatial DAOs.
 * 
 * @author Brian O'Hare
 * @author Joe Vernon
 * 
 */
public interface ISpatialDao {

	/**
	 * Purge the search cache
	 */
	void clearGetFeaturesCache();
	
	
	/**
	 * Purge getFeaturesForClosestMatchSearchCache
	 */
	void clearGetFeaturesForClosestMatchSearchCache();
	
	
	/**
	 * Purge getFootprintsForIdentifierCach
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
	public IResult getFeatures(String name, String featureType, BoundingBox bbox, String operator, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, Integer startDate, Integer endDate, Integer spatialMask, String realSpatial, Integer buffer, String duplicates, String childTypes, String fullDeepRecord, String deepSrc);

	
	
	/**
	 * Returns an <code>IResult</code>.
	 * @param variantId
	 * @return
	 */
	public IResult getDeepAttestation(String variantId);
	
	/**
	 * Returns an <code>IResult</code>.
	 * 
	 * @param name 		the name of the feature(s)
	 * @param startRow 	the first result to display
	 * @param maxRows 	a maximum number of returned results
	 * @param gazetteer which gazetteer(s) to search
	 * @param count 	whether to provide a total results count
	 * @param country 	restrict results to a country
	 * @param srs 		which spatial reference system to return
	 * @param searchVariants TODO
	 * @param deepSrc TODO
	 * @return the results
	 */
	public IResult getFeaturesForName(String name, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String searchVariants, String deepSrc);
	
	/**
	 * Returns an <code>IResult</code>.
	 * 
	 * @param name 		the name of the feature(s)
	 * @param startRow 	the first result to display
	 * @param maxRows 	a maximum number of returned results
	 * @param gazetteer which gazetteer(s) to search
	 * @param count 	whether to provide a total results count
	 * @param country 	restrict results to a country
	 * @param srs 		which spatial reference system to return
	 * @param deepSrc 
	 * @param searchVariants 
	 * 
	 * @return the results
	 */
	public IResult getFeaturesForNameStartsWith(String name, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String searchVariants, String deepSrc);
	
	
	/**
	 * Returns an <code>IFootprint</code>.
	 * 
	 * @param identifier	the identifier for the feature.
	 * @param srs 			which spatial reference system to return
	 * 
	 * @return the result footprint
	 */
	public IResult getFootprintsForIdentifier(String identifier, String srs);


	/**
	 * Returns an <code>IResult</code>.
	 * 
	 * @param name the name of the feature
	 * @param featureType the feature type
	 * @param startRow the first result to display
	 * @param maxRows the maximum number of returned results
	 * @param gazetteer which gazetteer(s) to search
	 * @param count 	whether to provide a total results count
	 * @param country 	restrict results to a country
	 * @param srs 		which spatial reference system to return
	 * @param deepSrc 
	 * @param searchVariants 
	 * 
	 * @return the results
	 */
	public IResult getFeaturesForNameAndFeature(String name, String featureType, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String searchVariants, String deepSrc);


	/**
	 * Returns an <code>IResult</code>.
	 * 
	 * @param name 		the name of the feature
	 * @param bbox 		the bounding box
	 * @param startRow 	the first result to display
	 * @param maxRows 	the maximum number of returned results
	 * @param gazetteer which gazetteer(s) to search
	 * @param count 	whether to provide a total results count
	 * @param country 	restrict results to a country
	 * @param srs 		which spatial reference system to return
	 * @param deepSrc 
	 * @param searchVariants 
	 * 
	 * @return the results
	 */
	public IResult getFeaturesForNameAndBBox(String name, BoundingBox bbox, String operator, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String srs_in, String searchVariants, String deepSrc);


	/**
	 * Returns an <code>IResult</code>.
	 * 
	 * @param featureType 	the feature type
	 * @param bbox 			the bounding box
	 * @param startRow 		the first result to display
	 * @param maxRows 		the maximum number of returned results
	 * @param gazetteer 	which gazetteer(s) to search
	 * @param count 		whether to provide a total results count
	 * @param country 		restrict results to a country
	 * @param srs 			which spatial reference system to return
	 * 
	 * @return the results
	 */
	public IResult getFeaturesForFeatureTypeAndBBox(String featureType, BoundingBox bbox, String operator, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String srs_in, String searchVariants, String deepSrc);


	/**
	 * Returns an <code>IResult</code>.
	 * 
	 * @param postCode 	the post code
	 * @param startRow 	the first result to display
	 * @param maxRows 	the maximum number of returned results
	 * @param gazetteer which gazetteer(s) to search
	 * @param count 	whether to provide a total results count
	 * @param country 	restrict results to a country
	 * @param srs 		which spatial reference system to return
	 * 
	 * @return the results
	 */
	public IResult getFeaturesForPostCode(String postCode, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs);


	/**
	 * Returns an <code>IResult</code> with the gazetteer's feature types
	 * 
	 * @param gazetteer which gazetteer(s) to search
	 * 
	 * @return the results
	 */
	public IResult getFeatureTypes(String gazetteer);

	
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
	 * @param country 		restrict results to a country
	 * @param srs 			which spatial reference system to return
	 * @param deepSrc 
	 * @param searchVariants 
	 * 
	 * @return the results
	 */
	public IResult getFeaturesForClosestMatchSearch(String name, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String searchVariants, String deepSrc);
	
	
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
	public IResult getFeaturesForIdentifier(String identifier, String srs, String searchVariants, String deepSrc);

	/**
	 * Returns an <code>IResult</code> for the Unique feature name search API.
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
	 * @param deepSrc 
	 * @param searchVariants 
	 * 
	 * @return the results
	 */
	public IResult getFeaturesForUniqueNameSearch(String name, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs, String searchVariants, String deepSrc);


    /**
     * Returns an <code>IResult</code> for the distance between two features.
     * 
     * @param unlockIdA
     * @param unlockIdB
     * @return the distance between two features encapsulated in the IResult
     */
    public IResult getDistanceBetweenFeatures(Integer unlockIdA, Integer unlockIdB);

    /**
     * Returns an <code>IResult</code> for surrounding features a specified distance away from a given id.
     * @param identifier the identifier(s) for the feature(s)
     * @param maxRows    maximum number of returned results
     * @param featureType type of feature
     * @param gazetteer 
     * @param distance the distance in meters
     * @return the results
     */
    public IResult getBufferedSearchFeatures(String identifier, Integer maxRows, String featureType,  String gazetteer, Integer distance);
    
    /**
     * Fetch list of auto complete options.
     * @param name The search term.
     * @return List of auto-complete options.
     */
    public List<String> getAutoCompleteNames(String name, String country);
}