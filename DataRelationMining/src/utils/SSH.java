package utils;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SSH {
	public String base;
	public String host;
	public String user;
	public String password;
	public Integer port;

	public SSH(String uri) {
		String pattern = ".+@.*@.+:\\d+:.+";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(uri);
		if (!m.matches()) {
			System.out.println("remote ssh source error: format is user@password@host:port:dir. password can be null");
			System.exit(0);
		}

		StringTokenizer st = new StringTokenizer(uri, "@:");
		user = st.nextToken();
		if (st.countTokens() == 4) {
			password = st.nextToken();
		}
		host = st.nextToken();
		port = Integer.parseInt(st.nextToken());
		base = st.nextToken();
	}
}
