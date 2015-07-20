package utils;

import utils.reader.*;
import utils.writer.*;

// -usage: java -DHADOOP_USER_NAME=root -jar xxx.jar <src> <dst>
// src = l <dir for .dat files> / r user@password@host:port:dir / r hdfs://host/dir
// dst = l <dir for .csv output> / r hdfs://host/dir
// all dir route should end with '/'
public class PreProcess {
	public static void main(String[] args) throws Exception {
		if (args.length != 4) {
			System.out.println("args error");
			System.out.println("-usage: java -DHADOOP_USER_NAME=root -jar xxx.jar <src> <dst>\n"
					+ "src = l <dir for .dat files> / r user@password@host:dir\n" + "dst = l <dir for .csv output> / r hdfs://host/dir");
			System.exit(0);
		}

		String srcType = args[0];
		String src = args[1];
		String dstType = args[2];
		String dst = args[3];

		if (src.charAt(src.length() - 1) != '/' || dst.charAt(dst.length() - 1) != '/') {
			System.out.println("all dir route should end with '/'");
			System.exit(0);
		}

		Reader reader = parseSrc(srcType, src);
		Writer writer = parseDst(dstType, dst);

		PreProcess pp = new PreProcess();

		for (String[] filenameAndHead : Constant.FILENAME_AND_HEAD)
			pp.preProcess(reader, writer, filenameAndHead[0], filenameAndHead[1]);

		reader.finalize();
		writer.finalize();
	}

	public void preProcess(Reader reader, Writer writer, String filename, String head) throws Exception {
		int count = count(head, ',');
		reader.open(filename);
		writer.open(filename + ".csv");
		writer.println(head);

		String line = reader.readLine();
		while (line != null) {
			if (count != count(line, '|')) {
				System.out.println(filename + " error");
				break;
			}
			char[] chars = line.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if (chars[i] == '|')
					chars[i] = ',';
				else if (chars[i] == ',')
					chars[i] = '|';
			}
			writer.println(new String(chars));
			line = reader.readLine();
		}
		reader.close();
		writer.close();

		System.out.println(filename);
	}

	public int count(String line, char c) {
		int count = 0;
		for (char ctmp : line.toCharArray())
			if (ctmp == c)
				count++;
		return count;
	}

	public static Reader parseSrc(String srcType, String src) throws Exception {
		Reader reader = null;
		if (srcType.equals("l")) {
			reader = new LocalReader(src);
		} else if (srcType.startsWith("hdfs://")) {
			HDFS hdfs = new HDFS(src);
			reader = new HDFSReader(hdfs.base, hdfs.host);
		} else {
			SSH ssh = new SSH(src);
			reader = new SSHReader(ssh.base, ssh.host, ssh.user, ssh.password, ssh.port);
		}
		return reader;
	}

	public static Writer parseDst(String dstType, String dst) throws Exception {
		Writer writer = null;
		if (dstType.equals("l")) {
			writer = new LocalWriter(dst);
		} else {
			HDFS hdfs = new HDFS(dst);
			writer = new HDFSWriter(hdfs.base, hdfs.host);
		}
		return writer;
	}
	
	
}

// Reader reader = new LocalReader("Data/");
// Reader reader = new SSHReader("DDT/Linux/Data/", "9.112.234.69", "zengal", "1qaz2wsx", 22);
// Writer writer = new LocalWriter("input/");
// Writer writer = new HDFSWriter("/user/root/DDT/TPC/", "hdfs://9.112.234.69:9000");

// With MappingRule file
// BufferedReader br = new BufferedReader(new FileReader("MappingRule"));
// String line = br.readLine();
// while (line != null) {
// String[] filenameAndHead = line.split("\t");
// pp.preProcess(reader, writer, filenameAndHead[0], filenameAndHead[1]);
// line = br.readLine();
// }
// br.close();