package ca.bc.gov.iamp.bcparis.service;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.StringUtils;
import test.util.BCPARISTestUtil;

public class MessageServiceTest {

	private MessageService service = new MessageService();
	
	@Test
	public void valid_escape() {
		final String notEscaped = "&\"<>";
		Assert.assertEquals("&amp;&quot;&lt;&gt;", service.escape(notEscaped) );
	}

	@Test
	public void test_build_vehicle_error_response() {
		final Layer7Message message = BCPARISTestUtil.getInvalidVehicleQuery();
		MessageService service = new MessageService();
		String result = service.buildErrorResponse(message.getEnvelope().getBody(),"Error");


		Assert.assertTrue(result.contains("SEND MT:M\n"));
		Assert.assertTrue(result.contains("FMT:Y\n"));
		Assert.assertTrue(result.contains("FROM:BC41028\n"));
		Assert.assertTrue(result.contains("TO:BC41127\n"));
		Assert.assertTrue(result.contains("TEXT:RE: 8261\nHC BC11422\nBC41028\n\n"));
		Assert.assertTrue(result.contains("Error"));
	}
	@Test
	public void test_build_error_driver_response_with() {
		final Layer7Message message = BCPARISTestUtil.getInvalidDriverQuery();
		MessageService service = new MessageService();
		String result = service.buildErrorResponse(message.getEnvelope().getBody(),"Error");


		Assert.assertEquals(1, StringUtils.countOccurrencesOf(result, "SEND MT:M\n"));
		Assert.assertEquals(1,StringUtils.countOccurrencesOf(result, "FMT:Y\n"));
		Assert.assertEquals(1,StringUtils.countOccurrencesOf(result, "FROM:BC41027\n"));
		Assert.assertEquals(1,StringUtils.countOccurrencesOf(result, "TO:BC41127\n"));
		Assert.assertEquals(1,StringUtils.countOccurrencesOf(result, "TEXT:\\n\n\n"));
		Assert.assertEquals(1,StringUtils.countOccurrencesOf(result,"Error"));
	}
}
