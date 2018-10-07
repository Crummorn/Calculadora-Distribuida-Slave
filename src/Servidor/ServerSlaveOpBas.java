package Servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Modelo.Operacao;

public class ServerSlaveOpBas extends Thread {
	private static ServerSocket ss;
	private Socket s;
	private final static int PORT = 10010;

	public ServerSlaveOpBas(Socket s) {
		this.s = s;
	}

	public void run() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());

			Object x = null;

			x = in.readObject();

			if (x instanceof Operacao) {
				x = funcoes((Operacao) x);

				out.writeObject(x);

				System.out.println(x.toString());
			}

		} catch (IOException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		}
	}

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
			x.setResultado(9999999);
			break;
		}

		return x;
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
				System.out.println("\nRequisição Aceita");
				System.out.println("HOSTNAME = " + conexao.getInetAddress().getHostName());
				System.out.println("HOST ADDRESS = " + conexao.getInetAddress().getHostAddress());
				System.out.println("PORTA LOCAL = " + conexao.getLocalPort());
				System.out.println("PORTA DE CONEXAO = " + conexao.getPort());

				new ServerSlaveOpBas(conexao).start();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}