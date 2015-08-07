package spark.ibm.zeppelin.util.websocket.output;

public class Message extends WebsocketOutput {
	protected String message;

	public Message(String noteID, String paragraphID, String message) {
		super(noteID, paragraphID);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
