package br.com.marcoslsouza.leilao.service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.marcoslsouza.leilao.dao.PagamentoDao;
import br.com.marcoslsouza.leilao.model.Lance;
import br.com.marcoslsouza.leilao.model.Pagamento;

@Service
public class GeradorDePagamento {

	private PagamentoDao pagamentos;
	
	// Passar uma hora para teste
	private Clock clock;

	@Autowired
	public GeradorDePagamento(PagamentoDao pagamentos, Clock clock) {
		this.pagamentos = pagamentos;
		this.clock = clock;
	}

	public void gerarPagamento(Lance lanceVencedor) {
		// clock => para passar uma abstracao para teste, pois e um metodo estatico e para testar fica dificil.
		LocalDate vencimento = LocalDate.now(clock).plusDays(1);
		Pagamento pagamento = new Pagamento(lanceVencedor, this.proximoDiaUtil(vencimento));
		this.pagamentos.salvar(pagamento);
	}

	private LocalDate proximoDiaUtil(LocalDate dataBase) {
		
		DayOfWeek diaSemana = dataBase.getDayOfWeek();
		
		// Se for sabado
		if(diaSemana == DayOfWeek.SATURDAY) {
			return dataBase.plusDays(2);
		} else // Se for domingo
			if(diaSemana == DayOfWeek.SUNDAY) {
				return dataBase.plusDays(1);
			}
		
		return dataBase;
	}
}
