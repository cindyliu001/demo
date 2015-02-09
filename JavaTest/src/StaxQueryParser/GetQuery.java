package StaxQueryParser;


// TODO: rewrite testing using JUnit including the validation with a xsd file 
public class GetQuery {
	private static final String personnelID = "12345";

	public static void main(String[] args) {
		String query = QueryStaXParserImpl.getSqlMap().get("quickInvoice");
		QueryBuilder qb = new QueryBuilder(query);
		
		qb = qb.replaceParam("customerId", "12345")
				.replaceParam("days", "365")
				.replaceParam("inventoryGroup", "InventoryGroup")
				.replaceParam("hub", "HUB")
				.replaceParam("opsEntityId", "OpsEntityId")
				.replaceParam("companyId", "CompanyID")
				.replaceParam("personnelId", personnelID);
		
		System.out.print(qb.getQuery());
		
		qb.setQuery(query);
		
		qb = qb.convertcriterion("customerId", "12345")
				.convertcriterion("days", "365")
				.convertcriterion("inventoryGroup", "InventoryGroup")
				.convertcriterion("hub", "HUB")
				.convertcriterion("opsEntityId", "OpsEntityId")
				.convertcriterion("companyId", "CompanyID")
				.convertcriterion("personnelId", personnelID);
		
		System.out.print(qb.getQuery());

	}

}
