
package ca.bc.gov.iamp.bcparis.processor.datagram;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ca.bc.gov.iamp.bcparis.exception.message.InvalidMessage;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.model.message.body.Body;
import ca.bc.gov.iamp.bcparis.repository.ICBCRestRepository;
import ca.bc.gov.iamp.bcparis.repository.query.IMSRequest;

@Service
public class VehicleProcessor {

	private final Logger log = LoggerFactory.getLogger(VehicleProcessor.class);
	
	@Autowired
	private ICBCRestRepository ICBCrepository;
	
	public Layer7Message process(Layer7Message message) {
		log.debug("Processing Vehicle message.");
		
		String imsContent = createIMS(message);
		IMSRequest ims = IMSRequest.builder().imsRequest(imsContent).build();
		
		String icbcResponse = ICBCrepository.requestDetails(ims);
		
		icbcResponse = parseResponse(icbcResponse);
		
		String response = buildResponse(message, icbcResponse);
		message.getEnvelope().getBody().setMsgFFmt(response);
		
		log.info("Vehicle message processing completed.");
		
		return message;
	}
	
	public String createIMS(Layer7Message message) {
		
		final String schema = "${transactionName} HC ${fromORI} ${toORI} ${queryParams}";
		
		final Body body = message.getEnvelope().getBody();
		
		final String from = body.getCDATAAttribute("FROM");
		final String to = body.getCDATAAttribute("TO");
		final String queryParamAttr = getQueryParamAttribute(message);
		final String queryParams = queryParamAttr + ":" + getQueryParam(body, queryParamAttr);
		final String TRANSACTION = getTransaction(queryParamAttr);
		
		return schema
				.replace("${transactionName}", TRANSACTION)
				.replace("${fromORI}", from)
				.replace("${toORI}", to)
				.replace("${queryParams}", queryParams);
	}
	
	private String getTransaction(String queryParamAttr) {
		return ( "RVL".equalsIgnoreCase(queryParamAttr) || "RNS".equalsIgnoreCase(queryParamAttr))
			? "JISTRN2" : "JISTRAN";
	}
	
	private String getQueryParam(Body body, String queryParamAttr) {
		final String START = queryParamAttr + ":";
		final String END = "\\n";
		final String END2 = "\n";
		String result = body.cutFromCDATA(START, END);
		if(StringUtils.isEmpty(result))
			result = body.cutFromCDATA(START, END2);
		return result;
	}
	
	private String getQueryParamAttribute(Layer7Message message) {
		final Body body = message.getEnvelope().getBody();
		final List<String> validBC41028 = Arrays.asList("LIC", "ODN", "TAG", "FLC", "VIN", "REG", "RNS", "RVL");
		for (String string : validBC41028) {
			if(body.containAttribute(string))
				return string;
		}
		throw new InvalidMessage("Invalid BC41028 query param not found. Valid params=" + validBC41028);
	}
	
	private String parseResponse(String icbcResponse) {
		final String NEW_LINE = "\n";
		
		return icbcResponse
				.replaceAll("\\$\"", NEW_LINE)	// $” are converted to newline
				.replaceAll("\\$\\\\\"", NEW_LINE)	// $\” are converted to newline
				.replaceAll("\\u[0-9][0-9][0-9][0-9]", "")
				.replaceAll("[^\\x00-\\x7F]+", "");
	}

	private String buildResponse(Layer7Message message, String icbcResponse) {
		final String NEW_LINE = "\n";
		final String schema = "SEND MT:M" + NEW_LINE +
							  "FMT:Y" + NEW_LINE +
							  "FROM:${from}" + NEW_LINE + 
							  "TO:${to}" + NEW_LINE + 
							  "TEXT:${TEXT}RE:${RE}" + NEW_LINE +
							  NEW_LINE +
							  "${icbc_response}";
		
		final Body body = message.getEnvelope().getBody(); 
		final String from = body.getCDATAAttribute("FROM");
		final String to = body.getCDATAAttribute("TO");	
		final String text = body.getCDATAAttribute("TEXT");
		final String re = body.getCDATAAttribute("RE");
		
		return schema
				.replace("${from}", to)
				.replace("${to}", from)
				.replace("${TEXT}", text)
				.replace("${RE}", re)
				.replace("${icbc_response}", icbcResponse);
	}

}
