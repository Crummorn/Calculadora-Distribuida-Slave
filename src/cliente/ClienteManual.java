package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import modelo.Operacao;

public class ClienteManual {
	private static Socket socket;
	private static ObjectInputStream inMaster;
	private static ObjectOutputStream outMaster;
	
	private static Scanner scan = new Scanner(System.in);
	private static Operacao operacao = new Operacao();
	private static boolean continuar = true;
	
	public static void main(String[] args) {

		System.out.println("Cliente Inicializado!");

		try {
			while (continuar) {
				conectarMaster(10000);
				
				lerOperacao();
				
				if (operacao.getError().equals("")) {
					outMaster.writeObject(operacao);
					
					System.out.println(inMaster.readObject().toString());
				} else {
					System.out.println(operacao.getError());
					operacao.setError("");
				}

				desejaSair();
				
				desconectar();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		scan.close();
	}

	/*
	 * Faz a leitura de atributos e Retorna uma operação
	 */
	public static void lerOperacao() {
		System.out.println("som + | sub - | mul * | div / | pot # | por % | sqr $");
		System.out.println("\nInforme o operador: ");
		
		String operador = scan.next();

		// Valida se a operação selecionada é existente no sistema
		if (validarOperacao(operador)) {
			operacao.setOperacao(operador);

			try {

				// Testa se a operação é uma potenciação para ler somente 1 numero
				if (operador.equals("sqr") || operador.equals("$")) {
					System.out.println("Informe o valor: ");
					operacao.setValor1(Double.parseDouble(scan.next()));
				} else {
					System.out.println("Informe o primeiro valor: ");
					operacao.setValor1(Double.parseDouble(scan.next()));
					System.out.println("Informe o segundo valor: ");
					operacao.setValor2(Double.parseDouble(scan.next()));
				}

			} catch (NumberFormatException e) {
				operacao.setError("\nValor Invalido, Tente Novamente!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			operacao.setError("\nOperação Invalida, Tente Novamente!");
		}
	}

	/*
	 * Valida se uma operação é valida
	 */
	public static boolean validarOperacao(String op) {
		boolean retorno = true;

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
			retorno = true;
			break;
		default:
			retorno = false;
			break;
		}

		return retorno;
	}

	/*
	 * Testa se o usuario deseja sair da aplicação
	 */
	private static void desejaSair() {
		String sair = "";

		do {
			System.out.println("Deseja realizar outra operação? S/N");
			sair = scan.next();

			if (sair.equalsIgnoreCase("N"))
				continuar = false;
		} while (!sair.equalsIgnoreCase("S") && !sair.equalsIgnoreCase("N"));
	}

	/*
	 * Conecta o socket na porta do servidor master e cria o input/output
	 */
	public static void conectarMaster(int porta) throws UnknownHostException, IOException {
		System.out.println("\nIniciando conexão com o servidor. PORTA: " + porta);

		socket = new Socket("localhost", porta);

		inMaster = new ObjectInputStream(socket.getInputStream());
		outMaster = new ObjectOutputStream(socket.getOutputStream());
	}

	/*
	 * Desconecta socket, input e output
	 */
	private static void desconectar() throws IOException {
		if (inMaster != null)
			inMaster.close();

		if (outMaster != null)
			outMaster.close();

		if (socket != null && socket.isConnected())
			socket.close();
	}

}