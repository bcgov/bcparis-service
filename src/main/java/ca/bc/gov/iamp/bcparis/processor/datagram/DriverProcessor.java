package ca.bc.gov.iamp.bcparis.processor.datagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.exception.icbc.ICBCRestException;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.model.message.body.Body;
import ca.bc.gov.iamp.bcparis.repository.ICBCRestRepository;
import ca.bc.gov.iamp.bcparis.repository.query.IMSRequest;
import ca.bc.gov.iamp.bcparis.service.MessageService;

@Service
public class DriverProcessor implements DatagramProcessor {

	private final Logger log = LoggerFactory.getLogger(DriverProcessor.class);

	@Autowired
	private ICBCRestRepository icbcRepository;

	@Autowired
	private MessageService messageService;

	public Layer7Message process(Layer7Message message) {

		log.info("===== ENTER DriverProcessor.process() =====");

		// Log transaction name and message type for routing debug
		String msgFmt = (message != null && message.getEnvelope() != null && message.getEnvelope().getBody() != null)
				? message.getEnvelope().getBody().getMsgFFmt()
				: null;
		if (msgFmt != null) {
			log.info("MsgFFmt content: {}", msgFmt);
			if (msgFmt.contains("DSSMTCPC")) {
				log.info("DSSMTCPC transaction detected in MsgFFmt. This should route to DriverProcessor.");
			} else {
				log.warn("DSSMTCPC transaction NOT detected in MsgFFmt. Actual content: {}", msgFmt);
			}
		} else {
			log.warn("MsgFFmt is null or missing in incoming message");
		}

		// Detailed logging of the incoming message
		if (message != null && message.getEnvelope() != null && message.getEnvelope().getBody() != null) {
			log.info("Incoming Body: {}", message.getEnvelope().getBody());
			log.info("Incoming MsgFFmt: {}", message.getEnvelope().getBody().getMsgFFmt());
		} else {
			log.warn("Message, Envelope, or Body is null in incoming request");
		}

		if (message == null) {
			log.error("Message is NULL in DriverProcessor");
			throw new IllegalArgumentException("Message cannot be null");
		}

		log.debug("Message object: {}", message);
		log.debug("MessageType: {}", message.getMessageType());

		if (message.getEnvelope() == null) {
			log.error("Envelope is NULL in message");
			throw new IllegalStateException("Envelope missing in message");
		}

		if (message.getEnvelope().getBody() == null) {
			log.error("Body is NULL in envelope");
			throw new IllegalStateException("Body missing in envelope");
		}

		final Body body = message.getEnvelope().getBody();

		log.debug("Body object: {}", body);

		// Log header-like CDATA attributes
		log.info("Body CDATA FROM: {}", body.getCDATAAttribute("FROM"));
		log.info("Body CDATA TO: {}", body.getCDATAAttribute("TO"));

		List<String> queryParams = messageService.getQueryAttributesList(
				body, Arrays.asList("SNME", "DL"));

		log.info("Query parameters extracted: {}", queryParams);

		List<IMSRequest> requests = createIMSContent(message);

		log.info("IMS requests generated count: {}", requests.size());

		if (requests.isEmpty()) {
			log.warn("No IMS requests generated. Returning error response.");
			body.setMsgFFmt(
					messageService.buildErrorResponse(body, "Unable to parse/formatting error"));
			return message;
		}

		try {
			List<String> responseParsed = requests.parallelStream()
					.map(request -> {
						log.info("Calling ICBC with IMS payload: {}", request.getImsRequest());
						String response = icbcRepository.requestDetails(message, request);
						log.debug("Raw ICBC response: {}", response);
						return response;
					})
					.map(response -> {
						String parsed = messageService.parseResponse(response);
						log.debug("Parsed ICBC response: {}", parsed);
						return parsed;
					})
					.collect(Collectors.toList());

			log.info("ICBC responses received: {}", responseParsed.size());

			String finalResponse = messageService.buildResponse(
					body, String.join("\n\n", responseParsed));

			log.debug("Final formatted response: {}", finalResponse);

			body.setMsgFFmt(finalResponse);

			log.info("===== EXIT DriverProcessor.process() SUCCESS =====");
			return message;

		} catch (ICBCRestException e) {
			log.error("ICBC REST exception occurred", e);
			log.error("ICBC error response content: {}", e.getResponseContent());

			String content = messageService.parseResponseError(e.getResponseContent());
			content = messageService.parseResponse(content);
			content = messageService.buildResponse(body, content);
			body.setMsgFFmt(content);

			throw e;
		}
	}

	private List<IMSRequest> createIMSContent(Layer7Message message) {

		log.debug("Entering createIMSContent()");

		final List<IMSRequest> result = new ArrayList<>();

		final String icbcPayload = "${transactionName} HC ${fromORI} ${toORI} ${qdCode} ${queryParams}";

		final String transaction = "DSSMTCPC";
		final String code = "QD";

		final Body body = message.getEnvelope().getBody();

		final String from = body.getCDATAAttribute("FROM");
		final String to = body.getCDATAAttribute("TO");

		log.info("Building IMS content with FROM={}, TO={}", from, to);

		final List<String> queryParams = messageService.getQueryAttributesList(
				body, Arrays.asList("SNME", "DL"));

		log.debug("Query params for IMS content: {}", queryParams);

		for (String query : queryParams) {
			final String imsContent = icbcPayload
					.replace("${transactionName}", transaction)
					.replace("${fromORI}", from)
					.replace("${toORI}", to)
					.replace("${qdCode}", code)
					.replace("${queryParams}", query);

			log.debug("Generated IMS payload: {}", imsContent);

			result.add(IMSRequest.builder().imsRequest(imsContent).build());
		}

		log.info("Total IMS payloads created: {}", result.size());

		return result;
	}
}
