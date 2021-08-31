package com.ufcg.psoft.mercadofacil.model.Cliente;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.ufcg.psoft.mercadofacil.DTO.ClienteDTO;
import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.Compra;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO")
public abstract class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long CPF;

	private String nome;

	private Integer idade;

	private String endereco;

	@OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL)
	private Carrinho carrinho;

	@OneToMany
	private List<Compra> compras;

	public Cliente() {
		this.carrinho = new Carrinho();
		this.compras = new ArrayList<Compra>();
	}

	public Cliente(Long cpf, String nome, Integer idade, String endereco) {
		this();
		this.CPF = cpf;
		this.nome = nome;
		this.idade = idade;
		this.endereco = endereco;
	}

	public Cliente(ClienteDTO clienteDTO) {
		this();
		this.CPF = clienteDTO.getCPF();
		this.nome = clienteDTO.getNome();
		this.idade = clienteDTO.getIdade();
		this.endereco = clienteDTO.getEndereco();
	}

	public Cliente(Cliente novoPerfil) {

		this.id = novoPerfil.getId();
		this.CPF = novoPerfil.getCpf();
		this.nome = novoPerfil.getNome();
		this.idade = novoPerfil.getIdade();
		this.endereco = novoPerfil.getEndereco();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCpf() {
		return CPF;
	}

	public void setCpf(Long cpf) {
		this.CPF = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Carrinho getCarrinho() {
		return this.carrinho;
	}

	public void setCarrinho(Carrinho carrinho) {
		this.carrinho = carrinho;
	}

	public List<Compra> adicionaCompra(Compra compra) {
		this.compras.add(compra);
		return this.compras;
	}

	public List<Compra> getCompras() {
		return this.compras;
	}

	public void setCompras(List<Compra> compras) {
		this.compras = compras;
	}

	private String formataCompraSimples(Compra compra) {
		return compra.getId() + " - " + compra.getData() + " - " + compra.getProdutos().toString() + " - "
				+ compra.getValor();
	}

	private String formataCompraDetalhada(Compra compra) {
		return compra.getId() + " - " + compra.getData() + " - " + compra.getProdutos() + " - " + compra.getValor()
				+ " - " + compra.getPagamento().toString();

	}

	public List<String> compraSimples(List<Compra> compras) {
		List<String> simples = new ArrayList<>();
		for (Compra compraAtual : compras)
			simples.add(formataCompraSimples(compraAtual));

		return simples;
	}

	public List<String> compraDetalhada(Compra compra) {
		List<String> detalhada = new ArrayList<>();
		detalhada.add(formataCompraDetalhada(compra));
		return detalhada;
	}

	public abstract double descontoCompras(double valor, long quantidade);

}
