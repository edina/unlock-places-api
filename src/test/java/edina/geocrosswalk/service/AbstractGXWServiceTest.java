package edina.geocrosswalk.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import edina.geocrosswalk.domain.Feature;
import edina.geocrosswalk.domain.IFeature;

/**
 * Abstract base class for GXW service layer test cases.
 * 
 * @author Brian O'Hare
 * 
 */
@ContextConfiguration(locations = { "classpath:spring/testServiceContext.xml" })
public abstract class AbstractGXWServiceTest extends AbstractJUnit4SpringContextTests {
	protected static final String DEEP_EPNS_URL = "http://epns.ac.uk/deep/England/Cheshire/Macclesfield%20Hundred/Macclesfield%20Chapelry/Wildboarclough/Edinburgh";
	protected static final String DEEP_PLACENAME_URL = "http://placenames.org.uk/id/placename/44/006228";
	protected static final String MADSID = "epns-deep-44-d-mappedname-001314";
	protected static final String VARIANTID = "variantID";
	protected static final String ATTESTATIONS = "attestations";
	protected static final String LOCATIONS = "<locations><geo easting=\"398500\" kepnref=\"11718\" lat=\"53.2136\" long=\"-2.0239\" northing=\"368500\""+
	" source=\"kepn\"/><geo gazref=\"unlock:11322890\" lat=\"53.21327605975551\" long=\"-2.022464253999055\" source=\"unlock\"/><geo lat=\"53.20877832\"" +
	" long=\"-2.029949194\" source=\"epns\"/></locations>";
	protected static final String UNLCKFPSRC = "containedBy:kepn";

	String attestation = "<?xml version='1.0' encoding='UTF-8'?><attestations><attestation variantID='epns-deep-02-b-name-w665'>"
			+ "<date begin='1242' end='1242' subtype='simple'>1242</date><source id='bu37' style=''>Fees</source><item>873</item></attestation></attestations>";
	protected Document deepDocument;
	protected DefaultResult deepResult;

	protected void onSetUp() {
		String attestation = "<?xml version='1.0' encoding='UTF-8'?><attestations><attestation variantID='epns-deep-02-b-name-w665'>"
				+ "<date begin='1242' end='1242' subtype='simple'>1242</date><source id='bu37' style=''>Fees</source><item>873</item></attestation></attestations>";

		try {
			deepDocument = new SAXBuilder().build(new StringReader(attestation));
		} catch (JDOMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		deepResult = new DefaultResult();
		deepResult.setDeepAttestations(deepDocument);
		Feature deepFeature = new Feature();
		deepFeature.setUricdda(DEEP_PLACENAME_URL);
		deepFeature.setUriins(DEEP_EPNS_URL);
		deepFeature.setMadsid(MADSID);
		deepFeature.setVariantid(VARIANTID);
		deepFeature.setAttestations(ATTESTATIONS);
		deepFeature.setLocations(LOCATIONS);
		deepFeature.setUnlockfpsrc(UNLCKFPSRC);
		deepFeature.setName("Edinburgh");
		deepFeature.setGazetteer("deep");
		deepFeature.setXmin(new Double("-3.35081720352173"));
		deepFeature.setXmax(new Double("-3.01274991035461"));
		deepFeature.setYmin(new Double("55.8727264404297"));
		deepFeature.setYmax(new Double("55.9917373657227"));
		deepFeature.setIdentifier(9656);

		Feature deepFeature2 = new Feature();
		deepFeature2.setCustodian("Edina");
		deepFeature2.setIdentifier(9656);
		deepFeature2.setNameOptimised("edinburgh");
		deepFeature2.setName("Edinburgh");
		deepFeature2.setGazetteer("deep");
		deepFeature2.setFeatureType("cities");
		deepFeature2.setXmin(new Double("-3.35081720352173"));
		deepFeature2.setXmax(new Double("-3.01274991035461"));
		deepFeature2.setYmin(new Double("55.8727264404297"));
		deepFeature2.setYmax(new Double("55.9917373657227"));
		deepFeature2.setUricdda(DEEP_PLACENAME_URL);
		deepFeature2.setUriins(DEEP_EPNS_URL);
		deepFeature2.setMadsid(MADSID);
		deepFeature2.setVariantid(VARIANTID);
		deepFeature2.setAttestations(ATTESTATIONS);
		deepFeature2.setLocations(LOCATIONS);
		deepFeature2.setUnlockfpsrc(UNLCKFPSRC);

		
		

		LinkedList<IFeature> f = new LinkedList<IFeature>();
		f.add(deepFeature);
		f.add(deepFeature2);
		deepResult.setFeatures(f);
		deepResult.setTotalResultsCount(2);
	}
}
