package test.util;

import ca.bc.gov.iamp.bcparis.model.message.Envelope;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.model.message.body.Body;
import ca.bc.gov.iamp.bcparis.transformation.MessageTransform;

public class BCPARISTestUtil {

	public static Layer7Message getMessageDriverSNME() {		
		return getMessage("cdata/sample-driver-snme");
	}
	
	public static Layer7Message getMessageDriverDL() {
		return getMessage("cdata/sample-driver-dl");
	}
	
	public static Layer7Message getMessageDriverMultipleParams() {
		return getMessage("cdata/sample-driver-multiple-params");
	}
	
	public static Layer7Message getMessageVehicleVIN() {
		return getMessage("cdata/sample-vehicle-vin");
	}
	
	public static Layer7Message getMessageVehicleLIC() {
		return getMessage("cdata/sample-vehicle-lic");
	}
	
	public static Layer7Message getMessageVehicleRVL() {
		return getMessage("cdata/sample-vehicle-rvl");
	}
	
	public static Layer7Message getMessageVehicleRNS() {
		return getMessage("cdata/sample-vehicle-rns");
	}
	
	public static Layer7Message getMessageVehicleMultipleParams() {
		return getMessage("cdata/sample-vehicle-multiple-params");
	}
	
	public static Layer7Message getMessagePOR() {
		return getMessage("cdata/sample-por");
	}
	
	public static Layer7Message getMessageSatellite() {
		return getMessage("cdata/sample-satellite");
	}
	
	public static Layer7Message getMessageSatelliteRoundTrip() {
		return getMessage("cdata/sample-satellite-round-trip");
	}
	
	private static Layer7Message getMessage(final String cdata) {
		MessageTransform transform = new MessageTransform();
		return transform.parse(Layer7Message.builder()
				.envelope(Envelope.builder().body(getMessageBody(cdata)).build())
				.build());
	}
	
	public static Body getMessageBody(String file) {
		final String msgFFmt = TestUtil.readFile(file);
		final Body body = new Body();
		body.setMsgFFmt(msgFFmt);
		return body;
	}
	
}
