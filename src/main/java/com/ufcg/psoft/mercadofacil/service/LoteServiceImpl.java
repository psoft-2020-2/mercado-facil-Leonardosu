package com.ufcg.psoft.mercadofacil.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.mercadofacil.DTO.LoteDTO;
import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.LoteRepository;

@Service
public class LoteServiceImpl implements LoteService {
	
	@Autowired
	private LoteRepository loteRepository;
	
	public Optional<Lote> getLoteById(long id){
		return loteRepository.findById(id);
	}
	
	public List<Lote> listarLotes() {
		return loteRepository.findAll();
	}

	public void salvarLote(Lote lote) {
		loteRepository.save(lote);		
	}

	public Lote criaLote(int numItens, Produto produto, String validade) {
		Lote lote = new Lote(produto, numItens, validade);
		return lote;
	}
	
	public void removeLodeCadastrado(Lote lote) {
		loteRepository.delete(lote);
	}

	public Lote atualizaLote(LoteDTO loteDTO, Lote lote) {
		lote.setValidade(loteDTO.getValidade());
		lote.setNumeroDeItens(loteDTO.getNumeroDeItens());
		return lote;
	}
 
	public void removerLote(Lote lote) {
		loteRepository.delete(lote);		
	}
}
