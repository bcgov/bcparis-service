package ca.bc.gov.iamp.bcparis.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;

@Getter
public class RegexTokenizer {

	private List<String> tokens = new ArrayList<>();
	
	public RegexTokenizer(String text, List<String> delimiters) {
		executeTokenizer(text, String.join("|", delimiters));
	}
	
	private void executeTokenizer(final String text, final String regularExpression) {
		Matcher matcher = Pattern.compile(regularExpression).matcher(text);
		List<Integer> indexes = extractMatchIndexes(matcher);

		for (int i = 0; i < indexes.size(); i++) {
			Integer start = indexes.get(i);
			Integer end = (i < indexes.size() - 1) ? indexes.get(i + 1) : text.length();

			String token = text.substring(start, end);
			tokens.add(token.trim());
		}
	}
	
	private List<Integer> extractMatchIndexes(Matcher matcher){
		List<Integer> indexes = new ArrayList<>();
		while (matcher.find())
			indexes.add(matcher.start());
		return indexes;
	}
	
	public static void main(String[] args) {
		String message = "LIC:233AWB/H/LIC:GVW143/H/LIC:007F" + 
				"JR/H/LIC:JXX477/REG:957167" + 
				"/VIN:1GNEL19W1XB163160/VIN:163160/P:Y/VIN:163160/P:Y/RSVP:16";
		
		RegexTokenizer tokenizer = new RegexTokenizer(message, Arrays.asList("LIC:", "REG:", "VIN:"));
		
		System.out.println(tokenizer.getTokens());
	}
}
