package utils.writer;

import java.io.PrintWriter;

public class LocalWriter extends Writer {
	private PrintWriter pw;

	public LocalWriter(String base) {
		super(base);
	}

	public void open(String filename) throws Exception {
		pw = new PrintWriter(base + filename);
	}

	@Override
	public void println(String line) throws Exception {
		pw.println(line);
	}

	@Override
	public void close() throws Exception {
		pw.close();
	}

	@Override
	public void finalize() throws Exception {

	}
}
