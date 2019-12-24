package ca.bc.gov.iamp.bcparis.message;

import ca.bc.gov.iamp.bcparis.Keys;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class MessageUtils {

    public static final String SEMICOLLON = ":";

    public static String GetValue(String message, String key) {

        HashSet<String> knownTokens = new HashSet<String>() {{
            add("SN");
            add("MT");
            add("MSID");
            add(Keys.RESPONSE_SCHEMA_FROM_KEY);
            add("TO");
            add("SUBJ");
            add("TEXT");
            add("RE");
            add("SNME");
            add("DL");
            add("LIC");
            add("ODN");
            add("FLC");
            add("VIN");
            add("REG");
            add("RNS");
            add("RVL");
            // TODO: REMOVE TOKEN AFTER SATELLITE SERVICE IS DEPRECATED
            add("TestRNS");
        }};

        if(knownTokens.contains(key)) {
            knownTokens.remove(key);
        } else {
            throw new  IllegalArgumentException("Key is not a known token.");
        }

        if(StringUtils.isEmpty(message)) return "";

        int startIndex = message.indexOf(key + SEMICOLLON) + key.length() + 1;
        int currentEndIndex = message.length();

        if(startIndex == -1) return "";

        for(String token: knownTokens) {

            int tokenIndex = message.indexOf(token + SEMICOLLON);

            if(tokenIndex < currentEndIndex && tokenIndex >= startIndex) {
                currentEndIndex = tokenIndex;
            }
        }

        return message.substring(startIndex, currentEndIndex);

    }

}


