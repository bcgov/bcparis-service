package ca.bc.gov.iamp.bcparis.processor.datagram;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ca.bc.gov.iamp.bcparis.exception.icbc.ICBCRestException;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.model.message.body.Body;
import ca.bc.gov.iamp.bcparis.repository.ICBCRestRepository;
import ca.bc.gov.iamp.bcparis.repository.query.IMSRequest;
import ca.bc.gov.iamp.bcparis.service.MessageService;

@Service
public class VehicleProcessor implements DatagramProcessor {

	private final Logger log = LoggerFactory.getLogger(VehicleProcessor.class);

	@Autowired
	private ICBCRestRepository icbcRepository;

	@Autowired
	private MessageService messageService;

	public Layer7Message process(Layer7Message message) {

		log.info("===== ENTER VehicleProcessor.process() =====");

		if (message == null) {
			log.error("Message is NULL in VehicleProcessor");
			throw new IllegalArgumentException("Message cannot be null");
		}

		if (message.getEnvelope() == null || message.getEnvelope().getBody() == null) {
			log.error("Envelope or Body is NULL in message");
			throw new IllegalStateException("Envelope/Body missing");
		}

		final Body body = message.getEnvelope().getBody();

		List<String> requestsList = messageService.getQueryAttributesList(
				body,
				Arrays.asList("LIC", "ODN", "TAG", "FLC", "VIN", "REG", "RNS", "RVL"));

		log.info("Vehicle query parameters extracted: {}", requestsList);

		List<IMSRequest> requests = createIMSContent(message);

		log.info("IMS requests generated count: {}", requests.size());

		if (requests.isEmpty()) {
			body.setMsgFFmt(
					messageService.buildErrorResponse(body, "Unable to parse/formatting error"));
			return message;
		}

		try {
			List<String> responseParsed = requests.parallelStream()
					.map(request -> {
						log.info("Calling ICBC with IMS payload: {}", request.getImsRequest());
						return icbcRepository.requestDetails(message, request);
					})
					.map(messageService::parseResponse)
					.collect(Collectors.toList());

			String finalResponse = messageService.buildResponse(
					body, String.join("\n\n", responseParsed));

			finalResponse = finalResponse.replace("{&quot;responseString&quot;:&quot;", "").replace("&quot;}", "");
			body.setMsgFFmt(finalResponse);

			log.info("===== EXIT VehicleProcessor.process() SUCCESS =====");
			return message;

		} catch (ICBCRestException e) {
			log.error("ICBC REST exception occurred", e);

			String content = messageService.parseResponseError(e.getResponseContent());
			content = messageService.parseResponse(content);
			content = messageService.buildResponse(body, content);
			body.setMsgFFmt(content);

			throw e;
		}
	}

	private List<IMSRequest> createIMSContent(Layer7Message message) {

		final List<IMSRequest> result = new ArrayList<>();

		final String icbcPayload = "${transactionName} HC ${fromORI} ${toORI} ${queryParams}";

		final Body body = message.getEnvelope().getBody();
		final String from = body.getCDATAAttribute("FROM");
		final String to = body.getCDATAAttribute("TO");

		final List<String> queryParams = messageService.getQueryAttributesList(
				body,
				Arrays.asList("LIC", "ODN", "TAG", "FLC", "VIN", "REG", "RNS", "RVL"));

		for (String originalQuery : queryParams) {

			String query = originalQuery;

			final String transaction = getTransaction(query);

			if (query.startsWith("RNS")) {
				query = getRNSQuery(query);
			}

			if (query.endsWith("/h") || query.endsWith("/H")) {
				query = query.substring(0, query.length() - 2);
			}

			// Add date and time to all query types
			// Restores original BCPARIS-292 behavior for VIN, LIC, ODN, TAG, FLC, REG, RNS, RVL
			// Ensure query ends with a slash before adding timestamp (ICBC requires double slash)
		if (!query.endsWith("/")) {
			query += "/";
		}
		query += "/" + getLocalTimeNowICBCFormat();

			final String imsContent = icbcPayload
					.replace("${transactionName}", transaction)
					.replace("${fromORI}", from)
					.replace("${toORI}", to)
					.replace("${queryParams}", query);

			result.add(IMSRequest.builder()
					.imsRequest(imsContent)
					.build());
		}

		log.info("Total IMS payloads created: {}", result.size());
		return result;
	}

	private String getRNSQuery(final String query) {

		final String surname = extractFromQuery(query, "RNS:");
		final String g1 = extractFromQuery(query, "G1:");
		final String g2 = extractFromQuery(query, "G2:");
		final String dobRaw = extractFromQuery(query, "DOB:");
		final String dob = formatDate(dobRaw);
		final String rsvp = extractFromQuery(query, "RSVP:");

		String rnsQuery = String.format("RNS:%s/%s/%s/%s", surname, g1, g2, dob);

		if (StringUtils.hasLength(rsvp)) {
			rnsQuery += "/RSVP:" + rsvp;
		}

		return rnsQuery;
	}

	private String formatDate(String date) {
		if (StringUtils.hasLength(date)) {
			return String.format("%s-%s-%s",
					date.substring(0, 4),
					date.substring(4, 6),
					date.substring(6, 8));
		}
		return "";
	}

	private String extractFromQuery(final String query, final String attribute) {

		final int indexAttr = query.indexOf(attribute);
		if (indexAttr != -1) {
			final int indexEnd = query.indexOf("/", indexAttr);
			final int attrLength = attribute.length();
			return (indexEnd != -1)
					? query.substring(indexAttr + attrLength, indexEnd)
					: query.substring(indexAttr + attrLength);
		}

		return "";
	}

	// ✅ CHANGE #2:
	// VIN must use JISTRN2
	private String getTransaction(String query) {
		String upper = query.toUpperCase();

		if (upper.startsWith("RVL")
				|| upper.startsWith("RNS")
				|| upper.startsWith("VIN")) {
			return "JISTRN2";
		}

		return "JISTRAN";
	}

	private String getLocalTimeNowICBCFormat() {
		return LocalDateTime.now()
				.format(DateTimeFormatter.ofPattern("ddMMMyy\\HH:mm:ss"));
	}
}
