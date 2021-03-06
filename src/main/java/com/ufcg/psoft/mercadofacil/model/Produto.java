package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ufcg.psoft.mercadofacil.model.Produto;

@Entity
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private BigDecimal preco;

	private String codigoBarra;

	private String fabricante;

	private String categoria;

	private String descricao;

	private boolean isDisponivel;

	private int qtdEstoque;

	private boolean fragil;

	private boolean refrigeracao;

	private Produto() {
	}

	public Produto(String nome, String codigoBarra, String fabricante, BigDecimal preco, String nomeCategoria,
			String descricao) {

		this();

		this.nome = nome;
		this.preco = preco;
		this.codigoBarra = codigoBarra;
		this.fabricante = fabricante;
		this.categoria = nomeCategoria;
		this.descricao = descricao;

		this.isDisponivel = false;
		this.qtdEstoque = 0;
		this.fragil = false;
		this.refrigeracao = false;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void mudaFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public String getCodigoBarra() {
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public String getCategoria() {
		return this.categoria;
	}

	public void mudaCategoria(String categoria) {
		this.categoria = categoria;
	}

	public void tornaDisponivel() {
		this.isDisponivel = true;
	}

	public void tornaIndisponivel() {
		this.isDisponivel = false;
	}

	public boolean isDisponivel() {
		return this.isDisponivel;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void mudaDescricao(String novaDescricao) {
		this.descricao = novaDescricao;
	}

	public void adicionarEstoque(long quantidade) {
		this.qtdEstoque += quantidade;
	}

	public void removerEstoque(long quantidade) {
		this.qtdEstoque -= quantidade;
	}

	public long getEstoque() {
		return this.qtdEstoque;
	}

	public void setFragil(boolean fragil) {
		this.fragil = fragil;
	}

	public void setRefrigeracao(boolean refrigeracao) {
		this.refrigeracao = refrigeracao;
	}

	public boolean isFragil() {
		return this.fragil;
	}

	public boolean isRefrigeracao() {
		return this.refrigeracao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fabricante == null) ? 0 : fabricante.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		Produto other = (Produto) obj;

		if (fabricante == null)
			if (other.fabricante != null)
				return false;
			else if (!fabricante.equals(other.fabricante))
				return false;

		if (nome == null)
			if (other.nome != null)
				return false;
			else if (!nome.equals(other.nome))
				return false;

		return true;
	}

	public String toString() {
		return this.id + "-" + this.nome;
	}

}