package Cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import Modelo.Operacao;

public class ClientManual {

	private static Socket s;

	private static ObjectInputStream in;
	private static ObjectOutputStream out;

	public static void main(String[] args) {

		System.out.println("Iniciando Cliente");

		Scanner scan = new Scanner(System.in);

		try {
			for (int i = 0; i < 8; i++) {
				conectar(10000);

				Operacao x = leitura(scan);

				if (x.getError().equals("")) {
					out.writeObject(x);

					System.out.println(in.readObject().toString());

					out.writeObject("fim");

				} else {
					System.out.println(x.getError());
				}

				desconectar();
			}

		} catch (IOException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		} finally {
			scan.close();

		}
	}

	/*
	 * Faz a leitura de atributos e cria um objeto Operação
	 */
	public static Operacao leitura(Scanner scan) {

		double valor1 = 0, valor2 = 0;
		String error = "";

		System.out.println("\nInforme o operador: ");
		String operador = scan.next();

		// Valida se a operação selecionada é existente no sistema
		if (validarOperacao(operador)) {
			try {

				// Testa se a operação é uma potenciação para ler somente 1 numero
				if (operador.equals("pot") || operador.equals("#")) {
					System.out.println("Informe o valor: ");
					valor1 = Double.parseDouble(scan.next());

				} else {
					System.out.println("Informe o primeiro valor: ");
					valor1 = Double.parseDouble(scan.next());

					System.out.println("Informe o segundo valor: ");
					valor2 = Double.parseDouble(scan.next());
					
				}

			} catch (NumberFormatException e) {
				error += "\nValor Invalido, Tente Novamente!";

			} catch (Exception e) {
				System.err.println(e);

			}
		} else {
			error += "\nOperação Invalida, Tente Novamente!";

		}

		return new Operacao(operador, valor1, valor2, error);
	}

	/*
	 * Valida se uma operação é valida
	 */
	public static boolean validarOperacao(String op) {
		boolean x;

		switch (op) {
		case "+":
		case "som":
		case "-":
		case "sub":
		case "*":
		case "mul":
		case "/":
		case "div":
		case "#":
		case "pot":
		case "%":
		case "por":
		case "$":
		case "sqr":
			x = true;
			break;
		default:
			x = false;
			break;
		}

		return x;
	}

	/*
	 * Conecta o socket na porta do servidor master e cria o input/output
	 */
	public static void conectar(int porta) throws UnknownHostException, IOException {

		System.out.println("\nIniciando conexão com o servidor. PORTA: " + porta);

		s = new Socket("localhost", porta);

		in = new ObjectInputStream(s.getInputStream());
		out = new ObjectOutputStream(s.getOutputStream());
	}

	/*
	 * Desconecta socket, input e output
	 */
	private static void desconectar() throws IOException {
		if (in != null)
			in.close();

		if (out != null)
			out.close();

		if (s != null && s.isConnected())
			s.close();

	}

}