package ca.bc.gov.iamp.bcparis.api.exception;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import ca.bc.gov.iamp.bcparis.exception.icbc.ICBCRestException;
import ca.bc.gov.iamp.bcparis.exception.layer7.Layer7RestException;
import ca.bc.gov.iamp.bcparis.exception.por.PORRestException;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.processor.datagram.DriverProcessor;
import ca.bc.gov.iamp.bcparis.processor.datagram.PORProcessor;
import ca.bc.gov.iamp.bcparis.processor.datagram.VehicleProcessor;
import ca.bc.gov.iamp.bcparis.repository.Layer7MessageRepository;
import test.util.BCPARISTestUtil;
import test.util.TestUtil;

public class ExceptionLoggerTest {
    @Test
	public void checkICBCExceptionLog() {
		DriverProcessor mock = mock(DriverProcessor.class);

        final Layer7Message driverSNME = BCPARISTestUtil.getMessageDriverSNME();

		doThrow(new ICBCRestException("Second error line","error_line_2",new Throwable("Error")))
				.when(mock)
				.process(driverSNME);

            Exception exception = assertThrows(ICBCRestException.class, () -> {
                mock.process(driverSNME);
            });

            String expectedMessage = "Exception to call #ICBC Rest Service#";
            String actualMessage = exception.getMessage();
            Matcher m = Pattern.compile("#(.*?)#").matcher(actualMessage);
            while (m.find()) {
                System.out.println("Actual error log : "+actualMessage.trim());
                System.out.println("Error component = " + m.group(1));
            }
            assertTrue(actualMessage.contains(expectedMessage));
	}

    @Test
	public void checkLayer7ExceptionLog() {
		Layer7MessageRepository mock = mock(Layer7MessageRepository.class);

		//final String mockICBCResponse = TestUtil.readFile("ICBC/response-vehicle");
		final Layer7Message message = BCPARISTestUtil.getMessageVehicleVIN();

		doThrow(new Layer7RestException("Second error line","error_line_2",new Throwable("Error")))
				.when(mock)
				.sendMessage(message);

            Exception exception = assertThrows(Layer7RestException.class, () -> {
                mock.sendMessage(message);
            });

            String expectedMessage = "Exception to call #Layer 7 REST service#";
            String actualMessage = exception.getMessage();
            Matcher m = Pattern.compile("#(.*?)#").matcher(actualMessage);
            while (m.find()) {
                System.out.println("Actual error log : "+actualMessage.trim());
                System.out.println("Error component = " + m.group(1));
            }
            assertTrue(actualMessage.contains(expectedMessage));
	}

    @Test
	public void checkPORExceptionLog() {
		PORProcessor mock = mock(PORProcessor.class);

		final Layer7Message message = BCPARISTestUtil.getMessagePOR();

		doThrow(new PORRestException("Second error line","error_line_2",new Throwable("Error")))
				.when(mock)
				.process(message);

            Exception exception = assertThrows(PORRestException.class, () -> {
                mock.process(message);
            });

            String expectedMessage = "Exception to call #POR Rest service #";
            String actualMessage = exception.getMessage();
            Matcher m = Pattern.compile("#(.*?)#").matcher(actualMessage);
            while (m.find()) {
                System.out.println("Actual error log : "+actualMessage.trim());
                System.out.println("Error component = " + m.group(1));
            }
            assertTrue(actualMessage.contains(expectedMessage));
	}
}