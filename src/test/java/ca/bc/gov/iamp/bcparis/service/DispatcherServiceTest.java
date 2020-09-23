package ca.bc.gov.iamp.bcparis.service;

import ca.bc.gov.iamp.bcparis.message.DriverProcessor;
import ca.bc.gov.iamp.bcparis.message.PORProcessor;
import ca.bc.gov.iamp.bcparis.message.VehicleProcessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import test.util.BCPARISTestUtil;

public class DispatcherServiceTest {

	@InjectMocks
	private DispatcherService service = new DispatcherService();
	
	@Mock
	private DriverProcessor driver;
	
	@Mock
	private VehicleProcessor vehicle;
	
	@Mock
	private PORProcessor por;
	
	@Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void dispatch_driver_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageDriverSNME();
		service.dispatch(message);
		Mockito.verify(driver, Mockito.times(1)).process(message);
	}
	
	@Test
	public void dispatch_vehicle_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageVehicleLIC();
		service.dispatch(message);
		Mockito.verify(vehicle, Mockito.times(1)).process(message);
	}
	
	@Test
	public void dispatch_por_success() {
		final Layer7Message message = BCPARISTestUtil.getMessagePOR();
		service.dispatch(message);
		Mockito.verify(por, Mockito.times(1)).process(message);
	}

}
