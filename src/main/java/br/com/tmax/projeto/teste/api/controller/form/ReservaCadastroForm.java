package br.com.tmax.projeto.teste.api.controller.form;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import br.com.tmax.projeto.teste.api.model.Aluno;
import br.com.tmax.projeto.teste.api.model.Livro;
import br.com.tmax.projeto.teste.api.model.Reserva;
import br.com.tmax.projeto.teste.api.repository.AlunoRepository;
import br.com.tmax.projeto.teste.api.repository.LivroRepository;

public class ReservaCadastroForm {

	@NotNull
	private Long idAluno;
	@NotNull
	@NotEmpty
	@Size(min=1, max=3)
	private List<Long> idLivros;
	@NotNull @Positive @Size(min = 7, max = 10)
	private Integer diasParaDevolucao = 7;

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

	public Integer getDiasParaDevolucao() {
		return diasParaDevolucao;
	}

	public void setDiasParaDevolucao(Integer diasParaDevolucao) {
		this.diasParaDevolucao = diasParaDevolucao;
	}

	public Reserva converter(AlunoRepository alunoRepository, LivroRepository livroRepository) {
		Reserva reserva = new Reserva();

		Aluno aluno = alunoRepository.findById(idAluno).get();
		reserva.setAluno(aluno);

		for (Long idLivro : idLivros) {
			Optional<Livro> optional = livroRepository.findById(idLivro);
//			if (!optional.isPresent()) {
//				throw new LivroNaoEncontradoException;
//			}

			Livro livro = optional.get();
			livro.setReserva(reserva);
			reserva.getLivros().add(livro);
		}

		reserva.setDataReserva(LocalDateTime.now());
		reserva.setDataDevolucao(LocalDateTime.now().plusDays(this.diasParaDevolucao));

		return reserva;
	}
}
