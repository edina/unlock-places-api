package edina.geocrosswalk.web.ws.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

import edina.geocrosswalk.domain.BoundingBox;

public class SrsBboxValidator {
	
	private static final Logger logger = Logger.getLogger(SrsBboxValidator.class);
	private static final BoundingBox wgs84 = new BoundingBox(-180.0, 180.0, -90.0, 90.0);
	private static final BoundingBox bng = new BoundingBox(0.0, 700000.0, 0.0, 1300000.0);
	private static final BoundingBox google = new BoundingBox(-20037508.3427892, 20037508.3427892, -147730762.669922, 147730758.194568);
	private static final LinkedHashMap projections = new LinkedHashMap<String,BoundingBox>();
	private static final LinkedHashMap messages = new LinkedHashMap<String, String>();
	static{
		projections.put("4326", wgs84);
		projections.put("27700", bng);
		projections.put("900913", google);
	}
	
	static{
		messages.put("4326", " WGS84");
		messages.put("27700", " British National Grid");
		messages.put("900913", " the Google Merkator");
	}
	

	public boolean validate(String srs, BoundingBox bbox){
		BoundingBox projection = (BoundingBox)projections.get(srs);
		return ((validateValue(bbox.getMinx(), projection.getMinx(), projection.getMaxx())) && (validateValue(bbox.getMaxx(), projection.getMinx(), projection.getMaxx())) && (validateValue(bbox.getMiny(), projection.getMiny(), projection.getMaxy())) && (validateValue(bbox.getMaxy(), projection.getMiny(), projection.getMaxy())));
	}
	
	public boolean validateValue(Double x, Double xmin, Double xmax){
		return ((x > xmin) && (x < xmax));
	}
	
	public HashMap getValidCSystems(BoundingBox bbox){
		HashMap valids = new HashMap<String,String>();
		List<String> list = new ArrayList<String>(projections.keySet());
		for(String proj: list){
			if(validate(proj, bbox)){
				valids.put(proj, (String)messages.get(proj));
				break;
			}
		}
		return valids;
	}
}
