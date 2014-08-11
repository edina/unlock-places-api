package edina.geocrosswalk.service.plugins;

import java.io.StringWriter;
import java.util.List;

import net.sf.json.xml.XMLSerializer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jdom.output.XMLOutputter;
import org.postgis.Geometry;
import org.postgis.LineString;
import org.postgis.MultiLineString;
import org.postgis.MultiPoint;
import org.postgis.MultiPolygon;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.Polygon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edina.geocrosswalk.domain.IFootprint;
import edina.geocrosswalk.service.IResult;

/**
 * Provides GeoJSON.
 * 
 * @author Joe Vernon
 */

public class JSONFormatProviderPlugin extends AbstractFormatProviderPlugin {

	private static Logger logger = Logger.getLogger(JSONFormatProviderPlugin.class);
	private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";
	private static final String CONTENT_TYPE_CALLBACK = "text/javascript;charset=UTF-8";
	private static final GXWFormat SUPPORTED_FORMAT = GXWFormat.JSON;
	private static final String NAME = "GeoJSON Format Provider";
	private static final String DESCRIPTION = "GeoJSON Format Provider";
	private static final String TOTAL_RESULTS_COUNT = "totalResultsCount";
	private static final String FEATURE_LIST = "featureList";
	private static final String OUTPUT = "output";
	private static final String TOTAL_FEATURE_TYPES_COUNT = "totalFeatureTypesCount";
	private static final String TOTAL_FOOTPRINTS_COUNT = "totalFootprintsCount";
	private static final String FEATURE_TYPES_LIST = "featureTypesList";
	private static final String CALLBACK_HEADER = "callbackHeader";
	private static final String CALLBACK_FOOTER = "callbackFooter";
	private static final String KEY = "key";

