package utils;

import java.util.StringTokenizer;

public class SSH {
	public String base;
	public String host;
	public String user;
	public String password;
	public Integer port;

	public SSH(String uri) {
		StringTokenizer st = new StringTokenizer(uri, "@:");
		if (st.countTokens() != 5) {
			System.out.println("remote ssh source error: format is user@password@host:port:dir");
			System.exit(0);
		}
		user = st.nextToken();
		password = st.nextToken();
		host = st.nextToken();
		port = Integer.parseInt(st.nextToken());
		base = st.nextToken();
	}
}
