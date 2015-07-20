package utils.reader;

public class HDFSReaderTest {
	public static void main(String[] args) throws Exception {
		HDFSReader reader = new HDFSReader("/user/root/DDT/TPC/", "hdfs://9.112.234.69:9000");
		reader.open("call_center.dat.csv");
		String line = reader.readLine();
		while (line != null) {
			System.out.println(line);
			line = reader.readLine();
		}
		reader.close();
		reader.finalize();
	}
}