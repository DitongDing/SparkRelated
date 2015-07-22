package utils;

public class SSHTest {
	public static void main(String[] args) {
		SSH ssh = new SSH("zengal@@9.112.234.59:22:DDT/TPCDS/Data/");
		System.out.println(ssh.password);
	}
}
