package utils.writer;

public abstract class Writer {
	protected String base;
	
	public Writer(String base) {
		this.base = base;
	}
	
	public abstract void open(String filename) throws Exception;
	public abstract void println(String line) throws Exception;
	public abstract void close() throws Exception;
	public abstract void finalize() throws Exception;
}
