package br.com.marcoslsouza.leilao.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.marcoslsouza.leilao.dao.LeilaoDao;
import br.com.marcoslsouza.leilao.model.Lance;
import br.com.marcoslsouza.leilao.model.Leilao;
import br.com.marcoslsouza.leilao.model.Usuario;

class FinalizarLeilaoServiceTest {

	private FinalizarLeilaoService service;

	@Mock
	private LeilaoDao dao;
	
	// Roda BeforeEach() antes de todos os metodos.
	// O Mockito ler todos os atributos @Mock desta classe e cria os Mocks.
	@BeforeEach
	public void BeforeEach() {
		MockitoAnnotations.initMocks(this);
		this.service = new FinalizarLeilaoService(dao);
	}
	
	@Test
	void deveriaFinalizarUmLeilao() {
		
		List<Leilao> leiloes = this.leiloes();
		
		this.service.finalizarLeiloesExpirados();
	}

	// Metodo para criar uma lista de leiloes, pois o Mock retornara uma lista vazia em FinalizarLeilaoService->finalizarLeiloesExpirados 
	// (List<Leilao> expirados = leiloes.buscarLeiloesExpirados();)
	private List<Leilao> leiloes() {
		List<Leilao> lista = new ArrayList();
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));
		
		Lance primeiro = new Lance(new Usuario("Beltrano"), new BigDecimal("600"));
		Lance segundo = new Lance(new Usuario("Beltrano"), new BigDecimal("600"));
		
		leilao.propoe(primeiro);
		leilao.propoe(segundo);
		
		lista.add(leilao);
		
		return lista;
	}
}
