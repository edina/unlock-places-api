package edina.geocrosswalk.service.plugins;

import java.io.StringWriter;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.postgis.Geometry;
import org.postgis.LineString;
import org.postgis.MultiLineString;
import org.postgis.MultiPoint;
import org.postgis.MultiPolygon;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.Polygon;

import edina.geocrosswalk.domain.IFeatureType;
import edina.geocrosswalk.domain.IFootprint;
import edina.geocrosswalk.service.IResult;

/**
 * Provides plaintext output.
 * 
 * @author Joe Vernon
 */
public class TXTFormatProviderPlugin extends AbstractFormatProviderPlugin {

	private static Logger logger = Logger.getLogger(TXTFormatProviderPlugin.class);
	private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";
	private static final GXWFormat SUPPORTED_FORMAT = GXWFormat.TXT;
	private static final String NAME = "TXT Format Provider";
	private static final String DESCRIPTION = "TXT Format Provider";
	private static final String TOTAL_RESULTS_COUNT = "totalResultsCount";
	private static final String FEATURE_LIST = "featureList";
	private static final String OUTPUT = "output";
	private static final String TOTAL_FEATURE_TYPES_COUNT = "totalFeatureTypesCount";
	private static final String FEATURE_TYPES_LIST = "featureTypesList";
	private static final String TOTAL_FOOTPRINTS_COUNT = "totalFootprintsCount";
	private static final String KEY = "key";

	public String getXmlForFeatures(IResult result, String callback, String key) throws Exception {
		VelocityEngine ve = getVelocityEngine();
		Template template = ve.getTemplate(getFeatureFormatTemplate());
		VelocityContext context = new VelocityContext();
		StringWriter writer = new StringWriter();

		context.put(TOTAL_RESULTS_COUNT, result.getTotalResultsCount());
		context.put(FEATURE_LIST, result.getFeatures());
		context.put(KEY, key);
		
		template.merge(context, writer);
		logger.debug("Output from feature template is:\n" + writer.toString());

		return writer.toString();
	}


	public String getXmlForFootprint(IResult result, String callback, String key) throws Exception {
		VelocityEngine ve = getVelocityEngine();
		Template template = ve.getTemplate(getFootprintFormatTemplate());
		VelocityContext context = new VelocityContext();
		StringWriter writer = new StringWriter();
		StringBuilder output = new StringBuilder();
		List<IFootprint> footprints = result.getFootprints();
		Integer totalFootprintsCount = 0;
		
		if (footprints != null && footprints.size() > 0) {
			totalFootprintsCount = result.getFootprints().size();
			
			output.append(totalFootprintsCount + "\n");
			output.append("id, custodian, footprint\n");
			
			for (IFootprint footprint : result.getFootprints()) {
				// Add feature identifier and custodian	
				output.append("\"" + footprint.getIdentifier() + "\"" + ", ");
				output.append("\"" + footprint.getCustodian() + "\"" + ", ");

				// Add feature footprint coordinates
				PGgeometry geom = footprint.getGeometry();

				// When the feature has a point geometry
				if (geom.getGeoType() == Geometry.POINT) {
					logger.debug("Found point footprint.");
					Point point = (Point) geom.getGeometry();
					output.append("\"(");
					output.append(point.getX());
					output.append(",");
					output.append(point.getY());
					output.append(")\"");
				}

				// When the feature has a MultiPoint geometry
				if (geom.getGeoType() == Geometry.MULTIPOINT){
					logger.debug("Found multipoint footprint.");
					MultiPoint multipoint = (MultiPoint) geom.getGeometry();
					Point[] points = multipoint.getPoints();
					output.append("\"(");
					for (int i = 0; i < points.length; i++) {
						output.append("(");
						output.append(points[i].getX());
						output.append(",");
						output.append(points[i].getY());
						output.append(")");
						if (i != points.length - 1) {
							output.append(" ");
						}
					}
					output.append(")\"");
				}
				  
				// When the feature has a LineString geometry
				if (geom.getGeoType() == Geometry.LINESTRING){
					logger.debug("Found LineString footprint.");
					LineString linestring = (LineString) geom.getGeometry();
					Point[] points = linestring.getPoints();
					output.append("\"(");
					for (int j = 0; j < points.length; j++) {
						output.append("(");
						output.append(points[j].getX());
						output.append(", ");
						output.append(points[j].getY());
						output.append(")");
						if (j != points.length - 1)
							output.append(", ");
					}
					output.append(")\"\n");
				}
				
				// When the feature has a MultiLineString geometry
				if (geom.getGeoType() == Geometry.MULTILINESTRING){
					logger.debug("Found a MultiLineString footprint.");
					MultiLineString multilinestring = (MultiLineString) geom.getGeometry();
					LineString[] linestrings = multilinestring.getLines();
					output.append("\"(");
					for (int i = 0; i < linestrings.length; i++) {
						Point[] points = linestrings[i].getPoints();
						output.append("(");
						for (int j = 0; j < points.length; j++) {
							output.append("(");
							output.append(points[j].getX());
							output.append(", ");
							output.append(points[j].getY());
							output.append(")");
							if (j != points.length - 1)
								output.append(", ");
						}
						output.append(")");
						if (i != linestrings.length - 1)
							output.append(",\n");
					}
					output.append(")\"\n");
				}
				
				// When the feature has a Polygon geometry
				if (geom.getGeoType() == Geometry.POLYGON) {
					logger.debug("Found polygon/multipolygon footprint.");
					Polygon poly = (Polygon) geom.getGeometry();
					Point[] points = poly.getRing(0).getPoints();
					output.append("\"(");
					for (int i = 0; i < points.length; i++) {
						output.append(points[i].getX());
						output.append(",");
						output.append(points[i].getY());
						if (i != points.length - 1) {
							output.append(" ");
						}
					}
					output.append(")\"");
				}
				
				// When the feature has a MultiPolygon geometry
				if (geom.getGeoType() == Geometry.MULTIPOLYGON) {
					MultiPolygon mpoly = (MultiPolygon) geom.getGeometry();
					Polygon[] polys = mpoly.getPolygons();
					for (int i = 0; i < polys.length; i++) {
						output.append("\"(");
						Point[] points = polys[i].getRing(0).getPoints();
						for (int j = 0; j < points.length; j++) {
							output.append(points[j].getX());
							output.append(",");
							output.append(points[j].getY());
							if (j != points.length - 1) {
								output.append(" ");
							}
						}
						output.append(")");
						if (i != polys.length - 1) {
							output.append("\n");
						}
					}
				}
				output.append("\n");
			}
			context.put(OUTPUT, output);
		}
		else{
			context.put(OUTPUT, "0");
		}
		template.merge(context, writer);

		logger.debug("Output from footprint template is:\n" + writer.toString());

		return writer.toString();
	}


