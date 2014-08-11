package edina.geocrosswalk.service.plugins;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

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
import edina.geocrosswalk.web.ws.FlatListToHierarchyListConverter;
import edina.geocrosswalk.web.ws.SingleNode;

/**
 * Provides HTML format.
 * 
 * @author Joe Vernon
 * 
 */
public class HTMLFormatProviderPlugin extends AbstractFormatProviderPlugin {

	private static Logger logger = Logger.getLogger(HTMLFormatProviderPlugin.class);
	private static final String CONTENT_TYPE = "text/html;charset=utf-8";
	private static final GXWFormat SUPPORTED_FORMAT = GXWFormat.HTML;
	private static final String NAME = "HTML Format Provider";
	private static final String DESCRIPTION = "HTML Format Provider";
	private static final String TOTAL_RESULTS_COUNT = "totalResultsCount";
	private static final String FEATURE_LIST = "featureList";
	private static final String FOOTPRINT_LIST = "footprintList";
	private static final String FOOTPRINT_IDS = "footprintIDs";
	private static final String OUTPUT = "output";
	private static final String TOTAL_FEATURE_TYPES_COUNT = "totalFeatureTypesCount";
	private static final String FEATURE_TYPES_LIST = "featureTypesList";
	private static final String TOTAL_FOOTPRINTS_COUNT = "totalFootprintsCount";
	private static final String PAGE_COUNT = "pageCount";
	private static final String PAGE_LIST = "pageList";
	private static final int RESULTS_PER_PAGE = 20;
	private static final String KEY = "key";

