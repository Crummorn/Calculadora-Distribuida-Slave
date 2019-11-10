package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import modelo.Operacao;

public class ServidorSlaveOpBas extends Thread {
	private Socket socket;
	private static ServerSocket serverSocket;
	private final static int PORT = 10010;

	public ServidorSlaveOpBas(Socket s) {
		this.socket = s;
	}

	public void run() {
		try {
			ObjectOutputStream outMaster = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream inMaster = new ObjectInputStream(socket.getInputStream());

			Operacao x = null;

			x = (Operacao) inMaster.readObject();

			x = funcoes(x);

			outMaster.writeObject(x);

			System.out.println(x.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		System.out.println("Server Slave de operações basicas rodando na porta = " + serverSocket.getLocalPort());

		while (true) {
			try {
				Socket conexao = serverSocket.accept();

				System.out.println("\n======================================");
				System.out.println("\nHOSTNAME = " + conexao.getInetAddress().getHostName());
				System.out.println("HOST ADDRESS = " + conexao.getInetAddress().getHostAddress());
				System.out.println("PORTA LOCAL = " + conexao.getLocalPort());
				System.out.println("PORTA DE CONEXAO = " + conexao.getPort());

				new ServidorSlaveOpBas(conexao).start();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/*
	 * Baseado na operação desejada, faz a conta e adiciona ao resultado
	 */
	public Operacao funcoes(Operacao x) {
		switch (x.getOperacao().toLowerCase()) {
		case "+":
		case "som":
			x.setResultado(x.getValor1() + x.getValor2());
			break;
		case "-":
		case "sub":
			x.setResultado(x.getValor1() - x.getValor2());
			break;
		case "/":
		case "div":
			x.setResultado(x.getValor1() / x.getValor2());
			break;
		case "*":
		case "mul":
			x.setResultado(x.getValor1() * x.getValor2());
			break;
		default:
			x.setResultado(99999999);
			break;
		}

		return x;
	}

}