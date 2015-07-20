package utils.reader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SSHReader extends Reader {
	private Session session;
	private OutputStream out;
	private BufferedReader in;

	private byte[] sig = { 0 };

	public SSHReader(String base, String host, String user, String password,
			Integer port) throws Exception {
		super(base);
		JSch jsch = new JSch();
		session = jsch.getSession(user, host, port);
		session.setPassword(password);
		session.setConfig("StrictHostKeyChecking", "no");
		session.connect();
	}

	@Override
	public void open(String filename) throws Exception {
		// exec 'scp -f rfile' remotely
		String command = "scp -f " + base + filename;
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand(command);

		out = channel.getOutputStream();
		in = new BufferedReader(new InputStreamReader(channel.getInputStream()));

		channel.connect();

		String firstLine = readLine();
		if (firstLine.charAt(0) != 'C')
			throw new Exception("No such file");
	}

	@Override
	public String readLine() throws Exception {
		out.write(sig);
		out.flush();
		String line = in.readLine();
		if(line.charAt(0) == 0)
			return null;
		return line;
	}

	@Override
	public void close() throws Exception {
		out.close();
		in.close();
	}
	
	@Override
	public void finalize() throws Exception {
		session.disconnect();
	}
}
