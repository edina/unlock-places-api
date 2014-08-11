package edina.geocrosswalk.service.plugins;

import java.io.StringWriter;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.postgis.Geometry;
import org.postgis.LineString;
import org.postgis.LinearRing;
import org.postgis.MultiLineString;
import org.postgis.MultiPoint;
import org.postgis.MultiPolygon;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.Polygon;

import edina.geocrosswalk.domain.IFootprint;
import edina.geocrosswalk.service.IResult;

/**
 * Provides KML xml.
 * 
 * @author Joe Vernon
 */
public class KMLFormatProviderPlugin extends AbstractFormatProviderPlugin {

	private static Logger logger = Logger.getLogger(KMLFormatProviderPlugin.class);
	private static final String CONTENT_TYPE = "application/vnd.google-earth.kml+xml";
	private static final GXWFormat SUPPORTED_FORMAT = GXWFormat.KML;
	private static final String NAME = "KML Format Provider";
	private static final String DESCRIPTION = "KML Format Provider";
	private static final String TOTAL_RESULTS_COUNT = "totalResultsCount";
	private static final String FEATURE_LIST = "featureList";
	private static final String OUTPUT = "output";
	private static final String TOTAL_FEATURE_TYPES_COUNT = "totalFeatureTypesCount";
	private static final String TOTAL_FOOTPRINT_COUNT = "totalFootprintCount";
	private static final String FEATURE_TYPES_LIST = "featureTypesList";
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
		Integer totalFootprintCount = 0;
		
