package Servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Modelo.Operacao;

public class ServerSlaveOpCom extends Thread {
	private static ServerSocket ss;

	private Socket s;

	private final static int PORT = 10020;

	public ServerSlaveOpCom(Socket s) {
		this.s = s;
	}

	public void run() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());

			Object x = null;

			x = in.readObject();

			x = funcoes((Operacao) x);

			out.writeObject(x);

			System.out.println(x.toString());

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

		System.out.println("Server Slave de operações basicas rodando na porta = " + ss.getLocalPort());

		while (true) {
			try {
				Socket conexao = ss.accept();

				System.out.println("\n======================================");
				System.out.println("\nHOSTNAME = " + conexao.getInetAddress().getHostName());
				System.out.println("HOST ADDRESS = " + conexao.getInetAddress().getHostAddress());
				System.out.println("PORTA LOCAL = " + conexao.getLocalPort());
				System.out.println("PORTA DE CONEXAO = " + conexao.getPort());

				new ServerSlaveOpCom(conexao).start();

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
		case "#":
		case "pot":
			x.setResultado(Math.pow(x.getValor1(), x.getValor2()));
			break;
		case "%":
		case "por":
			x.setResultado((x.getValor1() * x.getValor2()) / 100);
			break;
		case "$":
		case "sqr":
			x.setResultado(Math.sqrt(x.getValor1()));
			break;
		default:
			x.setResultado(99999999);
			break;
		}

		return x;
	}

}