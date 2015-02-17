package StaxQueryParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/******************************************************************************
 * CLASSNAME: QueryStaXParserImpl <br>
 * <p> 
 * 	One of the features I like about Hibernate is we can separate the queries from the java codes.
 *  This implementation is to simulate this functionality if the application does not use Hibernate.
 * </p>
 * @version: 1.0, Feb. 8, 2014 <br>
 *****************************************************************************/

public class QueryStaXParserImpl {
	// This can be moved to a property file.
	private static final String FILE_NAME = "C:\\Users\\cindy.liu\\git\\LocalGitRepository\\JavaTest\\officialquery.xml";
	// Name of Elements in the xml file
	private static final String SQL_TAG = "sql";
	private static volatile Map<String, String> sqlMap = null;
	private static volatile long lastModifiedTime; 
	
	static {
		sqlMap = new HashMap<String, String>();
		parseXML(FILE_NAME);
	}
	
	public static Map<String, String> getSqlMap(String fileName) {
		File file = new File(fileName);
		// If the xml file is updated, update the MAP using the current one.
		if(file.lastModified() != lastModifiedTime) 
			parseXML(fileName);
		
		return sqlMap;
	}

	// Stax parser for query.xml
	public static void parseXML(String fileName) {
		File file = new File(fileName);
		lastModifiedTime = file.lastModified(); 
		
		Map<String, String> newSqlMap = new HashMap<String, String>();
		try {
			// First, create a new XMLInputFactory
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// Setup a new eventReader
			XMLStreamReader reader = inputFactory.createXMLStreamReader(new FileInputStream(fileName));
		
			// read the XML document
			String tagContent = null, name = null, query = null;
			while(reader.hasNext()){
				int event = reader.next();
				switch(event){
					case XMLStreamConstants.START_ELEMENT:
						if (SQL_TAG.equals(reader.getLocalName()))
							name = reader.getAttributeValue(0); // Assume each sql element only has one attribute, name of the query
						break;
	
					case XMLStreamConstants.CHARACTERS:
					case XMLStreamConstants.CDATA:
						String temp = reader.getText().trim();
						if(temp.length() > 0)
							tagContent = reader.getText();
						break;
						
					case XMLStreamConstants.END_ELEMENT:
						if (SQL_TAG.equals(reader.getLocalName())) {
							query = tagContent;
							newSqlMap.put(name, query);
						}
						tagContent = "";
						break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} 
		
		synchronized (sqlMap) {
			sqlMap = Collections.unmodifiableMap(newSqlMap);
		}
	}

} 