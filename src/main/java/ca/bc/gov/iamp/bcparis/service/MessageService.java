package ca.bc.gov.iamp.bcparis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.exception.message.InvalidMessage;
import ca.bc.gov.iamp.bcparis.model.message.body.Body;

@Service
public class MessageService {


	public List<String> getQueryAttributesList(Body body, List<String> validAttributes) {
		final List<String> result = new ArrayList<>();
		
		for (String paramName : validAttributes) {
			result.addAll(body.getAttributeList(paramName));
		}

		if(result.size() > 0)
			return result;
		
		throw new InvalidMessage("No valid query. Valid params: " + validAttributes);
	}
	
	public String buildResponse(final Body body, final String icbcResponse) {
		final String NEW_LINE = "\n";
		final String from = body.getCDATAAttribute("FROM");
		final String to = body.getCDATAAttribute("TO");	
		final String text = body.getCDATAAttribute("TEXT");
		final String re = body.getCDATAAttribute("RE");
		
		
		final String schema = "SEND MT:M" + NEW_LINE +
				  "FMT:Y" + NEW_LINE +
				  "FROM:${from}" + NEW_LINE + 
				  "TO:${to}" + NEW_LINE + 
				  "TEXT:${text}${re}" + NEW_LINE +
				  NEW_LINE +
				  "${icbc_response}";
		
		return schema
				.replace("${from}", to)
				.replace("${to}", from)
				.replace("${text}", text)
				.replace("${re}",  body.containAttribute("RE") ? "RE:" + re : "")
				.replace("${icbc_response}", icbcResponse);
	}
	
}
