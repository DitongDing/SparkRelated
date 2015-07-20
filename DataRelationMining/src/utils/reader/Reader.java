package utils.reader;

public abstract class Reader {
	protected String base;
	
	public Reader(String base) {
		this.base = base;
	}
	
	public abstract void open(String filename) throws Exception;
	public abstract String readLine() throws Exception;
	public abstract void close() throws Exception;
	public abstract void finalize() throws Exception;
}
