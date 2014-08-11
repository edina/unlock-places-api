package edina.geocrosswalk.domain;

import org.postgis.PGgeometry;

/**
 * Defines a geocrosswalk feature's footprint.
 * 
 * @author Joe Vernon
 * 
 */

public interface IFootprint {

	public Integer getIdentifier();

	public PGgeometry getGeometry();

	public String getCustodian();
}
