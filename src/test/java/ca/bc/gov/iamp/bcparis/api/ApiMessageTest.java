package ca.bc.gov.iamp.bcparis.api;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.repository.Layer7MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;

@DisplayName("API Message Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiMessageTest {

    private static final String MESSAGE_RESULT = "RESULT";

    @InjectMocks
    MessageApiTest sut = new MessageApiTest();

    @Mock
    Layer7MessageRepository layer7MessageRepositoryMock;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Test Api return expected result")
    public void testApiReturnsExpected() {

        Mockito.when(layer7MessageRepositoryMock.sendMessage(any())).thenReturn(MESSAGE_RESULT);


        ResponseEntity result = sut.testPutMessageLayer7(new Layer7Message());

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(MESSAGE_RESULT, result.getBody());

    }
}
