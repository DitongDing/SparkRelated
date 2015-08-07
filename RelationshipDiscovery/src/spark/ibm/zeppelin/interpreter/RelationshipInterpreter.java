package spark.ibm.zeppelin.interpreter;

import java.util.List;
import java.util.Properties;

import org.apache.spark.SparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.apache.zeppelin.interpreter.LazyOpenInterpreter;
import org.apache.zeppelin.interpreter.WrappedInterpreter;
import org.apache.zeppelin.interpreter.InterpreterResult.Code;
import org.apache.zeppelin.scheduler.Scheduler;
import org.apache.zeppelin.scheduler.SchedulerFactory;
import org.apache.zeppelin.spark.SparkInterpreter;

import spark.ibm.zeppelin.util.ComUtils;
import spark.ibm.zeppelin.util.websocket.WebsocketServer;
import spark.ibm.zeppelin.util.websocket.output.Message;

public class RelationshipInterpreter extends Interpreter {
	private static WebsocketServer websocket = new WebsocketServer();

	static {
		Interpreter.register("relationship", "spark", RelationshipInterpreter.class.getName());
		websocket.start();
	}

	public RelationshipInterpreter(Properties property) {
		super(property);
	}

	@Override
	public void open() {
	}

	@Override
	public void close() {
	}

	@Override
	public InterpreterResult interpret(String st, InterpreterContext interpreterContext) {
		InterpreterResult result = null;

		String noteID = interpreterContext.getNoteId();
		String paragraphID = interpreterContext.getParagraphId();

//		@SuppressWarnings("unused")
//		SparkContext sparkc = getSparkInterpreter().getSparkContext();
//		SQLContext sqlc = getSparkInterpreter().getSQLContext();
//		DataFrame[] dfs = new DataFrame[6];
//		String[] names = sqlc.tableNames();
		String html = "";
//		for (int i = 0; i < names.length && i < 6; i++) {
//			dfs[i] = sqlc.table(names[i]);
//			html += "<p>" + names[i] + " " + dfs[i].schema() + "   </p>                     ";
//		}

		websocket.broadcast(ComUtils.toJson(new Message(noteID, paragraphID, "push 0")));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		websocket.broadcast(ComUtils.toJson(new Message(noteID, paragraphID, "push 1")));
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		result = new InterpreterResult(Code.SUCCESS, "%html " + html);

		return result;
	}

	@Override
	public void cancel(InterpreterContext context) {
	}

	@Override
	public FormType getFormType() {
		return FormType.SIMPLE;
	}

	@Override
	public int getProgress(InterpreterContext context) {
		return 0;
	}

	@Override
	public Scheduler getScheduler() {
		return SchedulerFactory.singleton().createOrGetParallelScheduler(RelationshipInterpreter.class.getName() + this.hashCode(), 5);
	}

	@Override
	public List<String> completion(String buf, int cursor) {
		return null;
	}

	private SparkInterpreter getSparkInterpreter() {
		for (Interpreter intp : getInterpreterGroup()) {
			if (intp.getClassName().equals(SparkInterpreter.class.getName())) {
				Interpreter p = intp;
				while (p instanceof WrappedInterpreter) {
					if (p instanceof LazyOpenInterpreter) {
						p.open();
					}
					p = ((WrappedInterpreter) p).getInnerInterpreter();
				}
				return (SparkInterpreter) p;
			}
		}
		return null;
	}
}