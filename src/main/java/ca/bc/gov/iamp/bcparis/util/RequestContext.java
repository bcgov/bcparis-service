package ca.bc.gov.iamp.bcparis.util;

import javax.annotation.ManagedBean;

import org.springframework.web.context.annotation.RequestScope;

import lombok.Getter;
import lombok.Setter;

@ManagedBean
@RequestScope
@Getter
@Setter
public class RequestContext {

	public Object requestObject;
	
}
