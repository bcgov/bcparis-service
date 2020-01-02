package ca.bc.gov.iamp.bcparis.message;

import ca.bc.gov.iamp.bcparis.Keys;
import org.junit.Assert;
import org.junit.Test;

public class MessageUtilsEdgeCaseGetValueTest {

    @Test(expected = IllegalArgumentException.class)
    public void WithTokenNotKnownTokenShouldThrowException() {
        MessageUtils.getValue("A message", "token");
    }


    @Test
    public void WithEmptyStringShouldReturnEmptyString() {
        String result = MessageUtils.getValue("", Keys.REQUEST_SCHEMA_FROM_KEY);
        Assert.assertNull(result);
    }

    @Test
    public void WithNullStringShouldReturnNullString() {
        String result = MessageUtils.getValue(null, Keys.REQUEST_SCHEMA_FROM_KEY);
        Assert.assertNull(result);
    }


}