	public String getXmlForFeatures(IResult result, String callback, String key) throws Exception {
		VelocityEngine ve = getVelocityEngine();
		Template template = ve.getTemplate(getFeatureFormatTemplate());
		VelocityContext context = new VelocityContext();
		StringWriter writer = new StringWriter();

		// If a JSONP callback parameter was specified, add it to the context
		if (StringUtils.isBlank(callback)) {
			context.put(CALLBACK_HEADER, "");
			context.put(CALLBACK_FOOTER, "");
		} else {
			context.put(CALLBACK_HEADER, callback + "(");
			context.put(CALLBACK_FOOTER, ");");
		}

		// Add the features
		context.put(TOTAL_RESULTS_COUNT, result.getTotalResultsCount());
		context.put(FEATURE_LIST, result.getFeatures());

		// If a key was specified, add it to the context for footprint URLs
		if (key != null && key != "") {
			key = "&key=" + key;
		} else {
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
		Integer totalFootprintsCount = 0;

		for (IFootprint footprint : result.getFootprints()) {
			totalFootprintsCount = result.getFootprints().size();

			output.append("    { \"type\" : \"Footprint\",\n");
			output.append("      \"geometry\" : {\n");

			// Add feature footprint coordinates
			PGgeometry geom = footprint.getGeometry();

			// When the feature has a Point geometry
			if (geom.getGeoType() == Geometry.POINT) {
				logger.debug("Found a Point footprint.");
				Point point = (Point) geom.getGeometry();
				output.append("        \"type\" : \"Point\",\n");
				output.append("        \"coordinates\" : [");
				output.append(point.getX());
				output.append(", ");
				output.append(point.getY());
				output.append("]\n");
			}

			// When the feature has a MultiPoint geometry
			if (geom.getGeoType() == Geometry.MULTIPOINT) {
				logger.debug("Found a MultiPoint footprint.");
				MultiPoint multipoint = (MultiPoint) geom.getGeometry();
				Point[] points = multipoint.getPoints();
				output.append("        \"type\" : \"MultiPoint\",\n");
				output.append("        \"coordinates\" : [\n");
				for (int i = 0; i < points.length; i++) {
					output.append("           [");
					output.append(points[i].getX());
					output.append(", ");
					output.append(points[i].getY());
					output.append("]");
					if (i != points.length - 1)
						output.append(", ");
				}
				output.append("\n        ]");
			}

			// When the feature has a LineString geometry
			if (geom.getGeoType() == Geometry.LINESTRING) {
				logger.debug("Found LineString footprint.");
				LineString linestring = (LineString) geom.getGeometry();
				Point[] points = linestring.getPoints();
				output.append("        \"type\" : \"LineString\",\n");
				output.append("        \"coordinates\" : [\n");
				output.append("           [");
				for (int j = 0; j < points.length; j++) {
					output.append("[");
					output.append(points[j].getX());
					output.append(", ");
					output.append(points[j].getY());
					output.append("]");
					if (j != points.length - 1)
						output.append(", ");
				}
				output.append("]\n      ]");
			}

			// When the feature has a MultiLineString geometry
			if (geom.getGeoType() == Geometry.MULTILINESTRING) {
				logger.debug("Found a MultiLineString footprint.");
				MultiLineString multilinestring = (MultiLineString) geom.getGeometry();
				LineString[] linestrings = multilinestring.getLines();
				output.append("        \"type\" : \"MultiLineString\",\n");
				output.append("        \"coordinates\" : [\n");
				for (int i = 0; i < linestrings.length; i++) {
					Point[] points = linestrings[i].getPoints();
					output.append("           [");
					for (int j = 0; j < points.length; j++) {
						output.append("[");
						output.append(points[j].getX());
						output.append(", ");
						output.append(points[j].getY());
						output.append("]");
						if (j != points.length - 1)
							output.append(", ");
					}
					output.append("]");
					if (i != linestrings.length - 1)
						output.append(",\n");
				}
				output.append("\n      ]");
			}

			// When the feature has a Polygon geometry
			if (geom.getGeoType() == Geometry.POLYGON) {
				logger.debug("Found a Polygon footprint.");
				Polygon poly = (Polygon) geom.getGeometry();

				// Outer ring
				Point[] points = poly.getRing(0).getPoints();
				output.append("        \"type\" : \"Polygon\",\n");
				output.append("        \"coordinates\" : [\n");
				output.append("	    [");
				for (int i = 0; i < points.length; i++) {
					output.append("[");
					output.append(points[i].getX());
					output.append(", ");
					output.append(points[i].getY());
					output.append("]");
					if (i != points.length - 1)
						output.append(", ");
				}
				output.append("]");

				// Write out any inner rings
				if (poly.numRings() > 1)
					output.append(",");

				for (int i = 1; i < poly.numRings(); i++) {
					Point[] innerRingPoints = poly.getRing(i).getPoints();
					output.append("\n	    [");
					for (int j = 0; j < innerRingPoints.length; j++) {
						output.append("[");
						output.append(innerRingPoints[j].getX());
						output.append(", ");
						output.append(innerRingPoints[j].getY());
						output.append("]");
						if (j != innerRingPoints.length - 1)
							output.append(", ");
					}
					output.append("]");
					if (i != poly.numRings() - 1)
						output.append(",");
				}
				output.append("\n      ]");
			}

			// When the feature has a MultiPolygon geometry, things get
			// complicated.
			if (geom.getGeoType() == Geometry.MULTIPOLYGON) {
				logger.debug("Found a MultiPolygon footprint.");
				MultiPolygon mpoly = (MultiPolygon) geom.getGeometry();
				Polygon[] polys = mpoly.getPolygons();

				output.append("        \"type\" : \"MultiPolygon\",\n");
				output.append("        \"coordinates\" : [\n");

				for (int i = 0; i < polys.length; i++) {
					output.append("	    [");
					// Outer ring
					Point[] points = polys[i].getRing(0).getPoints();
					output.append("[");
					for (int j = 0; j < points.length; j++) {
						output.append("[");
						output.append(points[j].getX());
						output.append(", ");
						output.append(points[j].getY());
						output.append("]");
						if (j != points.length - 1)
							output.append(", ");
					}
					output.append("]");

					// Write out any inner rings
					if (polys[i].numRings() > 1)
						output.append(",");

					for (int j = 1; j < polys[i].numRings(); j++) {
						Point[] innerRingPoints = polys[i].getRing(j).getPoints();
						output.append("\n	    [");
						for (int k = 0; k < innerRingPoints.length; k++) {
							output.append("[");
							output.append(innerRingPoints[k].getX());
							output.append(", ");
							output.append(innerRingPoints[k].getY());
							output.append("]");
							if (k != innerRingPoints.length - 1)
								output.append(", ");
						}
						output.append(" ]");
						if (j != polys[i].numRings() - 1)
							output.append(",");
					}
					output.append("]");
					if (i != polys.length - 1)
						output.append(",\n");
				}
				output.append("\n        ]");
			}

			output.append("},\n");
			output.append("      \"properties\" : {\n");
			output.append("        \"id\" : \"" + footprint.getIdentifier() + "\",\n");
			output.append("        \"custodian\" : \"" + footprint.getCustodian() + "\"\n");
			output.append("      }\n");
			output.append("    }");

			if (result.getFootprints().indexOf(footprint) == result.getFootprints().size() - 1) {
				output.append("");
			} else
				output.append(",\n");
		}

		if (StringUtils.isBlank(callback)) {
			context.put(CALLBACK_HEADER, "");
			context.put(CALLBACK_FOOTER, "");
		} else {
			context.put(CALLBACK_HEADER, callback + "(");
			context.put(CALLBACK_FOOTER, ");");
		}
		context.put(TOTAL_FOOTPRINTS_COUNT, totalFootprintsCount);
		context.put(OUTPUT, output);
		template.merge(context, writer);
		logger.debug("Output from footprint template is:\n" + writer.toString());
		return writer.toString();
	}

	public String getXmlForFeatureTypes(IResult result, String callback) throws Exception {
		VelocityEngine ve = getVelocityEngine();
		Template template = ve.getTemplate(getFeatureTypesFormatTemplate());
		VelocityContext context = new VelocityContext();
		StringWriter writer = new StringWriter();
		if (StringUtils.isBlank(callback)) {
			context.put(CALLBACK_HEADER, "");
			context.put(CALLBACK_FOOTER, "");
		} else {
			context.put(CALLBACK_HEADER, callback + "(");
			context.put(CALLBACK_FOOTER, ");");
		}
		context.put(TOTAL_FEATURE_TYPES_COUNT, result.getFeatureTypes().size());
		context.put(FEATURE_TYPES_LIST, result.getFeatureTypes());
		template.merge(context, writer);
		logger.debug("Output from feature types template is:\n" + writer.toString());
		return writer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edina.geocrosswalk.service.plugins.AbstractFormatProviderPlugin#
	 * getOutputForAutoComplete(java.util.List)
	 */

	public String getOutputForAutoComplete(List<String> names, String callback) {
		Gson gSon = new GsonBuilder().setPrettyPrinting().create();

		if(callback == null){
			return gSon.toJson(new AutoComplete(names));
		}
		
		// If callback isn't null, wrap result
		StringBuilder sb = new StringBuilder();
		sb.append(callback);
		sb.append("(");
		sb.append(gSon.toJson(new AutoComplete(names)));
		sb.append(");");

		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.service.plugins.IFormatProviderPlugin#getContentType()
	 */
	public String getContentType(String callback) {
		if (StringUtils.isBlank(callback)) {
			return CONTENT_TYPE;
		} else {
			return CONTENT_TYPE_CALLBACK;
		}
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

	private class AutoComplete {
		private List<String> names;

		public AutoComplete(List<String> names) {
			this.names = names;

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edina.geocrosswalk.service.plugins.AbstractFormatProviderPlugin#
	 * getOutputForDeepAttestation(edina.geocrosswalk.service.IResult,
	 * java.lang.String)
	 */
	public String getOutputForDeepAttestation(IResult result, String callback) {
		return new XMLSerializer().read(new XMLOutputter().outputString(result.getDeepAttestations())).toString(2);
	}
}