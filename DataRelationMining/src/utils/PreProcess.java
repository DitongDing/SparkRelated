package utils;

import java.io.BufferedReader;
import java.io.FileReader;

import utils.reader.*;
import utils.writer.*;

// -usage: java -DHADOOP_USER_NAME=root -jar xxx.jar <src> <dst> <file list file>
// src = l <dir for .dat files> / r user@password@host:port:dir / r hdfs://host/dir
// dst = l <dir for .csv output> / r hdfs://host/dir
// file list file = local file which contains file list
// all dir route should end with '/'
// There should be a 'files' file in the same location
public class PreProcess {
	public static void main(String[] args) {
		try {
			if (args.length != 5) {
				System.out.println("args error");
				System.out.println("-usage: java -DHADOOP_USER_NAME=root -jar xxx.jar <src> <dst> <file list file>\n"
						+ "src = l <dir for .dat files> / r user@password@host:dir / r hdfs://host/dir\n"
						+ "dst = l <dir for .csv output> / r hdfs://host/dir\n" + "file list file = local file which contains file list\n");
				System.exit(0);
			}

			String srcType = args[0];
			String src = args[1];
			String dstType = args[2];
			String dst = args[3];
			String filesList = args[4];

			if (src.charAt(src.length() - 1) != '/' || dst.charAt(dst.length() - 1) != '/') {
				System.out.println("all dir route should end with '/'");
				System.exit(0);
			}

			Reader reader = parseSrc(srcType, src);
			Writer writer = parseDst(dstType, dst);

			PreProcess pp = new PreProcess();

			BufferedReader br = new BufferedReader(new FileReader(filesList));
			String line = br.readLine();
			while (line != null) {
				String filename = line;
				String head = Constant.FILENAME_HEAD.get(filename);
				if (head == null) {
					System.out.println("'files' file error, '" + filename + "' shoud not be in the list");
					System.exit(0);
				}
				pp.preProcess(reader, writer, filename, head);
				line = br.readLine();
			}
			br.close();

			reader.finalize();
			writer.finalize();

			System.out.println("===========\n\nFinished\n\n===========");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void preProcess(Reader reader, Writer writer, String filename, String head) throws Exception {
		System.out.println("===========\n" + filename + " started");

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

		System.out.println(filename + " finished");
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

// for (Integer i = start; i < end; i++) {
// String[] filenameAndHead = Constant.FILENAME_AND_HEAD[i];
// pp.preProcess(reader, writer, filenameAndHead[0], filenameAndHead[1]);
// }