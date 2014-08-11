package edina.geocrosswalk.web.ws;

import java.io.OutputStream;
import java.io.StringReader;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.web.servlet.view.AbstractView;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Custom view for Unlock Web Services.
 * 
 * @author Brian O'Hare
 * @author Joe Vernon
 * 
 */
public class GXWView extends AbstractView {

	private static final String RESULT_DOCUMENT = "resultDocument";
	private static final String FORMAT = "format";
	private static final String KML_RESULT_NAME = "unlock_search_result.kml";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel
	 * (java.util.Map, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String output = (String) model.get(RESULT_DOCUMENT);
		int status = HttpServletResponse.SC_OK;
		GXWFormat format = (GXWFormat) model.get(FORMAT);
		response.setStatus(status);
		response.setContentType(getContentType());
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Vary", "User-Agent,Accept-Encoding");		
		
		if (format.isXML()) {
			if (format.isKML()) {
				response.setHeader("Content-Disposition", new StringBuffer("attachment; filename=").append(KML_RESULT_NAME).toString()); 
			}
			OutputStream out = response.getOutputStream();
			InputSource source = new InputSource(new StringReader(output));
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			// This is important - we found that the georss results were not displaying the atom namespace unless this was set to true.
			dbFactory.setNamespaceAware(true);

			Document doc = dbFactory.newDocumentBuilder().parse(source);
		
			TransformerFactory tfactory = TransformerFactory.newInstance();
	        Transformer serializer;
	        try {
	            serializer = tfactory.newTransformer();
	            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
	            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				serializer.transform(new DOMSource(doc), new StreamResult(out));
	        } catch (TransformerException e) {
	            e.printStackTrace();
	            throw new RuntimeException(e);
	        }
		}
		else {
			response.getWriter().write(output);
		}
        // flush the response
		response.flushBuffer();
	}
}
