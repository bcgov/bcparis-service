package ca.bc.gov.iamp.bcparis.model.message;

import org.junit.Assert;
import org.junit.Test;

import ca.bc.gov.iamp.bcparis.model.MessageType;

public class MessageTypeTest {

	@Test
	public void codes_correct() {
		Assert.assertEquals(MessageType.POR.getCode(), "BC41029");
		Assert.assertEquals(MessageType.REPORT.getCode(), null);
		Assert.assertEquals(MessageType.DRIVER.getCode(), "BC41027");
		Assert.assertEquals(MessageType.VEHICLE.getCode(), "BC41028");
		Assert.assertEquals(MessageType.SATELLITE.getCode(), "BC41127");
	}
	
}
