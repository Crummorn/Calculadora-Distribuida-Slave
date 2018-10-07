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

				if (!x.getError().equals(null)) {
					out.writeObject(x);

					System.out.println(in.readObject().toString());

					out.writeObject("fim");

				} else {
					System.out.println(x.getError());
				}

				disconectar();
			}

		} catch (IOException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		} finally {
			scan.close();

		}
	}

	public static Operacao leitura(Scanner scan) {

		double valor1 = 0, valor2 = 0;
		String error = null;

		System.out.println("\nInforme o operador: ");
		String operador = scan.next();

		try {

			System.out.println("Informe o primeiro valor: ");
			valor1 = Double.parseDouble(scan.next());

			System.out.println("Informe o operador: ");
			valor2 = Double.parseDouble(scan.next());

		} catch (NumberFormatException e) {
			System.out.println("Valor Invalido, Tente Novamente!");

		} catch (Exception e) {
			System.err.println(e);

		}

		return new Operacao(operador, valor1, valor2, error);
	}

	public static boolean validator(String op) {
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
			x = true;
			break;
		default:
			x = false;
			break;
		}

		return x;
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