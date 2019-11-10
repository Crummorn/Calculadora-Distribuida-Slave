package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import modelo.Operacao;

public class ClienteAutomatico {
	private static Socket socket;
	private static ObjectInputStream inMaster;
	private static ObjectOutputStream outMaster;
	
	public static void main(String[] args) {
		try {
			for (int i = 1; i < 1000; i++) {
				Operacao operacao = new Operacao();
				
				if ((i % 2) == 0) {
					operacao.setOperacao("som");
					operacao.setValor1(i);
					operacao.setValor2(i);
				} else {
					operacao.setOperacao("por");
					operacao.setValor1(i + i);
					operacao.setValor2(10);
				}
				
				conectarMaster(10000);
				outMaster.writeObject(operacao);
				System.out.println(inMaster.readObject().toString());
				desconectarMaster();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	private static void desconectarMaster() throws IOException {
		if (inMaster != null)
			inMaster.close();

		if (outMaster != null)
			outMaster.close();

		if (socket != null && socket.isConnected())
			socket.close();
	}
}