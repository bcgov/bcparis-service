package ca.bc.gov.iamp.bcparis.api.messageController;

import ca.bc.gov.iamp.bcparis.api.MessageController;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.processor.MessageProcessor;
import ca.bc.gov.iamp.bcparis.repository.Layer7MessageRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;

@DisplayName("API Message Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestPutMessageLayer7Test {

    private static final String MESSAGE_RESULT = "RESULT";

    private MessageController sut;

    @Mock
    private Layer7MessageRepository layer7MessageRepositoryMock;

    @Mock
    private MessageProcessor processorMock;

    @BeforeEach
    public void setUp(){

        MockitoAnnotations.initMocks(this);

        Mockito.when(layer7MessageRepositoryMock.sendMessage(any())).thenReturn(MESSAGE_RESULT);

        sut =  new MessageController(processorMock, layer7MessageRepositoryMock);

    }

    @Test
    @DisplayName("200: Test Api return expected result")
    public void testApiReturnsExpected() {
        ResponseEntity result = sut.testPutMessageLayer7(new Layer7Message());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(MESSAGE_RESULT, result.getBody());
    }
}
