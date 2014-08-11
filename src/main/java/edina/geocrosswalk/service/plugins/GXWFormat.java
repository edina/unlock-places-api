package edina.geocrosswalk.service.plugins;

/**
 * Enumeration of allowed geocrosswalk formats.
 * 
 * @author Brian O'Hare
 * 
 */
public enum GXWFormat {

	GXW, KML, JSON, GEORSS, TXT, HTML;
	
	public boolean isXML() {
		if (this.equals(GXW) || this.equals(KML) || this.equals(GEORSS)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isKML() {
		return this.equals(KML);
	}
}