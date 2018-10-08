package Servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import Modelo.Operacao;

public class ServerMaster extends Thread {
	private static ServerSocket ss;
	
	private final static int PORT = 10000;
		
	private Socket s;
	private static Socket slave;
		
	private static ObjectInputStream inSlave;
	private static ObjectOutputStream outSlave;

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
					
					conectarSlave (acharServidor((Operacao) x));
					
					outSlave.writeObject(x);

					out.writeObject(inSlave.readObject());

					desconectarSlave();
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

		System.out.println("Server Master rodando na porta = " + ss.getLocalPort());

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
	
	/*
	 * Conecta o socket na porta do servidor slave e cria o input/output
	 */
	public static void conectarSlave(int porta) throws UnknownHostException, IOException {
		System.out.println("\nIniciando conexão com o servidor slave. PORTA: " + porta);

		slave = new Socket("localhost", porta);

		inSlave = new ObjectInputStream(slave.getInputStream());
		outSlave = new ObjectOutputStream(slave.getOutputStream());
	}

	/*
	 * Desconecta socket, input e output
	 */
	private static void desconectarSlave() throws IOException {
		if (inSlave != null)
			inSlave.close();

		if (outSlave != null)
			outSlave.close();

		if (slave != null && slave.isConnected())
			slave.close();
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


}