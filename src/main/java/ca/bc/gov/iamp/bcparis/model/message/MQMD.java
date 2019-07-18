package ca.bc.gov.iamp.bcparis.model.message;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MQMD implements Serializable {

	private static final long serialVersionUID = -6017561699789308061L;
	
	private String feedback;
	private String replyToQueueManagerName;
	private String messageIdByte;
	private String messageType;
	private String format;
	private String replyToQueueName;
	private String correlationIdByte;

}
