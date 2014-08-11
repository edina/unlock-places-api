package edina.geocrosswalk.domain;

import java.io.Serializable;

import org.postgis.PGgeometry;

/**
 * Default implementation of <code>IFootprint</code>.
 * 
 * @author Joe Vernon
 * 
 */
public class Footprint implements IFootprint, Serializable {

	private static final long serialVersionUID = -6199088074562449160L;
	
	private Integer identifier;
	private PGgeometry geometry;
	private String custodian;

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
	 * @return the geometry
	 */
	public PGgeometry getGeometry() {
		return geometry;
	}


	/**
	 * @param geometry the geometry to set
	 */
	public void setGeometry(PGgeometry geometry) {
		this.geometry = geometry;
	}


	/**
	 * @return the custodian
	 */
	public String getCustodian() {
		return custodian;
	}


	/**
	 * @param custodian the custodian to set
	 */
	public void setCustodian(String custodian) {
		this.custodian = custodian;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((custodian == null) ? 0 : custodian.hashCode());
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
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
		Footprint other = (Footprint) obj;
		if (custodian == null) {
			if (other.custodian != null)
				return false;
		} else if (!custodian.equals(other.custodian))
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}

}
