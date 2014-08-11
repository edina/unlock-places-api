package edina.geocrosswalk.web.ws;


/**
 * Command object for <code>DistanceController</code>.
 * 
 * 
 */
public class BufferedSearchCommand extends AbstractGXWCommand {
	
	private Integer distance;
	private String identifier;
	private String featureType;
	private String gazetteer;
	
    public Integer getDistance() {
        return distance;
    }
    public void setDistance(Integer distance) {
        this.distance = distance;
    }
    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public String getFeatureType() {
        return featureType;
    }
    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }
    public String getGazetteer() {
        return gazetteer;
    }
    public void setGazetteer(String gazetteer) {
        this.gazetteer = gazetteer;
    }
	
	

}