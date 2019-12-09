package ca.bc.gov.iamp.bcparis.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.exception.message.InvalidMessage;
import ca.bc.gov.iamp.bcparis.model.message.body.Body;

@Service
public class MessageService {

	private final String NEW_LINE = "\n";
	private final String schema = "SEND MT:M" + NEW_LINE +
			  "FMT:Y" + NEW_LINE +
			  "FROM:${from}" + NEW_LINE + 
			  "TO:${to}" + NEW_LINE + 
			  "TEXT:${text}${re}" + NEW_LINE +
			  NEW_LINE +
			  "${icbc_response}";

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
		final String receiver = this.parseResponse(body.getCDATAAttribute("FROM")); //This becomes the receiver of the message
		final String sender = this.parseResponse(body.getCDATAAttribute("TO")); //This will become the sender
		final String text = this.parseResponse(body.getCDATAAttribute("TEXT"));
		final String re = this.parseResponse(body.getCDATAAttribute("RE"));
		return schema
				.replace("${from}", sender)
				.replace("${to}", receiver)
				.replace("${text}", text)
				.replace("${re}",  body.containAttribute("RE") ? "RE:" + re : "")
				.replace("${icbc_response}", icbcResponse);
	}
	
	public String escape(String message) {
		return message
				.replaceAll("&", "&amp;")
				.replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;")
				.replaceAll("\"", "&quot;");
	}
	
	public String parseResponseError(String message) {
		return isSOAPResponse(message) ? parseError(message) : message;
	}
	
	private boolean isSOAPResponse(String message) {
		return message.contains("</soapenv:Envelope>");
	}
	
	private String parseError(String message) {
		final String details = cutFromSOAPResponse(message, "<detail>", "</detail>");
		final String detailsParsed = details.trim()
				.replaceAll("</", "")
				.replaceAll("/>", "")
				.replaceAll("[<,>]", "")
				.replaceAll("\t", "");
		
		return Arrays.stream(detailsParsed.split("\n"))
			.map( s->s.trim() ).collect(Collectors.joining("\n"));
	}

	public String parseResponse(String icbcResponse) {
		final String NEW_LINE = "\n";
		icbcResponse = icbcResponse
				.replaceAll("[^\\x00-\\x7F]+", "")
				.replaceAll("\\\\u[0-9][0-9][0-9][0-9]", "")
				.replaceAll("\\]\"", NEW_LINE)		// ]” are converted to newline
				.replaceAll("\\]\\\\\"", NEW_LINE) 	// ]/” are converted to newline
				.replaceAll("\\$\"", NEW_LINE)	// $” are converted to newline
				.replaceAll("\\$\\\\\"", NEW_LINE);	// $\” are converted to newline

		return this.escape(icbcResponse);
	}
	private String cutFromSOAPResponse(final String message, final String start, final String end) {
		final int beginIndex = message.indexOf(start);
		final int endIndex = message.indexOf(end);
		return (beginIndex != -1 && endIndex != -1) 
				? message.substring(beginIndex+ start.length(), endIndex) : "";
	}
}
