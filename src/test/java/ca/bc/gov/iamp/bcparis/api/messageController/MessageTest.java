package ca.bc.gov.iamp.bcparis.api.messageController;

import ca.bc.gov.iamp.bcparis.api.MessageController;
import ca.bc.gov.iamp.bcparis.model.message.Envelope;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.model.message.body.Body;
import ca.bc.gov.iamp.bcparis.processor.MessageProcessor;
import ca.bc.gov.iamp.bcparis.repository.Layer7MessageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;

public class MessageTest {

    public static final String MSG_F_FMT = "a message";
    private MessageController sut;

    @Mock
    private Layer7MessageRepository layer7MessageRepositoryMock;

    @Mock
    private MessageProcessor processorMock;

    @BeforeEach
    public void setUp(){

        MockitoAnnotations.initMocks(this);

        Layer7Message layer7Message = new Layer7Message();
        Envelope envelope = Envelope
                .builder()
                .body(Body.builder().msgFFmt(MSG_F_FMT).build())
                .build();
        layer7Message.setEnvelope(envelope);


        Mockito.when(processorMock.processMessage(Mockito.any()))
                .thenReturn(layer7Message);

        sut =  new MessageController(processorMock, layer7MessageRepositoryMock);

    }

    @Test
    @DisplayName("200: Message Api returns layer7message")
    public void testApiReturnsExpected() {

        ResponseEntity<Object> actual = sut.message(new Layer7Message());
        Assertions.assertEquals(MSG_F_FMT, ((Layer7Message)actual.getBody()).getEnvelope().getBody().getMsgFFmt());

    }

}
