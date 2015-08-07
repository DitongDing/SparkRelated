package spark.ibm.zeppelin.util.bean.table;

public class RelationTurple {
	protected String tableName1;
	protected String columneName1;
	protected String tableName2;
	protected String columneName2;

	public RelationTurple(String tableName1, String columneName1, String tableName2, String columneName2) {
		this.tableName1 = tableName1;
		this.columneName1 = columneName1;
		this.tableName2 = tableName2;
		this.columneName2 = columneName2;
	}

	public String getTableName1() {
		return tableName1;
	}

	public String getColumneName1() {
		return columneName1;
	}

	public String getTableName2() {
		return tableName2;
	}

	public String getColumneName2() {
		return columneName2;
	}
}
