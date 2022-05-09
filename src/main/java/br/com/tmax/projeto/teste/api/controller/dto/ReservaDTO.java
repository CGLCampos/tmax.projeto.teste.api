package br.com.tmax.projeto.teste.api.controller.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import br.com.tmax.projeto.teste.api.model.Livro;
import br.com.tmax.projeto.teste.api.model.Reserva;
import br.com.tmax.projeto.teste.api.util.DateUtil;

public class ReservaDTO {

	private Long id;
	private AlunoDTO aluno;
	private List<LivroReservadoDTO> listaLivros = new ArrayList<>();
	private String dataReserva;
	private String dataDevolucao;

	public ReservaDTO(Reserva reserva) {
		this.id = reserva.getId();
		this.aluno = new AlunoDTO(reserva.getAluno());
		for (Livro livro : reserva.getLivros()) {
			this.listaLivros.add(new LivroReservadoDTO(livro));
		}
		this.dataReserva = DateUtil.localDateToString(reserva.getDataReserva().toLocalDate());
		this.dataDevolucao = DateUtil.localDateToString(reserva.getDataDevolucao().toLocalDate());
	}

	public Long getId() {
		return id;
	}

	public AlunoDTO getAluno() {
		return aluno;
	}

	public List<LivroReservadoDTO> getListaLivros() {
		return listaLivros;
	}

	public String getDataReserva() {
		return dataReserva;
	}

	public String getDataDevolucao() {
		return dataDevolucao;
	}

	public static Page<ReservaDTO> converter(Page<Reserva> reservas) {
		return reservas.map(ReservaDTO::new);
	}

}
