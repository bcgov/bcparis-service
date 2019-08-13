package ca.bc.gov.iamp.bcparis.transformation;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class MessageTransformTest {

	private final String EMPTY_LINE = "";
	
	@Test
	public void extract_CDATA_success() {
		String message = "SN:M00001-0001 MT:MUF MSID:BRKR-190515-20:05:48 FROM:BC41127TO:BC41029TEXT:RE: 0509\nHC BC40940\nBC41027\nSNME:WISKIN/G1:TOMAS/G2:GEORGE/G3:ALPHONSE/DOB:20020325\n\n2019051520054820190515200548";
		
		List<String> list = MessageTransform.extractCDATAAttributes(message);
		
		System.out.println("Extract 1");
		list.forEach( attr ->System.out.println("attribute= " + attr));
		
		Assert.assertEquals("SN:M00001-0001", list.get(0));
		Assert.assertEquals("MT:MUF", list.get(1));
		Assert.assertEquals("MSID:BRKR-190515-20:05:48", list.get(2));
		Assert.assertEquals("FROM:BC41127", list.get(3));
		Assert.assertEquals("TO:BC41029", list.get(4));
		Assert.assertEquals("TEXT:", list.get(5));
		Assert.assertEquals("RE: 0509", list.get(6));  //\n\n
		Assert.assertEquals("HC BC40940", list.get(7));
		Assert.assertEquals("BC41027", list.get(8));
		Assert.assertEquals(EMPTY_LINE, list.get(9));
		Assert.assertEquals("SNME:WISKIN", list.get(10));
		Assert.assertEquals("G1:TOMAS", list.get(11));
		Assert.assertEquals("G2:GEORGE", list.get(12));
		Assert.assertEquals("G3:ALPHONSE", list.get(13));
		Assert.assertEquals("DOB:20020325", list.get(14));
		Assert.assertEquals(EMPTY_LINE, list.get(15));
		Assert.assertEquals("2019051520054820190515200548", list.get(16));
	}
	
	@Test
	public void extract_CDATA_escaped_success() {
		String message = "G3:ALPHONSE/DOB:20020325\n\n2019051520054820190515200548";
		
		List<String> list = MessageTransform.extractCDATAAttributes(message);
		
		System.out.println("\nExtract 2");
		list.forEach( attr ->System.out.println("attribute= " + attr));
		
		Assert.assertEquals("G3:ALPHONSE", list.get(0));
		Assert.assertEquals("DOB:20020325", list.get(1));
		Assert.assertEquals(EMPTY_LINE, list.get(2));
		Assert.assertEquals("2019051520054820190515200548", list.get(3));
	}

}
