package ca.bc.gov.iamp.bcparis.message;

import ca.bc.gov.iamp.bcparis.Keys;
import org.junit.Assert;
import org.junit.Test;

import java.security.Key;

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
