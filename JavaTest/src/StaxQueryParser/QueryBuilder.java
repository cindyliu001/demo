package StaxQueryParser;

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
		if (value != null) 
			this.query = query.replace(":"+var, value);
		else if(value == null)
			this.query = query.replace(":"+var, "null");
		
		return this;
	}
	
	/*
	 *  This function is to create the query dynamically for better performance.
	 *  We get rid the unnecessary criteria in the where clause
	 */
	public  QueryBuilder convertcriterion(String var, String value) {
		StringBuilder regex = new StringBuilder( "AND\\s+\\((.*):\\b").append(var).append("\\b(.*)\\)"); 
		if (value != null) 
			query = query.replace(":"+var, value);
		else if(value == null)
			query = query.replaceAll(regex.toString(), ""); 
		
		return this;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return this.query;
	}
}