package Servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Model.SlaveOpBas;

public class Server extends Thread {
	private static ServerSocket ss;
	private Socket s;
	private Socket slave;
	private final static int PORT = 10000;

	public Server(Socket s) {
		this.s = s;
	}

	public void run() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());

			Object x = null;

			do {
				x = in.readObject();

				if (x instanceof SlaveOpBas) {
					slave = new Socket("localhost", 10010);

					ObjectOutputStream outSlave = new ObjectOutputStream(slave.getOutputStream());
					ObjectInputStream inSlave = new ObjectInputStream(slave.getInputStream());

					outSlave.writeObject(x);

					out.writeObject(inSlave.readObject());

					slave.close();
					outSlave.close();
					inSlave.close();
				}
			} while (!(x instanceof String) && x != "fim");

			if (x instanceof String && x == "fim") {
				System.out.println(x);

				s.close();
				out.close();
				in.close();
			}

		} catch (IOException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		}
	}

	public static void main(String[] args) {
		try {
			ss = new ServerSocket(PORT);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		System.out.println("Server Socket rodando na porta = " + ss.getLocalPort());

		while (true) {
			try {
				Socket conexao = ss.accept();

				System.out.println("======================================");
				System.out.println("\nCliente Aceito");
				System.out.println("HOSTNAME = " + conexao.getInetAddress().getHostName());
				System.out.println("HOST ADDRESS = " + conexao.getInetAddress().getHostAddress());
				System.out.println("PORTA LOCAL = " + conexao.getLocalPort());
				System.out.println("PORTA DE CONEXAO = " + conexao.getPort());

				new Server(conexao).start();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}