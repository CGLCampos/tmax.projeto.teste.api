package br.com.tmax.projeto.teste.api.controller.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.tmax.projeto.teste.api.model.LivroReservado;
import br.com.tmax.projeto.teste.api.model.Reserva;
import br.com.tmax.projeto.teste.api.util.DateUtil;

public class ReservaSemAlunoDTO {

	private Long id;

	private List<LivroReservadoDTO> listaLivros = new ArrayList<>();

	private String dataReserva;

	private boolean finalizado;
	private String dataDevoluçãoEfetiva = "Em aberto";

	public ReservaSemAlunoDTO(Reserva reserva) {
		this.id = reserva.getId();

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

}
