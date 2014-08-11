package edina.geocrosswalk.domain;

import java.math.BigDecimal;


/**
 * Defines an Unlock feature.
 * 
 * @author Brian O'Hare
 *
 */
public interface IFeature {
    
	public Integer getRowCount();      

    public Integer getIdentifier();
    
    public Integer getSourceIdentifier();
    
    public String getFeatureType();
    
    public String getEdinaFeatureCode();
    
    public String getGazetteer();

    public String getName();
    
    public String getNameEscapedXML();
    
    public String getNameOptimised();
    
    public Double getXmax();
    
    public Double getXmin();
    
    public Double getYmax();
    
    public Double getYmin();
    
    public String getCountry();
    
    public String getCustodian();  
    
    public Double getCentroidX();
    
    public Double getCentroidY();
    
    public String getAlternativeNames();
    
    public String getScale();
    
    public Double getXCentroid();
    
    public Double getYCentroid();
 
    public Double getArea();
    
    public Double getPerimeter();
    
    public Double getElevation();
    
    public BigDecimal getPopulation();
    
    public String getCountryCode();
    
    public String getAdminLevel1();
    
    public String getAdminLevel2();
    
    public String getAdminLevel3();
    
    public String getUriins();
    
    public String getUricdda();
    
    public String getMadsid();
    
    public String getVariantid();
    
    public String getAttestations();
    
    public String getLocations();
    
    public String getUnlockfpsrc();
    
    public String getAdminLevel4();
}