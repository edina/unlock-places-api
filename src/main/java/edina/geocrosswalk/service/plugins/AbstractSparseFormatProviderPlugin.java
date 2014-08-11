package edina.geocrosswalk.service.plugins;

import org.apache.velocity.app.VelocityEngine;

/**
 *  base class for Unlock sparse format provider plugins.
 * 
 */
public abstract class AbstractSparseFormatProviderPlugin implements ISparseFormatProviderPlugin {

	protected VelocityEngine velocityEngine;
	protected String template;


	
    public String getTemplate() {
        return template;
    }

    
    public void setTemplate(String template){
        this.template = template;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @seeedina.geocrosswalk.service.plugins.IFormatProviderPlugin#
	 * getSupportedExportFormat()
	 */
	public GXWFormat getSupportedFormat() {
		// to be overridden
		return null;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.service.plugins.IFormatProviderPlugin#supports(edina
	 * .geocrosswalk.service.plugins.GXWFormat)
	 */
	public boolean supports(GXWFormat format) {
		// to be overridden
		return false;
	}

	/**
	 * @return the velocityEngine
	 */
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	/**
	 * @param velocityEngine the velocityEngine to set
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object that) {
		if (this == that)
			return true;
		if (!(that instanceof ISparseFormatProviderPlugin))
			return false;
		ProviderPlugin thatPlugin = (ProviderPlugin) that;
		return (this.getSupportedFormat().equals(thatPlugin.getSupportedFormat()) && (this.getName().equals(thatPlugin
				.getName())));
	}
	
	


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result = 17;
		result = 37 * result + getSupportedFormat().hashCode();
		result = 37 * result + getName().hashCode();
		return result;
	}


}