package br.com.marcoslsouza.leilao.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.marcoslsouza.leilao.dao.LeilaoDao;
import br.com.marcoslsouza.leilao.model.Lance;
import br.com.marcoslsouza.leilao.model.Leilao;

@Service
public class FinalizarLeilaoService {

	private LeilaoDao leiloes;
	private EnviadorDeEmails enviadorDeEmails;
	
	@Autowired
	public FinalizarLeilaoService(LeilaoDao leiloes, EnviadorDeEmails enviadorDeEmails) {
		this.leiloes = leiloes;
		this.enviadorDeEmails = enviadorDeEmails;
	}

	public void finalizarLeiloesExpirados() {
		List<Leilao> expirados = leiloes.buscarLeiloesExpirados();
		expirados.forEach(leilao -> {
			Lance maiorLance = maiorLanceDadoNoLeilao(leilao);
			leilao.setLanceVencedor(maiorLance);
			leilao.fechar();
			leiloes.salvar(leilao);
			this.enviadorDeEmails.enviarEmailVencedorLeilao(maiorLance);
		});
	}

	private Lance maiorLanceDadoNoLeilao(Leilao leilao) {
		List<Lance> lancesDoLeilao = new ArrayList<>(leilao.getLances());
		lancesDoLeilao.sort((lance1, lance2) -> {
			return lance2.getValor().compareTo(lance1.getValor());
		});
		return lancesDoLeilao.get(0);
	}
	
}
