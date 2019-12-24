package ca.bc.gov.iamp.bcparis.message;

import ca.bc.gov.iamp.bcparis.Keys;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class MessageUtils {

    public static final String SEMICOLLON = ":";

    public static String GetValue(String message, String key) {

        HashSet<String> knownTokens = new HashSet<String>() {{
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

        if (knownTokens.contains(key)) {
            knownTokens.remove(key);
        } else {
            throw new IllegalArgumentException("Key is not a known token.");
        }

        if (StringUtils.isEmpty(message)) return "";

        int startIndex = message.indexOf(key + SEMICOLLON);
        if (startIndex == -1) return "";

        startIndex += key.length() + 1;

        int currentEndIndex = message.length();

        for (String token : knownTokens) {

            int tokenIndex = message.indexOf(token + SEMICOLLON);

            if(tokenIndex == -1) continue;

            if (tokenIndex < currentEndIndex && tokenIndex >= startIndex) {
                currentEndIndex = tokenIndex;
            }
        }

        return message.substring(startIndex, currentEndIndex);

    }

}


