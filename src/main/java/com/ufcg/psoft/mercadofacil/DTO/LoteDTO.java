package com.ufcg.psoft.mercadofacil.DTO;

public class LoteDTO {
	
	private int numeroDeItens;
	private String validade;	
	
	public int getNumeroDeItens() {
		return this.numeroDeItens;
	}
	
	public void setNumeroDeItens(int numeroDeItens) {
		this.numeroDeItens = numeroDeItens;
	}
	
	public String getValidade() {
		return this.validade;
	}
	
	public void setValidade(String novaValidade) {
		this.validade = novaValidade;
	}
}
