package spark.ibm.zeppelin.util.websocket.output;

public class WebsocketOutput {
	protected String noteID;
	protected String paragraphID;

	public WebsocketOutput(String noteID, String paragraphID) {
		super();
		this.noteID = noteID;
		this.paragraphID = paragraphID;
	}

	public String getNoteID() {
		return noteID;
	}

	public String getParagraphID() {
		return paragraphID;
	}
}
