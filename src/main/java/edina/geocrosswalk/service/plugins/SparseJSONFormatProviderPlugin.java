package edina.geocrosswalk.service.plugins;

import java.io.StringWriter;

import org.apache.commons.lang.StringUtils;
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

import edina.geocrosswalk.domain.IFootprint;
import edina.geocrosswalk.service.IResult;

/**
 * Provides GeoJSON for the sparse templates
 * 
 */

public class SparseJSONFormatProviderPlugin extends AbstractSparseFormatProviderPlugin {

    private static Logger logger = Logger.getLogger(SparseJSONFormatProviderPlugin.class);
    private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";
    private static final String CONTENT_TYPE_CALLBACK = "text/javascript;charset=UTF-8";
    private static final GXWFormat SUPPORTED_FORMAT = GXWFormat.JSON;
    private static final String NAME = "Sparse GeoJSON Format Provider";
    private static final String DESCRIPTION = "SparseGeoJSON Format Provider";
    private static final String DISTANCE = "distance";

	private static final String CALLBACK_HEADER = "callbackHeader";
	private static final String CALLBACK_FOOTER = "callbackFooter";


    
	

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
        
        VelocityEngine ve = getVelocityEngine();
        Template template = ve.getTemplate(getTemplate());
        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();
        
        // If a JSONP callback parameter was specified, add it to the context
        if(StringUtils.isBlank(callback)){
            context.put(CALLBACK_HEADER, "");
            context.put(CALLBACK_FOOTER, "");
        }else{
            context.put(CALLBACK_HEADER, callback+"(");
            context.put(CALLBACK_FOOTER, ");");
        }

        Integer distanceBetweenFeatures = result.getDistanceBetweenFeatures();
        context.put(DISTANCE, distanceBetweenFeatures);    

        
        template.merge(context, writer);
        logger.debug("Output from feature template is:\n" + writer.toString());


        return writer.toString();

    }

    public String getContentType(String callback) {
        if(StringUtils.isBlank(callback)){
            return CONTENT_TYPE;
        }
        else{
            return CONTENT_TYPE_CALLBACK;
        }
    }


}