		if (footprints != null && footprints.size() > 0) {
			totalFootprintCount = footprints.size();
			for (IFootprint footprint : result.getFootprints()) {
			
				output.append("<Placemark id=\""+footprint.getIdentifier()+"\">");
				output.append("<description>Geometry for feature "+footprint.getIdentifier()+" from "+footprint.getCustodian()+"</description>");
				output.append("<styleUrl>#unlockStyle</styleUrl>");
				
				// Add feature footprint coordinates
				PGgeometry geom = footprint.getGeometry();

				// When the feature has a Point geometry
				if (geom.getGeoType() == Geometry.POINT) {
					logger.debug("Found point footprint.");
					Point point = (Point) geom.getGeometry();
					output.append("<Point>\n<coordinates>");
					output.append(point.getX());
					output.append(",");
					output.append(point.getY());
					output.append(",0");
					output.append("</coordinates>\n</Point>\n");
				}

				// When the feature has a MultiPoint geometry
				if (geom.getGeoType() == Geometry.MULTIPOINT){
					logger.debug("Found multipoint footprint.");
					MultiPoint multipoint = (MultiPoint) geom.getGeometry();
					Point[] points = multipoint.getPoints();
					output.append("<MultiPoint>");
					for (int i = 0; i < points.length; i++) {
						output.append("<Point>\n<coordinates>");
						output.append(points[i].getX());
						output.append(",");
						output.append(points[i].getY());
						output.append("</coordinates>\n</Point>");
						if (i != points.length - 1) {
							output.append(" ");
						}
					}
					output.append("</MultiPoint>");
				}

				// When the feature has a LineString geometry
				if (geom.getGeoType() == Geometry.LINESTRING){
					logger.debug("Found LineString footprint.");
					LineString linestring = (LineString) geom.getGeometry();
					Point[] points = linestring.getPoints();
					output.append("<LineString>\n<coordinates>\n");
					for (int j = 0; j < points.length; j++) {
						output.append(points[j].getX());
						output.append(", ");
						output.append(points[j].getY());
						output.append("\n");
						if (j != points.length - 1)
							output.append("\n");
					}
					output.append("</coordinates>\n</LineString>");
				}
				
				// When the feature has a MultiLineString geometry
				if (geom.getGeoType() == Geometry.MULTILINESTRING){
					logger.debug("Found a MultiLineString footprint.");
					MultiLineString multilinestring = (MultiLineString) geom.getGeometry();
					LineString[] linestrings = multilinestring.getLines();
					output.append("<MultiGeometry>\n");
					for (int i = 0; i < linestrings.length; i++) {
						Point[] points = linestrings[i].getPoints();
						output.append("<LineString>\n<coordinates>\n");
						for (int j = 0; j < points.length; j++) {
							output.append(points[j].getX());
							output.append(", ");
							output.append(points[j].getY());
							if (j != points.length - 1)
								output.append("\n");
						}
						output.append("</coordinates>\n</LineString>");
						if (i != linestrings.length - 1)
							output.append("\n");
					}
					output.append("\n</MultiGeometry>");
				}
								
				// When the feature has a Polygon geometry
				if (geom.getGeoType() == Geometry.POLYGON) {
					logger.debug("Found polygon footprint.");
					output.append("<Polygon>\n");
					Polygon poly = (Polygon) geom.getGeometry();
					LinearRing ring = new LinearRing(poly.getRing(0).toString());
					Point[] points = ring.getPoints();
					output.append("<outerBoundaryIs>\n<LinearRing>\n<coordinates>");
					for (int i = 0; i < points.length; i++) {
						output.append(points[i].getX());
						output.append(",");
						output.append(points[i].getY());
						output.append(",0 ");
					}
					output.append("</coordinates>\n</LinearRing>\n</outerBoundaryIs>");

					for (int i = 1; i < poly.numRings(); i++) {
						LinearRing innerRing = new LinearRing(poly.getRing(i).toString());
						Point[] innerRingPoints = innerRing.getPoints();
						output.append("<innerBoundaryIs>\n<LinearRing>\n<coordinates>\n");
						for (int j = 0; j < innerRingPoints.length; j++) {
							output.append(innerRingPoints[j].getX());
							output.append(",");
							output.append(innerRingPoints[j].getY());
							output.append(",0 ");
						}
						output.append("\n</coordinates>\n</LinearRing>\n</innerBoundaryIs>\n");
					}
					output.append("</Polygon>\n");
				}

				// When the feature has a MultiPolygon geometry
				if (geom.getGeoType() == Geometry.MULTIPOLYGON) {
					logger.debug("Found multipolygon footprint.");
					MultiPolygon mpoly = (MultiPolygon) geom.getGeometry();
					Polygon[] polys = mpoly.getPolygons();
					output.append("<MultiGeometry>");
					for (int i = 0; i < polys.length; i++) {
						output.append("<Polygon>\n");
						LinearRing ring = new LinearRing(polys[i].getRing(0).toString());
						Point[] points = ring.getPoints();
						output.append("<outerBoundaryIs>\n<LinearRing>\n<coordinates>");
						for (int j = 0; j < points.length; j++) {
							output.append(points[j].getX());
							output.append(",");
							output.append(points[j].getY());
							output.append(",0 ");
						}
						output.append("</coordinates>\n</LinearRing>\n</outerBoundaryIs>");

						for (int j = 1; j < polys[i].numRings(); j++) {
							LinearRing innerRing = new LinearRing(polys[i].getRing(j).toString());
							Point[] innerRingPoints = innerRing.getPoints();
							output.append("<innerBoundaryIs>\n<LinearRing>\n<coordinates>\n");
							for (int k = 0; k < innerRingPoints.length; k++) {
								output.append(innerRingPoints[k].getX());
								output.append(",");
								output.append(innerRingPoints[k].getY());
								output.append(",0 ");
							}
							output.append("\n</coordinates>\n</LinearRing>\n</innerBoundaryIs>\n");
						}
						output.append("</Polygon>\n");
					}
					output.append("</MultiGeometry>");
				}
				output.append("</Placemark>");
			}
		}
		context.put(OUTPUT, output);
		context.put(TOTAL_FOOTPRINT_COUNT, totalFootprintCount);
		template.merge(context, writer);

		logger.debug("Output from footprint template is:\n" + writer.toString());

		// Return a String
		return writer.toString();
	}


	public String getXmlForFeatureTypes(IResult result, String callback) throws Exception {
		VelocityEngine ve = getVelocityEngine();
		Template template = ve.getTemplate(getFeatureTypesFormatTemplate());
		VelocityContext context = new VelocityContext();
		StringWriter writer = new StringWriter();

		context.put(TOTAL_FEATURE_TYPES_COUNT, result.getFeatureTypes().size());
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
