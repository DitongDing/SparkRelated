package spark.ibm.zeppelin.util;

import org.apache.zeppelin.interpreter.InterpreterContext;

import spark.ibm.zeppelin.util.bean.table.RelationTurple;
import spark.ibm.zeppelin.util.websocket.WebsocketServer;
import spark.ibm.zeppelin.util.websocket.output.RelationshipOutput;

public class RelationshipDiscovery {
	public static void discorery(String text, InterpreterContext interpreterContext, WebsocketServer websocket) {
		String noteID = interpreterContext.getNoteId();
		String paragraphID = interpreterContext.getParagraphId();

		// TODO do real search.
		try {
			for (int i = 0; i < 2; i++) {
				Thread.sleep(1000);
				String tableName1 = "rel_" + i + "_table_" + 1;
				String columnName1 = "rel_" + i + "_column_" + 1;
				String tableName2 = "rel_" + i + "_table_" + 2;
				String columnName2 = "rel_" + i + "_column_" + 2;
				RelationshipOutput output = new RelationshipOutput(noteID, paragraphID, new RelationTurple(tableName1, columnName1, tableName2,
						columnName2));
				websocket.broadcast(ComUtils.toJson(output));
			}
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
