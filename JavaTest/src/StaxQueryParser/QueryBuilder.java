package StaxQueryParser;

import org.apache.commons.lang.StringUtils;

/******************************************************************************
 * CLASSNAME: QueryBuilder <br>
 * @version: 1.0, Feb 8, 2015 <br>
 *****************************************************************************/
public class QueryBuilder  {
	private String query;

	// constructor
	public QueryBuilder(String query) {
		this.query = query;
	}

	// This function is to replace the values of the variables. 
	public QueryBuilder replaceParam(String var, String value)  {
		if (!StringUtils.isBlank(var) && !StringUtils.isBlank(value)) 
			this.query = query.replace(":"+var, value);
		else if(!StringUtils.isBlank(var) && StringUtils.isBlank(value))
			this.query = query.replace(" = :"+var, " is null");
		else
			throw new RuntimeException("Not a valid variable");
		
		return this;
	}
	
	/*
	 *  This function is to create the query dynamically for better performance.
	 *  We get rid the unnecessary criteria in the where clause
	 *  
	 *  Requirement: the criteria need to be within the parentheses after "AND"
	 *  Result: "AND" with the (complex) criteria within the parentheses will be gone.
	 */
	public  QueryBuilder convertcriterion(String var, String value) {
		StringBuilder regex = new StringBuilder( "(?i)AND\\s+\\((.*):\\b").append(var).append("\\b(.*)\\)"); 
		if (!StringUtils.isBlank(var) && !StringUtils.isBlank(value)) 
			query = query.replace(":"+var, value);
		else if(!StringUtils.isBlank(var) && StringUtils.isBlank(value))
			query = query.replaceAll(regex.toString(), ""); 
		else
			throw new RuntimeException("Not a valid variable");
		
		return this;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return this.query;
	}
}