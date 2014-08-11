package edina.geocrosswalk.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;


/**
 * Default implementation of <code>IFeature</code>.
 * 
 * @author Brian O'Hare
 * @author Joe Vernon
 *
 */
public class Feature implements IFeature, Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -5789318039836327743L;
	private Integer rowCount;
    private Integer identifier;
    private Integer sourceIdentifier;
    private String name;
    private String nameOptimised;
    private String featureType;
    private Double xmax;
    private Double xmin;
    private Double ymax;
    private Double ymin;
    private String gazetteer;
    private String country;
    private String custodian;
   
    // Extended feature data variables
    private String alternativeNames;
    private String scale;
    private Double xcentroid;
    private Double ycentroid;
    private Double area;
    private Double perimeter;
    private Double elevation;
    private BigDecimal population; 
    private String countryCode;
    private String adminLevel1;
    private String adminLevel2;
    private String adminLevel3;
    private String adminLevel4;
    private String uriins;
    private String uricdda;
    private String madsid;
    private String variantid;
    private String attestations;
    private String locations;
    private String unlockfpsrc; 
    
    private List<Integer> alternativeIds;
    private String edinaFeatureCode;
    
    /**
	 * @param rowCount the rowCount to set
	 */
	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	/**
	 * @return the rowCount
	 */
	public Integer getRowCount() {
		return rowCount;
	}

	/**
     * @return the identifier
     */
    public Integer getIdentifier() {
        return identifier;
    }
    
    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }
    
    /**
	 * @param sourceIdentifier the sourceIdentifier to set
	 */
	public void setSourceIdentifier(Integer sourceIdentifier) {
		this.sourceIdentifier = sourceIdentifier;
	}

	/**
	 * @return the sourceIdentifier
	 */
	public Integer getSourceIdentifier() {
		return sourceIdentifier;
	}

	/**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return the name (escaped for XML)
     */
    public String getNameEscapedXML(){
    	return StringEscapeUtils.escapeXml(name);
    }

    /**
     * @return the name (escaped for JavaScript)
     */
    public String getNameEscapedJSON() {
    	return StringEscapeUtils.escapeJavaScript(name);
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the nameOptimised
     */
    public String getNameOptimised() {
        return nameOptimised;
    }

    /**
     * @param nameOptimised the nameOptimised to set
     */
    public void setNameOptimised(String nameOptimised) {
        this.nameOptimised = nameOptimised;
    }

    /**
     * @return the featureType
     */
    public String getFeatureType() {
        return featureType;
    }
    
    /**
     * @param featureType the featureType to set
     */
    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }
    
    /**
     * @param fCodeEdina the edina feature code
     */
    public void setEdinaFeatureCode(String fCodeEdina) {
        this.edinaFeatureCode = fCodeEdina; 
    }
    
    /**
     * @return the edinaFeatureType
     */
    public String getEdinaFeatureCode() {
        return this.edinaFeatureCode;
    }
    
    /**
     * @return the xmax
     */
    public Double getXmax() {
        return xmax;
    }

    /**
     * @param xmax the xmax to set
     */
    public void setXmax(Double xmax) {
        this.xmax = xmax;
    }

    /**
     * @return the xmin
     */
    public Double getXmin() {
        return xmin;
    }

    /**
     * @param xmin the xmin to set
     */
    public void setXmin(Double xmin) {
        this.xmin = xmin;
    }

    /**
     * @return the ymax
     */
    public Double getYmax() {
        return ymax;
    }

    /**
     * @param ymax the ymax to set
     */
    public void setYmax(Double ymax) {
        this.ymax = ymax;
    }

    /**
     * @return the ymin
     */
    public Double getYmin() {
        return ymin;
    }

    /**
     * @param ymin the ymin to set
     */
    public void setYmin(Double ymin) {
        this.ymin = ymin;
    }

    /**
     * @return the gazetteer
     */
    public String getGazetteer() {
        return gazetteer;
    }

    /**
     * @param gazetteer the gazetteer to set
     */
    public void setGazetteer(String gazetteer) {
        this.gazetteer = gazetteer;
    }
    
    /**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param custodian the custodian to set
	 */
	public void setCustodian(String custodian) {
		this.custodian = custodian;
	}

	/**
	 * @return the custodian
	 */
	public String getCustodian() {
		return custodian;
	}

	public Double getCentroidX() {
		return (xmin + ((xmax - xmin)/2));
	}

	public Double getCentroidY() {
		return (ymin + ((ymax - ymin)/2));
	}
	
	/**
	 * @param alternativeNames the alternativeNames to set
	 */
	public void setAlternativeNames(String alternativeNames) {
		this.alternativeNames = alternativeNames;
	}

	/**
	 * @return the alternativeNames
	 */
	public String getAlternativeNames() {
		return alternativeNames;
	}
	
	/**
	 * @return the alternativeNamesEscaped
	 */
	public String getAlternativeNamesEscapedXML() {
		return StringEscapeUtils.escapeXml(alternativeNames);
	}
	
	/**
	 * @return the alternativeNamesEscaped
	 */
	public String getAlternativeNamesEscapedJSON() {
		return StringEscapeUtils.escapeJavaScript(alternativeNames);
	}
	
	/**
	 * @return the alternativeNamesEscaped
	 */
	public String getAlternativeNamesEscapedHTML() {
		return StringEscapeUtils.escapeHtml(alternativeNames).replace(",", ", ");
	}
	
	/**
	 * @param scale the scale to set
	 */
	public void setScale(String scale) {
		this.scale = scale;
	}

	/**
	 * @return the scale
	 */
	public String getScale() {
		return scale;
	}

	/**
	 * @param xcentroid the xcentroid to set
	 */
	public void setXCentroid(Double xcentroid) {
		this.xcentroid = xcentroid;
	}

	/**
	 * @return the xcentroid
	 */
	public Double getXCentroid() {
		return xcentroid;
	}

	/**
	 * @param ycentroid the ycentroid to set
	 */
	public void setYCentroid(Double ycentroid) {
		this.ycentroid = ycentroid;
	}

	/**
	 * @return the ycentroid
	 */
	public Double getYCentroid() {
		return ycentroid;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(Double area) {
		this.area = area;
	}

	/**
	 * @return the area
	 */
	public Double getArea() {
		return area;
	}

	/**
	 * @param perimeter the perimeter to set
	 */
	public void setPerimeter(Double perimeter) {
		this.perimeter = perimeter;
	}

	/**
	 * @return the perimeter
	 */
	public Double getPerimeter() {
		return perimeter;
	}

	/**
	 * @param elevation the elevation to set
	 */
	public void setElevation(Double elevation) {
		this.elevation = elevation;
	}

	/**
	 * @return the elevation
	 */
	public Double getElevation() {
		return elevation;
	}

	/**
	 * @param population the population to set
	 */
	public void setPopulation(BigDecimal population) {
		this.population = population;
	}

	/**
	 * @return the population
	 */
	public BigDecimal getPopulation() {
		return population;
	}

	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * @param adminLevel1 the adminLevel1 to set
	 */
	public void setAdminLevel1(String adminLevel1) {
		this.adminLevel1 = adminLevel1;
	}

	/**
	 * @return the adminLevel1
	 */
	public String getAdminLevel1() {
		return adminLevel1;
	}

	/**
	 * @return the adminLevel1 escaped for XML
	 */
	public String getAdminLevel1EscapedXML() {
		return StringEscapeUtils.escapeXml(adminLevel1);
	}
	
	/**
	 * @return the adminLevel1 escaped for Javascript
	 */
	public String getAdminLevel1EscapedJSON() {
		return StringEscapeUtils.escapeJavaScript(adminLevel1);
	}
	
	/**
	 * @return the adminLevel1 escaped for HTML
	 */
	public String getAdminLevel1EscapedHTML() {
		return StringEscapeUtils.escapeHtml(adminLevel1);
	}
	
	/**
	 * @param adminLevel2 the adminLevel2 to set
	 */
	public void setAdminLevel2(String adminLevel2) {
		this.adminLevel2 = adminLevel2;
	}

	/**
	 * @return the adminLevel2
	 */
	public String getAdminLevel2() {
		return adminLevel2;
	}

	/**
	 * @return the adminLevel2 escaped for XML
	 */
	public String getAdminLevel2EscapedXML() {
		return StringEscapeUtils.escapeXml(adminLevel2);
	}
	
	/**
	 * @return the adminLevel2 escaped for Javascript
	 */
	public String getAdminLevel2EscapedJSON() {
		return StringEscapeUtils.escapeJavaScript(adminLevel2);
	}
	
	/**
	 * @return the adminLevel2 escaped for HTML
	 */
	public String getAdminLevel2EscapedHTML() {
		return StringEscapeUtils.escapeHtml(adminLevel2);
	}
	
	/**
	 * @param adminLevel3 the adminLevel3 to set
	 */
	public void setAdminLevel3(String adminLevel3) {
		this.adminLevel3 = adminLevel3;
	}

	/**
	 * @return the adminLevel3
	 */
	public String getAdminLevel3() {
		return adminLevel3;
	}

	/**
	 * @return the adminLevel3 escaped for XML
	 */
	public String getAdminLevel3EscapedXML() {
		return StringEscapeUtils.escapeXml(adminLevel3);
	}
	
	/**
	 * @return the adminLevel1 escaped for Javascript
	 */
	public String getAdminLevel3EscapedJSON() {
		return StringEscapeUtils.escapeJavaScript(adminLevel3);
	}
	
	/**
	 * @param adminLevel4 the adminLevel4 to set
	 */
	public void setAdminLevel4(String adminLevel4) {
		this.adminLevel4 = adminLevel4;
	}

	/**
	 * @return the adminLevel4
	 */
	public String getAdminLevel4() {
		return adminLevel4;
	}

	/**
	 * @return the adminLevel4 escaped for XML
	 */
	public String getAdminLevel4EscapedXML() {
		return StringEscapeUtils.escapeXml(adminLevel4);
	}
	
	/**
	 * @return the adminLevel4 escaped for Javascript
	 */
	public String getAdminLevel4EscapedJSON() {
		return StringEscapeUtils.escapeJavaScript(adminLevel4);
	}
	/**
	 * @param alternativeIds the alternativeIds to set
	 */
	public void setAlternativeIds(List<Integer> alternativeIds) {
		this.alternativeIds = alternativeIds;
	}

	/**
	 * @return the alternativeIds
	 */
	public List<Integer> getAlternativeIds() {
		return alternativeIds;
	}

	public String getUriins() {
		return uriins;
	}

	public void setUriins(String uriins) {
		this.uriins = uriins;
	}

	public String getUricdda() {
		return uricdda;
	}

	public void setUricdda(String uricdda) {
		this.uricdda = uricdda;
	}
	
	public String getMadsid() {
		return madsid;
	}

	public void setMadsid(String madsid) {
		this.madsid = madsid;
	}
	
	public String getVariantid() {
		return variantid;
	} 

	public void setVariantid(String variantid) {
		this.variantid = variantid;
	}
	
	public String getAttestations() {
		return attestations;
	}
	
	public String getAttestationsEscapedJavascript() {
		return StringEscapeUtils.escapeJavaScript(attestations);
	}
	
	public String getAttestationsSingleQuotes() {
		return attestations.replace("\"", "'");
	}
	
	public void setAttestations(String attestations) {
		this.attestations = attestations;
	}

	public String getLocations() {
		return locations;
	}
	
	public String getLocationsEscapedJavascript() {
		return StringEscapeUtils.escapeJavaScript(locations);
	}
	
	public String getLocationsSingleQuotes() {
		return locations.replace("\"", "'");
	}
	
	public void setLocations(String locations) {
		this.locations = locations;
	}
	
	public String getUnlockfpsrc() {
		return unlockfpsrc;
	}

	public void setUnlockfpsrc(String unlockfpsrc) {
		this.unlockfpsrc = unlockfpsrc;
	}
	
	@Override
	public String toString() {
		return "Feature [rowCount=" + rowCount + ", identifier=" + identifier + ", sourceIdentifier=" + sourceIdentifier + ", name=" + name
				+ ", nameOptimised=" + nameOptimised + ", featureType=" + featureType + ", xmax=" + xmax + ", xmin=" + xmin + ", ymax=" + ymax
				+ ", ymin=" + ymin + ", gazetteer=" + gazetteer + ", country=" + country + ", custodian=" + custodian + ", alternativeNames="
				+ alternativeNames + ", scale=" + scale + ", xcentroid=" + xcentroid + ", ycentroid=" + ycentroid + ", area=" + area + ", perimeter="
				+ perimeter + ", elevation=" + elevation + ", population=" + population + ", countryCode=" + countryCode + ", adminLevel1="
				+ adminLevel1 + ", adminLevel2=" + adminLevel2 + ", adminLevel3=" + adminLevel3 + ", adminLevel4=" + adminLevel4 + ", uriins="
				+ uriins + ", uricdda=" + uricdda + ", madsid=" + madsid + ", variantid=" + variantid + ", attestations=" + attestations
				+ ", locations=" + locations + ", unlockfpsrc=" + unlockfpsrc + ", alternativeIds=" + alternativeIds + ", edinaFeatureCode="
				+ edinaFeatureCode + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adminLevel1 == null) ? 0 : adminLevel1.hashCode());
		result = prime * result + ((adminLevel2 == null) ? 0 : adminLevel2.hashCode());
		result = prime * result + ((adminLevel3 == null) ? 0 : adminLevel3.hashCode());
		result = prime * result + ((adminLevel4 == null) ? 0 : adminLevel4.hashCode());
		result = prime * result + ((alternativeIds == null) ? 0 : alternativeIds.hashCode());
		result = prime * result + ((alternativeNames == null) ? 0 : alternativeNames.hashCode());
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		result = prime * result + ((attestations == null) ? 0 : attestations.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((countryCode == null) ? 0 : countryCode.hashCode());
		result = prime * result + ((custodian == null) ? 0 : custodian.hashCode());
		result = prime * result + ((edinaFeatureCode == null) ? 0 : edinaFeatureCode.hashCode());
		result = prime * result + ((elevation == null) ? 0 : elevation.hashCode());
		result = prime * result + ((featureType == null) ? 0 : featureType.hashCode());
		result = prime * result + ((gazetteer == null) ? 0 : gazetteer.hashCode());
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((locations == null) ? 0 : locations.hashCode());
		result = prime * result + ((madsid == null) ? 0 : madsid.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nameOptimised == null) ? 0 : nameOptimised.hashCode());
		result = prime * result + ((perimeter == null) ? 0 : perimeter.hashCode());
		result = prime * result + ((population == null) ? 0 : population.hashCode());
		result = prime * result + ((rowCount == null) ? 0 : rowCount.hashCode());
		result = prime * result + ((scale == null) ? 0 : scale.hashCode());
		result = prime * result + ((sourceIdentifier == null) ? 0 : sourceIdentifier.hashCode());
		result = prime * result + ((unlockfpsrc == null) ? 0 : unlockfpsrc.hashCode());
		result = prime * result + ((uricdda == null) ? 0 : uricdda.hashCode());
		result = prime * result + ((uriins == null) ? 0 : uriins.hashCode());
		result = prime * result + ((variantid == null) ? 0 : variantid.hashCode());
		result = prime * result + ((xcentroid == null) ? 0 : xcentroid.hashCode());
		result = prime * result + ((xmax == null) ? 0 : xmax.hashCode());
		result = prime * result + ((xmin == null) ? 0 : xmin.hashCode());
		result = prime * result + ((ycentroid == null) ? 0 : ycentroid.hashCode());
		result = prime * result + ((ymax == null) ? 0 : ymax.hashCode());
		result = prime * result + ((ymin == null) ? 0 : ymin.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Feature other = (Feature) obj;
		if (adminLevel1 == null) {
			if (other.adminLevel1 != null)
				return false;
		} else if (!adminLevel1.equals(other.adminLevel1))
			return false;
		if (adminLevel2 == null) {
			if (other.adminLevel2 != null)
				return false;
		} else if (!adminLevel2.equals(other.adminLevel2))
			return false;
		if (adminLevel3 == null) {
			if (other.adminLevel3 != null)
				return false;
		} else if (!adminLevel3.equals(other.adminLevel3))
			return false;
		if (adminLevel4 == null) {
			if (other.adminLevel4 != null)
				return false;
		} else if (!adminLevel4.equals(other.adminLevel4))
			return false;
		if (alternativeIds == null) {
			if (other.alternativeIds != null)
				return false;
		} else if (!alternativeIds.equals(other.alternativeIds))
			return false;
		if (alternativeNames == null) {
			if (other.alternativeNames != null)
				return false;
		} else if (!alternativeNames.equals(other.alternativeNames))
			return false;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		if (attestations == null) {
			if (other.attestations != null)
				return false;
		} else if (!attestations.equals(other.attestations))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (countryCode == null) {
			if (other.countryCode != null)
				return false;
		} else if (!countryCode.equals(other.countryCode))
			return false;
		if (custodian == null) {
			if (other.custodian != null)
				return false;
		} else if (!custodian.equals(other.custodian))
			return false;
		if (edinaFeatureCode == null) {
			if (other.edinaFeatureCode != null)
				return false;
		} else if (!edinaFeatureCode.equals(other.edinaFeatureCode))
			return false;
		if (elevation == null) {
			if (other.elevation != null)
				return false;
		} else if (!elevation.equals(other.elevation))
			return false;
		if (featureType == null) {
			if (other.featureType != null)
				return false;
		} else if (!featureType.equals(other.featureType))
			return false;
		if (gazetteer == null) {
			if (other.gazetteer != null)
				return false;
		} else if (!gazetteer.equals(other.gazetteer))
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (locations == null) {
			if (other.locations != null)
				return false;
		} else if (!locations.equals(other.locations))
			return false;
		if (madsid == null) {
			if (other.madsid != null)
				return false;
		} else if (!madsid.equals(other.madsid))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nameOptimised == null) {
			if (other.nameOptimised != null)
				return false;
		} else if (!nameOptimised.equals(other.nameOptimised))
			return false;
		if (perimeter == null) {
			if (other.perimeter != null)
				return false;
		} else if (!perimeter.equals(other.perimeter))
			return false;
		if (population == null) {
			if (other.population != null)
				return false;
		} else if (!population.equals(other.population))
			return false;
		if (rowCount == null) {
			if (other.rowCount != null)
				return false;
		} else if (!rowCount.equals(other.rowCount))
			return false;
		if (scale == null) {
			if (other.scale != null)
				return false;
		} else if (!scale.equals(other.scale))
			return false;
		if (sourceIdentifier == null) {
			if (other.sourceIdentifier != null)
				return false;
		} else if (!sourceIdentifier.equals(other.sourceIdentifier))
			return false;
		if (unlockfpsrc == null) {
			if (other.unlockfpsrc != null)
				return false;
		} else if (!unlockfpsrc.equals(other.unlockfpsrc))
			return false;
		if (uricdda == null) {
			if (other.uricdda != null)
				return false;
		} else if (!uricdda.equals(other.uricdda))
			return false;
		if (uriins == null) {
			if (other.uriins != null)
				return false;
		} else if (!uriins.equals(other.uriins))
			return false;
		if (variantid == null) {
			if (other.variantid != null)
				return false;
		} else if (!variantid.equals(other.variantid))
			return false;
		if (xcentroid == null) {
			if (other.xcentroid != null)
				return false;
		} else if (!xcentroid.equals(other.xcentroid))
			return false;
		if (xmax == null) {
			if (other.xmax != null)
				return false;
		} else if (!xmax.equals(other.xmax))
			return false;
		if (xmin == null) {
			if (other.xmin != null)
				return false;
		} else if (!xmin.equals(other.xmin))
			return false;
		if (ycentroid == null) {
			if (other.ycentroid != null)
				return false;
		} else if (!ycentroid.equals(other.ycentroid))
			return false;
		if (ymax == null) {
			if (other.ymax != null)
				return false;
		} else if (!ymax.equals(other.ymax))
			return false;
		if (ymin == null) {
			if (other.ymin != null)
				return false;
		} else if (!ymin.equals(other.ymin))
			return false;
		return true;
	}
    










 
}
