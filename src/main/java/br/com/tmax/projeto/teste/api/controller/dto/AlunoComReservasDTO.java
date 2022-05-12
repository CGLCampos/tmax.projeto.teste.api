package br.com.tmax.projeto.teste.api.controller.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.tmax.projeto.teste.api.model.Aluno;
import br.com.tmax.projeto.teste.api.model.Reserva;
import br.com.tmax.projeto.teste.api.util.DateUtil;

public class AlunoComReservasDTO {

	private Long id;
	private String nome;
	private String dataNascimento;
	private String turma;
	private String email;
	private List<ReservaSemAlunoDTO> reservas = new ArrayList<>();

	public AlunoComReservasDTO(Aluno aluno) {
		this.id = aluno.getId();
		this.nome = aluno.getNome();
		this.dataNascimento = DateUtil.localDateToString(aluno.getDataNascimento());
		this.turma = aluno.getTurma();
		this.email = aluno.getEmail();
		for(Reserva reserva : aluno.getReservas()) {
			this.reservas.add(new ReservaSemAlunoDTO(reserva));
		}
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public String getTurma() {
		return turma;
	}

	public String getEmail() {
		return email;
	}
	
	public List<ReservaSemAlunoDTO> getReservas() {
		return reservas;
	}

}
