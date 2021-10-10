package br.com.marcoslsouza.leilao.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.marcoslsouza.leilao.dao.LeilaoDao;
import br.com.marcoslsouza.leilao.model.Lance;
import br.com.marcoslsouza.leilao.model.Leilao;
import br.com.marcoslsouza.leilao.model.Usuario;

class FinalizarLeilaoServiceTest {

	private FinalizarLeilaoService service;

	@Mock
	private LeilaoDao dao;
	
	@Mock
	private EnviadorDeEmails enviadorDeEmails;
	
	// Roda BeforeEach() antes de todos os metodos.
	@BeforeEach
	public void BeforeEach() {
		// O Mockito ler todos os atributos @Mock desta classe e cria os Mocks.
		MockitoAnnotations.initMocks(this);
		
		// Executa FinalizarLeilaoService
		this.service = new FinalizarLeilaoService(dao, enviadorDeEmails);
	}
	
	@Test
	void deveriaFinalizarUmLeilao() {
		
		// Um unico leilao com 2 lances
		List<Leilao> leiloes = this.leiloes();
		
		// Mockito.when => Quando esse metodo for chamado, devolva a lista leiloes. 
		// (Para nao retornar a lista fazia)
		Mockito.when(dao.buscarLeiloesExpirados()).thenReturn(leiloes);
		
		this.service.finalizarLeiloesExpirados();
		
		// Fazer os assert.
		
		// Só tem um leilao
		Leilao leilao = leiloes.get(0);
		
		// Verifica se o leilao finalizou (Fechou)
		Assert.assertTrue(leilao.isFechado());
		
		// Verifica se o lance vencedor e o de 900
		Assert.assertEquals(new BigDecimal("900"), leilao.getLanceVencedor().getValor());
		
		// Verificar se os dados no metodo no service foram salvos
		Mockito.verify(dao).salvar(leilao);
	}
	
	@Test
	void deveriaEnviarEmailParaVencedorDoLeilao() {
		
		List<Leilao> leiloes = this.leiloes();
		
		// Mockito.when => Quando esse metodo for chamado, devolva a lista leiloes. 
		// (Para nao retornar a lista fazia)
		Mockito.when(dao.buscarLeiloesExpirados()).thenReturn(leiloes);
		
		this.service.finalizarLeiloesExpirados();
		
		Leilao leilao = leiloes.get(0);
		
		Lance lanceVencedor = leilao.getLanceVencedor();
		
		// Verifica se o mock enviador de emails, enviou o email para o lance vencedor
		Mockito.verify(this.enviadorDeEmails).enviarEmailVencedorLeilao(lanceVencedor);
	}

	@Test
	void naoDeveriaEnviarEmailParaVencedorDoLeilaoEmCasoDeErroAoEncerrarOLeilao() {
		
		List<Leilao> leiloes = this.leiloes();
		
		// Mockito.when => Quando esse metodo for chamado, devolva a lista leiloes. 
		// (Para nao retornar a lista fazia)
		Mockito.when(dao.buscarLeiloesExpirados()).thenReturn(leiloes);
		
		Mockito.when(dao.salvar(Mockito.any())).thenThrow(RuntimeException.class);
		
		try {
			service.finalizarLeiloesExpirados();
			Mockito.verifyNoInteractions(this.enviadorDeEmails);
		} catch(Exception e) {}
	}
	
	// Metodo para criar uma lista de leiloes, pois o Mock retornara uma lista vazia em FinalizarLeilaoService->finalizarLeiloesExpirados 
	// (List<Leilao> expirados = leiloes.buscarLeiloesExpirados();)
	private List<Leilao> leiloes() {
		List<Leilao> lista = new ArrayList();
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));
		
		Lance primeiro = new Lance(new Usuario("Beltrano"), new BigDecimal("900"));
		Lance segundo = new Lance(new Usuario("Beltrano"), new BigDecimal("600"));
		
		leilao.propoe(primeiro);
		leilao.propoe(segundo);
		
		lista.add(leilao);
		
		return lista;
	}
}
