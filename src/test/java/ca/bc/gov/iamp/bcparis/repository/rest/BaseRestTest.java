package ca.bc.gov.iamp.bcparis.repository.rest;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import ca.bc.gov.iamp.bcparis.exception.rest.ResponseCodeNotExpected;

public class BaseRestTest {

	public BaseRest base = new BaseRest();
	
	@Test(expected=ResponseCodeNotExpected.class)
	public void test() {
		base.assertResponse(HttpStatus.OK, HttpStatus.NOT_FOUND, "body");
	}
}
