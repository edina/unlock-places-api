package edina.geocrosswalk.domain;

/**
 * Holds the coordinates of a bounding box.
 * 
 * @author Brian O'Hare
 * 
 * @version $Rev:$, $Date:$
 */
public class BoundingBox {
	
	private Double minx = 0.0;
	private Double maxx = 0.0;
	private Double miny = 0.0;
	private Double maxy = 0.0;
	
	public BoundingBox(Double minx, Double maxx, Double miny, Double maxy) {
		if (minx == null || maxx == null || miny == null || maxy == null){
			minx = -180.0;
			maxx = 180.0;
			miny = -90.0;
			maxy = 90.0;
		}
		this.minx = minx;
		this.maxx = maxx;
		this.miny = miny;
		this.maxy = maxy;
	}
	
	/**
	 * Gets the minx
	 *
	 * @return the minx
	 */
	public Double getMinx() {
		return minx;
	}
	
	/**
	 * Gets the maxx
	 *
	 * @return the maxx
	 */
	public Double getMaxx() {
		return maxx;
	}
	
	/**
	 * Gets the miny
	 *
	 * @return the miny
	 */
	public Double getMiny() {
		return miny;
	}
	
	/**
	 * Gets the maxy
	 *
	 * @return the maxy
	 */
	public Double getMaxy() {
		return maxy;
	}
	
	/**
	 * Returns whether this is a valid bounding box.
	 * 
	 * @return	<code>true</code> if valid, <code>false</code> otherwise.
	 */
	public boolean isValid() {
		return (getMaxx() >= getMinx()) && (getMaxy() >= getMiny());
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object that) {
		if (this == that) return true;
		if (!(that instanceof BoundingBox)) return false;
		BoundingBox bbox = (BoundingBox) that;
		return (this.getMaxx().equals(bbox.getMaxx())
				&& this.getMaxy().equals(bbox.getMaxy())
				&& this.getMinx().equals(bbox.getMinx())
				&& this.getMiny().equals(bbox.getMiny()));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;
		long f = Double.doubleToLongBits(getMinx());
		result = 37 * result + (int) (f ^ (f >>> 32));
		f = Double.doubleToLongBits(getMaxx());
		result = 37 * result + (int) (f ^ (f >>> 32));
		f = Double.doubleToLongBits(getMiny());
		result = 37 * result + (int) (f ^ (f >>> 32));
		f = Double.doubleToLongBits(getMaxy());
		result = 37 * result + (int) (f ^ (f >>> 32));
		return result;
	}
}