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
import edina.geocrosswalk.web.ws.FlatListToHierarchyListConverter;
import edina.geocrosswalk.web.ws.SingleNode;

/**
 * Provides Unlock / GeoCrossWalk format XML for sparse format.
 * 
 */
public class SparseGXWFormatProviderPlugin extends AbstractSparseFormatProviderPlugin {

	private static Logger logger = Logger.getLogger(SparseGXWFormatProviderPlugin.class);
	private static final String CONTENT_TYPE = "text/xml;charset=utf-8";
	private static final GXWFormat SUPPORTED_FORMAT = GXWFormat.GXW;
	private static final String NAME = "Sparse GXW XML Format Provider";
	private static final String DESCRIPTION = "Sparse GXW Default Format Provider";


    private static final String DISTANCE = "distance";

	

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


    public String getXmlForDistance(IResult result, String callback, String key) throws Exception {
        // TODO Auto-generated method stub
        VelocityEngine ve = getVelocityEngine();
        Template template = ve.getTemplate(getTemplate());
        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();

        
        Integer distanceBetweenFeatures = result.getDistanceBetweenFeatures();
        context.put(DISTANCE, distanceBetweenFeatures);   

        template.merge(context, writer);
        logger.debug("Output from feature template is:\n" + writer.toString());


        return writer.toString();
 

    }
}