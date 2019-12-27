package ca.bc.gov.iamp.bcparis.message;

import ca.bc.gov.iamp.bcparis.Keys;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class MessageUtils {

    private static final String SEMICOLLON = ":";
    private static final String STRING_END_ONE = "]]>$";
    private static final String STRING_END_TWO = "\n$";

    private static HashSet<String> KNOWN_TOKENS = new HashSet<String>() {{
        add(Keys.REQUEST_SCHEMA_SN_KEY);
        add(Keys.REQUEST_SCHEMA_MT_KEY);
        add(Keys.REQUEST_SCHEMA_MSID_KEY);
        add(Keys.REQUEST_SCHEMA_FROM_KEY);
        add(Keys.REQUEST_SCHEMA_TO_KEY);
        add(Keys.REQUEST_SCHEMA_SUBJ_KEY);
        add(Keys.REQUEST_SCHEMA_TEXT_KEY);
        add(Keys.REQUEST_SCHEMA_RE_KEY);
        add(Keys.REQUEST_SCHEMA_SNME_KEY);
        add(Keys.REQUEST_SCHEMA_DL_KEY);
        add(Keys.REQUEST_SCHEMA_LIC_KEY);
        add(Keys.REQUEST_SCHEMA_ODN_KEY);
        add(Keys.REQUEST_SCHEMA_FLC_KEY);
        add(Keys.REQUEST_SCHEMA_VIN_KEY);
        add(Keys.REQUEST_SCHEMA_REG_KEY);
        add(Keys.REQUEST_SCHEMA_RNS_KEY);
        add(Keys.REQUEST_SCHEMA_RVL_KEY);
        add(Keys.REQUEST_SCHEMA_TEST_RNS_KEY);
    }};

    public static String GetValue(String message, String key) {

        if(!KNOWN_TOKENS.contains(key)) throw new IllegalArgumentException("key must be a known token");

        if (StringUtils.isEmpty(message)) return null;

        message = removeKnownEnd(message);

        message = removeToToken(message, key);

        if(message == null) return null;

        int currentEndIndex = getEndIndex(message);

        return message.substring(0, currentEndIndex).replaceAll("\\s+$", "");

    }

    private static String removeKnownEnd(String message) {
        message = message.replaceAll(STRING_END_ONE, "");
        return message.replaceAll(STRING_END_TWO, "");
    }

    private static String removeToToken(String message, String token) {
        int startIndex = message.indexOf(token + SEMICOLLON);
        if (startIndex == -1) return null;

        startIndex += token.length() + 1;

        return message.substring(startIndex);
    }

    private static int getEndIndex(String message) {
        int currentEndIndex = message.length();

        for (String token : KNOWN_TOKENS) {

            int tokenIndex = message.indexOf(token + SEMICOLLON);

            if (tokenIndex < currentEndIndex && tokenIndex >= 0) {
                currentEndIndex = tokenIndex;
            }

            if(message.indexOf(":") > currentEndIndex) break;
        }

        return currentEndIndex;
    }
}


