package utils.reader;

import java.io.BufferedReader;
import java.io.FileReader;

public class LocalReader extends Reader {
	private BufferedReader br;
	
	public LocalReader(String base) {
		super(base);
	}

	public void open(String filename) throws Exception {
		br = new BufferedReader(new FileReader(base+filename));
	}

	public String readLine() throws Exception {
		return br.readLine();
	}

	public void close() throws Exception {
		br.close();
	}

	@Override
	public void finalize() throws Exception {
		
	}
}
