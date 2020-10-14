package ca.bc.gov.iamp.bcparis.message;

import ca.bc.gov.iamp.bcparis.model.MessageType;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import org.springframework.stereotype.Service;

@Service
public class ReportProcessor implements DatagramProcessor {
    @Override
    public MessageType getType() {
        return MessageType.REPORT;
    }

    @Override
    public Layer7Message process(Layer7Message message) {
        return message;
    }
}
