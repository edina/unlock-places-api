package edina.geocrosswalk.domain.dao;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.CacheManager;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.postgis.PGgeometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;

import edina.geocrosswalk.domain.BoundingBox;
import edina.geocrosswalk.domain.Feature;
import edina.geocrosswalk.domain.FeatureType;
import edina.geocrosswalk.domain.Footprint;
import edina.geocrosswalk.domain.IFeature;
import edina.geocrosswalk.domain.IFeatureType;
import edina.geocrosswalk.domain.IFootprint;
import edina.geocrosswalk.service.DefaultResult;
import edina.geocrosswalk.service.IResult;

/**
 * Default implementation of <code>ISpatialDao</code>.
 * 
 * @author Brian O'Hare
 * @author Joe Vernon
 */

/**
 * @author colin
 *
 */
public class DefaultSpatialDao extends AbstractGXWDao implements ISpatialDao {
	private static final String REFCURSOR = "ref";
	private static final String NAME_CURSOR = "name_cursor";

	private static final String NAME_DUPLICATES = "name_duplicates";
	private static final String NAME_FUZZINESS = "name_fuzziness";
	private static final String FTYPE_HIERARCHY = "ftype_hierarchy";
	private static final String SPATIAL_OP = "spatial_op";
	private static final String SRS_IN = "srs_in";
	private static final String SRS_OUT = "srs_out";
	private static final String REAL_SPATIAL = "realspatial";
	private static final String SPATIAL_MASK_ID = "spatial_maskid";
	private static final String SPATIAL_BUFFER_M = "spatial_buffer_m";
	private static final String SQL_DEBUG = "sql_debug";
	private static final String RESAMPLE_DIST = "resample_dist_m";

	private static final String API_GENERIC_SEARCH = "api_generic_search";
	private static final String FOOTPRINT_LOOKUP = "api_footprint_lookup";
	private static final String POSTCODE_SEARCH = "api_postcode_search";
	private static final String SUPPORTED_FEATURE_TYPES = "api_supported_feature_types";
	private static final String CLOSEST_MATCH_SEARCH = "api_closest_name_match_search";
	private static final String CALCULATE_DISTANCE_BETWEEN_FEATURES = "api_calculate_distance";
	private static final String BUFFERED_SEARCH = "api_feature_buffer_search";
	private static final String NAME_INDEX_SEARCH = "api_name_index_search";

	private static final String IDENTIFIER = "unlockid";
	private static final String SOURCE_IDENTIFIER = "source_id";
	private static final String RETURNVALUE = "returnvalue";
	private static final String NAME = "name";

	private static final String ASCII_NAME = "asciiname";
	private static final String FCODE_EDINA = "fcode";
	private static final String NAME_OPTIMISED = "name_optimised";
	private static final String POSTCODE_OPTIMISED = "postcode_optimised";
	private static final String START_ROW = "startRow";
	private static final String MAX_ROWS = "maxRows";
	private static final String FEATURE_TYPE = "ftype";
	private static final String GAZETTEER = "gazetteer";
	private static final String XMIN = "xmin";
	private static final String XMAX = "xmax";
	private static final String YMIN = "ymin";
	private static final String YMAX = "ymax";
	private static final String COUNTRY = "country";
	private static final String SRS = "srs";
	private static final String CUSTODIAN = "custodian";
	private static final String GEOMETRY = "geom";
	private static final String AUTHORISED = "authorised";
	private static final String FCODE = "fcode";
	private static final String HLEVEL = "hlevel";
	private static final String START_YEAR = "start_year";
	private static final String END_YEAR = "end_year";
	private static final String UNLOCK_ID_A = "unlockid_a";
	private static final String UNLOCK_ID_B = "unlockid_b";
	private static final String USER_AUTHORISED = "user_authorised";

	// Extended feature data parameters
	private static final String XCENTROID = "xcentroid";
	private static final String YCENTROID = "ycentroid";
	private static final String AREA = "area";
	private static final String PERIMETER = "perimeter";
	private static final String ELEVATION = "elevation";
	private static final String POPULATION = "population";
	private static final String ALT_NAME = "alt_name";
	private static final String SCALE = "scale";
	private static final String ISO_COUNTRY_CODE = "iso_country_code";
	private static final String ADMIN_LEVEL_1 = "admin_level1";
	private static final String ADMIN_LEVEL_2 = "admin_level2";
	private static final String ADMIN_LEVEL_3 = "admin_level3";
	private static final String ADMIN_LEVEL_4 = "admin_level4";
	private static final String ALT_UNLOCKID = "alt_unlockid";
	private static final String ROW_COUNT = "rowcount";

	private static final String FEATURE_IDENTIFIER = "buffer_unlockid";
	private static final String BUFFER_DISTANCE = "buffer_dist";
	private static final String POSTCODE = "postcode";

	// Deep specific
	private static final String SEARCH_VARIANTS = "search_variants";
	private static final String DEEP_SRC = "deep_src";
	private static final String API_DEEP_ATTESTATION = "get_deep_attestation_asxml";
	private static final String IN_VARIANT_ID = "in_variant_id";
	private static final String URIINS = "uriins";
	private static final String URICDDA = "uricdda";
	private static final String MADSID = "madsid";
	private static final String VARIANTID = "variantid";
	private static final String ATTESTATIONS = "attestations";
	private static final String LOCATIONS = "locations";
	private static final String UNLOCKFPSRC = "unlockfpsrc";
	
	@Autowired
    private CacheManager manager;
	
	
	/* (non-Javadoc)
	 * @see edina.geocrosswalk.domain.dao.ISpatialDao#clearCache()
	 */  
	@TriggersRemove(cacheName = "getFeaturesCache", removeAll = true)
	public void clearGetFeaturesCache() {
		manager.getCache("getFeaturesCache").clearStatistics();
	};

	/* (non-Javadoc)
	 * @see edina.geocrosswalk.domain.dao.ISpatialDao#clearGetFeaturesForClosestMatchSearchCache()
	 */
	@TriggersRemove(cacheName = "getFeaturesForClosestMatchSearchCache", removeAll = true)
	public void clearGetFeaturesForClosestMatchSearchCache() {
		manager.getCache("getFeaturesForClosestMatchSearchCache").clearStatistics();
	};

	/* (non-Javadoc)
	 * @see edina.geocrosswalk.domain.dao.ISpatialDao#cleargetFootprintsForIdentifierCache()
	 */
	@TriggersRemove(cacheName = "getFootprintsForIdentifierCache", removeAll = true)
	public void clearGetFootprintsForIdentifierCache() {
		manager.getCache("getFootprintsForIdentifierCache").clearStatistics();
	}; 

	// New generic search functionality
	
