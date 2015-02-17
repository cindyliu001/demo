package StaxQueryParser;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Test;

public class StaxQueryParser {
	private static final String XML_FILE_1 = "C:\\Users\\cindy.liu\\git\\LocalGitRepository\\JavaTest\\querybad.xml";
	private static final String XML_FILE_2 = "C:\\Users\\cindy.liu\\git\\LocalGitRepository\\JavaTest\\query.xml";
	private static final String XSD_FILE = "C:\\Users\\cindy.liu\\git\\LocalGitRepository\\JavaTest\\query.xsd";
	
	static boolean validateAgainstXSD(InputStream xml, InputStream xsd)
	{
	    try
	    {
	        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	        Schema schema = factory.newSchema(new StreamSource(xsd));
	        Validator validator = schema.newValidator();
	        validator.validate(new StreamSource(xml));
	        return true;
	    }
	    catch(Exception ex)
	    {
	//    	ex.printStackTrace();
	        return false;
	    }
	}
	

	@Test
	public void testXMLValidation() {
		try {
			assertFalse("Missing attribute for sql tag", validateAgainstXSD(new FileInputStream(XML_FILE_1), new FileInputStream(XSD_FILE)));
			assertTrue("Correct format", validateAgainstXSD(new FileInputStream(XML_FILE_2), new FileInputStream(XSD_FILE)));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found");
		}
	}
	
	@Test
	public void testReplaceParam() {
		String query = QueryStaXParserImpl.getSqlMap(XML_FILE_2).get("quickInvoice");
		QueryBuilder qb = new QueryBuilder(query);
	
		assertEquals("Replace paramerters", qb.replaceParam("customerId", "12345").getQuery().trim(), 
				"SELECT pr.customer_id FROM purchase_request pr WHERE invoice_at_shipping = 'Y' AND (pr.customer_id = 12345)");
		
		qb.setQuery(query);
		
		assertEquals("Replace paramerters", qb.replaceParam("customerId", null).getQuery().trim(), 
				"SELECT pr.customer_id FROM purchase_request pr WHERE invoice_at_shipping = 'Y' AND (pr.customer_id is null)");
	}
	
	
	@Test
	public void testConvertcriterion() {
		String query = QueryStaXParserImpl.getSqlMap(XML_FILE_2).get("quickInvoice");
		QueryBuilder qb = new QueryBuilder(query);

		assertEquals("Replace paramerters", qb.convertcriterion("customerId", "12345").getQuery().trim(), 
				"SELECT pr.customer_id FROM purchase_request pr WHERE invoice_at_shipping = 'Y' AND (pr.customer_id = 12345)");
		
		qb.setQuery(query);
		// Uppercase AND
		assertEquals("Replace paramerters", qb.convertcriterion("customerId", null).getQuery().trim(), 
				"SELECT pr.customer_id FROM purchase_request pr WHERE invoice_at_shipping = 'Y'");
		
		String query2 = QueryStaXParserImpl.getSqlMap(XML_FILE_2).get("Lowercase and");
		qb.setQuery(query2);
		// Lowercase and
		assertEquals("Replace paramerters", qb.convertcriterion("customerId", null).getQuery().trim(), 
				"SELECT pr.customer_id FROM purchase_request pr WHERE invoice_at_shipping = 'Y'");
		
		String query3 = QueryStaXParserImpl.getSqlMap(XML_FILE_2).get("Simple Criteria");
		qb.setQuery(query3);
		// Get rid of the criteria in the middle of where clause
		assertEquals("Replace paramerters", qb.convertcriterion("customerId", null).getQuery().trim(), 
				"SELECT pr.customer_id FROM purchase_request pr WHERE invoice_at_shipping = 'Y'  AND company_id = :companyId");
		
		String query4 = QueryStaXParserImpl.getSqlMap(XML_FILE_2).get("Complex Criteria");
		qb.setQuery(query4);
		// Get rid of the complex criteria in the middle of where clause
		assertEquals("Replace paramerters", qb.convertcriterion("customerId", null).getQuery().trim(), 
				"SELECT pr.customer_id FROM purchase_request pr WHERE invoice_at_shipping = 'Y'  AND company_id = :companyId");
	}

}
