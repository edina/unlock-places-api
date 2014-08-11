package edina.geocrosswalk.web.ws;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

/**
 * Custom view for rendering application errors.
 * 
 * @author Brian O'Hare
 * 
 */
public class GXWExceptionView extends AbstractView {

	private static final String CONTENT_TYPE = "text/xml;charset=utf-8";
	private static final String STATUS = "status";
	private static final String ERROR = "error";


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel
	 * (java.util.Map, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.setContentType(CONTENT_TYPE);
		Integer status = (Integer) model.get(STATUS);
		response.setStatus(status);
		String output = (String) model.get(ERROR);
		response.getWriter().write(output);
		response.flushBuffer();
	}

}