	/* (non-Javadoc)
	 * @see edina.geocrosswalk.domain.dao.ISpatialDao#getFeatures(java.lang.String, java.lang.String, edina.geocrosswalk.domain.BoundingBox, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Cacheable(cacheName = "getFeaturesCache")
	public IResult getFeatures(String name, String featureType, BoundingBox bbox, String operator, Integer startRow, Integer maxRows,
			String gazetteer, String count, String country, String srs, Integer startYear, Integer endYear, Integer spatialMask, String realSpatial,
			Integer buffer, String duplicates, String childTypes, String searchVariants, String deepSrc) {
	
		String featureTypeOptimised = "";
		
		if (featureType != null) {
			String[] featureTypes = featureType.split(",");

			if (featureTypes.length < 2) {
				featureTypeOptimised = normalizeString(featureType);
			} else {
				featureTypeOptimised = getNormalizedStringList(featureType);
			}
		}
		if (featureTypeOptimised == "")
			featureTypeOptimised = null;

		String[] gazetteers = gazetteer.split(",");
		String gazetteer_optimised = "";
		if (gazetteers.length < 2) {
			gazetteer_optimised = normalizeString(gazetteer);
		} else {
			gazetteer_optimised = getNormalizedStringList(gazetteer);
		}

		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue(NAME_CURSOR, REFCURSOR);
		params.addValue(NAME, name); // Add object type here?
		params.addValue(FEATURE_TYPE, featureTypeOptimised);
		if (bbox.getMinx() == null || bbox.getMaxx() == null || bbox.getMiny() == null || bbox.getMaxy() == null) {
			params.addValue(XMIN, null);
			params.addValue(YMIN, null);
			params.addValue(XMAX, null);
			params.addValue(YMAX, null);
			params.addValue(SPATIAL_OP, null);
		} else {
			params.addValue(XMIN, bbox.getMinx());
			params.addValue(YMIN, bbox.getMiny());
			params.addValue(XMAX, bbox.getMaxx());
			params.addValue(YMAX, bbox.getMaxy());
			params.addValue(SPATIAL_OP, operator);
		}
		params.addValue(START_ROW, startRow);
		params.addValue(MAX_ROWS, maxRows);
		params.addValue(GAZETTEER, gazetteer_optimised);
		params.addValue(USER_AUTHORISED, isAuthorised());
		params.addValue(COUNTRY, country);
		params.addValue(ROW_COUNT, Boolean.parseBoolean(count));
		params.addValue(SRS_IN, srs); // TODO SRS parameters in and out...
		params.addValue(SRS_OUT, srs);
		params.addValue(REAL_SPATIAL, realSpatial);
		params.addValue(SPATIAL_MASK_ID, spatialMask);
		params.addValue(SPATIAL_BUFFER_M, buffer);
		params.addValue(NAME_DUPLICATES, duplicates);
		params.addValue(NAME_FUZZINESS, null); // This parameter is currently
												// not
												// supported by the SQL query
		params.addValue(FTYPE_HIERARCHY, childTypes);
		params.addValue(START_YEAR, startYear);
		params.addValue(END_YEAR, endYear);

		// Deep only
		params.addValue(SEARCH_VARIANTS, searchVariants);
		params.addValue(DEEP_SRC, deepSrc);

		params.addValue(SQL_DEBUG, false);

		SimpleJdbcCall proc = new SimpleJdbcCall(getDataSource());
		proc.withSchemaName(getSchemaName());
		proc.withFunctionName(API_GENERIC_SEARCH);
		FeatureRowMapper rowMapper = new FeatureRowMapper();
		proc.returningResultSet(REFCURSOR, rowMapper);
		proc.execute(params);

		DefaultResult result = new DefaultResult();
		List<IFeature> features = rowMapper.getFeatures();

		result.setFeatures(features);
		result.setTotalResultsCount(rowMapper.getRowCount());

		return result;
	}

	/**
	 * Given a variant_id, will return the full set of attestation information
	 * for the variant placename
	 * 
	 * @param variantId
	 * @return
	 */
	public IResult getDeepAttestation(String variantId) {

		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue(NAME_CURSOR, REFCURSOR);
		params.addValue(IN_VARIANT_ID, variantId);

		SimpleJdbcCall proc = new SimpleJdbcCall(getDataSource());
		proc.withSchemaName(getSchemaName());
		proc.withFunctionName(API_DEEP_ATTESTATION);

		DeepAttestationRowMapper rowMapper = new DeepAttestationRowMapper();
		proc.returningResultSet(REFCURSOR, rowMapper);
		proc.execute(params);

		DefaultResult result = new DefaultResult();
		result.setDeepAttestations(rowMapper.getDeepAttestationsDocument());
		
		return result;
	}

	/**
	 * Optimises name(s) and gazetteer(s) lists, then retrieves features for
	 * feature name(s) using a simple search.
	 * 
	 * @param name
	 *            the name of the feature(s)
	 * @param startRow
	 *            the first result to display
	 * @param maxRows
	 *            a maximum number of returned results
	 * @param gazetteer
	 *            which gazetteer(s) to search
	 * @param count
	 *            whether to provide a total results count
	 * @param country
	 *            limit results to a specific country
	 * @param srs
	 *            which spatial reference system to return
	 * @return the retrieved feature(s) and total results count
	 */
	public IResult getFeaturesForName(String name, Integer startRow, Integer maxRows, String gazetteer, String count, String country, String srs,
			String searchVariants, String deepSrc) {
		// Split the list of names and normalise each name to search
		String[] names = name.split(",");
		String name_optimised = "";
		if (names.length < 2) {
			name_optimised = normalizeName(name);
		} else {
			name_optimised = getNormalizedNameList(name);
		}

		// Split list of gazetteers in the same way
		String[] gazetteers = gazetteer.split(",");
		String gazetteer_optimised = "";
		if (gazetteers.length < 2) {
			gazetteer_optimised = normalizeString(gazetteer);
		} else {
			gazetteer_optimised = getNormalizedStringList(gazetteer);
		}

		// Add search parameters to SQL query
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue(NAME_CURSOR, REFCURSOR);
		params.addValue(NAME, name_optimised);
		params.addValue(FEATURE_TYPE, null);
		params.addValue(SPATIAL_OP, null);
		params.addValue(NAME_DUPLICATES, true);
		params.addValue(NAME_FUZZINESS, 1);
		params.addValue(FTYPE_HIERARCHY, false);
		params.addValue(COUNTRY, country);
		params.addValue(SRS, srs);
		params.addValue(SPATIAL_OP, null);
		params.addValue(XMIN, null);
		params.addValue(XMAX, null);
		params.addValue(YMIN, null);
		params.addValue(YMAX, null);
		params.addValue(GAZETTEER, gazetteer_optimised);
		params.addValue(SRS_IN, srs);
		params.addValue(SRS_OUT, srs);
		params.addValue(REAL_SPATIAL, false); // These parameters are ignored
												// for now...
		params.addValue(SPATIAL_MASK_ID, null);
		params.addValue(SPATIAL_BUFFER_M, null);
		params.addValue(NAME_DUPLICATES, null);
		params.addValue(NAME_FUZZINESS, null);
		params.addValue(FTYPE_HIERARCHY, null);
		params.addValue(START_YEAR, null);
		params.addValue(END_YEAR, null);
		params.addValue(USER_AUTHORISED, isAuthorised());
		params.addValue(START_ROW, startRow);
		params.addValue(MAX_ROWS, maxRows);
		params.addValue(ROW_COUNT, Boolean.parseBoolean(count));
		params.addValue(SQL_DEBUG, false);

		// Deep only
		params.addValue(SEARCH_VARIANTS, searchVariants);
		params.addValue(DEEP_SRC, deepSrc);

		// Execute SQL query
		SimpleJdbcCall proc = new SimpleJdbcCall(getDataSource());
		proc.withSchemaName(getSchemaName());
		proc.withFunctionName(API_GENERIC_SEARCH);
		FeatureRowMapper rowMapper = new FeatureRowMapper();
		proc.returningResultSet(REFCURSOR, rowMapper);
		proc.execute(params);

		// Retrieve results and parse each row as a feature
		DefaultResult result = new DefaultResult();
		List<IFeature> features = rowMapper.getFeatures();
		result.setFeatures(features);
		result.setTotalResultsCount(rowMapper.getRowCount());

		return result;
	}

