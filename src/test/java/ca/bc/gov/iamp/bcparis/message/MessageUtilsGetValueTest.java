package ca.bc.gov.iamp.bcparis.message;


import ca.bc.gov.iamp.bcparis.FakeCData;
import ca.bc.gov.iamp.bcparis.Keys;
import ca.bc.gov.iamp.bcparis.model.message.body.Body;
import ca.bc.gov.iamp.bcparis.util.RegexTokenizer;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class MessageUtilsGetValueTest {

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {
                        "DL: TEST DL SNME: test smne TEXT: test text FLC: test flc VIN:REG:",
                        new String[][]{
                                {"DL", " TEST DL"},
                                {"SNME", " test smne"},
                                {"TEXT", " test text"},
                                {"FLC", " test flc"},
                                {"VIN", ""},
                                {"REG", ""}
                        }
                },
                {
                        FakeCData.SAMPLE_DRIVER_DL,
                        new String[][]{
                                {Keys.REQUEST_SCHEMA_FROM_KEY, "BC41127"},
                                {Keys.REQUEST_SCHEMA_TO_KEY, "BC41027"},
                                {Keys.REQUEST_SCHEMA_TEXT_KEY, ""},
                                {Keys.REQUEST_SCHEMA_RE_KEY, null},
                                {Keys.REQUEST_SCHEMA_SN_KEY, "M00001-0001"},
                                {Keys.REQUEST_SCHEMA_MT_KEY, "DUF"},
                                {Keys.REQUEST_SCHEMA_MSID_KEY, "BRKR-190515-20:02:07"},
                                {Keys.REQUEST_SCHEMA_SUBJ_KEY, null},
                                {Keys.REQUEST_SCHEMA_SNME_KEY, null},
                                {Keys.REQUEST_SCHEMA_DL_KEY, "3559874"},
                                {Keys.REQUEST_SCHEMA_LIC_KEY, null},
                                {Keys.REQUEST_SCHEMA_ODN_KEY, null},
                                {Keys.REQUEST_SCHEMA_FLC_KEY, null},
                                {Keys.REQUEST_SCHEMA_VIN_KEY, null},
                                {Keys.REQUEST_SCHEMA_REG_KEY, null},
                                {Keys.REQUEST_SCHEMA_RNS_KEY, null},
                                {Keys.REQUEST_SCHEMA_RVL_KEY, null},
                                {Keys.REQUEST_SCHEMA_TEST_RNS_KEY, null}
                        }
                },
                {
                        FakeCData.SAMPLE_DRIVER_MULTIPLE_PARAMS,
                        new String[][]{
                                {Keys.REQUEST_SCHEMA_FROM_KEY, "BC41127"},
                                {Keys.REQUEST_SCHEMA_TO_KEY, "BC41027"},
                                {Keys.REQUEST_SCHEMA_TEXT_KEY, "BCPARIS Diagnostic Test qwe20190827173834"},
                                {Keys.REQUEST_SCHEMA_RE_KEY, null},
                                {Keys.REQUEST_SCHEMA_SN_KEY, "M00001-0001"},
                                {Keys.REQUEST_SCHEMA_MT_KEY, "DUF"},
                                {Keys.REQUEST_SCHEMA_MSID_KEY, "BRKR-190515-20:02:07"},
                                {Keys.REQUEST_SCHEMA_SUBJ_KEY, null},
                                {Keys.REQUEST_SCHEMA_SNME_KEY, "NEWMAN/G1:OLDSON/G2:MIKE/DOB:19900214"},
                                {Keys.REQUEST_SCHEMA_DL_KEY, "3559874"},
                                {Keys.REQUEST_SCHEMA_LIC_KEY, null},
                                {Keys.REQUEST_SCHEMA_ODN_KEY, null},
                                {Keys.REQUEST_SCHEMA_FLC_KEY, null},
                                {Keys.REQUEST_SCHEMA_VIN_KEY, null},
                                {Keys.REQUEST_SCHEMA_REG_KEY, null},
                                {Keys.REQUEST_SCHEMA_RNS_KEY, null},
                                {Keys.REQUEST_SCHEMA_RVL_KEY, null},
                                {Keys.REQUEST_SCHEMA_TEST_RNS_KEY, null}
                        }
                },
                {
                        FakeCData.SAMPLE_DRIVER_SNME,
                        new String[][]{
                                {Keys.REQUEST_SCHEMA_FROM_KEY, "BC41127"},
                                {Keys.REQUEST_SCHEMA_TO_KEY, "BC41027"},
                                {Keys.REQUEST_SCHEMA_TEXT_KEY, ""},
                                {Keys.REQUEST_SCHEMA_RE_KEY, " 0509"},
                                {Keys.REQUEST_SCHEMA_SN_KEY, "M00001-0001"},
                                {Keys.REQUEST_SCHEMA_MT_KEY, "MUF"},
                                {Keys.REQUEST_SCHEMA_MSID_KEY, "BRKR-190515-20:05:48"},
                                {Keys.REQUEST_SCHEMA_SUBJ_KEY, null},
                                {Keys.REQUEST_SCHEMA_SNME_KEY, "NEWMAN/G1:OLDSON/G2:MIKE/DOB:19900214"},
                                {Keys.REQUEST_SCHEMA_DL_KEY, null},
                                {Keys.REQUEST_SCHEMA_LIC_KEY, null},
                                {Keys.REQUEST_SCHEMA_ODN_KEY, null},
                                {Keys.REQUEST_SCHEMA_FLC_KEY, null},
                                {Keys.REQUEST_SCHEMA_VIN_KEY, null},
                                {Keys.REQUEST_SCHEMA_REG_KEY, null},
                                {Keys.REQUEST_SCHEMA_RNS_KEY, null},
                                {Keys.REQUEST_SCHEMA_RVL_KEY, null},
                                {Keys.REQUEST_SCHEMA_TEST_RNS_KEY, null}
                        }
                },
                {
                        FakeCData.SAMPLE_INVALID_DRIVER,
                        new String[][]{
                                {Keys.REQUEST_SCHEMA_FROM_KEY, "BC41127"},
                                {Keys.REQUEST_SCHEMA_TO_KEY, "BC41027"},
                                {Keys.REQUEST_SCHEMA_TEXT_KEY, ""},
                                {Keys.REQUEST_SCHEMA_RE_KEY, null},
                                {Keys.REQUEST_SCHEMA_SN_KEY, "M00001-0001"},
                                {Keys.REQUEST_SCHEMA_MT_KEY, "DUF"},
                                {Keys.REQUEST_SCHEMA_MSID_KEY, "BRKR-190515-20:02:07"},
                                {Keys.REQUEST_SCHEMA_SUBJ_KEY, null},
                                {Keys.REQUEST_SCHEMA_SNME_KEY, null},
                                {Keys.REQUEST_SCHEMA_DL_KEY, null},
                                {Keys.REQUEST_SCHEMA_LIC_KEY, null},
                                {Keys.REQUEST_SCHEMA_ODN_KEY, null},
                                {Keys.REQUEST_SCHEMA_FLC_KEY, null},
                                {Keys.REQUEST_SCHEMA_VIN_KEY, null},
                                {Keys.REQUEST_SCHEMA_REG_KEY, null},
                                {Keys.REQUEST_SCHEMA_RNS_KEY, null},
                                {Keys.REQUEST_SCHEMA_RVL_KEY, null},
                                {Keys.REQUEST_SCHEMA_TEST_RNS_KEY, null}
                        }
                },
                {
                        FakeCData.SAMPLE_INVALID_VEHICLE,
                        new String[][]{
                                {Keys.REQUEST_SCHEMA_FROM_KEY, "BC41127"},
                                {Keys.REQUEST_SCHEMA_TO_KEY, "BC41028"},
                                {Keys.REQUEST_SCHEMA_TEXT_KEY, ""},
                                {Keys.REQUEST_SCHEMA_RE_KEY, " 8261"},
                                {Keys.REQUEST_SCHEMA_SN_KEY, "M00001-0001"},
                                {Keys.REQUEST_SCHEMA_MT_KEY, "MUF"},
                                {Keys.REQUEST_SCHEMA_MSID_KEY, "BRKR-190515-20:02:04"},
                                {Keys.REQUEST_SCHEMA_SUBJ_KEY, null},
                                {Keys.REQUEST_SCHEMA_SNME_KEY, null},
                                {Keys.REQUEST_SCHEMA_DL_KEY, null},
                                {Keys.REQUEST_SCHEMA_LIC_KEY, null},
                                {Keys.REQUEST_SCHEMA_ODN_KEY, null},
                                {Keys.REQUEST_SCHEMA_FLC_KEY, null},
                                {Keys.REQUEST_SCHEMA_VIN_KEY, null},
                                {Keys.REQUEST_SCHEMA_REG_KEY, null},
                                {Keys.REQUEST_SCHEMA_RNS_KEY, null},
                                {Keys.REQUEST_SCHEMA_RVL_KEY, null},
                                {Keys.REQUEST_SCHEMA_TEST_RNS_KEY, null}
                        }
                },
                {
                        FakeCData.SAMPLE_POR,
                        new String[][]{
                                {Keys.REQUEST_SCHEMA_FROM_KEY, "BC41127"},
                                {Keys.REQUEST_SCHEMA_TO_KEY, "BC41029"},
                                {Keys.REQUEST_SCHEMA_TEXT_KEY, ""},
                                {Keys.REQUEST_SCHEMA_RE_KEY, " 0509"},
                                {Keys.REQUEST_SCHEMA_SN_KEY, "M00001-0001"},
                                {Keys.REQUEST_SCHEMA_MT_KEY, "MUF"},
                                {Keys.REQUEST_SCHEMA_MSID_KEY, "BRKR-190515-20:05:48"},
                                {Keys.REQUEST_SCHEMA_SUBJ_KEY, null},
                                {Keys.REQUEST_SCHEMA_SNME_KEY, "WISKIN/G1:TOMAS/G2:GEORGE/G3:ALPHONSE/DOB:20050505"},
                                {Keys.REQUEST_SCHEMA_DL_KEY, null},
                                {Keys.REQUEST_SCHEMA_LIC_KEY, null},
                                {Keys.REQUEST_SCHEMA_ODN_KEY, null},
                                {Keys.REQUEST_SCHEMA_FLC_KEY, null},
                                {Keys.REQUEST_SCHEMA_VIN_KEY, null},
                                {Keys.REQUEST_SCHEMA_REG_KEY, null},
                                {Keys.REQUEST_SCHEMA_RNS_KEY, null},
                                {Keys.REQUEST_SCHEMA_RVL_KEY, null},
                                {Keys.REQUEST_SCHEMA_TEST_RNS_KEY, null}
                        }
                },
                {
                        FakeCData.SAMPLE_SATELITTE,
                        new String[][]{
                                {Keys.REQUEST_SCHEMA_FROM_KEY, "BC41127"},
                                {Keys.REQUEST_SCHEMA_TO_KEY, "BC41027"},
                                {Keys.REQUEST_SCHEMA_TEXT_KEY, "BCPARIS Diagnostic Test qwe20190820202619"},
                                {Keys.REQUEST_SCHEMA_RE_KEY, null},
                                {Keys.REQUEST_SCHEMA_SN_KEY, "M00001-0001"},
                                {Keys.REQUEST_SCHEMA_MT_KEY, "MUF"},
                                {Keys.REQUEST_SCHEMA_MSID_KEY, "BRKR-190820-16:26:19"},
                                {Keys.REQUEST_SCHEMA_SUBJ_KEY, null},
                                {Keys.REQUEST_SCHEMA_SNME_KEY, "SMITH/G1:JOHN/"},
                                {Keys.REQUEST_SCHEMA_DL_KEY, null},
                                {Keys.REQUEST_SCHEMA_LIC_KEY, null},
                                {Keys.REQUEST_SCHEMA_ODN_KEY, null},
                                {Keys.REQUEST_SCHEMA_FLC_KEY, null},
                                {Keys.REQUEST_SCHEMA_VIN_KEY, null},
                                {Keys.REQUEST_SCHEMA_REG_KEY, "2156746"},
                                {Keys.REQUEST_SCHEMA_RNS_KEY, null},
                                {Keys.REQUEST_SCHEMA_RVL_KEY, null},
                                {Keys.REQUEST_SCHEMA_TEST_RNS_KEY, null}
                        }
                },
                {
                        FakeCData.SAMPLE_SATELITTE_ROUND_TRIP,
                        new String[][]{
                                {Keys.REQUEST_SCHEMA_FROM_KEY, "BC41127"},
                                {Keys.REQUEST_SCHEMA_TO_KEY, "BC41127"},
                                {Keys.REQUEST_SCHEMA_TEXT_KEY, "BCPARIS Diagnostic Test qwe20190820202619"},
                                {Keys.REQUEST_SCHEMA_RE_KEY, null},
                                {Keys.REQUEST_SCHEMA_SN_KEY, "M00001-0001"},
                                {Keys.REQUEST_SCHEMA_MT_KEY, "MUF"},
                                {Keys.REQUEST_SCHEMA_MSID_KEY, "BRKR-190820-16:26:19"},
                                {Keys.REQUEST_SCHEMA_SUBJ_KEY, null},
                                {Keys.REQUEST_SCHEMA_SNME_KEY, "SMITH/G1:JOHN/"},
                                {Keys.REQUEST_SCHEMA_DL_KEY, null},
                                {Keys.REQUEST_SCHEMA_LIC_KEY, null},
                                {Keys.REQUEST_SCHEMA_ODN_KEY, null},
                                {Keys.REQUEST_SCHEMA_FLC_KEY, null},
                                {Keys.REQUEST_SCHEMA_VIN_KEY, null},
                                {Keys.REQUEST_SCHEMA_REG_KEY, "2156746"},
                                {Keys.REQUEST_SCHEMA_RNS_KEY, null},
                                {Keys.REQUEST_SCHEMA_RVL_KEY, null},
                                {Keys.REQUEST_SCHEMA_TEST_RNS_KEY, null}
                        }
                },
                {
                        FakeCData.SAMPLE_VEHICLE_LIC,
                        new String[][]{
                                {Keys.REQUEST_SCHEMA_FROM_KEY, "BC41127"},
                                {Keys.REQUEST_SCHEMA_TO_KEY, "BC41028"},
                                {Keys.REQUEST_SCHEMA_TEXT_KEY, ""},
                                {Keys.REQUEST_SCHEMA_RE_KEY, " 8261"},
                                {Keys.REQUEST_SCHEMA_SN_KEY, "M00001-0001"},
                                {Keys.REQUEST_SCHEMA_MT_KEY, "MUF"},
                                {Keys.REQUEST_SCHEMA_MSID_KEY, "BRKR-190515-20:02:04"},
                                {Keys.REQUEST_SCHEMA_SUBJ_KEY, null},
                                {Keys.REQUEST_SCHEMA_SNME_KEY, null},
                                {Keys.REQUEST_SCHEMA_DL_KEY, null},
                                {Keys.REQUEST_SCHEMA_LIC_KEY, "PN890H"},
                                {Keys.REQUEST_SCHEMA_ODN_KEY, null},
                                {Keys.REQUEST_SCHEMA_FLC_KEY, null},
                                {Keys.REQUEST_SCHEMA_VIN_KEY, null},
                                {Keys.REQUEST_SCHEMA_REG_KEY, null},
                                {Keys.REQUEST_SCHEMA_RNS_KEY, null},
                                {Keys.REQUEST_SCHEMA_RVL_KEY, null},
                                {Keys.REQUEST_SCHEMA_TEST_RNS_KEY, null}
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

        for (int i = 0; i < expected.length; i++) {

            String result = MessageUtils.GetValue(this.input, expected[i][0]);

            String oldResult = executeOldAlgo(this.input, this.expected[i][0]);


            if(result == null && oldResult.equals("")) {
            } else {
                try {
                    Assert.assertEquals(MessageFormat.format("Attribute [{0}] new result did not produced same:", expected[i][0]), oldResult, result);
                } catch (Throwable t) {
                    collector.addError(t);
                }
            }


            Assert.assertEquals(
                    MessageFormat.format("Attribute [{0}] Value different from input", expected[i][0]),
                    expected[i][1],
                    result);
        }

    }

    public String executeOldAlgo(String text, String key) {
        List<String> delimiters = Arrays.asList("SN:", "MT:", "MSID:", "FROM:", "TO:", "SUBJ:", "TEXT:", "RE:",
                "TestRNS:", // Satellite test
                "SNME:", //POR and Driver
                "DL:",	//Driver
                "LIC:", "ODN:", "TAG:", "FLC:", "VIN:", "REG:", "RNS:", "RVL:", // Vehicle
                "\\n","\\\\n", "\n\n", "\\\\n\\\\n"); // New line and New line escaped (backslash)

        List<String> result = new RegexTokenizer(text, delimiters).getTokens();

        Body body = new Body();

        body.setCdataAttributes(result);

        return body.getCDATAAttribute(key);

    }

}
