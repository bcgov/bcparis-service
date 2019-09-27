package ca.bc.gov.iamp.bcparis.model.message;

import org.junit.Assert;
import org.junit.Test;

import ca.bc.gov.iamp.bcparis.model.message.body.MQMD;
import ca.bc.gov.iamp.bcparis.model.message.header.Header;
import ca.bc.gov.iamp.bcparis.model.message.header.MsgSrvc;
import ca.bc.gov.iamp.bcparis.model.message.header.Origin;
import ca.bc.gov.iamp.bcparis.model.message.header.Routing;

public class Layer7MessageTest {

	@Test
	public void test_MQMD() {
		MQMD mqmd = MQMD.builder()
			.feedback("feedback")
			.replyToQueueManagerName("replyToQueueManagerName")
			.messageIdByte("messageIdByte")
			.messageType("messageType")
			.format("format")
			.replyToQueueName("replyToQueueName")
			.correlationIdByte("correlationIdByte").build();
		
		Assert.assertEquals("feedback", mqmd.getFeedback());
		Assert.assertEquals("replyToQueueManagerName", mqmd.getReplyToQueueManagerName());
		Assert.assertEquals("messageIdByte", mqmd.getMessageIdByte());
		Assert.assertEquals("messageType", mqmd.getMessageType());
		Assert.assertEquals("format", mqmd.getFormat());
		Assert.assertEquals("replyToQueueName", mqmd.getReplyToQueueName());
		Assert.assertEquals("correlationIdByte", mqmd.getCorrelationIdByte());
	}
	
	@Test
	public void test_Routing() {
		Routing routing = Routing.builder()
				.qname("qname")
				.qMgrName("qMgrName").build();
		
		Assert.assertEquals("qname", routing.getQname());
		Assert.assertEquals("qMgrName", routing.getQMgrName());
	}
	
	@Test
	public void test_Origin() {
		Origin origin = Origin.builder()
				.qname("qname")
				.qMgrName("qMgrName").build();
		
		Assert.assertEquals("qname", origin.getQname());
		Assert.assertEquals("qMgrName", origin.getQMgrName());
	}
	
	@Test
	public void test_MsgSrvc() {
		MsgSrvc msg = MsgSrvc.builder()
				.msgActn("msgActn")
				.build();
		
		Assert.assertEquals("msgActn", msg.getMsgActn());
	}
	
	@Test
	public void test_Header() {
		MsgSrvc msg = MsgSrvc.builder().build();
		Routing routing = Routing.builder().build();
		Origin origin = Origin.builder().build();		
		
		Header header = Header.builder()
				.cpicVer("cpicVer")
				.udf("udf")
				.priority("priority")
				.msgSrvc(msg)
				.routing(routing)
				.origin(origin)
				.role("role")
				.agencyId("agencyId")
				.userId("userId")
				.deviceId("deviceId")
				.build();
		
		Assert.assertEquals("cpicVer", header.getCpicVer());
		Assert.assertEquals("udf", header.getUdf());
		Assert.assertEquals("priority", header.getPriority());
		Assert.assertEquals(msg, header.getMsgSrvc());
		Assert.assertEquals(routing, header.getRouting());
		Assert.assertEquals(origin, header.getOrigin());
		Assert.assertEquals("role", header.getRole());
		Assert.assertEquals("agencyId", header.getAgencyId());
		Assert.assertEquals("userId", header.getUserId());
		Assert.assertEquals("deviceId", header.getDeviceId());
	}
}
