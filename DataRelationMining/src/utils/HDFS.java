package utils;

public class HDFS {
	public HDFS(String uri) {
		int count = 0;
		int index = 0;
		for (; index < uri.length(); index++)
			if (uri.charAt(index) == ':' && count++ == 1)
				break;
		for (; index < uri.length(); index++)
			if (uri.charAt(index) == '/')
				break;
		if (index == uri.length()) {
			System.out.println("remote hdfs uri error: format is hdfs://host/dir");
			System.exit(0);
		}
		base = uri.substring(index);
		host = uri.substring(0, index);
	}

	public String base;
	public String host;

}
