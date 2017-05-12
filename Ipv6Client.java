import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Ipv6Client {
	static Socket socket = null;
	static InputStream in = null;
	static OutputStream out = null;
	static byte[] magicNumber = new byte[4];
	static byte[] ipPacket = new byte[40 + 4096];

	public static void main(String[] args) throws Exception {
		socket = new Socket("codebank.xyz", 38004);
		in = socket.getInputStream();
		out = socket.getOutputStream();

		ipPacket[0] = (byte) 0x60; // version 0101 0000
		ipPacket[6] = (byte) 0x11; // next Header = udp
		ipPacket[7] = (byte) 20; // Hoplimit = 20

		
		//server ip address
		byte[] ip = socket.getInetAddress().getAddress();
		ipPacket[34] = (byte) 0xff;
		ipPacket[35] = (byte) 0xff;
		ipPacket[36] = ip[0];
		ipPacket[37] = ip[1];
		ipPacket[38] = ip[2];
		ipPacket[39] = ip[3];
		
		ip = InetAddress.getLocalHost().getAddress();
		ipPacket[18] = (byte) 0xff;
		ipPacket[19] = (byte) 0xff;
		ipPacket[20] = ip[0];
		ipPacket[21] = ip[1];
		ipPacket[22] = ip[2];
		ipPacket[23] = ip[3];

		for (int i = 2; i <= 4096; i *= 2) {
			ipPacket[4] = (byte) ((i) >> 8); //total length upper
			ipPacket[5] = (byte) ((i) & 0xff); //total length lower
			
			System.out.println("Sending packet length: " + i);
			out.write(ipPacket, 0, 40 + i); //send the bytes
			in.read(magicNumber);
			System.out.printf("Server Response: %x%x%x%x\n",magicNumber[0],magicNumber[1],magicNumber[2],magicNumber[3]); //read the response
			System.out.println();
		}
	}
}
