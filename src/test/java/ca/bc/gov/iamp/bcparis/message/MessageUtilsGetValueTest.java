package ca.bc.gov.iamp.bcparis.message;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MessageUtilsGetValueTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {
                  "DL: TEST DL SNME: test smne TEXT: test text FLC: test flcVIN:REG:",
                  new String [][] {
                          {"DL", " TEST DL "},
                          {"SNME", " test smne "},
                          {"TEXT", " test text "},
                          {"FLC", " test flc"},
                          {"VIN", ""},
                          {"REG", ""}
                  }
                },
                {
                        "HC  BC41027]\\\"BC41127]\\\"RE SNME:SMITH/G1:JANE/G2:MARY/DOB:19000101/SEX:F]\\\"BCDL: 7088384       STATUS: NORMAL            PRIMARY DL STATUS:   NONE]\\\"SMITH, JUDY MADELINE                          LEARNER DL STATUS:   NONE]\\\"M/A BOX 195                                   TEMPORARY DL STATUS: NONE]\\\"DUNCAN BC]\\\"5175 TZOUHALEM RD                             LICENCE TYPE:CLIENT STUB]\\\"V9L 3X3]\\\"                                                OTHER JUR DL:]\\",
                        new String[][]{
                                {"SNME", "SMITH/G1:JANE/G2:MARY/DOB:19000101/SEX:F]\\\"BC"},
                                {"DL", " 7088384       STATUS: NORMAL            PRIMARY DL STATUS:   NONE]\\\"SMITH, JUDY MADELINE                          LEARNER DL STATUS:   NONE]\\\"M/A BOX 195                                   TEMPORARY DL STATUS: NONE]\\\"DUNCAN BC]\\\"5175 TZOUHALEM RD                             LICENCE TYPE:CLIENT STUB]\\\"V9L 3X3]\\\"                                                OTHER JUR DL:]\\"}}
                },
                {
                        "\\u000?345 <XMl>?>[[CDATA: \rSN:M00001-0001 MT:MUF MSID:BRKR-191220-18:10:04\nFROM:BC41127\nTO:BC41027\nTEXT:BCPARIS Diagnostic Test SOAPUI 443 qwe20190703102453\nHC BC41127 BC41028 G1:JEREMY/ 2019120410145020191204101450\n  \r",
                        new String[][]{
                                {"RE", null}
                        }
                }
        });
    }

    public String input;
    public String[][] expected;

    public MessageUtilsGetValueTest(String input, String[][] expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void execute() {

        for(int i = 0; i < expected.length; i++) {

            String result = MessageUtils.GetValue(this.input, expected[i][0]);

            Assert.assertEquals(
                    MessageFormat.format("Attribute [{0}] Value different from input", expected[i][0]),
                    expected[i][1],
                    result);
        }

    }

}
