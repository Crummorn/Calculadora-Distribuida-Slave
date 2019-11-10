package modelo;

import java.io.Serializable;

public class Operacao implements Serializable {

	private static final long serialVersionUID = 940075609663291305L;
	
	private String operacao;
	private double valor1;
	private double valor2;
	private double resultado;
	private String error = "";

	public Operacao() {
		
	}
	
	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	public double getValor1() {
		return valor1;
	}

	public void setValor1(double valor1) {
		this.valor1 = valor1;
	}

	public double getValor2() {
		return valor2;
	}

	public void setValor2(double valor2) {
		this.valor2 = valor2;
	}

	public double getResultado() {
		return resultado;
	}

	public void setResultado(double resultado) {
		this.resultado = resultado;
	}
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String toString() {
		return "\nOperacao: " + this.operacao + "\nValor 1: " + this.valor1 + "\nValor 2: " + this.valor2
				+ "\nResultado: " + this.resultado;
	}
}