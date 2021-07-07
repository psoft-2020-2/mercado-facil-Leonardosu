package com.ufcg.psoft.mercadofacil.service;

import java.util.List;
import java.util.Optional;

import com.ufcg.psoft.mercadofacil.DTO.LoteDTO;
import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Produto;

public interface LoteService {
	
	public Optional<Lote> getLoteById(long id);
	
	public List<Lote> listarLotes();

	public Lote criaLote(int numItens, Produto produto, String validade);
	
	public void salvarLote(Lote lote);
	
	public void removerLote(Lote lote);
	
	public Lote atualizaLote(LoteDTO loteDTO, Lote lote);
}
