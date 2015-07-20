package utils.writer;

import java.io.PrintWriter;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;

public class HDFSWriter extends Writer {
	private PrintWriter pw;
	private DistributedFileSystem hdfs = null;

	public HDFSWriter(String base, String uri) throws Exception {
		super(base);
		hdfs = (DistributedFileSystem) (FileSystem.get(URI.create(uri), new Configuration()));
	}

	@Override
	public void open(String filename) throws Exception {
		pw = new PrintWriter(hdfs.create(new Path(base + filename)));
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
		hdfs.close();
	}
}