	/**
	 * Optimises name(s) and gazetteer(s) lists, then retrieves features for
	 * feature name(s) using a partial name.
	 * 
	 * @param name
	 *            the name of the feature(s)
	 * @param startRow
	 *            the first result to display
	 * @param maxRows
	 *            a maximum number of returned results
	 * @param gazetteer
	 *            which gazetteer(s) to search
	 * @param count
	 *            whether to provide a total results count
	 * @param country
	 *            limit results to a specific country
	 * @param srs
	 *            which spatial reference system to return
	 * 
	 * @return the retrieved feature(s) and total results count
	 */
	public IResult getFeaturesForNameStartsWith(String name, Integer startRow, Integer maxRows, String gazetteer, String count, String country,
			String srs, String searchVariants, String deepSrc) {
		String[] names = name.split(",");
		String name_optimised = "";
		if (names.length < 2) {
			name_optimised = normalizeName(name);
		} else {
			name_optimised = getNormalizedNameList(name);
		}

		String[] gazetteers = gazetteer.split(",");
		String gazetteer_optimised = "";
		if (gazetteers.length < 2) {
			gazetteer_optimised = normalizeString(gazetteer);
		} else {
			gazetteer_optimised = getNormalizedStringList(gazetteer);
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(NAME_CURSOR, REFCURSOR);
		params.addValue(NAME, name_optimised);
		params.addValue(FEATURE_TYPE, null);
		params.addValue(SPATIAL_OP, null);
		params.addValue(NAME_DUPLICATES, true);
		params.addValue(NAME_FUZZINESS, 1);
		params.addValue(FTYPE_HIERARCHY, false);
		params.addValue(COUNTRY, country);
		params.addValue(SRS, srs);
		params.addValue(SPATIAL_OP, null);
		params.addValue(XMIN, null);
		params.addValue(XMAX, null);
		params.addValue(YMIN, null);
		params.addValue(YMAX, null);
		params.addValue(GAZETTEER, gazetteer_optimised);
		params.addValue(SRS_IN, srs);
		params.addValue(SRS_OUT, srs);
		params.addValue(REAL_SPATIAL, false);
		params.addValue(SPATIAL_MASK_ID, null);
		params.addValue(SPATIAL_BUFFER_M, null);
		params.addValue(NAME_DUPLICATES, null);
		params.addValue(NAME_FUZZINESS, null);
		params.addValue(FTYPE_HIERARCHY, null);
		params.addValue(START_YEAR, null);
		params.addValue(END_YEAR, null);
		params.addValue(USER_AUTHORISED, isAuthorised());
		params.addValue(START_ROW, startRow);
		params.addValue(MAX_ROWS, maxRows);
		params.addValue(ROW_COUNT, Boolean.parseBoolean(count));
		params.addValue(SQL_DEBUG, false);

		// Deep only
		params.addValue(SEARCH_VARIANTS, searchVariants);
		params.addValue(DEEP_SRC, deepSrc);

		SimpleJdbcCall proc = new SimpleJdbcCall(getDataSource());
		proc.withSchemaName(getSchemaName());
		proc.withFunctionName(API_GENERIC_SEARCH);
		FeatureRowMapper rowMapper = new FeatureRowMapper();
		proc.returningResultSet(REFCURSOR, rowMapper);
		proc.execute(params);

		DefaultResult result = new DefaultResult();

		List<IFeature> features = rowMapper.getFeatures();
		result.setFeatures(features);
		result.setTotalResultsCount(rowMapper.getRowCount());

		return result;
	}

	/**
	 * Returns list of auto-complete options.
	 * 
	 * @param name
	 *            The search term.
	 * @return List of auto-complete options.
	 */
	public List<String> getAutoCompleteNames(String name, String country) {
		SimpleJdbcCall proc = new SimpleJdbcCall(getDataSource());
		proc.withSchemaName(getSchemaName());
		proc.withFunctionName(NAME_INDEX_SEARCH);

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(NAME_CURSOR, REFCURSOR);
		params.addValue(NAME_OPTIMISED, name);
		params.addValue(COUNTRY, country);
		params.addValue(SQL_DEBUG, false);

		NameRowMapper rowMapper = new NameRowMapper();
		proc.returningResultSet(REFCURSOR, rowMapper);
		proc.execute(params);

		return rowMapper.getNames();
	}

	/**
	 * Optimises identifier(s) and gazetteer(s) lists, then retrieves footprints
	 * for identifier(s).
	 * 
	 * @param identifier
	 *            the Unlock identifier of the feature(s)
	 * @param srs
	 *            which spatial reference system to return
	 * 
	 * @return the retrieved footprint(s) and total results count
	 */
	@Cacheable(cacheName = "getFootprintsForIdentifierCache")
	public IResult getFootprintsForIdentifier(String identifier, String srs) {
		String[] identifiers = identifier.split(",");
		String identifier_optimised = "";
		if (identifiers.length < 2) {
			identifier_optimised = normalizeString(identifier);
		} else {
			identifier_optimised = getNormalizedStringList(identifier);
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(NAME_CURSOR, REFCURSOR);
		params.addValue(IDENTIFIER, identifier_optimised);
		params.addValue(SRS, srs);
		params.addValue(RESAMPLE_DIST, 0); // This can generalise geometry
		params.addValue(USER_AUTHORISED, isAuthorised());
		params.addValue(ROW_COUNT, true);
		params.addValue(SQL_DEBUG, false);

		SimpleJdbcCall proc = new SimpleJdbcCall(getDataSource());
		proc.withSchemaName(getSchemaName());
		proc.withFunctionName(FOOTPRINT_LOOKUP);

		FootprintRowMapper rowMapper = new FootprintRowMapper();
		proc.returningResultSet(REFCURSOR, rowMapper);
		proc.execute(params);

		DefaultResult result = new DefaultResult();
		List<IFootprint> footprints = rowMapper.getFootprints();
		result.setFootprints(footprints);
		result.setTotalResultsCount(footprints.size());

		return result;
	}

	/**
	 * Optimises name(s), feature type(s) and gazetteer(s) lists, then retrieves
	 * features by name, restricted by feature type.
	 * 
	 * @param name
	 *            the name of the feature(s)
	 * @param featureType
	 *            feature types to restrict search
	 * @param startRow
	 *            the first result to display
	 * @param maxRows
	 *            a maximum number of returned results
	 * @param gazetteer
	 *            which gazetteer(s) to search
	 * @param count
	 *            whether to provide a total results count
	 * @param country
	 *            limit results to a specific country
	 * @param srs
	 *            which spatial reference system to return
	 * 
	 * @return the retrieved feature(s) and total results count
	 */
	public IResult getFeaturesForNameAndFeature(String name, String featureType, Integer startRow, Integer maxRows, String gazetteer, String count,
			String country, String srs, String searchVariants, String deepSrc) {
		String[] names = name.split(",");
		String name_optimised = "";
		if (names.length < 2) {
			name_optimised = normalizeName(name);
		} else {
			name_optimised = getNormalizedNameList(name);
		}

		String[] featureTypes = featureType.split(",");
		String featureTypeOptimised = "";
		if (featureTypes.length < 2) {
			featureTypeOptimised = normalizeString(featureType);
		} else {
			featureTypeOptimised = getNormalizedStringList(featureType);
		}

		String[] gazetteers = gazetteer.split(",");
		String gazetteer_optimised = "";
		if (gazetteers.length < 2) {
			gazetteer_optimised = normalizeString(gazetteer);
		} else {
			gazetteer_optimised = getNormalizedStringList(gazetteer);
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(NAME_CURSOR, REFCURSOR);
		params.addValue(NAME, name_optimised);
		params.addValue(FEATURE_TYPE, featureTypeOptimised);
		params.addValue(START_ROW, startRow);
		params.addValue(MAX_ROWS, maxRows);
		params.addValue(ROW_COUNT, Boolean.parseBoolean(count));
		params.addValue(GAZETTEER, gazetteer_optimised);
		params.addValue(AUTHORISED, isAuthorised());
		params.addValue(COUNTRY, country);
		params.addValue(SRS_IN, srs);
		params.addValue(SRS_OUT, srs);
		params.addValue(REAL_SPATIAL, false);
		params.addValue(SPATIAL_MASK_ID, null);
		params.addValue(SPATIAL_BUFFER_M, null);
		params.addValue(NAME_DUPLICATES, null);
		params.addValue(NAME_FUZZINESS, null);
		params.addValue(FTYPE_HIERARCHY, null);
		params.addValue(START_YEAR, null);
		params.addValue(END_YEAR, null);
		params.addValue(SQL_DEBUG, false);
		params.addValue(XMIN, null);
		params.addValue(YMIN, null);
		params.addValue(XMAX, null);
		params.addValue(YMAX, null);
		params.addValue(SPATIAL_OP, null);
		params.addValue(USER_AUTHORISED, isAuthorised());
		params.addValue(SQL_DEBUG, false);

		// Deep only
		params.addValue(SEARCH_VARIANTS, searchVariants);
		params.addValue(DEEP_SRC, deepSrc);

		SimpleJdbcCall proc = new SimpleJdbcCall(getDataSource());
		proc.withSchemaName(getSchemaName());
		proc.withFunctionName(API_GENERIC_SEARCH);
		FeatureRowMapper rowMapper = new FeatureRowMapper();
		proc.returningResultSet(REFCURSOR, rowMapper);
		proc.execute(params);

		DefaultResult result = new DefaultResult();
		List<IFeature> features = rowMapper.getFeatures();
		result.setFeatures(features);
		result.setTotalResultsCount(rowMapper.getRowCount());

		return result;
	}

	/**
	 * Optimises feature type and gazetteer(s) lists, then retrieves features
	 * found within/intersecting a bounding box.
	 * 
	 * @param featureType
	 *            feature types to restrict search
	 * @param bbox
	 *            the bounding box
	 * @param operator
	 *            spatial operator (within or intersects)
	 * @param startRow
	 *            the first result to display
	 * @param maxRows
	 *            a maximum number of returned results
	 * @param gazetteer
	 *            which gazetteer(s) to search
	 * @param count
	 *            whether to provide a total results count
	 * @param country
	 *            limit results to a specific country
	 * @param srs
	 *            which spatial reference system to return
	 * 
	 * @return the retrieved feature(s) and total results count
	 */
	public IResult getFeaturesForFeatureTypeAndBBox(String featureType, BoundingBox bbox, String operator, Integer startRow, Integer maxRows,
			String gazetteer, String count, String country, String srs, String srs_in, String searchVariants, String deepSrc) {

		String[] featureTypes = featureType.split(",");
		String featureTypeOptimised = "";
		if (featureTypes.length < 2) {
			featureTypeOptimised = normalizeString(featureType);
		} else {
			featureTypeOptimised = getNormalizedStringList(featureType);
		}

		String[] gazetteers = gazetteer.split(",");
		String gazetteer_optimised = "";
		if (gazetteers.length < 2) {
			gazetteer_optimised = normalizeString(gazetteer);
		} else {
			gazetteer_optimised = getNormalizedStringList(gazetteer);
		}

		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue(NAME_CURSOR, REFCURSOR);
		params.addValue(NAME, null);
		params.addValue(FEATURE_TYPE, featureTypeOptimised);
		params.addValue(XMIN, bbox.getMinx());
		params.addValue(YMIN, bbox.getMiny());
		params.addValue(XMAX, bbox.getMaxx());
		params.addValue(YMAX, bbox.getMaxy());
		params.addValue(SPATIAL_OP, operator);
		params.addValue(REAL_SPATIAL, false); // These parameters are ignored
												// for now...
		params.addValue(SPATIAL_MASK_ID, null);
		params.addValue(SPATIAL_BUFFER_M, null);
		params.addValue(START_ROW, startRow);
		params.addValue(MAX_ROWS, maxRows);
		params.addValue(ROW_COUNT, Boolean.parseBoolean(count));
		params.addValue(GAZETTEER, gazetteer_optimised);
		params.addValue(USER_AUTHORISED, isAuthorised());
		params.addValue(COUNTRY, country);
		params.addValue(SRS_IN, srs_in);
		params.addValue(SRS_OUT, srs);
		params.addValue(NAME_DUPLICATES, null);
		params.addValue(NAME_FUZZINESS, null);
		params.addValue(FTYPE_HIERARCHY, null);
		params.addValue(START_YEAR, null);
		params.addValue(END_YEAR, null);
		params.addValue(SQL_DEBUG, false);
		// Deep only
		params.addValue(SEARCH_VARIANTS, searchVariants);
		params.addValue(DEEP_SRC, deepSrc);

		SimpleJdbcCall proc = new SimpleJdbcCall(getDataSource());
		proc.withSchemaName(getSchemaName());
		proc.withFunctionName(API_GENERIC_SEARCH);
		FeatureRowMapper rowMapper = new FeatureRowMapper();
		proc.returningResultSet(REFCURSOR, rowMapper);
		proc.execute(params);

		DefaultResult result = new DefaultResult();
		List<IFeature> features = rowMapper.getFeatures();
		result.setFeatures(features);
		result.setTotalResultsCount(rowMapper.getRowCount());

		return result;
	}

	/**
	 * Optimises name and gazetteer lists, then retrieves features found
	 * within/intersecting a bounding box.
	 * 
	 * @param name
	 *            the name of the feature(s)
	 * @param bbox
	 *            the bounding box
	 * @param operator
	 *            spatial operator (within or intersects)
	 * @param startRow
	 *            the first result to display
	 * @param maxRows
	 *            a maximum number of returned results
	 * @param gazetteer
	 *            which gazetteer(s) to search
	 * @param count
	 *            whether to provide a total results count
	 * @param country
	 *            limit results to a specific country
	 * @param srs
	 *            which spatial reference system to return
	 * 
	 * @return the retrieved feature(s) and total results count
	 */
	public IResult getFeaturesForNameAndBBox(String name, BoundingBox bbox, String operator, Integer startRow, Integer maxRows, String gazetteer,
			String count, String country, String srs, String srs_in, String searchVariants, String deepSrc) {

		String[] names = name.split(",");
		String name_optimised = "";
		if (names.length < 2) {
			name_optimised = normalizeName(name);
		} else {
			name_optimised = getNormalizedNameList(name);
		}

		String[] gazetteers = gazetteer.split(",");
		String gazetteer_optimised = "";
		if (gazetteers.length < 2) {
			gazetteer_optimised = normalizeString(gazetteer);
		} else {
			gazetteer_optimised = getNormalizedStringList(gazetteer);
		}

		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue(NAME_CURSOR, REFCURSOR);
		params.addValue(NAME, name_optimised);
		params.addValue(FEATURE_TYPE, null);
		params.addValue(XMIN, bbox.getMinx());
		params.addValue(YMIN, bbox.getMiny());
		params.addValue(XMAX, bbox.getMaxx());
		params.addValue(YMAX, bbox.getMaxy());
		params.addValue(SPATIAL_OP, operator);
		params.addValue(START_ROW, startRow);
		params.addValue(MAX_ROWS, maxRows);
		params.addValue(ROW_COUNT, Boolean.parseBoolean(count));
		params.addValue(GAZETTEER, gazetteer_optimised);
		params.addValue(USER_AUTHORISED, isAuthorised());
		params.addValue(COUNTRY, country);
		params.addValue(SRS_IN, srs_in);
		params.addValue(SRS_OUT, srs);
		params.addValue(REAL_SPATIAL, false);
		params.addValue(SPATIAL_MASK_ID, null);
		params.addValue(SPATIAL_BUFFER_M, null);
		params.addValue(NAME_DUPLICATES, null);
		params.addValue(NAME_FUZZINESS, null);
		params.addValue(FTYPE_HIERARCHY, null);
		params.addValue(START_YEAR, null);
		params.addValue(END_YEAR, null);
		params.addValue(SQL_DEBUG, false);
		// Deep only
		params.addValue(SEARCH_VARIANTS, searchVariants);
		params.addValue(DEEP_SRC, deepSrc);

		SimpleJdbcCall proc = new SimpleJdbcCall(getDataSource());
		proc.withSchemaName(getSchemaName());
		proc.withFunctionName(API_GENERIC_SEARCH);
		FeatureRowMapper rowMapper = new FeatureRowMapper();
		proc.returningResultSet(REFCURSOR, rowMapper);
		proc.execute(params);

		DefaultResult result = new DefaultResult();
		List<IFeature> features = rowMapper.getFeatures();
		result.setFeatures(features);
		result.setTotalResultsCount(rowMapper.getRowCount());

		return result;
	}

	/**
	 * Optimises name and gazetteer lists, then retrieves features found
	 * within/intersecting a bounding box.
	 * 
	 * @param postCode
	 *            a postal unit to search for
	 * @param startRow
	 *            the first result to display
	 * @param maxRows
	 *            a maximum number of returned results
	 * @param gazetteer
	 *            which gazetteer(s) to search
	 * @param count
	 *            whether to provide a total results count
	 * @param country
	 *            limit results to a specific country
	 * @param srs
	 *            which spatial reference system to return
	 * 
	 * @return the retrieved post code feature(s) and total results count
	 */
	public IResult getFeaturesForPostCode(String postCode, Integer startRow, Integer maxRows, String gazetteer, String count, String country,
			String srs) {
		String postcode_optimised = "";
		postcode_optimised = normalizeString(postCode);
		postcode_optimised = StringUtils.deleteWhitespace(postcode_optimised);

		String[] gazetteers = gazetteer.split(",");
		String gazetteer_optimised = "";
		if (gazetteers.length < 2) {
			gazetteer_optimised = normalizeString(gazetteer);
		} else {
			gazetteer_optimised = getNormalizedStringList(gazetteer);
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(NAME_CURSOR, REFCURSOR);
		params.addValue(POSTCODE, postcode_optimised);
		params.addValue(POSTCODE_OPTIMISED, postcode_optimised);
		params.addValue(START_ROW, startRow);
		params.addValue(MAX_ROWS, maxRows);
		params.addValue(ROW_COUNT, Boolean.parseBoolean(count));
		params.addValue(GAZETTEER, gazetteer_optimised);
		params.addValue(USER_AUTHORISED, isAuthorised());
		params.addValue(COUNTRY, country);
		params.addValue(SRS, srs);
		params.addValue(ROW_COUNT, count);
		params.addValue(SQL_DEBUG, false);

		SimpleJdbcCall proc = new SimpleJdbcCall(getDataSource());
		proc.withSchemaName(getSchemaName());
		proc.withFunctionName(POSTCODE_SEARCH);
		FeatureRowMapper rowMapper = new FeatureRowMapper();
		proc.returningResultSet(REFCURSOR, rowMapper);
		proc.execute(params);

		DefaultResult result = new DefaultResult();
		List<IFeature> features = rowMapper.getFeatures();
		result.setFeatures(features);
		result.setTotalResultsCount(rowMapper.getRowCount());

		return result;
	}

	/**
	 * Retrieves a supported feature type list for specified gazetteer
	 * 
	 * @param gazetteer
	 *            which gazetteer(s) to search
	 * 
	 * @return the supported feature types
	 */
	public IResult getFeatureTypes(String gazetteer) {
		String[] gazetteers = gazetteer.split(",");
		String gazetteer_optimised = "";
		if (gazetteers.length < 2) {
			gazetteer_optimised = normalizeString(gazetteer);
		} else {
			gazetteer_optimised = getNormalizedStringList(gazetteer);
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(NAME_CURSOR, REFCURSOR);
		params.addValue(GAZETTEER, gazetteer_optimised);
		params.addValue(USER_AUTHORISED, isAuthorised());
		params.addValue(ROW_COUNT, true);
		params.addValue(SQL_DEBUG, false);

		SimpleJdbcCall proc = new SimpleJdbcCall(getDataSource());
		proc.withSchemaName(getSchemaName());
		proc.withFunctionName(SUPPORTED_FEATURE_TYPES);
		FeatureTypeRowMapper rowMapper = new FeatureTypeRowMapper();
		proc.returningResultSet(REFCURSOR, rowMapper);
		proc.execute(params);

		DefaultResult result = new DefaultResult();
		List<IFeatureType> featureTypes = rowMapper.getFeatureTypes();
		result.setFeatureTypes(featureTypes);
		result.setTotalResultsCount(featureTypes.size());
		return result;
	}

		
	/**
	 * Closest Match search is very similar to the nameSearch API but returns
	 * just one (or none) result.
	 * 
	 * @param name
	 *            the name of the feature(s)
	 * @param startRow
	 *            the first result to display
	 * @param maxRows
	 *            a maximum number of returned results
	 * @param gazetteer
	 *            which gazetteer(s) to search
	 * @param count
	 *            whether to provide a total results count
	 * @param country
	 *            limit results to a specific country
	 * @param srs
	 *            which spatial reference system to return
	 * 
	 * @return the retrieved feature and total results count
	 */
	@Cacheable(cacheName = "getFeaturesForClosestMatchSearchCache")
	public IResult getFeaturesForClosestMatchSearch(String name, Integer startRow, Integer maxRows, String gazetteer, String count, String country,
			String srs, String searchVariants, String deepSrc) {
		// Split the list of names and normalise each name to search
		String[] names = name.split(",");
		String name_optimised = "";
		if (names.length < 2) {
			name_optimised = normalizeName(name);
		} else {
			name_optimised = getNormalizedNameList(name);
		}

		String[] gazetteers = gazetteer.split(",");
		String gazetteer_optimised = "";
		if (gazetteers.length < 2) {
			gazetteer_optimised = normalizeString(gazetteer);
		} else {
			gazetteer_optimised = getNormalizedStringList(gazetteer);
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(NAME_CURSOR, REFCURSOR);
		params.addValue(NAME, name_optimised);
		params.addValue(NAME_DUPLICATES, null);
		params.addValue(NAME_FUZZINESS, null);
		params.addValue(FEATURE_TYPE, null);
		params.addValue(FTYPE_HIERARCHY, null);
		params.addValue(COUNTRY, country);
		params.addValue(SRS_IN, srs);
		params.addValue(SRS_OUT, srs);
		params.addValue(SPATIAL_OP, null);
		params.addValue(REAL_SPATIAL, null);
		params.addValue(SPATIAL_MASK_ID, null);
		params.addValue(SPATIAL_BUFFER_M, null);
		params.addValue(XMIN, null);
		params.addValue(YMIN, null);
		params.addValue(YMAX, null);
		params.addValue(XMAX, null);
		params.addValue(START_YEAR, null);
		params.addValue(END_YEAR, null);
		params.addValue(GAZETTEER, gazetteer_optimised);
		params.addValue(USER_AUTHORISED, isAuthorised());
		params.addValue(START_ROW, startRow);
		params.addValue(MAX_ROWS, maxRows);
		params.addValue(ROW_COUNT, Boolean.parseBoolean(count));
		params.addValue(SQL_DEBUG, false);
		// Deep only
		params.addValue(SEARCH_VARIANTS, searchVariants);
		params.addValue(DEEP_SRC, deepSrc);

		SimpleJdbcCall proc = new SimpleJdbcCall(getDataSource());
		proc.withSchemaName(getSchemaName());
		proc.withFunctionName(CLOSEST_MATCH_SEARCH);
		FeatureRowMapper rowMapper = new FeatureRowMapper();
		proc.returningResultSet(REFCURSOR, rowMapper);
		proc.execute(params);

		DefaultResult result = new DefaultResult();

		List<IFeature> features = rowMapper.getFeatures();
		result.setFeatures(features);
		// This will always be 1 or 0 if no features found.
		result.setTotalResultsCount(features.size());
		return result;
	}

	/**
	 * Unique feature Name search is very similar to the nameSearch API but
	 * de-duplicates cross-gazetteer features.
	 * 
	 * @param name
	 *            the name of the feature(s)
	 * @param startRow
	 *            the first result to display
	 * @param maxRows
	 *            a maximum number of returned results
	 * @param gazetteer
	 *            which gazetteer(s) to search
	 * @param count
	 *            whether to provide a total results count
	 * @param country
	 *            limit results to a specific country
	 * @param srs
	 *            which spatial reference system to return
	 * 
	 * @return the retrieved feature(s) and total results count
	 */

	public IResult getFeaturesForUniqueNameSearch(String name, Integer startRow, Integer maxRows, String gazetteer, String count, String country,
			String srs, String searchVariants, String deepSrc) {
		// Split the list of names and normalise each name to search
		String[] names = name.split(",");
		String name_optimised = "";
		if (names.length < 2) {
			name_optimised = normalizeName(name);
		} else {
			name_optimised = getNormalizedNameList(name);
		}

		String[] gazetteers = gazetteer.split(",");
		String gazetteer_optimised = "";
		if (gazetteers.length < 2) {
			gazetteer_optimised = normalizeString(gazetteer);
		} else {
			gazetteer_optimised = getNormalizedStringList(gazetteer);
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(NAME_CURSOR, REFCURSOR);
		params.addValue(NAME, name_optimised);
		params.addValue(FEATURE_TYPE, null);
		params.addValue(SPATIAL_OP, null);
		params.addValue(NAME_DUPLICATES, true);
		params.addValue(NAME_FUZZINESS, 1);
		params.addValue(FTYPE_HIERARCHY, false);
		params.addValue(COUNTRY, country);
		params.addValue(XMIN, null);
		params.addValue(XMAX, null);
		params.addValue(YMIN, null);
		params.addValue(YMAX, null);
		params.addValue(GAZETTEER, gazetteer_optimised);
		params.addValue(SRS_IN, srs);
		params.addValue(SRS_OUT, srs);
		params.addValue(REAL_SPATIAL, false); // These parameters are ignored
												// for now...
		params.addValue(SPATIAL_MASK_ID, null);
		params.addValue(SPATIAL_BUFFER_M, null);
		params.addValue(START_YEAR, null);
		params.addValue(END_YEAR, null);
		params.addValue(USER_AUTHORISED, isAuthorised());
		params.addValue(START_ROW, startRow);
		params.addValue(MAX_ROWS, maxRows);
		params.addValue(ROW_COUNT, Boolean.parseBoolean(count));
		params.addValue(SQL_DEBUG, false);
		// Deep only
		params.addValue(SEARCH_VARIANTS, searchVariants);
		params.addValue(DEEP_SRC, deepSrc);

		SimpleJdbcCall proc = new SimpleJdbcCall(getDataSource());
		proc.withSchemaName(getSchemaName());
		proc.withFunctionName(API_GENERIC_SEARCH);
		FeatureRowMapper rowMapper = new FeatureRowMapper();
		proc.returningResultSet(REFCURSOR, rowMapper);
		proc.execute(params);

		DefaultResult result = new DefaultResult();
		List<IFeature> features = rowMapper.getFeatures();
		result.setFeatures(features);
		result.setTotalResultsCount(rowMapper.getRowCount());

		return result;
	}

	/**
	 * Optimises identifier(s) and gazetteer(s) lists, then retrieves footprints
	 * for identifier(s).
	 * 
	 * @param identifier
	 *            the Unlock identifier of the feature(s)
	 * @param srs
	 *            the spatial reference system to use
	 * 
	 * @return the retrieved footprint(s) and total results count
	 */
	public IResult getFeaturesForIdentifier(String identifier, String srs, String searchVariants, String deepSrc) {
		String[] identifiers = identifier.split(",");
		String identifier_optimised = "";
		if (identifiers.length < 2) {
			identifier_optimised = normalizeString(identifier);
		} else {
			identifier_optimised = getNormalizedStringList(identifier);
		}

		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue(NAME_CURSOR, REFCURSOR);
		params.addValue(NAME, identifier_optimised);
		params.addValue(FEATURE_TYPE, null);
		params.addValue(XMIN, null);
		params.addValue(YMIN, null);
		params.addValue(XMAX, null);
		params.addValue(YMAX, null);
		params.addValue(SPATIAL_OP, null);
		params.addValue(START_ROW, null);
		params.addValue(MAX_ROWS, null);
		params.addValue(GAZETTEER, null);
		params.addValue(USER_AUTHORISED, isAuthorised());
		params.addValue(COUNTRY, null);
		params.addValue(ROW_COUNT, null);
		params.addValue(SRS_IN, srs); // TODO SRS parameters in and out...
		params.addValue(SRS_OUT, srs);
		params.addValue(REAL_SPATIAL, null);
		params.addValue(SPATIAL_MASK_ID, null);
		params.addValue(SPATIAL_BUFFER_M, null);
		params.addValue(NAME_DUPLICATES, null);
		params.addValue(NAME_FUZZINESS, null); // This parameter is currently
												// not supported by the SQL
												// query
		params.addValue(FTYPE_HIERARCHY, null);
		params.addValue(START_YEAR, null);
		params.addValue(END_YEAR, null);
		params.addValue(SQL_DEBUG, false);

		// Deep only
		params.addValue(SEARCH_VARIANTS, searchVariants);
		params.addValue(DEEP_SRC, deepSrc);

		SimpleJdbcCall proc = new SimpleJdbcCall(getDataSource());
		proc.withSchemaName(getSchemaName());
		proc.withFunctionName(API_GENERIC_SEARCH);
		FeatureRowMapper rowMapper = new FeatureRowMapper();
		proc.returningResultSet(REFCURSOR, rowMapper);
		proc.execute(params);

		DefaultResult result = new DefaultResult();
		List<IFeature> features = rowMapper.getFeatures();
		result.setFeatures(features);
		result.setTotalResultsCount(rowMapper.getRowCount());

		return result;
	}

	/**
	 * Optimises the name search string.
	 * 
	 * @param name
	 *            the name of the feature(s)
	 * 
	 * @return an normalised/optimised list of feature(s)
	 */
	protected String normalizeName(String name) {
		String normalisedName = StringUtils.defaultString(name);

		// Add SQL wild-cards
		normalisedName = normalisedName.replace('*', '%');
		normalisedName = normalisedName.replace('?', '_');

		// Lower case and remove diacritical marks
		// normalisedName = normalisedName.toLowerCase();
		// normalisedName = Normalizer.decompose(normalisedName, false,
		// 0).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

		/*
		 * Commenting these normalisations out, due to database now doing
		 * them...
		 * 
		 * // Move these words to the end of the search string String[]
		 * hydrographicWords = {"river", "afon", "beck", "brook", "burn",
		 * "allt", "drain", "cut", "dike", "dyke", "east", "easter", "falls",
		 * "loch", "lochan", "lake", "battle"}; for(int
		 * i=0;i<hydrographicWords.length;i++){
		 * if(normalisedName.contains(hydrographicWords[i])){ normalisedName =
		 * normalisedName.replaceAll(hydrographicWords[i], ""); normalisedName =
		 * normalisedName += hydrographicWords[i]; } }
		 * 
		 * // Remove these words from inside the search string normalisedName =
		 * StringUtils.removeStart(normalisedName, "the "); normalisedName =
		 * StringUtils.removeEnd(normalisedName, " the"); normalisedName =
		 * normalisedName.replaceAll(" the ", ""); normalisedName =
		 * normalisedName.replaceAll(" of ", ""); normalisedName =
		 * normalisedName.replaceAll(" in ", ""); normalisedName =
		 * normalisedName.replaceAll(" over ", ""); normalisedName =
		 * normalisedName.replaceAll(" under ", ""); normalisedName =
		 * normalisedName.replaceAll(" for ", "");
		 */
		// Remove potential punctuation and whitespace

		normalisedName = normalisedName.replace('-', ' ');
		normalisedName = normalisedName.replace(')', ' ');
		normalisedName = normalisedName.replace('(', ' ');
		normalisedName = normalisedName.replace(':', ' ');
		normalisedName = normalisedName.replace(';', ' ');
		normalisedName = normalisedName.replace('\'', ' ');
		normalisedName = normalisedName.replace('\\', ' ');
		normalisedName = normalisedName.replace(',', ' '); // this won't happen
		normalisedName = normalisedName.replace('.', ' ');
		normalisedName = normalisedName.replace('/', ' ');
		normalisedName = normalisedName.replace('!', ' ');
		normalisedName = normalisedName.replace('|', ' ');
		normalisedName = normalisedName.replace('$', ' ');
		normalisedName = normalisedName.replace('£', ' ');
		normalisedName = normalisedName.replace('^', ' ');
		normalisedName = normalisedName.replace('@', ' ');
		normalisedName = normalisedName.replace('#', ' ');
		normalisedName = normalisedName.replace('=', ' ');

		normalisedName = StringUtils.trimToEmpty(normalisedName);
		// normalisedName = StringUtils.deleteWhitespace(normalisedName);

		String[] vals = normalisedName.split(" ");
		StringBuilder sb = new StringBuilder();
		for (String s : vals) {
			if (s.trim().length() > 0) {
				sb.append(s).append(" ");
			}
		}
		normalisedName = sb.toString().trim();
		logger.debug("Normalised name is: " + normalisedName);

		return normalisedName;
	}

	/**
	 * Optimises the gazetteer or featureType search string.
	 * 
	 * @param string
	 *            a list of gazetteer(s) or feature type(s).
	 * 
	 * @return a normalised/optimised string list
	 */
	protected String normalizeString(String string) {
		String normalisedString = StringUtils.defaultString(string);

		// Add wildcards
		normalisedString = normalisedString.replace('*', '%');
		normalisedString = normalisedString.replace('?', '_');

		// Lower case
		normalisedString = normalisedString.toLowerCase();

		// Trim
		normalisedString = StringUtils.trimToEmpty(normalisedString);

		return normalisedString;
	}

	/**
	 * Optimises a comma separated list of names
	 * 
	 * @param names
	 *            a list of names.
	 * 
	 * @return a normalised/optimised list of names
	 */
	protected String getNormalizedNameList(String names) {
		String[] nl = names.split(",");
		String[] op = new String[nl.length];
		for (int i = 0; i < nl.length; i++) {
			String optimised = normalizeName(nl[i]);
			op[i] = optimised;
		}
		return StringUtils.join(op, ",");
	}

	/**
	 * Optimises a comma separated list of gazetteers or feature types
	 * 
	 * @param names
	 *            a list of names.
	 * 
	 * @return a normalised/optimised list
	 */
	protected String getNormalizedStringList(String string) {
		String[] nl = string.split(",");
		String[] op = new String[nl.length];
		for (int i = 0; i < nl.length; i++) {
			String optimised = normalizeString(nl[i]);
			op[i] = optimised;
		}
		return StringUtils.join(op, ",");
	}

	/**
	 * Private class to map database results to <code>IFeature</code> objects.
	 * 
	 * @author Joe Vernon
	 */
	private class FeatureRowMapper implements ParameterizedRowMapper<IFeature> {
		
		private List<IFeature> features;
		// We want to limit the perimeter to two decimal places
		private String perimeterDecimalPlacePattern = "###.##";
		private DecimalFormat decimalPlaceFormatter;

		public FeatureRowMapper() {
			decimalPlaceFormatter = new DecimalFormat(perimeterDecimalPlacePattern);
			features = new ArrayList<IFeature>();
		}

		/**
		 * Maps a <code>ResultSet</code> to an <code>IFeature</code> object.
		 * 
		 * @param rs
		 *            the result set
		 * @param rowNum
		 *            the current row number
		 * @throws SQLException
		 *             if there is an exception from the database
		 */
		public IFeature mapRow(ResultSet rs, int rowNum) throws SQLException {
			Integer rowCount = rs.getInt(ROW_COUNT);
			Feature feature = new Feature();
			Integer identifier = rs.getInt(IDENTIFIER);
			Integer sourceIdentifier = rs.getInt(SOURCE_IDENTIFIER);
			String asciiname = rs.getString(ASCII_NAME);
			String name = rs.getString(NAME);
			String gazetteer = rs.getString(GAZETTEER);
			String featureType = rs.getString(FEATURE_TYPE);
			String fCodeEdina = rs.getString(FCODE_EDINA);
			Double xmin = rs.getDouble(XMIN);
			Double xmax = rs.getDouble(XMAX);
			Double ymin = rs.getDouble(YMIN);
			Double ymax = rs.getDouble(YMAX);
			String country = rs.getString(COUNTRY);
			String custodian = rs.getString(CUSTODIAN);
			Double xcentroid = rs.getDouble(XCENTROID);
			Double ycentroid = rs.getDouble(YCENTROID);
			Double area = rs.getDouble(AREA);
			Double perimeter = Double.parseDouble(decimalPlaceFormatter.format(rs.getDouble(PERIMETER)));
			Double elevation = rs.getDouble(ELEVATION);
			BigDecimal population = rs.getBigDecimal(POPULATION);
			String alternativeNames = rs.getString(ALT_NAME);
			String scale = rs.getString(SCALE);
			String isoCountryCode = rs.getString(ISO_COUNTRY_CODE);
			String adminLevel1 = rs.getString(ADMIN_LEVEL_1);
			String adminLevel2 = rs.getString(ADMIN_LEVEL_2);
			String uriins = rs.getString(URIINS);
			String uricdda = rs.getString(URICDDA);
			String madsid = rs.getString(MADSID);
			String variantid = rs.getString(VARIANTID);
			String attestations = rs.getString(ATTESTATIONS);
			String locations = rs.getString(LOCATIONS);
			String unlockfpsrc = rs.getString(UNLOCKFPSRC);
			
			String altIDs = rs.getString(ALT_UNLOCKID);
			List<Integer> alternativeIDList = null;
			if (StringUtils.isNotBlank(altIDs)) {
				alternativeIDList = new ArrayList<Integer>(altIDs.split(",").length);
				for (String myId : altIDs.split(",")) {
					alternativeIDList.add(Integer.parseInt(myId));
				}
			}
			feature.setRowCount(rowCount);
			feature.setIdentifier(identifier);
			feature.setSourceIdentifier(sourceIdentifier);
			feature.setNameOptimised(asciiname);
			feature.setName(name);
			feature.setGazetteer(gazetteer);
			feature.setFeatureType(featureType);
			feature.setEdinaFeatureCode(fCodeEdina);
			feature.setXmin(xmin);
			feature.setXmax(xmax);
			feature.setYmin(ymin);
			feature.setYmax(ymax);
			if (country == null) {
				country = "Undefined";
			}
			feature.setCountry(country);
			feature.setCustodian(custodian);
			feature.setXCentroid(xcentroid);
			feature.setYCentroid(ycentroid);
			feature.setArea(area);
			feature.setPerimeter(perimeter);
			feature.setElevation(elevation);
			feature.setPopulation(population);
			feature.setAlternativeNames(alternativeNames);
			feature.setScale(scale);
			if (isoCountryCode == null) {
				isoCountryCode = "Undefined";
			}
			feature.setCountryCode(isoCountryCode);
			feature.setAdminLevel1(adminLevel1);
			feature.setAdminLevel2(adminLevel2);
			feature.setAdminLevel3("");
			feature.setAdminLevel4("");
			feature.setAlternativeIds(alternativeIDList);
			feature.setUriins(uriins);
			feature.setUricdda(uricdda);
			feature.setMadsid(madsid);
			feature.setVariantid(variantid);
			feature.setAttestations(attestations);
			feature.setLocations(locations);
			feature.setUnlockfpsrc(unlockfpsrc);

			features.add(feature);
			return feature;
		}

		/**
		 * Gets the <code>List</code> of <code>IFeature</code>s.
		 * 
		 * @return the list of features.
		 */
		public List<IFeature> getFeatures() {
			return features;
		}

		/**
		 * Gets the total number of features available from this search.
		 * 
		 * @return the number of features.
		 */
		public Integer getRowCount() {
			if (features.isEmpty())
				return 0;
			else
				return features.get(0).getRowCount();
		}
	}

	/**
	 * Private class to map database results to <code>IFootprint</code>.
	 * 
	 * @author Brian O'Hare
	 * @author Joe Vernon
	 */
	private class FootprintRowMapper implements ParameterizedRowMapper<IFootprint> {

		private List<IFootprint> footprints;

		public FootprintRowMapper() {
			footprints = new ArrayList<IFootprint>();
		}

		public IFootprint mapRow(ResultSet rs, int rowNum) throws SQLException {
			Footprint footprint = new Footprint();
			Integer identifier = rs.getInt(IDENTIFIER);
			PGgeometry geom = new PGgeometry(rs.getString(GEOMETRY));
			String custodian = rs.getString(CUSTODIAN);

			footprint.setIdentifier(identifier);
			footprint.setGeometry(geom);
			footprint.setCustodian(custodian);
			footprints.add(footprint);
			return footprint;
		}

		public List<IFootprint> getFootprints() {
			return footprints;
		}
	}

	/**
	 * Private class to map database results to <code>IFeatureType</code>.
	 * 
	 * @author Brian O'Hare
	 * @author Joe Vernon
	 */
	private class FeatureTypeRowMapper implements ParameterizedRowMapper<IFeatureType> {

		private List<IFeatureType> featureTypes;

		public FeatureTypeRowMapper() {
			featureTypes = new ArrayList<IFeatureType>();
		}

		/**
		 * Maps a <code>ResultSet</code> to an <code>IFeatureType</code> object.
		 * 
		 * @param rs
		 *            the result set
		 * @param rowNum
		 *            the current row number
		 * @throws SQLException
		 *             if there is an exception from the database
		 */
		public IFeatureType mapRow(ResultSet rs, int rowNum) throws SQLException {
			FeatureType featureType = new FeatureType();
			String featureCode = rs.getString(FCODE);
			String name = rs.getString(NAME);
			Integer hierarchyLevel = rs.getInt(HLEVEL);
			featureType.setFeatureCode(featureCode);
			featureType.setName(name);
			featureType.setHierarchyLevel(hierarchyLevel);
			if (!name.equals("Not Available")) {
				featureTypes.add(featureType);
			}

			return featureType;
		}

		/**
		 * Gets the <code>List</code> of <code>IFeatureType</code>s.
		 * 
		 * @return the list of feature types.
		 */
		public List<IFeatureType> getFeatureTypes() {
			return featureTypes;
		}
	}

	public IResult getDistanceBetweenFeatures(final Integer unlockIdA, final Integer unlockIdB) {

		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue(UNLOCK_ID_A, unlockIdA, Types.BIGINT);
		params.addValue(UNLOCK_ID_B, unlockIdB, Types.BIGINT);
		params.addValue(USER_AUTHORISED, isAuthorised());
		params.addValue(SQL_DEBUG, false);

		DefaultResult result = new DefaultResult();

		SimpleJdbcCall proc = new SimpleJdbcCall(this.getDataSource());
		proc.withSchemaName(getSchemaName());
		proc.withFunctionName(CALCULATE_DISTANCE_BETWEEN_FEATURES);
		Map<String, Object> out = proc.execute(params);

		result.setDistanceBetweenFeatures((Integer) out.get(RETURNVALUE));

		return result;

	}

	public IResult getBufferedSearchFeatures(String identifier, Integer maxRows, String featureType, String gazetteer, Integer distance) {

		String featureTypeOptimised = "";

		if (StringUtils.isNotEmpty(featureType)) {
			String[] featureTypes = featureType.split(",");

			if (featureTypes.length < 2) {
				featureTypeOptimised = normalizeString(featureType);
			} else {
				featureTypeOptimised = getNormalizedStringList(featureType);
			}
		}
		String gazetteer_optimised = "";
		if (StringUtils.isNotEmpty(featureType)) {
			String[] gazetteers = gazetteer.split(",");

			if (gazetteers.length < 2) {
				gazetteer_optimised = normalizeString(gazetteer);
			} else {
				gazetteer_optimised = getNormalizedStringList(gazetteer);
			}
		}

		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue(NAME_CURSOR, "REFCURSOR");

		params.addValue(FEATURE_TYPE, featureTypeOptimised);

		params.addValue(XMIN, null);
		params.addValue(YMIN, null);
		params.addValue(XMAX, null);
		params.addValue(YMAX, null);
		params.addValue(SPATIAL_OP, null);
		params.addValue(START_ROW, 1);
		params.addValue(MAX_ROWS, maxRows);
		params.addValue(GAZETTEER, gazetteer_optimised);
		params.addValue(USER_AUTHORISED, isAuthorised());
		params.addValue(COUNTRY, null);
		params.addValue(SRS, null);
		params.addValue(NAME_DUPLICATES, null);
		params.addValue(NAME_FUZZINESS, null);
		params.addValue(FTYPE_HIERARCHY, null);
		params.addValue(START_YEAR, null);
		params.addValue(END_YEAR, null);
		params.addValue(SQL_DEBUG, false);
		params.addValue(NAME, null);
		params.addValue(FEATURE_IDENTIFIER, identifier);
		params.addValue(BUFFER_DISTANCE, distance);
		params.addValue(SRS_IN, null);
		params.addValue(SRS_OUT, null);
		SimpleJdbcCall proc = new SimpleJdbcCall(getDataSource());
		proc.withSchemaName(getSchemaName());
		proc.withFunctionName(BUFFERED_SEARCH);

		FeatureRowMapper rowMapper = new FeatureRowMapper();
		proc.returningResultSet(REFCURSOR, rowMapper);
		proc.execute(params);

		DefaultResult result = new DefaultResult();
		List<IFeature> features = rowMapper.getFeatures();
		result.setFeatures(features);
		result.setTotalResultsCount(rowMapper.getRowCount());

		return result;

	}

	private class NameRowMapper implements ParameterizedRowMapper<String> {
		private List<String> names;

		public NameRowMapper() {
			this.names = new ArrayList<String>();
		}

		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			this.names.add(rs.getString(NAME));
			return null;
		}

		public List<String> getNames() {
			return this.names;
		}
	}

	/**
	 * Wraps a list of deepAttestations
	 * 
	 * @author colin
	 */
	private class DeepAttestationRowMapper implements ParameterizedRowMapper<List<String>> {

		private static final String ATTESTATION_XML = "attestation_xml";
		private List<String> deepAttestations;

		public DeepAttestationRowMapper() {
			this.deepAttestations = new ArrayList<String>();
		}

		public List<String> mapRow(ResultSet rs, int rowNum) throws SQLException {
			String attestationString = rs.getString(ATTESTATION_XML);
			this.deepAttestations.add(attestationString);
			return this.deepAttestations;
		}
		
		/**
		 * @return Document of all attestations
		 */
		public Document getDeepAttestationsDocument() {

			// Set root element
			Document doc = new Document(new Element("attestations"));

			// Create a new Document for each attestation,
			// detach content, then add to main doc
			for (String attestation : this.deepAttestations) {
				try {
					Document d = new SAXBuilder().build(new StringReader(attestation));
					doc.getRootElement().addContent(d.detachRootElement());
				} catch (JDOMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return doc;
		}

	}
}