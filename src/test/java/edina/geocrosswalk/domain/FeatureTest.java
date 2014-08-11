package edina.geocrosswalk.domain;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link Feature}.
 * 
 * @author Joe Vernon
 * 
 */
public class FeatureTest {
	
	public Feature feature1;
	public Feature feature2;
	public Integer feature3;
	
	@Before
	public void onSetUp() {
		feature1 = new Feature();
	    feature1.setIdentifier(9702061);
	    feature1.setSourceIdentifier(884151);
	    feature1.setName("Edinburgh");
	    feature1.setNameOptimised("edinburgh");
	    feature1.setFeatureType("cities");
	    feature1.setXmax(332656.125654954);
	    feature1.setXmin(322320.335881673);
	    feature1.setYmax(678668.179037631);
	    feature1.setYmin(668332.1774546);
	    feature1.setGazetteer("OS 1:50 000 Scale Gazetteer");
	    feature1.setCountry("United Kingdom");
	    feature1.setCustodian("Ordnance Survey");
	    feature1.setAlternativeNames("Edinbur' & Somewhere");
	    feature1.setScale("1:50 000");
	    feature1.setXCentroid(327494.166321762);
	    feature1.setYCentroid(673498.238443492);
	    feature1.setArea(0.0000);
	    feature1.setPerimeter(0.0000);
	    feature1.setElevation(0.0000);
	    feature1.setPopulation(BigDecimal.ZERO);
	    feature1.setCountryCode("GB");
	    feature1.setAdminLevel1("Scotland Euro Region");
	    feature1.setAdminLevel2("City of Edinburgh");
	    feature1.setAdminLevel3("");
	    feature1.setAdminLevel4("");
	    List<Integer> myAlternativeIds = new ArrayList<Integer>();
	    myAlternativeIds.add(12345);
	    myAlternativeIds.add(67890);
	    feature1.setAlternativeIds(myAlternativeIds);
	    
		feature2 = new Feature();
	    feature2.setIdentifier(9702061);
	    feature2.setName("Edinburgh");
	    feature2.setNameOptimised("edinburgh");
	    feature2.setXmax(332656.125654954);
	    feature2.setXmin(322320.335881673);
	    feature2.setYmax(678668.179037631);
	    feature2.setYmin(668332.1774546);
	    feature2.setGazetteer("OS 1:50 000 Scale Gazetteer");
	    feature2.setFeatureType("cities");
	}

	@Test
	public void testEscaping() {
		assertTrue(StringUtils.contains(feature1.getAlternativeNames(), "Edinbur' & Somewhere"));
		assertFalse(StringUtils.contains(feature1.getAlternativeNamesEscapedXML(), "Edinbur' & Somewhere"));
		assertTrue(StringUtils.contains(feature1.getAlternativeNamesEscapedXML(), "Edinbur&apos; &amp; Somewhere"));
		assertFalse(StringUtils.contains(feature1.getAlternativeNamesEscapedJSON(), "Edinbur' & Somewhere"));
		assertTrue(StringUtils.contains(feature1.getAlternativeNamesEscapedJSON(), "Edinbur\\' & Somewhere"));
	}

	@Test
	public void testNulls() {
		assertNotNull(feature1.getIdentifier());
		assertNotNull(feature1.getSourceIdentifier());
		assertNotNull(feature1.getName());
		assertNotNull(feature1.getNameEscapedXML());
		assertNotNull(feature1.getNameEscapedJSON());
		assertNotNull(feature1.getNameOptimised());
		assertNotNull(feature1.getFeatureType());
		assertNotNull(feature1.getXmax());
		assertNotNull(feature1.getXmin());
		assertNotNull(feature1.getYmax());
		assertNotNull(feature1.getYmin());
		assertNotNull(feature1.getGazetteer());
		assertNotNull(feature1.getCountry());
		assertNotNull(feature1.getCountryCode());
		assertNotNull(feature1.getCustodian());
		assertNotNull(feature1.getAlternativeIds());
		assertNotNull(feature1.getAlternativeNames());
		assertNotNull(feature1.getAlternativeNamesEscapedXML());
		assertNotNull(feature1.getAlternativeNamesEscapedJSON());
		assertNotNull(feature1.getScale());
		assertNotNull(feature1.getXCentroid());
		assertNotNull(feature1.getYCentroid());
		assertNotNull(feature1.getArea());
		assertNotNull(feature1.getPerimeter());
		assertNotNull(feature1.getElevation());
		assertNotNull(feature1.getPopulation());
		assertNotNull(feature1.getAdminLevel1());
		assertNotNull(feature1.getAdminLevel2());
		assertNotNull(feature1.getAdminLevel3());
		assertNotNull(feature1.getAdminLevel4());
		assertNotNull(feature1.getAdminLevel1EscapedJSON());
		assertNotNull(feature1.getAdminLevel2EscapedJSON());
		assertNotNull(feature1.getAdminLevel3EscapedJSON());
		assertNotNull(feature1.getAdminLevel4EscapedJSON());
		assertNotNull(feature1.getAdminLevel1EscapedXML());
		assertNotNull(feature1.getAdminLevel2EscapedXML());
		assertNotNull(feature1.getAdminLevel3EscapedXML());
		assertNotNull(feature1.getAdminLevel4EscapedXML());
		assertNotNull(feature1.getAlternativeIds());
		assertNull(feature1.getUricdda());
		assertNull(feature1.getUriins());
		assertNull(feature1.getVariantid());
	}
	
	@Test
	public void testEquals(){
		assertTrue(feature1.equals(feature1));
		assertFalse(feature1.equals(feature2));
		assertFalse(feature1.equals(feature3));
	}
	
	@Test
	public void testToString(){
		assertNotNull(feature1.toString());
	}
	@Test
	public void testToHashCode(){
		assertNotNull(feature1.hashCode());
	}
}