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
        MessageUtils.GetValue("", Keys.RESPONSE_SCHEMA_FROM_KEY);
    }

    @Test
    public void WithNullStringShouldReturnNullString() {
        MessageUtils.GetValue(null, Keys.RESPONSE_SCHEMA_FROM_KEY);
    }

}
