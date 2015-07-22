package utils.writer;

public class HDFSWriterTest {
	public static void main(String[] args) throws Exception {
		HDFSWriter writer = new HDFSWriter("/user/root/DDT/TPC/", "hdfs://9.112.234.69:9000");
		writer.open("call_center.dat.csv");
		writer.println("");
		writer.close();
		writer.finalize();
	}
}