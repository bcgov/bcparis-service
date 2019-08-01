package ca.bc.gov.iamp.bcparis.processor.datagram;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ca.bc.gov.iamp.bcparis.repository.PORRestRepository;

public class PORProcessorTest {

	@InjectMocks
	private PORProcessor processor = new PORProcessor();
	
	private PORRestRepository repo = Mockito.mock(PORRestRepository.class);
	
	@Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }
	
}
