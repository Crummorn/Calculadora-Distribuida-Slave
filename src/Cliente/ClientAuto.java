package Cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import Modelo.Operacao;

public class ClientAuto {

	private static Socket s;

	private static ObjectInputStream in;
	private static ObjectOutputStream out;

	public static void main(String[] args) {
		try {
			for (int i = 0; i < 5000; i++) {

				conectar(10000);

				out.writeObject(new Operacao("Soma", 10, 5, true));

				System.out.println(in.readObject().toString());

				out.writeObject("fim");

				disconectar();
			}

		} catch (IOException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		}
	}

	public static void conectar(int porta) throws UnknownHostException, IOException {

		System.out.println("\nIniciando conexão com o servidor. PORTA: " + porta);

		s = new Socket("localhost", porta);

		in = new ObjectInputStream(s.getInputStream());
		out = new ObjectOutputStream(s.getOutputStream());
	}

	private static void disconectar() throws IOException {
		if (in != null)
			in.close();

		if (out != null)
			out.close();

		if (s != null && s.isConnected())
			s.close();

	}

}