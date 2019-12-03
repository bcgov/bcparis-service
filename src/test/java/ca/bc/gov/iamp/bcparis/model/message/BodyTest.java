package ca.bc.gov.iamp.bcparis.model.message;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ca.bc.gov.iamp.bcparis.model.message.body.Body;
import test.util.BCPARISTestUtil;

public class BodyTest {

	@Test
	public void extract_snme_success() {

		final Body body = BCPARISTestUtil.getMessageDriverSNME().getEnvelope().getBody();
		final List<String> snme = body.getSNME();
		
		Assert.assertEquals("SNME:NEWMAN", snme.get(0));
		Assert.assertEquals("G1:OLDSON", snme.get(1));
		Assert.assertEquals("G2:MIKE", snme.get(2));
		Assert.assertEquals("DOB:19900214", snme.get(3));
	}
	
	@Test
	public void contain_attributes() {
		final Body body = BCPARISTestUtil.getMessageDriverSNME().getEnvelope().getBody();
		
		Assert.assertEquals(true, body.containAttribute("FROM"));
		Assert.assertEquals(true, body.containAttribute("TO"));
		Assert.assertEquals(true, body.containAttribute("SNME"));
		Assert.assertEquals(false, body.containAttribute("VIN"));
	}
	
	@Test
	public void get_CDATA_attributes_success() {
		final Body body = BCPARISTestUtil.getMessageDriverSNME().getEnvelope().getBody();
		List<String> attributes = body.getCDATAAttributes();
		Assert.assertEquals(13, attributes.size());
	}
	
	@Test
	public void cut_from_CDATA_success() {
		final String expected = "TEXT:RE: 0509\\nHC BC40940\\nBC41027\\nSNME:NEWMAN/G1:OLDSON/G2:MIKE/DOB:19900214\\n\\n2019051520054820190515200548";
		
		final Body body = BCPARISTestUtil.getMessageDriverSNME().getEnvelope().getBody();
		final String text = body.cutFromCDATA("TEXT:");
		
		Assert.assertEquals(expected, text);
	}

}
