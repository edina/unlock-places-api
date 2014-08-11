package edina.geocrosswalk.service.plugins;

import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.postgis.Geometry;
import org.postgis.MultiPoint;
import org.postgis.MultiPolygon;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.Polygon;

import edina.geocrosswalk.domain.IFootprint;
import edina.geocrosswalk.service.IResult;

/**
 * Provides GeoRSS.
 * 
 * @author Joe Vernon
 */

public class GEORSSFormatProviderPlugin extends AbstractFormatProviderPlugin {

	private static Logger logger = Logger.getLogger(GEORSSFormatProviderPlugin.class);
	private static final String CONTENT_TYPE = "text/xml;charset=utf-8";
	private static final GXWFormat SUPPORTED_FORMAT = GXWFormat.GEORSS;
	private static final String NAME = "GeoRSS Format Provider";
	private static final String DESCRIPTION = "GeoRSS Format Provider";
	private static final String TOTAL_RESULTS_COUNT = "totalResultsCount";
	private static final String FEATURE_LIST = "featureList";
	private static final String IDENTIFIER = "identifier";
	private static final String CUSTODIAN = "custodian";
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
		if(key != null && key != ""){
			key = "&amp;key="+key;
		}
		else{
			key = "";
		}
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
		if (footprints !=null && footprints.size() > 0) {
			totalFootprintsCount = footprints.size();	
			for (IFootprint footprint : result.getFootprints()) {
				
				output.append("  <entry>");
				output.append("    <id>"+footprint.getIdentifier()+"</id>");
				output.append("    <summary>Geometry for feature "+footprint.getIdentifier()+" from "+footprint.getCustodian()+"</summary>");

				// Add feature footprint coordinates
				PGgeometry geom = footprint.getGeometry();

				// When the feature has a Point geometry
				if (geom.getGeoType() == Geometry.POINT) {
					logger.debug("Found point footprint.");
					Point point = (Point) geom.getGeometry();
					output.append("    <georss:point>");
					output.append(point.getY());
					output.append(" ");
					output.append(point.getX());
					output.append("</georss:point>");
				}

				// When the feature has a MultiPoint geometry
				if (geom.getGeoType() == Geometry.MULTIPOINT){
					logger.debug("Found multipoint footprint.");
					MultiPoint multipoint = (MultiPoint) geom.getGeometry();
					Point[] points = multipoint.getPoints();
					output.append("    <georss:multipoint>\n");
					for (int i = 0; i < points.length; i++) {
						output.append("    <georss:point>");
						output.append(points[i].getY());
						output.append(",");
						output.append(points[i].getX());
						output.append("</georss:point>");
						if (i != points.length - 1) {
							output.append(" ");
						}
					}
					output.append("</georss:multipoint>\n");
				}
				
				// LineString is not supported by GeoRSS-Simple
				
				// MultiLineString is not supported by GeoRSS-Simple
								
				// When the feature has a Polygon geometry
				if (geom.getGeoType() == Geometry.POLYGON) {
					logger.debug("Found polygon footprint.");
					output.append("    <georss:polygon>\n      ");
					Polygon poly = (Polygon) geom.getGeometry();
					Point[] points = poly.getRing(0).getPoints();
					for (int i = 0; i < points.length; i++) {
						output.append(points[i].getY());
						output.append(" ");
						output.append(points[i].getX());
						if (i != points.length - 1) {
							output.append(" ");
						}
					}
					output.append("\n    </georss:polygon>");
				}

				// When the feature has a MultiPolygon geometry
				if (geom.getGeoType() == Geometry.MULTIPOLYGON) {
					logger.debug("Found multipolygon footprint.");
					MultiPolygon mpoly = (MultiPolygon) geom.getGeometry();
					Polygon[] polys = mpoly.getPolygons();

					for (int i = 0; i < polys.length; i++) {
						output.append("    <georss:polygon>\n      ");
						Point[] points = polys[i].getRing(0).getPoints();
						for (int j = 0; j < points.length; j++) {
							output.append(points[j].getY());
							output.append(" ");
							output.append(points[j].getX());
							if (i != points.length - 1) {
								output.append(" ");
							}
						}
						output.append("\n    </georss:polygon>");
						if (i != polys.length - 1) {
							output.append("\n");
						}
					}
				}
				output.append("  </entry>");
			}
		}
		context.put("output", output);
		context.put(TOTAL_FOOTPRINTS_COUNT, totalFootprintsCount);
		template.merge(context, writer);

		logger.debug("Output from footprint template is:\n" + writer.toString());

		return writer.toString();
	}


	public String getXmlForFeatureTypes(IResult result, String callback) throws Exception {
		VelocityEngine ve = getVelocityEngine();
		Template template = ve.getTemplate(getFeatureTypesFormatTemplate());
		VelocityContext context = new VelocityContext();
		StringWriter writer = new StringWriter();

		context.put(TOTAL_FEATURE_TYPES_COUNT, result.getTotalResultsCount());
		context.put(FEATURE_TYPES_LIST, result.getFeatureTypes());
		template.merge(context, writer);
		
		logger.debug("Output from feature types template is:\n" + writer.toString());
		
		return writer.toString();
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
