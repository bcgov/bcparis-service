package ca.bc.gov.iamp.bcparis.model.message;

import org.junit.Assert;
import org.junit.Test;

import test.util.BCPARISTestUtil;

public class BodyTest {

	@Test
	public void extract_snme_success() {

		final Body body = BCPARISTestUtil.getMessageDriverSNME().getEnvelope().getBody();
		final String snme = body.getSNME();

		Assert.assertEquals("NEWMAN/G1:OLDSON/G2:MIKE/DOB:19900214", snme);
	}

}
