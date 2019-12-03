package ca.bc.gov.iamp.bcparis.repository.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IMSResponse {

	private String imsResponse;

	@Override
	public String toString() {
		return "IMSResponse [imsResponse=" + imsResponse + "]";
	}

}
