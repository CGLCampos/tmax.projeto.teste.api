package br.com.tmax.projeto.teste.api.controller.form;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.tmax.projeto.teste.api.model.Aluno;
import br.com.tmax.projeto.teste.api.model.Livro;
import br.com.tmax.projeto.teste.api.model.LivroReservado;
import br.com.tmax.projeto.teste.api.model.Reserva;

public class ReservaCadastroForm {

	@NotNull
	private Long idAluno;

	@Size(min = 1, max = 3)
	private List<Long> idLivros;

	public Long getIdAluno() {
		return idAluno;
	}

	public void setIdAluno(Long idAluno) {
		this.idAluno = idAluno;
	}

	public List<Long> getIdLivros() {
		return idLivros;
	}

	public void setIdLivros(List<Long> idLivros) {
		this.idLivros = idLivros;
	}

	public Reserva converter(Aluno aluno, List<Livro> livros) {
		Reserva reserva = new Reserva();

		reserva.setAluno(aluno);
		
		livros.forEach(livro -> {
			LivroReservado reservado = new LivroReservado();
			reservado.setLivro(livro);
			reservado.setReserva(reserva);
			reservado.setDataDevolucao(null);
		
			reserva.getLivrosReservados().add(reservado);
			livro.setReserva(reserva);
		});

		reserva.setDataReserva(LocalDateTime.now());
		reserva.setFinalizado(false);

		return reserva;
	}
}
