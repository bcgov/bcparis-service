package ca.bc.gov.iamp.bcparis.repository;

import lombok.Builder;

@Builder
public class IMSRequest {

	public final String imsRequest;

	public IMSRequest(String imsRequest) {
		this.imsRequest = imsRequest;
	}

	public String getImsRequest() {
		return imsRequest;
	}

}
