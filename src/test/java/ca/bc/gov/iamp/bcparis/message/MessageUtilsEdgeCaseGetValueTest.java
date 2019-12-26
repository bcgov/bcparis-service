package ca.bc.gov.iamp.bcparis.message;

import ca.bc.gov.iamp.bcparis.Keys;
import ca.bc.gov.iamp.bcparis.model.message.body.Body;
import ca.bc.gov.iamp.bcparis.util.RegexTokenizer;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class MessageUtilsEdgeCaseGetValueTest {

    @Test(expected = IllegalArgumentException.class)
    public void WithTokenNotKnownTokenShouldThrowException() {
        MessageUtils.GetValue("A message", "token");
    }


    @Test
    public void WithEmptyStringShouldReturnEmptyString() {
        String result = MessageUtils.GetValue("", Keys.REQUEST_SCHEMA_FROM_KEY);
        Assert.assertNull(result);
    }

    @Test
    public void WithNullStringShouldReturnNullString() {
        String result = MessageUtils.GetValue(null, Keys.REQUEST_SCHEMA_FROM_KEY);
        Assert.assertNull(result);
    }

    @Test
    public void Test() {
        List<String> delimiters = Arrays.asList("SN:", "MT:", "MSID:", "FROM:", "TO:", "SUBJ:", "TEXT:", "RE:",
                "TestRNS:", // Satellite test
                "SNME:", //POR and Driver
                "DL:",	//Driver
                "LIC:", "ODN:", "TAG:", "FLC:", "VIN:", "REG:", "RNS:", "RVL:", // Vehicle
                "\\n","\\\\n", "\n\n", "\\\\n\\\\n"); // New line and New line escaped (backslash)

        List<String> result = new RegexTokenizer("\"HC  BC41027]\\\\\\\"BC41127]\\\\\\\"RE SNME:SMITH/G1:JANE/G2:MARY/DOB:19000101/SEX:F]\\\\\\\"BCDL: 7088384       STATUS: NORMAL            PRIMARY DL STATUS:   NONE]\\\\\\\"SMITH, JUDY MADELINE                          LEARNER DL STATUS:   NONE]\\\\\\\"M/A BOX 195                                   TEMPORARY DL STATUS: NONE]\\\\\\\"DUNCAN BC]\\\\\\\"5175 TZOUHALEM RD                             LICENCE TYPE:CLIENT STUB]\\\\\\\"V9L 3X3]\\\\\\\"                                                OTHER JUR DL:]\\\\\",\n", delimiters).getTokens();

        Body body = new Body();

        body.setCdataAttributes(result);

        String result3 = body.getCDATAAttribute("SNME");

        Assert.assertNull(result);
    }

}