	public String getXmlForFeatures(IResult result, String callback, String key) throws Exception {
		VelocityEngine ve = getVelocityEngine();
		Template template = ve.getTemplate(getFeatureFormatTemplate());
		VelocityContext context = new VelocityContext();
		StringWriter writer = new StringWriter();

		context.put(TOTAL_RESULTS_COUNT, result.getTotalResultsCount());
		Integer numberOfPages = (result.getTotalResultsCount() / RESULTS_PER_PAGE) + (result.getTotalResultsCount() % RESULTS_PER_PAGE > 0 ? 1 : 0);
		List<Integer> pageList = new ArrayList<Integer>(numberOfPages);
		for(int i=0;i<numberOfPages;i++) pageList.add(i);
		context.put(PAGE_LIST, pageList);
		
		context.put(FEATURE_LIST, result.getFeatures());
		if(key != null && key != ""){key = "&key="+key;}
		else{key = "";}
		context.put(KEY, key);
		
		template.merge(context, writer);
		logger.debug("Output from feature template is:\n" + writer.toString());

		// Return a String
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
		if(key != null && key != ""){key = "&key="+key;}
		else{key = "";}
		context.put(KEY, key);
		if (footprints != null && footprints.size() > 0) {
			totalFootprintsCount = result.getFootprints().size();
			String footprintIDs = new String("");
			
			for (IFootprint footprint : result.getFootprints()) {
				footprintIDs+=footprint.getIdentifier();
				footprintIDs+=",";
				
				context.put(FOOTPRINT_LIST, result.getFootprints());

				output.append("<div class='footprint'>");
				// Add feature identifier and custodian
				//output.delete(0, output.length());

			    output.append("<p class='footprint-identifier'>Feature ID: "+ footprint.getIdentifier() + ", " + footprint.getCustodian()+"</p>");

				// Add feature footprint coordinates
				PGgeometry geom = footprint.getGeometry();
				output.append("<p class='footprint-geometry'>");
				
				// When the feature has a Point geometry
				if (geom.getGeoType() == Geometry.POINT) {
					logger.debug("Found point footprint.");
					Point point = (Point) geom.getGeometry();
					output.append("<p class='geom-point'>Point:<br/>      <span class='geom-coordinates'>");
					output.append(point.getX());
					output.append(",");
					output.append(point.getY());
					output.append("</span><br/>    </p>");
				}

				// When the feature has a MultiPoint geometry
				if (geom.getGeoType() == Geometry.MULTIPOINT){
					logger.debug("Found multipoint footprint.");
					MultiPoint multipoint = (MultiPoint) geom.getGeometry();
					Point[] points = multipoint.getPoints();
					output.append("<p class='geom-multipoint'>Multi-point:<br/><span class='geom-coordinates'>");
					for (int i = 0; i < points.length; i++) {
						output.append(points[i].getX());
						output.append(",");
						output.append(points[i].getY());
						if (i != points.length - 1) {
							output.append(" ");
						}
					}
					output.append("</span><br/></p>");
				}
				
				// When the feature has a LineString geometry
				if (geom.getGeoType() == Geometry.LINESTRING){
					logger.debug("Found LineString footprint.");
					LineString linestring = (LineString) geom.getGeometry();
					Point[] points = linestring.getPoints();
					output.append("<p class='geom-linestring'>Linestring:<br/><span class='geom-coordinates'>");
					for (int j = 0; j < points.length; j++) {
						output.append(points[j].getX());
						output.append(", ");
						output.append(points[j].getY());
						if (j != points.length - 1)
							output.append(" ");
					}
					output.append("</span><br/></p>");
				}
				
				// When the feature has a MultiLineString geometry
				if (geom.getGeoType() == Geometry.MULTILINESTRING){
					logger.debug("Found a MultiLineString footprint.");
					MultiLineString multilinestring = (MultiLineString) geom.getGeometry();
					LineString[] linestrings = multilinestring.getLines();
					output.append("<p class='geom-multilinestring'>Multi-Linestring:<br/><span class='geom-coordinates'>");
					for (int i = 0; i < linestrings.length; i++) {
						Point[] points = linestrings[i].getPoints();
						output.append("<span class='geom-line'>");
						for (int j = 0; j < points.length; j++) {
							output.append(points[j].getX());
							output.append(", ");
							output.append(points[j].getY());
							if (j != points.length - 1)
								output.append(" ");
						}
						output.append("</span>");
						if (i != linestrings.length - 1)
							output.append("\n");
					}
					output.append("</span><br/></p>");
				}
				
				// When the feature has a Polygon geometry
				if (geom.getGeoType() == Geometry.POLYGON) {
					logger.debug("Found polygon/multipolygon footprint.");
					Polygon poly = (Polygon) geom.getGeometry();
					Point[] points = poly.getRing(0).getPoints();
					output.append("<p class='geom-polygon'>Polygon:<br/><span class='geom-coordinates'>");
					for (int i = 0; i < points.length; i++) {
						output.append(points[i].getX());
						output.append(",");
						output.append(points[i].getY());
						if (i != points.length - 1) {
							output.append(" ");
						}
					}
					output.append("</span><br/></p>");
				}

				// When the feature has a MultiPolygon geometry
				if (geom.getGeoType() == Geometry.MULTIPOLYGON) {
					MultiPolygon mpoly = (MultiPolygon) geom.getGeometry();
					Polygon[] polys = mpoly.getPolygons();
					for (int i = 0; i < polys.length; i++) {
						output.append("<p class='geom-multipolygon'>Multi-polygon:<br/><span class='geom-coordinates'>");
						Point[] points = polys[i].getRing(0).getPoints();
						for (int j = 0; j < points.length; j++) {
							output.append(points[j].getX());
							output.append(",");
							output.append(points[j].getY());
							if (j != points.length - 1) {
								output.append(" ");
							}
						}
						output.append("</span>\n    </p>");
						if (i != polys.length - 1) {
							output.append("<br/>");
						}
					}
				}
				output.append("    </p>");
				output.append("  </div>");
			}
			footprintIDs = footprintIDs.substring(0, footprintIDs.length()-1); // remove comma at the end
			context.put(FOOTPRINT_IDS, footprintIDs);
			context.put(OUTPUT, output);
		}
		else{
			context.put(OUTPUT, "");
		}
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

		for (IFeatureType element : result.getFeatureTypes()) {
			logger.debug(element.getName() + " " + element.getHierarchyLevel());
		}

		FlatListToHierarchyListConverter converter = new FlatListToHierarchyListConverter();
		List<IFeatureType> featureTypes = result.getFeatureTypes();
		Integer totalFeatureTypesCount = new Integer(0);
		List<SingleNode> hierarchy = null;
		if (featureTypes != null && featureTypes.size() > 0) {
			totalFeatureTypesCount = featureTypes.size();
			hierarchy = converter.hierarchify(result.getFeatureTypes());
			context.put(FEATURE_TYPES_LIST, hierarchy);
		}
		context.put(TOTAL_FEATURE_TYPES_COUNT, totalFeatureTypesCount);
		context.put(FEATURE_TYPES_LIST, result.getFeatureTypes());
		template.merge(context, writer);
		
		logger.debug("Output from feature format template is:\n" + writer.toString());

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