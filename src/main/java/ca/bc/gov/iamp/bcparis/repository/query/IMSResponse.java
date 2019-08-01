package ca.bc.gov.iamp.bcparis.repository.query;

import lombok.Builder;

@Builder
public class IMSResponse {

	public final String imsResponse;

	public IMSResponse(String imsResponse) {
		this.imsResponse = imsResponse;
	}

	public String getImsResponse() {
		return imsResponse;
	}

	@Override
	public String toString() {
		return "IMSResponse [imsResponse=" + imsResponse + "]";
	}

}
