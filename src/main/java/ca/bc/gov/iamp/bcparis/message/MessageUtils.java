package ca.bc.gov.iamp.bcparis.message;

import ca.bc.gov.iamp.bcparis.Keys;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Helper class to manipulate ICBC Message
 */
public class MessageUtils {


    private MessageUtils() { }

    private static final String SEMICOLON = ":";
    private static final String STRING_END_ONE = "]]>$";
    private static final String STRING_END_TWO = "\n$";

    private static final Set<String> KNOWN_TOKENS;

    static {
        HashSet<String> knownTokens = new HashSet<>();
        knownTokens.add(Keys.REQUEST_SCHEMA_SN_KEY);
        knownTokens.add(Keys.REQUEST_SCHEMA_MT_KEY);
        knownTokens.add(Keys.REQUEST_SCHEMA_MSID_KEY);
        knownTokens.add(Keys.REQUEST_SCHEMA_FROM_KEY);
        knownTokens.add(Keys.REQUEST_SCHEMA_TO_KEY);
        knownTokens.add(Keys.REQUEST_SCHEMA_SUBJ_KEY);
        knownTokens.add(Keys.REQUEST_SCHEMA_TEXT_KEY);
        knownTokens.add(Keys.REQUEST_SCHEMA_RE_KEY);
        knownTokens.add(Keys.REQUEST_SCHEMA_SNME_KEY);
        knownTokens.add(Keys.REQUEST_SCHEMA_DL_KEY);
        knownTokens.add(Keys.REQUEST_SCHEMA_LIC_KEY);
        knownTokens.add(Keys.REQUEST_SCHEMA_ODN_KEY);
        knownTokens.add(Keys.REQUEST_SCHEMA_FLC_KEY);
        knownTokens.add(Keys.REQUEST_SCHEMA_VIN_KEY);
        knownTokens.add(Keys.REQUEST_SCHEMA_REG_KEY);
        knownTokens.add(Keys.REQUEST_SCHEMA_RNS_KEY);
        knownTokens.add(Keys.REQUEST_SCHEMA_RVL_KEY);
        knownTokens.add(Keys.REQUEST_SCHEMA_TEST_RNS_KEY);
        KNOWN_TOKENS = Collections.unmodifiableSet(knownTokens);
    }

    /**
     * Extract the attribute value based on a give token
     * Known tokens includes:
     * <ul>
     *  <li>FROM
     *  <li>TO
     *  <li>TEXT
     *  <li>RE
     *  <li>SN
     *  <li>MT
     *  <li>MSID
     *  <li>SUBJ
     *  <li>SNME
     *  <li>DL
     *  <li>LIC
     *  <li>ODN
     *  <li>FLC
     *  <li>VIN
     *  <li>REG
     *  <li>RNS
     *  <li>RVL
     *  <li>TestRNS
     * </ul>
     *
     * @param message the source message
     * @param key     a known key
     * @return the value of the attribute
     * @throws IllegalArgumentException if the key is not a known key
     * @since 1.0.20
     */
    public static String getValue(String message, String key) {

        if (!KNOWN_TOKENS.contains(key)) throw new IllegalArgumentException("key must be a known token");

        if (StringUtils.isEmpty(message)) return null;

        message = removeKnownEnd(message);

        message = removeToToken(message, key);

        if (message == null) return null;

        return message.substring(0, getEndIndex(message)).replaceAll("\\s+$", "");

    }

    private static String removeKnownEnd(String message) {
        message = message.replaceAll(STRING_END_ONE, "");
        return message.replaceAll(STRING_END_TWO, "");
    }

    private static String removeToToken(String message, String token) {
        int startIndex = message.indexOf(token + SEMICOLON);
        if (startIndex == -1) return null;

        startIndex += token.length() + 1;

        return message.substring(startIndex);
    }

    private static int getEndIndex(String message) {
        int currentEndIndex = message.length();

        for (String token : KNOWN_TOKENS) {

            int tokenIndex = message.indexOf(token + SEMICOLON);

            if (tokenIndex < currentEndIndex && tokenIndex >= 0) {
                currentEndIndex = tokenIndex;
            }

            if (message.indexOf(SEMICOLON) > currentEndIndex) break;
        }

        return currentEndIndex;
    }
}


