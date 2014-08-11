package edina.geocrosswalk.service.plugins;

import java.util.List;

import org.apache.velocity.app.VelocityEngine;

import edina.geocrosswalk.domain.IFeature;
import edina.geocrosswalk.domain.IFootprint;
import edina.geocrosswalk.service.IResult;

/**
 * Abstract base class for Unlock format provider plugins.
 * 
 * @author Brian O'Hare
 * 
 */
public abstract class AbstractFormatProviderPlugin implements IFormatProviderPlugin {

	protected VelocityEngine velocityEngine;
	protected String featureFormatTemplate;
	protected String footprintFormatTemplate;
	protected String featureTypesFormatTemplate;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.service.plugins.IFormatProviderPlugin#getContentType()
	 */
	public String getContentType() {
		// to be overridden
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.service.plugins.IFormatProviderPlugin#getDescription()
	 */
	public String getDescription() {
		// to be overridden
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.service.plugins.IFormatProviderPlugin#getDisplayName()
	 */
	public String getDisplayName() {
		// to be overridden
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edina.geocrosswalk.service.plugins.IFormatProviderPlugin#getName()
	 */
	public String getName() {
		// to be overridden
		return null;
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
	 * edina.geocrosswalk.service.plugins.IFormatProviderPlugin#getXmlForFeatures
	 * (java.util.List)
	 */
	public String getXmlForFeatures(List<IFeature> features) throws Exception {
		// to be overridden
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edina.geocrosswalk.service.plugins.IFormatProviderPlugin#getXmlForFootprint
	 * (edina.geocrosswalk.domain.IFootprint)
	 */
	public String getXmlForFootprint(List<IFootprint> footprints) throws Exception {
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
	 * @seeedina.geocrosswalk.service.plugins.IFormatProviderPlugin#
	 * getFeatureFormatTemplate()
	 */
	public String getFeatureFormatTemplate() {
		return featureFormatTemplate;
	}

	/**
	 * @param featureFormatTemplate the featureFormatTemplate to set
	 */
	public void setFeatureFormatTemplate(String featureFormatTemplate) {
		this.featureFormatTemplate = featureFormatTemplate;
	}

	/**
	 * @param footprintFormatTemplate the footprintFormatTemplate to set
	 */
	public void setFootprintFormatTemplate(String footprintFormatTemplate) {
		this.footprintFormatTemplate = footprintFormatTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeedina.geocrosswalk.service.plugins.IFormatProviderPlugin#
	 * getFootprintFormatTemplate()
	 */
	public String getFootprintFormatTemplate() {
		return footprintFormatTemplate;
	}

	/**
	 * @param featureTypesFormatTemplate the featureTypesFormatTemplate to set
	 */
	public void setFeatureTypesFormatTemplate(String featureTypesFormatTemplate) {
		this.featureTypesFormatTemplate = featureTypesFormatTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeedina.geocrosswalk.service.plugins.IFormatProviderPlugin#
	 * getFootprintFormatTemplate()
	 */
	public String getFeatureTypesFormatTemplate() {
		return featureTypesFormatTemplate;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edina.geocrosswalk.service.plugins.IFormatProviderPlugin#getOutputForAutoComplete(java.util.List)
	 */
	public String getOutputForAutoComplete(List<String> names, String callback){
	    return null;
	}
	
	
	/* (non-Javadoc)
	 * @see edina.geocrosswalk.service.plugins.IFormatProviderPlugin#getOutputForDeepAttestations(edina.geocrosswalk.service.IResult, java.lang.String)
	 */
	public String getOutputForDeepAttestation(IResult result, String callback){
	    return null;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object that) {
		if (this == that)
			return true;
		if (!(that instanceof IFormatProviderPlugin))
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