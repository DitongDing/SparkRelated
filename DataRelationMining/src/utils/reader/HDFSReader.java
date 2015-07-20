package utils.reader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;

public class HDFSReader extends Reader {
	private BufferedReader br;
	private DistributedFileSystem hdfs = null;

	public HDFSReader(String base, String uri) throws Exception {
		super(base);
		hdfs = (DistributedFileSystem) (FileSystem.get(URI.create(uri), new Configuration()));
	}

	@Override
	public void open(String filename) throws Exception {
		br = new BufferedReader(new InputStreamReader(hdfs.open(new Path(base+filename))));
	}

	@Override
	public String readLine() throws Exception {
		return br.readLine();
	}

	@Override
	public void close() throws Exception {
		br.close();
	}

	@Override
	public void finalize() throws Exception {
		hdfs.close();
	}

}