	public String getXmlForFeatureTypes(IResult result, String callback) throws Exception {
		VelocityEngine ve = getVelocityEngine();
		Template template = ve.getTemplate(getFeatureTypesFormatTemplate());

		VelocityContext context = new VelocityContext();
		StringWriter writer = new StringWriter();

		for (IFeatureType element : result.getFeatureTypes()) {
			logger.debug(element.getName() + " " + element.getHierarchyLevel());
		}

		context.put(TOTAL_FEATURE_TYPES_COUNT, result.getFeatureTypes().size());
		context.put(FEATURE_TYPES_LIST, result.getFeatureTypes());

		template.merge(context, writer);
		logger.debug("Output from feature format template is:\n" + writer.toString());

		return writer.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see edina.geocrosswalk.service.plugins.AbstractFormatProviderPlugin#getOutputForAutoComplete(java.util.List)
	 */

	public String getOutputForAutoComplete(List<String> names, String callback){
        StringBuffer buffer = new StringBuffer();
        for (String name : names) {
            buffer.append(name + "\n");            
        }
        
        return buffer.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.service.plugins.IFormatProviderPlugin#getContentType()
	 */
	public String getContentType(String callback) {
		return CONTENT_TYPE;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.service.plugins.IFormatProviderPlugin#getDescription()
	 */
	public String getDescription() {
		return DESCRIPTION;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.service.plugins.IFormatProviderPlugin#getDisplayName()
	 */
	public String getDisplayName() {
		return NAME;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see edina.geocrosswalk.service.plugins.IFormatProviderPlugin#getName()
	 */
	public String getName() {
		return NAME;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @seeedina.geocrosswalk.service.plugins.IFormatProviderPlugin#
	 * getSupportedExportFormat()
	 */
	public GXWFormat getSupportedFormat() {
		return SUPPORTED_FORMAT;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.service.plugins.IFormatProviderPlugin#supports(edina
	 * .geocrosswalk.service.plugins.GXWFormat)
	 */
	public boolean supports(GXWFormat format) {
		return SUPPORTED_FORMAT.equals(format);
	}

}
