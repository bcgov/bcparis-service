package ca.bc.gov.iamp.bcparis.service;

import org.junit.Assert;
import org.junit.Test;

public class MessageServiceTest {

	private MessageService service = new MessageService();
	
	@Test
	public void valid_escape() {
		final String notEscaped = "&\"<>";
		Assert.assertEquals("&amp;&quot;&lt;&gt;", service.escape(notEscaped) );
	}
	
}
