package br.com.marcoslsouza.leilao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.marcoslsouza.leilao.dao.LeilaoDao;
import br.com.marcoslsouza.leilao.model.Leilao;

public class HelloWorldMockito {
	
	

	@Test
	public void hello() {
		
		// Cria um mock da classe LeilaoDao
		LeilaoDao mock = Mockito.mock(LeilaoDao.class);
		
		// Simula que vai no metodo LeilaoDao->buscarTodos() e retorna uma lista vazia.
		List<Leilao> todos = mock.buscarTodos();
		Assert.assertTrue(todos.isEmpty());
	}
}
