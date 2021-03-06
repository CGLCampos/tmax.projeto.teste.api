package br.com.tmax.projeto.teste.api.controller.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import br.com.tmax.projeto.teste.api.model.LivroReservado;
import br.com.tmax.projeto.teste.api.model.Reserva;
import br.com.tmax.projeto.teste.api.util.DateUtil;

public class ReservaDTO {

	private Long id;

	private AlunoDTO aluno;

	private List<LivroReservadoDTO> listaLivros = new ArrayList<>();

	private String dataReserva;

	private boolean finalizado;
	private String dataDevoluçãoEfetiva = "Em aberto";

	public ReservaDTO(Reserva reserva) {
		this.id = reserva.getId();

		this.aluno = new AlunoDTO(reserva.getAluno());

		for (LivroReservado livroReservado : reserva.getLivrosReservados()) {
			this.listaLivros.add(new LivroReservadoDTO(livroReservado));
		}

		this.dataReserva = DateUtil.localDateToString(reserva.getDataReserva().toLocalDate());

		this.finalizado = reserva.estaFinalizado();
		if (reserva.getDataDevolucaoEfetiva() != null) {
			this.dataDevoluçãoEfetiva = DateUtil.localDateToString(reserva.getDataDevolucaoEfetiva().toLocalDate());
		}
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

	public boolean isFinalizado() {
		return finalizado;
	}

	public String getDataDevoluçãoEfetiva() {
		return dataDevoluçãoEfetiva;
	}

	public static Page<ReservaDTO> converter(Page<Reserva> reservas) {
		return reservas.map(ReservaDTO::new);
	}

}
