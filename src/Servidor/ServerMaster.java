package Servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Modelo.Operacao;

public class ServerMaster extends Thread {
	private static ServerSocket ss;
	private Socket s;
	private Socket slave;
	private final static int PORT = 10000;

	public ServerMaster(Socket s) {
		this.s = s;
	}

	public void run() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());

			Object x = null;

			do {
				x = in.readObject();

				if (x instanceof Operacao) {
					
					slave = new Socket("localhost", acharServidor((Operacao) x));

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

	/*
	 * Baseado na operação, seleciona a porta do servidor
	 */
	public int acharServidor (Operacao x) {
		int porta;
		
		switch (x.getOperacao()) {
		case "+":
		case "som":
		case "-":
		case "sub":
		case "*":
		case "mul":
		case "/":
		case "div":
			porta = 10010;
			break;
		case "#":
		case "pot":
		case "%":
		case "por":
		case "$":
		case "sqr":
			porta = 10020;
			break;
		default:
			porta = -1;
			break;
		}
				
		return porta;
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

				System.out.println("\n======================================");
				System.out.println("\nCliente Aceito");
				System.out.println("HOSTNAME = " + conexao.getInetAddress().getHostName());
				System.out.println("HOST ADDRESS = " + conexao.getInetAddress().getHostAddress());
				System.out.println("PORTA LOCAL = " + conexao.getLocalPort());
				System.out.println("PORTA DE CONEXAO = " + conexao.getPort());

				new ServerMaster(conexao).start();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}