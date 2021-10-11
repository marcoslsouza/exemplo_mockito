package br.com.marcoslsouza.leilao.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.marcoslsouza.leilao.dao.PagamentoDao;
import br.com.marcoslsouza.leilao.model.Lance;
import br.com.marcoslsouza.leilao.model.Leilao;
import br.com.marcoslsouza.leilao.model.Pagamento;
import br.com.marcoslsouza.leilao.model.Usuario;

class GeradorDePagamentoTest {
	
	private GeradorDePagamento geradorDePagamento;

	@Mock
	private PagamentoDao pagamentoDao;
	
	// Captura o Pagamento que esta sendo criado em GeradorDePagamento
	@Captor
	private ArgumentCaptor<Pagamento> captorPagamento;
	
	@Mock
	private Clock clock; 
	
	// Roda BeforeEach() antes de todos os metodos.
	@BeforeEach
	public void BeforeEach() {
		// O Mockito ler todos os atributos @Mock desta classe e cria os Mocks.
		MockitoAnnotations.initMocks(this);
		this.geradorDePagamento = new GeradorDePagamento(pagamentoDao, this.clock);
	}
	
	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilao() {
		Leilao leilao = this.leiloes();
		
		LocalDate data = LocalDate.of(2021, 10, 10);
		
		// Quando o clock chamar instant
		Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();
		Mockito.when(clock.instant()).thenReturn(instant);
		Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
		
		// Pega o unico lance para colocar como vencedor
		Lance vencedor = leilao.getLances().get(0);
		this.geradorDePagamento.gerarPagamento(vencedor);
		
		// Verifica se o metodo salvar foi chamado e captura o parametro passado
		Mockito.verify(pagamentoDao).salvar(captorPagamento.capture());
		
		Pagamento pagamento = captorPagamento.getValue();
		
		// Verifica a data de vencimento
		Assert.assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
		
		// Verifica o valor do pagamento
		Assert.assertEquals(vencedor.getValor(), pagamento.getValor());
		
		// Nao pode estar pago
		Assert.assertFalse(pagamento.getPago());
		
		// Quem e o usuario. Lance vencedor
		Assert.assertEquals(vencedor.getUsuario(), pagamento.getUsuario());
		
		// Verifica o leilao
		Assert.assertEquals(leilao, pagamento.getLeilao());
	}

	// Metodo para criar uma lista de leiloes, pois o Mock retornara uma lista vazia em FinalizarLeilaoService->finalizarLeiloesExpirados 
	// (List<Leilao> expirados = leiloes.buscarLeiloesExpirados();)
	private Leilao leiloes() {
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));
		
		Lance primeiro = new Lance(new Usuario("Beltrano"), new BigDecimal("900"));
		leilao.propoe(primeiro);
		
		return leilao;
	}
}
