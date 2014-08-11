package edina.geocrosswalk.web.ws;

import java.beans.PropertyEditorSupport;

import edina.geocrosswalk.service.plugins.GXWFormat;

/**
 * Editor to handle conversion of format request parameters to
 * <code>GXWFormat</code> enumeration.
 * 
 * @author Brian O'Hare
 * 
 */
public class FormatParamEditor extends PropertyEditorSupport {
	
	private static final String DEFAULT_FORMAT = "GXW";

	public void setAsText(String param) {
		GXWFormat format = null;
		if (param != null) {
			param = param.toUpperCase();	
		}
		else {
			param = DEFAULT_FORMAT;
		}
		try {
			format = Enum.valueOf(GXWFormat.class, param);
		}
		catch (IllegalArgumentException ex) {
			format = GXWFormat.GXW;
		}
		setValue(format);
	}
}
