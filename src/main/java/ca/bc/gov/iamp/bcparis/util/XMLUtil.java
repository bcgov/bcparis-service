package ca.bc.gov.iamp.bcparis.util;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUtil {

	public static final String CDATA_BEGIN = "<![CDATA[";
	public static final String CDATA_END = "]]>";
	
	public static Document parseXML(String xml) throws ParserConfigurationException, SAXException, IOException {
		xml = deleteCDATA(xml);
		
		InputSource is = new InputSource();
	    is.setCharacterStream(new StringReader(xml));
	    
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    
	    return db.parse(is);
	}

	/**
	 * Extract the CDATA
	 * @param msgFFmt
	 * 	Example:	<![CDATA[\rSN:M00001-0001 MT:MUF MSID:BRKR-190703-20:40:43\nFROM:BC41127\nTO:BC41028]]>
	 * @return
	 * 	Example:	SN:M00001-0001 MT:MUF MSID:BRKR-190703-20:40:43\nFROM:BC41127\nTO:BC41028
	 */
	public static String extractCDATAFromMsgFFmt(String msgFFmt) {
		int beginIndex = msgFFmt.indexOf(CDATA_BEGIN);
		int endIndex = msgFFmt.indexOf(CDATA_END);
		return (beginIndex != -1 && endIndex != -1)
				? msgFFmt.substring(beginIndex + CDATA_BEGIN.length(), endIndex)
				: msgFFmt;
	}
	
	public static boolean containsCDATA(String message) {
		return message.contains(CDATA_BEGIN);
	}
	
	/**
	 * Delete the CDATA and all content
	 * 
	 * @param message
	 * 	Example:
	 * 	"Body":{
     *   "MsgFFmt":"<![CDATA[SN:M00001-0001 MT:MUF MSID:BRKR-190515-20:05:48 FROM:BC41127TO:BC41029TEXT:RE: 0509\nHC BC40940\nBC41027\nSNME:NEWMAN/G1:OLDSON/G2:MIKE/DOB:19900214\n\n2019051520054820190515200548]]>"
     *	}
	 * @return
	 * 	Example:
	 * 	"Body":{
     *   "MsgFFmt":""
     *	}
	 * 
	 */
	public static String deleteCDATA(String message) {
		int beginIndex = message.indexOf(CDATA_BEGIN);
		int endIndex = message.indexOf(CDATA_END);
		return (beginIndex != -1 && endIndex != -1)
				? message.substring(0, beginIndex) + message.substring(endIndex + CDATA_END.length(), message.length())
				: message;
	}

}

