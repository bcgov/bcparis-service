package ca.bc.gov.iamp.bcparis.repository.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IMSResponse {

	public String imsResponse;

	public String getImsResponse() {
		return imsResponse;
	}

	@Override
	public String toString() {
		return "IMSResponse [imsResponse=" + imsResponse + "]";
	}

}
