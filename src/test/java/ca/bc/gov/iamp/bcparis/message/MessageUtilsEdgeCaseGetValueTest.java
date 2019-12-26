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


}
