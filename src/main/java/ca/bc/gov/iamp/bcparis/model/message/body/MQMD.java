package ca.bc.gov.iamp.bcparis.model.message.body;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
